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
        logger.error("IamPpsDepartmentsImport START");

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
        logger.error("DEPARTMENTS LOOP START");

        for(IamPpsDepartment department : departments) {
            //logger.error("DEPARTMENT START");
            //logger.error("To String: " + department.toString());

            if (department.getDeptDisplayName() == null) {
                logger.error("skipping department, displayName was null for department: " + department.toString());
                //logger.error("END DEPARTMENT");
                continue;
            }
            
            if (department.getDeptCode().matches("^\\d+$") == false) {
                logger.error("skipping department, deptCode is not a number for department: " + department.toString());
                continue;
            }

            if (department.getDeptCode().length() != 6) {
                logger.error("skipping department, deptCode is not 6 digits long for department: " + department.toString());
                continue;
            }
            
            //logger.error("OrgOId: " + department.getOrgOId());
            //logger.error("officialName: " + department.getDeptOfficialName());
            //logger.error("deptCode: " + department.getDeptCode());

            try {
                //logger.error("SAVE START");
                entityManager.getTransaction().begin();
                entityManager.merge(department);
                entityManager.getTransaction().commit();
                //logger.error("SAVE COMPLETE");
            } catch (javax.persistence.RollbackException e) {
                logger.error("Exception occurred while saving PPS department: " + department);
                logger.error(ExceptionUtils.stacktraceToString(e));
            }

            //logger.error("DEPARTMENT COMPLETE");
        }

        logger.error("DEPARTMENTS LOOP COMPLETE");

        entityManager.close();

        logger.error("IamPpsDepartmentsImport COMPLETE");

        return true;
    }

    public static boolean importBous(EntityManagerFactory entityManagerFactory) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        
        logger.error("importBous START");

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
            logger.error("attempting merge on bou: " + bou.getDeptCode());

            entityManager.merge( bou );
        }
        entityManager.getTransaction().commit();

        entityManager.close();

        return true;
    }
}
