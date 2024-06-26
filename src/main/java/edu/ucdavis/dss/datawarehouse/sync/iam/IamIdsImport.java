package edu.ucdavis.dss.datawarehouse.sync.iam;

import edu.ucdavis.dss.iam.client.IamClient;
import edu.ucdavis.dss.iam.dtos.IamPersonIdResult;
import java.time.DayOfWeek;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class IamIdsImport {
    static private Logger logger = LoggerFactory.getLogger("IamIdsImport");

    /**
     * Returns a subset of recently updated IAM Ids on weekdays
     */
    public static List<String> importIds() {
        /**
         * Initialize IAM client
         */
        IamClient iamClient = new IamClient(SettingsUtils.getIamApiKey());

        /**
         * Extract all IDs from IAM
         */
        logger.debug("Fetching all IAM IDs ...");

        List<IamPersonIdResult> personIdResults =
            LocalDate.now().getDayOfWeek() == DayOfWeek.SUNDAY ? iamClient.getAllIamIds() :
                iamClient.getModifiedIamIds();

        if(personIdResults == null) {
            logger.error("Unable to fetch IAM IDs.");
            return null;
        }

        List<String> iamIds = new ArrayList<String>();

        // Extract just the iamId and push to string array
        for(IamPersonIdResult result : personIdResults) {
            iamIds.add(result.getIamId());
        }

        return iamIds;
    }
}
