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

	/**
	 * Main entry point for IAM sync. Run as a console application when
	 * needed. Scheduling once a day via cron is recommended.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		EntityManagerFactory entityManagerFactory = null;

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

		/**
		 * Initialize IAM client
		 */
		IamClient iamClient = new IamClient(iamApiKey);

		EntityManager entityManager = entityManagerFactory.createEntityManager();

		/**
		 * Extract and load all departments from IAM
		 */
		List<IamDepartment> departments = iamClient.getAllDepartments();
		departments.clear(); // REMOVEME
		entityManager.getTransaction().begin();
		for(IamDepartment department : departments) {
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
		
		entityManager.getTransaction().begin();
		for(Long iamId : iamIds) {
			List<IamContactInfo> contactInfos = iamClient.getContactInfo(iamId);
			List<IamPerson> people = iamClient.getPersonInfo(iamId);
			List<IamPrikerbacct> prikerbaccts = iamClient.getPrikerbacct(iamId);
			
			for(IamContactInfo contactInfo : contactInfos) {
				try {
					entityManager.persist( contactInfo );
				} catch (DataException e) {
					logger.error("Unable to persist contactInfo: " + contactInfo);
					e.printStackTrace();
				}
			}
			
			for(IamPerson person : people) {
				try {
					entityManager.persist( person );
				} catch (DataException e) {
					logger.error("Unable to persist person: " + person);
					e.printStackTrace();
				}
			}

			for(IamPrikerbacct prikerbacct : prikerbaccts) {
				try {
					entityManager.persist( prikerbacct );
				} catch (DataException e) {
					logger.error("Unable to persist prikerbacct: " + prikerbacct);
					e.printStackTrace();
				}
			}
		}
		entityManager.getTransaction().commit();

		/**
		 * Close Hibernate
		 */
		entityManager.close();
		entityManagerFactory.close();
	}
}
