package edu.ucdavis.dss.datawarehouse.sync.iam;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.ucdavis.dss.datawarehouse.ldap.client.LdapClient;
import edu.ucdavis.dss.iam.client.IamClient;
import edu.ucdavis.dss.iam.dtos.IamPpsDepartment;

public class EntryPoint {
	static private Logger logger = LoggerFactory.getLogger("EntryPoint");
	static EntityManagerFactory entityManagerFactory = null;
	static EntityManager entityManager = null;
	static final int maxThreads = 30;
	static final int recordsPerThread = 100;

	// Set up a default uncaught exception handler as Hibernate will not let the process
	// exit if the main thread dies but Hibernate resources are not cleaned up.
	static Thread.UncaughtExceptionHandler uncaughtException = new Thread.UncaughtExceptionHandler() {
		public void uncaughtException(Thread t, Throwable e) {
			logger.error("Unhandled exception in thread: " + t.getName());
			logger.error("Exception: " + e.toString());
			logger.error(ExceptionUtils.stacktraceToString(e));

			// Clean up Hibernate
			if((entityManager != null) && (entityManager.isOpen())) { entityManager.close(); }
			if((entityManagerFactory != null) && (entityManagerFactory.isOpen())) { entityManagerFactory.close(); }

			// Exit with error
			System.exit(-1);
		}
	};
	
	/**
	 * Main entry point for IAM sync. Run as a console application when
	 * needed. Scheduling once a day via cron is recommended.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		logger.info("IAM/LDAP import started at " + new Date());

		if(SettingsUtils.initialize() == false) {
			logger.error("Unable to load settings. Cannot proceed.");
			System.exit(1);
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

		if(IamPpsDepartmentsImport.importPpsDepartments(entityManagerFactory) == false) {
			logger.error("Unable to import PPS departments! Will continue ...");
		}
		if(IamPpsDepartmentsImport.importBous(entityManagerFactory) == false) {
			logger.error("Unable to import BOUs! Will continue ...");
		}

		/**
		 * Get a list of all possible ucdPersonUUIDs using LDAP
		 */
		LdapClient ldapClient = new LdapClient(SettingsUtils.getLdapUrl(),
				SettingsUtils.getLdapBase(),
				SettingsUtils.getLdapUser(),
				SettingsUtils.getLdapPassword());
 
		logger.debug("Fetching all UCD person UUIDs from LDAP ...");
		List<String> allUcdPersonUUIDs = new ArrayList(ldapClient.fetchAllUcdPersonUUIDs());
		logger.debug("Finished fetching all UCD person UUIDs from LDAP.");

		logger.debug("Persisting all people ...");

		List<Thread> threads = new ArrayList<Thread>();
		
		List<List<String>> chunkedUuids = chunkList(allUcdPersonUUIDs, recordsPerThread);
		for(List<String> uuids : chunkedUuids) {
			Thread t = new Thread(new IamPersonImportThread(uuids, entityManagerFactory));
			t.setUncaughtExceptionHandler(uncaughtException);
			threads.add(t);
		}
		
		/**
		 * Convert all ucdPersonUUIDs to IAM IDs and fetch associated information
		 */
		int activeThreads = 0;
		
		// Start as many threads as possible (limited by maxThreads).
		// If we have too many threads, the while loop below will
		// start them once other threads finish.
		for(Thread t : threads) {
			if(activeThreads < maxThreads) {
				t.start();
				activeThreads++;
			}
		}

		// Ensure all threads get a chance to run and
		// loop until all threads have finished.
		while(threads.size() > 0) {
			Iterator<Thread> iter = threads.iterator();
			
			logger.debug("Threads remaining: " + threads.size());
			
			while(iter.hasNext()) {
				Thread t = iter.next();
				
				if(t.isAlive()) {
					try {
						t.join(500);
					} catch (InterruptedException e) {
						logger.warn("t.join(); was interrupted:");
						logger.warn(ExceptionUtils.stacktraceToString(e));
						e.printStackTrace();
					}
				} else {
					// If thread is terminated, remove it from the list.
					if(t.getState() == Thread.State.TERMINATED) {
						activeThreads--;
						iter.remove();
					}
					if(activeThreads < maxThreads) {
						if(t.getState() == Thread.State.NEW) {
							t.start();
							activeThreads++;
						}
					}
				}
			}
		}

		/**
		 * Close Hibernate
		 */
		entityManagerFactory.close();

		logger.info("Import completed successfully. Took " + (float)(new Date().getTime() - startTime) / 1000.0 + "s");
	}
	
	/**
	 * Return L lists of equal size composed with the contents of list
	 * 
	 * Credit: http://stackoverflow.com/questions/2895342/java-how-can-i-split-an-arraylist-in-multiple-small-arraylists
	 * 
	 * @param list - list to be chunked
	 * @param L    - desired size of each list
	 * @return
	 */
	static <T> List<List<T>> chunkList(List<T> list, final int L) {
		List<List<T>> parts = new ArrayList<List<T>>();
		final int N = list.size();
		
		for (int i = 0; i < N; i += L) {
			parts.add(new ArrayList<T>(
					list.subList(i, Math.min(N, i + L)))
					);
		}
		
		return parts;
	}
}
