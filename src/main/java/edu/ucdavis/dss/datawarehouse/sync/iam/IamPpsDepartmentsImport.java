package edu.ucdavis.dss.datawarehouse.sync.iam;

import edu.ucdavis.dss.datawarehouse.sync.StatusLogger;
import edu.ucdavis.dss.iam.client.IamClient;
import edu.ucdavis.dss.iam.dtos.IamBou;
import edu.ucdavis.dss.iam.dtos.IamPpsDepartment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class IamPpsDepartmentsImport {
    static private Logger logger = LoggerFactory.getLogger("IamPpsDepartmentsImport");
    private static final int IMPORT_RETRY_COUNT = 5;
    private static final int IMPORT_RETRY_SLEEP_DURATION = 3000; // milliseconds

    public static boolean importPpsDepartments(EntityManagerFactory entityManagerFactory) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        /**
         * Initialize IAM client
         */
        IamClient iamClient = new IamClient(SettingsUtils.getIamApiKey());

        /**
         * Extract and load all PPS departments from IAM
         */
        logger.debug("Persisting all PPS departments ...");
        int retryCount = 0;
        boolean importSuccess = false;

        List<IamPpsDepartment> departments = null;

        while(retryCount < IMPORT_RETRY_COUNT && !importSuccess) {
            departments = iamClient.getAllPpsDepartments();

            if(departments != null) {
                importSuccess = true;
            } else {
                retryCount++;

                try {
                    Thread.sleep(IMPORT_RETRY_SLEEP_DURATION);
                } catch (InterruptedException e) {
                    logger.error(ExceptionUtils.stacktraceToString(e));
                }
            }
        }

        if(departments == null) {
            logger.error("Unable to fetch departments after " + retryCount + " attempts.");
            return false;
        }

        for(IamPpsDepartment department : departments) {
            if (department.getDeptDisplayName() == null) { continue; }

            logger.info("attempting department");
            logger.info("To String: " + department.toString());
            logger.info("OrgOId: " + department.getOrgOId());
            logger.info("officialName: " + department.getDeptOfficialName());
            logger.info("deptCode: " + department.getDeptCode());

            try {
                entityManager.getTransaction().begin();
                entityManager.merge(department);
                entityManager.getTransaction().commit();
            } catch (javax.persistence.RollbackException e) {
                logger.error("Exception occurred while saving PPS department: " + department);
                logger.error(ExceptionUtils.stacktraceToString(e));
            }
        }

        entityManager.close();

        return true;
    }

    public static boolean importBous(EntityManagerFactory entityManagerFactory) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        /**
         * Initialize IAM client
         */
        IamClient iamClient = new IamClient(SettingsUtils.getIamApiKey());

        /**
         * Extract and load all BOUs (business office units) from IAM
         */
        logger.debug("Persisting all BOUs departments ...");
        int retryCount = 0;
        boolean importSuccess = false;

        List<IamBou> bous = null;

        while(retryCount < IMPORT_RETRY_COUNT && !importSuccess) {
            bous = iamClient.getAllBous();

            if(bous != null) {
                importSuccess = true;
            } else {
                retryCount++;

                try {
                    Thread.sleep(IMPORT_RETRY_SLEEP_DURATION);
                } catch (InterruptedException e) {
                    logger.error(ExceptionUtils.stacktraceToString(e));
                }
            }
        }

        if(bous == null) {
            logger.error("Unable to fetch BOUs after " + retryCount + " attempts.");
            return false;
        }

        entityManager.getTransaction().begin();
        for(IamBou bou : bous) {
            entityManager.merge( bou );
        }
        entityManager.getTransaction().commit();

        entityManager.close();

        return true;
    }
}
