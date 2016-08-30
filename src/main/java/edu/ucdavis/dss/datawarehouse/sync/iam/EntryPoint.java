package edu.ucdavis.dss.datawarehouse.sync.iam;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Root;

import org.hibernate.exception.DataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.ucdavis.dss.iam.client.IamClient;
import edu.ucdavis.dss.iam.dtos.IamAssociation;
import edu.ucdavis.dss.iam.dtos.IamContactInfo;
import edu.ucdavis.dss.iam.dtos.IamDepartment;
import edu.ucdavis.dss.iam.dtos.IamPerson;
import edu.ucdavis.dss.iam.dtos.IamPrikerbacct;

public class EntryPoint {
	public static String iamApiKey, localDBUrl, localDBUser, localDBPass;
	static Logger logger = LoggerFactory.getLogger("EntryPoint");
	static EntityManagerFactory entityManagerFactory = null;

	/**
	 * Main entry point for IAM sync. Run as a console application when
	 * needed. Scheduling once a day via cron is recommended.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		/**
		 * Load the IAM API key from ~/.data-warehouse/settings.properties.
		 * We expect a few keys to exist; see prop.getProperty() lines.
		 */
		String filename = System.getProperty("user.home") + File.separator + ".data-warehouse" + File.separator + "settings.properties";
		File propsFile = new File(filename);

		try {
			InputStream is = new FileInputStream(propsFile);

			Properties prop = new Properties();

			prop.load(is);
			is.close();

			localDBUrl = prop.getProperty("LOCAL_MYSQL_URL");
			localDBUser = prop.getProperty("LOCAL_MYSQL_USER");
			localDBPass = prop.getProperty("LOCAL_MYSQL_PASS");
			iamApiKey = prop.getProperty("IAM_API_KEY");

			logger.info("Settings file '" + filename + "' found.");
		} catch (FileNotFoundException e) {
			logger.warn("Could not find " + filename + ".");
			return;
		} catch (IOException e) {
			logger.error("An IOException occurred while loading " + filename);
			return;
		}

		/**
		 * Set up Hibernate
		 */
		entityManagerFactory = Persistence.createEntityManagerFactory( "edu.ucdavis.dss.datawarehouse.sync.iam" );
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		/**
		 * Set up new 'vers' snapshot
		 */
		Version vers = new Version();
		entityManager.getTransaction().begin();
		entityManager.persist( vers );
		entityManager.getTransaction().commit();
		
		logger.info("Created new version snapshot: " + vers);
		
		/**
		 * Remove any failed imports
		 */
		removeFailedImports(entityManager);
		
		/**
		 * Initialize IAM client
		 */
		IamClient iamClient = new IamClient(iamApiKey);

		/**
		 * Extract and load all departments from IAM
		 */
		List<IamDepartment> departments = iamClient.getAllDepartments();
		entityManager.getTransaction().begin();
		for(IamDepartment department : departments) {
			department.markAsVersion(vers);
			entityManager.persist( department );
		}
		entityManager.getTransaction().commit();

		/**
		 * Extract and load all associations by department from IAM
		 */
		for(IamDepartment department : departments) {
			List<IamAssociation> associations = iamClient.getAllAssociationsForDepartment(department.getDeptCode());
			entityManager.getTransaction().begin();
			for(IamAssociation association : associations) {
				association.markAsVersion(vers);
				entityManager.persist( association );
			}
			entityManager.getTransaction().commit();
		}

		/**
		 * Persist contact infos, people entries, and prikerbaccts
		 */
		entityManager.getTransaction().begin();
		List<Long> iamIds = entityManager.createQuery( "SELECT iamId from IamAssociation", Long.class ).getResultList();
		entityManager.getTransaction().commit();
		
		for(Long iamId : iamIds) {
			List<IamContactInfo> contactInfos = iamClient.getContactInfo(iamId);
			List<IamPerson> people = iamClient.getPersonInfo(iamId);
			List<IamPrikerbacct> prikerbaccts = iamClient.getPrikerbacct(iamId);
			
			entityManager.getTransaction().begin();
			
			for(IamContactInfo contactInfo : contactInfos) {
				try {
					contactInfo.markAsVersion(vers);
					entityManager.persist( contactInfo );
				} catch (DataException e) {
					logger.error("Unable to persist contactInfo: " + contactInfo);
					e.printStackTrace();
				}
			}
			
			for(IamPerson person : people) {
				try {
					person.markAsVersion(vers);
					entityManager.persist( person );
				} catch (DataException e) {
					logger.error("Unable to persist person: " + person);
					e.printStackTrace();
				}
			}

			for(IamPrikerbacct prikerbacct : prikerbaccts) {
				try {
					prikerbacct.markAsVersion(vers);
					entityManager.persist( prikerbacct );
				} catch (DataException e) {
					logger.error("Unable to persist prikerbacct: " + prikerbacct);
					e.printStackTrace();
				}
			}
			
			entityManager.getTransaction().commit();
		}

		/**
		 * Close Hibernate
		 */
		entityManager.close();
		entityManagerFactory.close();
	}
	
	/**
	 * Removes any rows from _any_ table that matches a 'vers' from
	 * the 'vers' table where import_finished is null (unfinished).
	 * 
	 * datwarehouse-sync-sis must not be running more than once
	 * for this to work correctly, else another instance could
	 * be importing and generate a valid 'vers' with no import_finished
	 * and this function would erroneously remove it.
	 */
	private static void removeFailedImports(EntityManager entityManager) {
		logger.info("Removing failed imports ...");

		// Find failed imports
		entityManager.getTransaction().begin();
		List<Version> failedVersions = entityManager.createQuery( "FROM Version WHERE importFinished IS NULL", Version.class ).getResultList();
		entityManager.getTransaction().commit();

		logger.info("Found " + failedVersions.size() + " failed imports.");

		// Remove data from any row utilizing a failed import
		for(Version failedVersion : failedVersions) {
			removeVersion(entityManager, failedVersion);
		}
		
		logger.info("Done removing " + failedVersions.size() + " failed imports.");
	}

	/**
	 * Removes all rows from any table where 'vers' equals 'version'.
	 * Also removes the given version from the 'vers' table.
	 * 
	 * @param version Timestamp to remove from database.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void removeVersion(EntityManager entityManager, Version version) {
		logger.info("Removing version " + version + " ...");

		entityManager.getTransaction().begin();
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();

		Class<?>[] tables = {IamAssociation.class, IamContactInfo.class, IamPerson.class, IamDepartment.class, IamPrikerbacct.class, Version.class};
		
		// Remove data from any row mentioning a failed 'vers'
		for(Class table : tables) {
			logger.debug("Removing data from " + table.getName() + " for version " + version);
			
			CriteriaDelete<Class<?>> deleteAssociation = cb.createCriteriaDelete(table);
			Root e = deleteAssociation.from(table);
			deleteAssociation.where(cb.equal(e.get("vers"), version.getVers()));
			entityManager.createQuery(deleteAssociation).executeUpdate();
		}
		
		entityManager.getTransaction().commit();

		logger.debug("Done removing version " + version + ".");
	}
}
