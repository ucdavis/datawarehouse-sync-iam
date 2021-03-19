package edu.ucdavis.dss.datawarehouse.sync.iam;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SettingsUtils {
	private static Logger logger = LoggerFactory.getLogger("SettingsUtils");
	private static String iamApiKey;
	private static String elasticSearchHost;

	public static boolean initialize() {
		/**
		 * Load needed database settings
		 */
		try {
			Map<String, String> env = System.getenv();

			iamApiKey = env.get("IAM_API_KEY");
			elasticSearchHost = env.get("ELASTICSEARCH_HOST");
			
			if (iamApiKey != null && elasticSearchHost != null) {
				return true;
			} else {
				System.out.println("Missing an environment variable.");
				return false;
			}
		} catch (SecurityException e) {
			logger.warn("A SecurityException occured while reading environment.");
			return false;
		}
	}

	public static String getIamApiKey() {
		return iamApiKey;
	}

	public static String getElasticSearchHost() {
		return elasticSearchHost;
	}
}
