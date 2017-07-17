package edu.ucdavis.dss.datawarehouse.sync.iam;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.ucdavis.dss.iam.client.IamClient;
import edu.ucdavis.dss.iam.dtos.IamAssociation;
import edu.ucdavis.dss.iam.dtos.IamContactInfo;
import edu.ucdavis.dss.iam.dtos.IamPerson;
import edu.ucdavis.dss.iam.dtos.IamPrikerbacct;

public class IamPersonImportThread implements Runnable {
	private List<String> uuids = null;
	private Logger logger = LoggerFactory.getLogger("IamPersonImportThread");
	private EntityManager entityManager = null;
	
	public IamPersonImportThread(List<String> uuids, EntityManager entityManager) {
		this.uuids = uuids;
		this.entityManager = entityManager;
	}

	@Override
	public void run() {
		IamClient iamClient = new IamClient(SettingsUtils.getIamApiKey());
		int count = 0;
		Long additionalStartTime = new Date().getTime();
		
		logger.debug("Starting IamPersonImportThread ...");
		
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

			// If an exception happened during the last iteration of the loop,
			// the entityManager is invalid and will need to be recreated.
			if(entityManager.isOpen() == false) {
				logger.error("EntityManager is not open!");
				return;
			}

			entityManager.getTransaction().begin();

			for(IamAssociation association : associations) {
				if(entityManager.isOpen()) {
					try {
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
				logger.error("EntityManager is not open!");
				return;
			}

			for(IamContactInfo contactInfo : contactInfos) {
				if(entityManager.isOpen()) {
					try {
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
				logger.error("EntityManager is not open!");
				return;
			}

			for(IamPerson person : people) {
				if(entityManager.isOpen()) {
					try {
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
				logger.error("EntityManager is not open!");
				return;
			}

			for(IamPrikerbacct prikerbacct : prikerbaccts) {
				if(entityManager.isOpen()) {
					try {
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
				logger.error("EntityManager is not open!");
				return;
			}

			entityManager.getTransaction().commit();

			count++;

			if(count % 100 == 0) {
				float progress = (float)count / (float)uuids.size();
				long currentTime = new Date().getTime();
				long timeSoFar = currentTime - additionalStartTime;
				Date estCompleted = new Date(additionalStartTime + (long)((float)timeSoFar / progress));
				String logMsg = String.format("\tProgress: %.2f%% (est. completion at %s)", progress * (float)100, estCompleted.toString());
				logger.info(logMsg);
				logger.info("Based on:\n\tprogress: " + progress + "\n\tcurrentTime: " + currentTime + "\n\ttimeSoFar: " + timeSoFar);
			}
		}
	}

}
