package edu.ucdavis.dss.datawarehouse.sync.iam;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import edu.ucdavis.dss.datawarehouse.sync.StatusLogger;
import edu.ucdavis.dss.elasticsearch.ESClient;
import edu.ucdavis.dss.iam.dtos.*;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntryPoint {
	static private Logger logger = LoggerFactory.getLogger("EntryPoint");
	static EntityManagerFactory entityManagerFactory = null;
	static EntityManager entityManager = null;
	static ESClient esClient = null;
	static final int maxThreads = 25;
	static final int recordsPerThread = 100;
	static final int expireRecordsOlderThanDays = 28;
	static final long DAY_IN_MS = 86400000;

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
		boolean shouldImportBOUs = true;
		boolean shouldImportPPSDepartments = true;

		logger.info("IAM import started at " + new Date());

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

		StatusLogger.markIamLastAttempt(entityManagerFactory);
		if (shouldImportPPSDepartments) {
			logger.error("Importing PPS departments ...");

			if(IamPpsDepartmentsImport.importPpsDepartments(entityManagerFactory) == false) {
				logger.error("Unable to import PPS departments! Will continue ...");
			}
		}

		if (shouldImportBOUs) {
			logger.error("Importing BOUs ...");

			if(IamPpsDepartmentsImport.importBous(entityManagerFactory) == false) {
				logger.error("Unable to import BOUs! Will continue ...");
			}
		}

		List<String> allIamIds = IamIdsImport.importIds();

		logger.debug("Persisting " + allIamIds.size() + " people ...");
		logger.error("Persisting " + allIamIds.size() + " people ...");

		List<Thread> threads = new ArrayList<Thread>();

		List<List<String>> chunkedIamIds = chunkList(allIamIds, recordsPerThread);
		for(List<String> iamIds : chunkedIamIds) {
			Thread t = new Thread(new IamPersonImportThread(iamIds, entityManagerFactory));
			t.setUncaughtExceptionHandler(uncaughtException);
			threads.add(t);
		}

		logger.debug("Queued " + threads.size() + " threads of size " + recordsPerThread);

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

		// We ignore this task if the number of people is under 25,000. UCD should have at least 70,000 as of
		// 3-18-18 and the 25,000 check is to avoid a mistake where the import fails and we start deleting people.
		if(allIamIds.size() > 25000) {
			// Remove people (and their associated records) older than 'expireRecordsOlderThanDays' days
			esClient = new edu.ucdavis.dss.elasticsearch.ESClient(SettingsUtils.getElasticSearchHost());

			if ((entityManager != null) && (entityManager.isOpen())) {
				entityManager.close();
			}
			entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();

			Date expiration = new Date(System.currentTimeMillis() - (expireRecordsOlderThanDays * DAY_IN_MS));
			List<Long> expiredIamIds = entityManager.createQuery("SELECT ci.iamId FROM IamPerson ci WHERE (ci.lastSeen < :expiration) OR (ci.lastSeen is NULL)")
					.setParameter("expiration", expiration).getResultList();

			entityManager.getTransaction().commit();

			for (Long iamId : expiredIamIds) {
				removeAllRecordsForIamId(iamId, entityManager, esClient);
			}

			// Resolve the case where multiple IAM IDs exist for an individual
			List<String> duplicatedUserIds = entityManager.createNativeQuery("select userId from (SELECT userId, COUNT(*) c FROM iam_prikerbacct GROUP BY userId HAVING c > 1) s").getResultList();
			for (String userId: duplicatedUserIds) {
				// A user ID is associated with more than one IAM ID. Pick one.

				// First, if only one IAM ID is active in IAM, choose that one.
				List<BigInteger> redundantIamIds = (List<BigInteger>)entityManager.createNativeQuery("select iamId FROM iam_prikerbacct WHERE userId=:userId order by iamId ASC").setParameter("userId", userId).getResultList();
				List<Long> inactiveIamIds = new ArrayList<>();
				List<Long> activeIamIds = new ArrayList<>();

				for(BigInteger _redundantIamId : redundantIamIds) {
					String redundantIamId = _redundantIamId.toString();

					if(allIamIds.contains(redundantIamId)) {
						activeIamIds.add(Long.parseLong(redundantIamId));
					} else {
						inactiveIamIds.add(Long.parseLong(redundantIamId));
					}
				}

				if(activeIamIds.size() == 1) {
					// Only one of the multiple IAM IDs are active. Delete the others.
					inactiveIamIds.forEach(iamId -> removeAllRecordsForIamId(iamId, entityManager, esClient));
				} else {
					// Multiple IAM IDs are active for the same person. Favor the bigger one (presumed to be newer and correct)

					// Sort IAM IDs so largest (the keeper) is at the end
					Collections.sort(redundantIamIds);

					// Remove the last IAM ID (the keeper)
					redundantIamIds.remove(redundantIamIds.size() - 1);

					redundantIamIds.forEach(iamId -> removeAllRecordsForIamId(iamId.longValue(), entityManager, esClient));
				}
			}

			entityManager.close();
		}

		StatusLogger.markIamLastSuccess(entityManagerFactory);
		StatusLogger.recordIamDuration(entityManagerFactory, (int)(new Date().getTime() - startTime) / 1000);

		/**
		 * Close Hibernate
		 */
		entityManagerFactory.close();

		logger.info("Import completed successfully. Took " + (float)(new Date().getTime() - startTime) / 1000.0 + "s");
	}

	private static void removeAllRecordsForIamId(Long iamId, EntityManager entityManager, ESClient esClient) {
		entityManager.getTransaction().begin();

		List<IamPpsAssociation> ppsAssociations = entityManager.createQuery("SELECT pa FROM IamPpsAssociation pa WHERE pa.iamId = :iamId")
				.setParameter("iamId", iamId).getResultList();

		for (IamPpsAssociation ppsAssociation : ppsAssociations) {
			entityManager.remove(ppsAssociation);
		}

		List<IamSisAssociation> sisAssociations = entityManager.createQuery("SELECT sa FROM IamSisAssociation sa WHERE sa.iamId = :iamId")
				.setParameter("iamId", iamId).getResultList();

		for (IamSisAssociation sisAssociation : sisAssociations) {
			entityManager.remove(sisAssociation);
		}

		List<IamContactInfo> contactInfos = entityManager.createQuery("SELECT ci FROM IamContactInfo ci WHERE ci.iamId = :iamId")
				.setParameter("iamId", iamId).getResultList();

		for (IamContactInfo contactInfo : contactInfos) {
			entityManager.remove(contactInfo);
		}

		List<IamPrikerbacct> prikerbaccts = entityManager.createQuery("SELECT pb FROM IamPrikerbacct pb WHERE pb.iamId = :iamId")
				.setParameter("iamId", iamId).getResultList();

		for (IamPrikerbacct prikerbacct : prikerbaccts) {
			entityManager.remove(prikerbacct);
		}

		List<IamPerson> people = entityManager.createQuery("SELECT p FROM IamPerson p WHERE p.iamId = :iamId")
				.setParameter("iamId", iamId).getResultList();

		for (IamPerson person: people) {
			entityManager.remove(person);
		}

		entityManager.getTransaction().commit();

		// Remove the ElasticSearch records for this IAM ID
		esClient.deleteDocument("dw", "people", iamId.toString());
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
