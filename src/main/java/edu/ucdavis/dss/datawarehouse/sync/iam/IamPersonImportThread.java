package edu.ucdavis.dss.datawarehouse.sync.iam;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import edu.ucdavis.dss.elasticsearch.ESClient;
import edu.ucdavis.dss.iam.client.IamClient;
import edu.ucdavis.dss.iam.dtos.IamPpsAssociation;
import edu.ucdavis.dss.iam.dtos.IamContactInfo;
import edu.ucdavis.dss.iam.dtos.IamPerson;
import edu.ucdavis.dss.iam.dtos.IamPrikerbacct;

public class IamPersonImportThread implements Runnable {
	private List<String> uuids = null;
	private Logger logger = LoggerFactory.getLogger("IamPersonImportThread");
	private EntityManagerFactory entityManagerFactory = null;
	private ESClient client = null;
	private boolean skipElasticUpdate = false;
	private static final int IMPORT_RETRY_COUNT = 5;
	private static final int IMPORT_RETRY_SLEEP_DURATION = 3000; // milliseconds
	
	public IamPersonImportThread(List<String> uuids, EntityManagerFactory entityManagerFactory) {
		this.uuids = uuids;
		this.entityManagerFactory = entityManagerFactory;
	}

	@Override
	public void run() {
		IamClient iamClient = new IamClient(SettingsUtils.getIamApiKey());
		int count = 0;
		Long additionalStartTime = new Date().getTime();
		
		logger.debug("Starting IamPersonImportThread ...");
		
		client = new edu.ucdavis.dss.elasticsearch.ESClient(SettingsUtils.getElasticSearchHost());

		for(String ucdPersonUUID : uuids) {
			int retryCount = 0;
			boolean personImported = false;
			Exception lastException = null;

			while((retryCount < IMPORT_RETRY_COUNT) && (personImported == false)) {
				logger.debug("Importing UUID " + ucdPersonUUID + " ...");

				Long iamId = iamClient.getIamIdFromMothraId(ucdPersonUUID);

				List<IamContactInfo> contactInfos = iamClient.getContactInfo(iamId);
				List<IamPerson> people = iamClient.getPersonInfo(iamId);
				List<IamPrikerbacct> prikerbaccts = iamClient.getPrikerbacct(iamId);
				List<IamPpsAssociation> associations = iamClient.getAllAssociationsForIamId(iamId);

				if ((contactInfos == null) || (people == null) || (prikerbaccts == null) || (associations == null)) {
					logger.warn("Unable to fetch from IAM for IAM ID " + iamId);
					retryCount++;
					continue;
				}

				EntityManager entityManager = null;
				try {
					entityManager = this.entityManagerFactory.createEntityManager();
					entityManager.getTransaction().begin();

					for (IamPpsAssociation association : associations) {
						entityManager.merge(association);
					}

					for (IamContactInfo contactInfo : contactInfos) {
						entityManager.merge(contactInfo);
					}

					for (IamPerson person : people) {
						entityManager.merge(person);
					}

					for (IamPrikerbacct prikerbacct : prikerbaccts) {
						entityManager.merge(prikerbacct);
					}

					entityManager.getTransaction().commit();
					entityManager.close();

					personImported = true;
				} catch (Exception e) {
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

				updateElasticSearchDocument(contactInfos, people, prikerbaccts, associations);
			}

			if(retryCount == IMPORT_RETRY_COUNT) {
				logger.error("Skipping ucdPersonUUID: " + ucdPersonUUID + ". Unable to commit transaction.");
				logger.error("Last exception was:");
				logger.error(ExceptionUtils.stacktraceToString(lastException));
			}

			count++;

			if (count % 250 == 0) {
				float progress = (float) count / (float) uuids.size();
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
			final List<IamPrikerbacct> prikerbaccts, final List<IamPpsAssociation> associations) {

		// Flag will be true if AWS SDK has had trouble this run already, else false.
		if(skipElasticUpdate) return;

		if(people.size() == 0) return;
		if(contactInfos.size() == 0) return;
		if(prikerbaccts.size() == 0) return;
		
		final IamPerson person = people.get(0);
		final IamContactInfo contactInfo = contactInfos.get(0);
		final IamPrikerbacct prikerbacct = prikerbaccts.get(0);
		
		try {
			Gson gson = new Gson();
			ESPersonDTO esPerson = new ESPersonDTO();
			
			esPerson.setIamId(person.getIamId().toString());
			esPerson.setDFirstName(person.getdFirstName());
			esPerson.setDLastName(person.getdLastName());
			esPerson.setUserId(prikerbacct.getUserId());
			esPerson.setEmail(contactInfo.getEmail());
			esPerson.setDMiddleName(person.getdMiddleName());
			esPerson.setOFirstName(person.getoFirstName());
			esPerson.setOMiddleName(person.getoMiddleName());
			esPerson.setOLastName(person.getoLastName());
			esPerson.setDFullName(person.getdFullName());
			esPerson.setOFullName(person.getoFullName());

			esPerson.setPeople(people);
			esPerson.setContactInfos(contactInfos);
			esPerson.setPrikerbaccts(prikerbaccts);
			esPerson.setAssociations(associations);
			
			client.putDocument("dw", "people", person.getIamId().toString(), gson.toJson(esPerson));
		} catch (com.amazonaws.SdkClientException e) {
			logger.error("SdkClientException occurred while updating ElasticSearch:");
			logger.error(ExceptionUtils.stacktraceToString(e));
			skipElasticUpdate = true;
			return;
		} catch (IOException e) {
			logger.error("IOException occurred while updating ElasticSearch:");
			logger.error(ExceptionUtils.stacktraceToString(e));
			return;
		}
	}
}
