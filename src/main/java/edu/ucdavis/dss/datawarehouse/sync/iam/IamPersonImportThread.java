package edu.ucdavis.dss.datawarehouse.sync.iam;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import edu.ucdavis.dss.iam.dtos.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import edu.ucdavis.dss.elasticsearch.ESClient;
import edu.ucdavis.dss.iam.client.IamClient;

public class IamPersonImportThread implements Runnable {
	private List<String> iamIds = null;
	private Logger logger = LoggerFactory.getLogger("IamPersonImportThread");
	private EntityManagerFactory entityManagerFactory = null;
	private ESClient client = null;
	private boolean skipElasticUpdate = false;
	private static final int IMPORT_RETRY_COUNT = 10;
	private static final int IMPORT_RETRY_SLEEP_DURATION = 5000; // milliseconds
	
	public IamPersonImportThread(List<String> iamIds, EntityManagerFactory entityManagerFactory) {
		this.iamIds = iamIds;
		this.entityManagerFactory = entityManagerFactory;
	}

	@Override
	public void run() {
		IamClient iamClient = new IamClient(SettingsUtils.getIamApiKey());
		int count = 0;
		Long additionalStartTime = new Date().getTime();
		
		logger.debug("Starting IamPersonImportThread ...");
		
		client = new edu.ucdavis.dss.elasticsearch.ESClient(SettingsUtils.getElasticSearchHost());

		for(String iamId_s : iamIds) {
			int retryCount = 0;
			boolean personImported = false;
			Exception lastException = null;

			while((retryCount < IMPORT_RETRY_COUNT) && (personImported == false)) {
				logger.debug("Importing UUID " + iamId_s + " ...");

				Long iamId = Long.parseLong(iamId_s);

				List<IamContactInfo> contactInfos = iamClient.getContactInfo(iamId);
				List<IamPerson> iamPeople = iamClient.getPersonInfo(iamId);
				List<IamPrikerbacct> prikerbaccts = iamClient.getPrikerbacct(iamId);
				List<IamPpsAssociation> ppsAssociations = iamClient.getAllPpsAssociationsForIamId(iamId);
				List<IamSisAssociation> sisAssociations = iamClient.getAllSisAssociationsForIamId(iamId);

				if ((contactInfos == null) || (iamPeople == null) || (prikerbaccts == null) || (ppsAssociations == null)) {
					logger.warn("Unable to fetch from IAM for IAM ID " + iamId);
					retryCount++;
					continue;
				}

				// IAM has returned multiple records for the same IAM ID in the past, i.e. the same person, listed twice
				// Sanitize the data in this case by only accepting one Person record.
				if(iamPeople.size() > 1) {
					IamPerson p = iamPeople.get(0);
					iamPeople.clear();
					iamPeople.add(p);
				}

				EntityManager entityManager = null;
				try {
					entityManager = this.entityManagerFactory.createEntityManager();
					entityManager.getTransaction().begin();

					if(ppsAssociations != null) {
						List<IamPpsAssociation> existingPpsAssociations = entityManager.createQuery("SELECT pa FROM IamPpsAssociation pa WHERE pa.iamId = :iamId")
								.setParameter("iamId", iamId).getResultList();
						for (IamPpsAssociation association : ppsAssociations) {
							if(existingPpsAssociations.contains(association) == false) {
								entityManager.persist(association);
							} else {
								existingPpsAssociations.remove(association);
							}
						}

						// Remaining existingPpsAssociations should be deleted
						for (IamPpsAssociation association : existingPpsAssociations) {
							entityManager.remove(association);
						}
					}

					if(sisAssociations != null) {
						List<IamSisAssociation> existingSisAssociations = entityManager.createQuery("SELECT sa FROM IamSisAssociation sa WHERE sa.iamId = :iamId")
								.setParameter("iamId", iamId).getResultList();

						for (IamSisAssociation association : sisAssociations) {
							if(existingSisAssociations.contains(association) == false) {
								entityManager.persist(association);
							} else {
								existingSisAssociations.remove(association);
							}
						}

						// Remaining existingSisAssociations should be deleted
						for (IamSisAssociation association : existingSisAssociations) {
							entityManager.remove(association);
						}
					}

					if(contactInfos != null) {
						List<IamContactInfo> existingContactInfos = entityManager.createQuery("SELECT ci FROM IamContactInfo ci WHERE ci.iamId = :iamId")
								.setParameter("iamId", iamId).getResultList();

						for (IamContactInfo contactInfo : contactInfos) {
							if(existingContactInfos.contains(contactInfo) == false) {
								entityManager.persist(contactInfo);
							} else {
								existingContactInfos.remove(contactInfo);
							}
						}

						// Remaining existingContactInfos should be deleted
						for (IamContactInfo contactInfo : existingContactInfos) {
							entityManager.remove(contactInfo);
						}
					}

					if(prikerbaccts != null) {
						List<IamPrikerbacct> existingPrikerbaccts = entityManager.createQuery("SELECT pb FROM IamPrikerbacct pb WHERE pb.iamId = :iamId")
								.setParameter("iamId", iamId).getResultList();

						for (IamPrikerbacct prikerbacct : prikerbaccts) {
							if(existingPrikerbaccts.contains(prikerbacct) == false) {
								entityManager.persist(prikerbacct);
							} else {
								existingPrikerbaccts.remove(prikerbacct);
							}
						}

						// Remaining existingContactInfos should be deleted
						for (IamPrikerbacct prikerbacct : existingPrikerbaccts) {
							entityManager.remove(prikerbacct);
						}
					}

					if(iamPeople != null) {
						List<IamPerson> existingPersons = entityManager.createQuery("SELECT p FROM IamPerson p WHERE p.iamId = :iamId")
								.setParameter("iamId", iamId).getResultList();

						for (IamPerson person : iamPeople) {
							if (existingPersons.contains(person) == false) {
								// If we don't have this person record locally, save it
								person.setLastSeen(new Date());
								entityManager.persist(person);
							} else {
								// If we do have this person record locally, remove from 'existingPersons' to mark that we're done parsing it
								existingPersons.remove(person);
							}
						}

						// Anything remaining in existingPersons neither needs saving, nor is something we already have.
						// Therefore, it is local data that is no longer needed.
						// We leave person records for a number of days after they disappear upstream however.
						// We do not delete them right away. See the cleanup logic at the end of EntryPoint.
						for (IamPerson person : existingPersons) {
							entityManager.remove(person);
						}
					}

					entityManager.getTransaction().commit();
					entityManager.close();

					personImported = true;
				} catch (Exception e) {
					logger.debug("Received exception persisting IAM ID: " + iamId + ". Retry count " + retryCount);
					logger.debug("Exception:");
					logger.debug(ExceptionUtils.stacktraceToString(e));

					lastException = e;
					entityManager.getTransaction().rollback();
					if (entityManager.isOpen()) entityManager.close();
					retryCount++;

					try {
						Thread.sleep(IMPORT_RETRY_SLEEP_DURATION);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}

					continue;
				}

				updateElasticSearchDocument(contactInfos, iamPeople, prikerbaccts, ppsAssociations, sisAssociations);
			}

			if(retryCount == IMPORT_RETRY_COUNT) {
				logger.error("Skipping IAM ID: " + iamId_s + ". Unable to commit transaction.");
				logger.error("Last exception:");
				logger.error(ExceptionUtils.stacktraceToString(lastException));
			}

			count++;

			if (count % 250 == 0) {
				float progress = (float) count / (float) iamIds.size();
				long currentTime = new Date().getTime();
				long timeSoFar = currentTime - additionalStartTime;
				Date estCompleted = new Date(additionalStartTime + (long) ((float) timeSoFar / progress));
				String logMsg = String.format("\tProgress: %.2f%% (est. completion at %s)", progress * (float) 100, estCompleted.toString());
				logger.debug(logMsg);
				logger.debug("Based on:\n\tprogress: " + progress + "\n\tcurrentTime: " + currentTime + "\n\ttimeSoFar: " + timeSoFar);
			}
		}
	}
	
	private void updateElasticSearchDocument(final List<IamContactInfo> contactInfos, final List<IamPerson> people,
			final List<IamPrikerbacct> prikerbaccts, final List<IamPpsAssociation> ppsAssociations,
											 final List<IamSisAssociation> sisAssociations) {

		// Flag will be true if AWS SDK has had trouble this run already, else false.
		if(skipElasticUpdate) return;

		if(people.size() == 0) return;

		final IamPerson person = people.get(0);

		try {
			Gson gson = new Gson();
			ESPersonDTO esPerson = new ESPersonDTO();
			
			esPerson.setIamId(person.getIamId().toString());
			esPerson.setDFirstName(person.getdFirstName());
			esPerson.setDLastName(person.getdLastName());
			if(prikerbaccts.size() > 0) {
				// A person can exist without a prikerbacct
				esPerson.setUserId(prikerbaccts.get(0).getUserId());
			}
			if(contactInfos.size() > 0) {
				// A person can exist with no contact info
				esPerson.setEmail(contactInfos.get(0).getEmail());
			}
			esPerson.setDMiddleName(person.getdMiddleName());
			esPerson.setOFirstName(person.getoFirstName());
			esPerson.setOMiddleName(person.getoMiddleName());
			esPerson.setOLastName(person.getoLastName());
			esPerson.setDFullName(person.getdFullName());
			esPerson.setOFullName(person.getoFullName());

			esPerson.setPeople(people);
			esPerson.setContactInfos(contactInfos);
			esPerson.setPrikerbaccts(prikerbaccts);
			esPerson.setPpsAssociations(ppsAssociations);
			esPerson.setSisAssociations(sisAssociations);

			esPerson.setLastSeen(new Date());
			
			client.putDocument("dw", "people", person.getIamId().toString(), gson.toJson(esPerson));
		} catch (com.amazonaws.SdkClientException e) {
			logger.error("SdkClientException occurred while updating ElasticSearch:");
			logger.error(ExceptionUtils.stacktraceToString(e));
			skipElasticUpdate = true;
			return;
		}
	}
}
