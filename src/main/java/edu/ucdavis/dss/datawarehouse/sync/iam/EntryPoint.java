package edu.ucdavis.dss.datawarehouse.sync.iam;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Root;

import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.ucdavis.dss.datawarehouse.ldap.client.LdapClient;
import edu.ucdavis.dss.iam.client.IamClient;
import edu.ucdavis.dss.iam.dtos.IamAssociation;
import edu.ucdavis.dss.iam.dtos.IamContactInfo;
import edu.ucdavis.dss.iam.dtos.IamDepartment;
import edu.ucdavis.dss.iam.dtos.IamPerson;
import edu.ucdavis.dss.iam.dtos.IamPrikerbacct;

public class EntryPoint {
	public static String iamApiKey;
	static private Logger logger = LoggerFactory.getLogger("EntryPoint");
	static EntityManagerFactory entityManagerFactory = null;
	static EntityManager entityManager = null;
	static int NUM_VALID_OLD_VERSIONS_TO_KEEP = 2;

	/**
	 * Main entry point for IAM sync. Run as a console application when
	 * needed. Scheduling once a day via cron is recommended.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// Set up a default uncaught exception handler as Hibernate will not let the process
		// exit if the main thread dies but Hibernate resources are not cleaned up.
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			public void uncaughtException(Thread t, Throwable ex) {
				logger.error("Unhandled exception in thread: " + t.getName());
				logger.error("Exception: " + ex.toString());
				logger.error(exceptionStacktraceToString(ex));
				
				// Clean up Hibernate
				if((entityManager != null) && (entityManager.isOpen())) { entityManager.close(); }
				if((entityManagerFactory != null) && (entityManagerFactory.isOpen())) { entityManagerFactory.close(); }

				// Exit with error
				System.exit(-1);
			}
		});
		
		logger.debug("IAM/LDAP import started at " + new Date());
		
		/**
		 * Load the IAM API key from ~/.data-warehouse/settings.properties.
		 * We expect a few keys to exist; see prop.getProperty() lines.
		 */
		String filename = System.getProperty("user.home") + File.separator + ".data-warehouse" + File.separator + "settings.properties";
		File propsFile = new File(filename);
		Properties configurationProperties = null;

		try {
			InputStream is = new FileInputStream(propsFile);

			configurationProperties = new Properties();

			configurationProperties.load(is);
			is.close();

			iamApiKey = configurationProperties.getProperty("IAM_API_KEY");

			if(iamApiKey == null || iamApiKey.length() == 0) {
				logger.error("IAM_API_KEY in settings.properties is missing or empty.");
				return;
			}

			logger.debug("Settings file '" + filename + "' found.");
		} catch (FileNotFoundException e) {
			logger.error("Could not find " + filename + ".");
			System.exit(-1);
		} catch (IOException e) {
			logger.error("An IOException occurred while loading " + filename);
			System.exit(-1);
		}

		long startTime = new Date().getTime();

		/**
		 * Set up Hibernate
		 */
		try {
			entityManagerFactory = Persistence.createEntityManagerFactory( "edu.ucdavis.dss.datawarehouse.sync.iam" );
		} catch (ServiceException e) {
			logger.error("Unable to create entity manager factory. Is the database running?");
			System.exit(-1);
		}
		
		entityManager = entityManagerFactory.createEntityManager();
		
		/**
		 * Set up new 'vers' snapshot
		 */
		Version vers = new Version();
		vers.setImportStarted(new Timestamp(new Date().getTime()));
		entityManager.getTransaction().begin();
		entityManager.persist( vers );
		entityManager.getTransaction().commit();

		logger.info("Created new version snapshot: " + vers);

		/**
		 * Get a list of all possible ucdPersonUUIDs using LDAP
		 */
		LdapClient ldapClient = new LdapClient(configurationProperties.getProperty("LDAP_URL"),
				configurationProperties.getProperty("LDAP_BASE"),
				configurationProperties.getProperty("LDAP_USER"),
				configurationProperties.getProperty("LDAP_PASSWORD"));
		
		logger.info("Fetching all UCD person UUIDs from LDAP ...");
		List<String> allUcdPersonUUIDs = ldapClient.fetchAllUcdPersonUUIDs();
		logger.info("Finished fetching all UCD person UUIDs from LDAP.");

		/**
		 * Initialize IAM client
		 */
		IamClient iamClient = new IamClient(iamApiKey);

		/**
		 * Extract and load all departments from IAM
		 */
		logger.info("Persisting all departments ...");
		List<IamDepartment> departments = iamClient.getAllDepartments();

		if(departments != null) {
			entityManager.getTransaction().begin();
			for(IamDepartment department : departments) {
				department.markAsVersion(vers);
				entityManager.persist( department );
			}
			entityManager.getTransaction().commit();
		} else {
			logger.error("Unable to fetch departments. Exiting ...");
			System.exit(-1);
		}
		
		/**
		 * Convert all ucdPersonUUIDs to IAM IDs and fetch associated information
		 */
		Long count = 0L;
		long additionalStartTime = new Date().getTime();
		logger.info("Persisting all people ...");
		for(String ucdPersonUUID : allUcdPersonUUIDs) {
			Long iamId = iamClient.getIamIdFromMothraId(ucdPersonUUID);
			
			List<IamContactInfo> contactInfos = iamClient.getContactInfo(iamId);
			List<IamPerson> people = iamClient.getPersonInfo(iamId);
			List<IamPrikerbacct> prikerbaccts = iamClient.getPrikerbacct(iamId);
			List<IamAssociation> associations = iamClient.getAllAssociationsForIamId(iamId);

			if(contactInfos == null) {
				logger.warn("Unable to fetch contact info for IAM ID " + iamId + ". Skipping.");
				continue;
			}
			if(people == null) {
				logger.warn("Unable to fetch person info for IAM ID " + iamId + ". Skipping.");
				continue;
			}
			if(prikerbaccts == null) {
				logger.warn("Unable to fetch prikerbacct info for IAM ID " + iamId + ". Skipping.");
				continue;
			}
			if(associations == null) {
				logger.warn("Unable to fetch associations info for IAM ID " + iamId + ". Skipping.");
				continue;
			}
			
			// If an exception happened during the last iteration of the loop,
			// the entityManager is invalid and will need to be recreated.
			if(entityManager.isOpen() == false) {
				entityManager = entityManagerFactory.createEntityManager();
			}

			entityManager.getTransaction().begin();

			for(IamAssociation association : associations) {
				if(entityManager.isOpen()) {
					try {
						association.markAsVersion(vers);
						entityManager.persist( association );
					} catch (Exception e) {
						logger.error("Unable to persist association: " + association);
						e.printStackTrace();
						entityManager.getTransaction().rollback();
						if(entityManager.isOpen()) entityManager.close();
					}
				}
			}
			
			if(entityManager.isOpen() == false) {
				logger.error("IamAssociation: Skipping IAM ID " + iamId + " due to previous exceptions.");
				continue;
			}

			for(IamContactInfo contactInfo : contactInfos) {
				if(entityManager.isOpen()) {
					try {
						contactInfo.markAsVersion(vers);
						entityManager.persist( contactInfo );
					} catch (Exception e) {
						logger.error("Unable to persist contactInfo: " + contactInfo);
						e.printStackTrace();
						entityManager.getTransaction().rollback();
						if(entityManager.isOpen()) entityManager.close();
					}
				}
			}
			
			if(entityManager.isOpen() == false) {
				logger.error("IamContactInfo: Skipping IAM ID " + iamId + " due to previous exceptions.");
				continue;
			}

			for(IamPerson person : people) {
				if(entityManager.isOpen()) {
					try {
						person.markAsVersion(vers);
						entityManager.persist( person );
					} catch (Exception e) {
						logger.error("Unable to persist person: " + person);
						e.printStackTrace();
						entityManager.getTransaction().rollback();
						if(entityManager.isOpen()) entityManager.close();
					}
				}
			}
			
			if(entityManager.isOpen() == false) {
				logger.error("IamPerson: Skipping IAM ID " + iamId + " due to previous exceptions.");
				continue;
			}

			for(IamPrikerbacct prikerbacct : prikerbaccts) {
				if(entityManager.isOpen()) {
					try {
						prikerbacct.markAsVersion(vers);
						entityManager.persist( prikerbacct );
					} catch (Exception e) {
						logger.error("Unable to persist prikerbacct: " + prikerbacct);
						e.printStackTrace();
						entityManager.getTransaction().rollback();
						if(entityManager.isOpen()) entityManager.close();
					}
				}
			}
			
			if(entityManager.isOpen() == false) {
				logger.error("IamPrikerbacct: Skipping IAM ID " + iamId + " due to previous exceptions.");
				continue;
			}

			entityManager.getTransaction().commit();

			count++;

			if(count % 1000 == 0) {
				float progress = (float)count / (float)allUcdPersonUUIDs.size();
				long currentTime = new Date().getTime();
				long timeSoFar = currentTime - additionalStartTime;
				Date estCompleted = new Date(additionalStartTime + (long)((float)timeSoFar / progress));
				String logMsg = String.format("\tProgress: %.2f%% (est. completion at %s)", progress * (float)100, estCompleted.toString());
				logger.debug(logMsg);
				logger.debug("Based on:\n\tprogress: " + progress + "\n\tcurrentTime: " + currentTime + "\n\ttimeSoFar: " + timeSoFar);
			}
		}

		if(entityManager.isOpen() == false) {
			entityManager = entityManagerFactory.createEntityManager();
		}
		
		/**
		 * Mark this snapshot as complete
		 */
		vers.setImportFinished(new Timestamp(new Date().getTime()));
		entityManager.getTransaction().begin();
		entityManager.merge( vers ); // 'vers' will be detached but we can override DB version safely
		entityManager.getTransaction().commit();
		
		/**
		 * Remove old data snapshots
		 */
		logger.info("Removing old snapshots ...");
		removeOldValidSnapshots(NUM_VALID_OLD_VERSIONS_TO_KEEP);

		/**
		 * Remove any failed imports
		 */
		removeFailedImports();
		
		/**
		 * Close Hibernate
		 */
		entityManager.close();
		entityManagerFactory.close();
		
		logger.info("Import completed successfully. Took " + (float)(new Date().getTime() - startTime) / 1000.0 + "s");
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
	private static void removeFailedImports() {
		logger.info("Removing failed imports ...");

		// Find failed imports
		entityManager.getTransaction().begin();
		List<Version> failedVersions = entityManager.createQuery( "FROM Version WHERE importFinished IS NULL", Version.class ).getResultList();
		entityManager.getTransaction().commit();

		logger.debug("Found " + failedVersions.size() + " failed import(s).");

		// Remove data from any row utilizing a failed import
		for(Version failedVersion : failedVersions) {
			removeVersion(failedVersion);
		}

		logger.info("Removed " + failedVersions.size() + " failed import(s).");
	}

	/**
	 * Removes all rows from any table where 'vers' equals 'version'.
	 * Also removes the given version from the 'vers' table.
	 * 
	 * @param version Timestamp to remove from database.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void removeVersion(Version version) {
		logger.debug("Removing version " + version + " ...");

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

	/**
	 * Removes all rows from any table where 'vers' is valid but too old,
	 * defined by older than the last 'keepVersions' recent versions.
	 * 
	 * @param keepVersions Number of most recent versions of data to keep.
	 */
	private static void removeOldValidSnapshots(int keepVersions) {
		logger.info("Removing old, valid imports ...");

		// Find old, valid imports
		entityManager.getTransaction().begin();
		List<Version> validVersions = entityManager.createQuery( "FROM Version WHERE importFinished IS NOT NULL ORDER BY vers DESC", Version.class ).getResultList();
		entityManager.getTransaction().commit();

		if(validVersions.size() > keepVersions) {
			List<Version> versionsToRemove = validVersions.subList(keepVersions, validVersions.size());

			logger.debug("Found " + versionsToRemove.size() + " snapshots to remove.");

			// Remove data from any row mentioning a failed 'vers'
			for(Version versToRemove : versionsToRemove) {
				removeVersion(versToRemove);
			}

			logger.info("Removed " + versionsToRemove.size() + " old, valid imports.");
		} else {
			logger.info("No old versions found (keeping " + validVersions.size() + ".");
		}
	}

	// Credit: http://stackoverflow.com/questions/10120709/difference-between-printstacktrace-and-tostring
	private static String exceptionStacktraceToString(Throwable ex) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);

		ex.printStackTrace(ps);
		ps.close();
		
		return baos.toString();
	}
}
