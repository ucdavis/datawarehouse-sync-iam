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

import edu.ucdavis.dss.elasticsearch.ESClient;
import edu.ucdavis.dss.iam.client.IamClient;
import edu.ucdavis.dss.iam.dtos.IamAssociation;
import edu.ucdavis.dss.iam.dtos.IamContactInfo;
import edu.ucdavis.dss.iam.dtos.IamPerson;
import edu.ucdavis.dss.iam.dtos.IamPrikerbacct;

public class IamPersonImportThread implements Runnable {
	private List<String> uuids = null;
	private Logger logger = LoggerFactory.getLogger("IamPersonImportThread");
	private EntityManagerFactory entityManagerFactory = null;
	private ESClient client = null;
	private boolean skipElasticUpdate = false;
	
	public IamPersonImportThread(List<String> uuids, EntityManagerFactory entityManagerFactory) {
		this.uuids = uuids;
		this.entityManagerFactory = entityManagerFactory;
	}

	@Override
	public void run() {
		IamClient iamClient = new IamClient(SettingsUtils.getIamApiKey());
		EntityManager entityManager = null;
		int count = 0;
		Long additionalStartTime = new Date().getTime();
		
		logger.debug("Starting IamPersonImportThread ...");
		
		client = new edu.ucdavis.dss.elasticsearch.ESClient(SettingsUtils.getElasticSearchHost());

		for(String ucdPersonUUID : uuids) {
			logger.debug("Importing UUID " + ucdPersonUUID + " ...");
			
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
			
			entityManager = this.entityManagerFactory.createEntityManager();

			// If an exception happened during the last iteration of the loop,
			// the entityManager is invalid and will need to be recreated.
			if(entityManager.isOpen() == false) {
				logger.error("EntityManager is not open!");
				continue;
			}
			
			entityManager.getTransaction().begin();

			for(IamAssociation association : associations) {
				if(entityManager.isOpen()) {
					try {
						entityManager.merge( association );
					} catch (Exception e) {
						logger.error("Skipping IAM ID: " + iamId + ". Unable to merge association: " + association);
						e.printStackTrace();
						entityManager.getTransaction().rollback();
						if(entityManager.isOpen()) entityManager.close();
						continue;
					}
				}
			}

//			if(entityManager.isOpen() == false) {
//				logger.error("EntityManager is not open!");
//				continue;
//			}

			for(IamContactInfo contactInfo : contactInfos) {
				if(entityManager.isOpen()) {
					try {
						entityManager.merge( contactInfo );
					} catch (Exception e) {
						logger.error("Skipping IAM ID: " + iamId + ". Unable to merge contactInfo: " + contactInfo);
						e.printStackTrace();
						entityManager.getTransaction().rollback();
						if(entityManager.isOpen()) entityManager.close();
						continue;
					}
				}
			}

//			if(entityManager.isOpen() == false) {
//				logger.error("EntityManager is not open!");
//				continue;
//			}

			for(IamPerson person : people) {
				if(entityManager.isOpen()) {
					try {
						entityManager.merge( person );
					} catch (Exception e) {
						logger.error("Skipping IAM ID: " + iamId + ". Unable to merge person: " + person);
						e.printStackTrace();
						entityManager.getTransaction().rollback();
						if(entityManager.isOpen()) entityManager.close();
						continue;
					}
				}
			}

//			if(entityManager.isOpen() == false) {
//				logger.error("EntityManager is not open!");
//				continue;
//			}

			for(IamPrikerbacct prikerbacct : prikerbaccts) {
				if(entityManager.isOpen()) {
					try {
						entityManager.merge( prikerbacct );
					} catch (Exception e) {
						logger.error("Skipping IAM ID: " + iamId + ". Unable to merge prikerbacct: " + prikerbacct);
						e.printStackTrace();
						entityManager.getTransaction().rollback();
						if(entityManager.isOpen()) entityManager.close();
						continue;
					}
				}
			}

//			if(entityManager.isOpen() == false) {
//				logger.error("EntityManager is not open!");
//				continue;
//			}

			try {
				entityManager.getTransaction().commit();
				entityManager.close();
			} catch (Exception e) {
				logger.error("Skipping IAM ID: " + iamId + ". Unable to commit transaction.");
				e.printStackTrace();
				entityManager.getTransaction().rollback();
				if(entityManager.isOpen()) entityManager.close();
				continue;
			}
			
			updateElasticSearchDocument(contactInfos, people, prikerbaccts, associations);

			count++;

			if(count % 250 == 0) {
				float progress = (float)count / (float)uuids.size();
				long currentTime = new Date().getTime();
				long timeSoFar = currentTime - additionalStartTime;
				Date estCompleted = new Date(additionalStartTime + (long)((float)timeSoFar / progress));
				String logMsg = String.format("\tProgress: %.2f%% (est. completion at %s)", progress * (float)100, estCompleted.toString());
				logger.debug(logMsg);
				logger.debug("Based on:\n\tprogress: " + progress + "\n\tcurrentTime: " + currentTime + "\n\ttimeSoFar: " + timeSoFar);
			}
		}
	}
	
	private void updateElasticSearchDocument(final List<IamContactInfo> contactInfos, final List<IamPerson> people,
			final List<IamPrikerbacct> prikerbaccts, final List<IamAssociation> associations) {

		// Flag will be true if AWS SDK has had trouble this run already, else false.
		if(skipElasticUpdate) return;

		if(people.size() == 0) return;
		if(contactInfos.size() == 0) return;
		if(prikerbaccts.size() == 0) return;
		
		final IamPerson person = people.get(0);
		final IamContactInfo contactInfo = contactInfos.get(0);
		final IamPrikerbacct prikerbacct = prikerbaccts.get(0);
		
		if(people.size() > 1) { logger.warn("More than one IamPerson found."); }
		if(contactInfos.size() > 1) { logger.warn("More than one IamContactInfo found."); }
		if(prikerbaccts.size() > 1) { logger.warn("More than one IamPrikerbacct found."); }
		
		try {
			client.putDocument("dw", "people", person.getIamId().toString(), String.format(
					"{ \"iamId\": \"%s\", \"dFirstName\": \"%s\", \"dLastName\": \"%s\", \"userId\": \"%s\", \"email\": \"%s\", \"dMiddleName\": \"%s\", \"oFirstName\": \"%s\", \"oMiddleName\": \"%s\", \"oLastName\": \"%s\", \"dFullName\": \"%s\", \"oFullName\": \"%s\" }",
					person.getIamId(), person.getdFirstName(), person.getdLastName(), prikerbacct.getUserId(), contactInfo.getEmail(), person.getdMiddleName(), person.getoFirstName(), person.getoMiddleName(), person.getoLastName(), person.getdFullName(), person.getoFullName()));
		} catch (com.amazonaws.SdkClientException e) {
			logger.error("SdkClientException occurred while updating ElasticSearch:");
			logger.error(exceptionStacktraceToString(e));
			skipElasticUpdate = true;
			return;
		} catch (IOException e) {
			logger.error("IOException occurred while updating ElasticSearch:");
			logger.error(exceptionStacktraceToString(e));
			return;
		}
	}
	
	// Credit: http://stackoverflow.com/questions/10120709/difference-between-printstacktrace-and-tostring
	private static String exceptionStacktraceToString(Exception e) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		e.printStackTrace(ps);
		ps.close();
		return baos.toString();
	}
}
