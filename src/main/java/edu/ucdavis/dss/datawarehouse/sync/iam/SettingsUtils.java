package edu.ucdavis.dss.datawarehouse.sync.iam;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SettingsUtils {
	private static Logger logger = LoggerFactory.getLogger("SettingsUtils");
	private static String iamApiKey, ldapUrl, ldapBase, ldapUser, ldapPassword;
	private static String elasticSearchHost;

	public static boolean initialize() {
		/**
		 * Load needed database settings
		 */
		try {
			Map<String, String> env = System.getenv();

			iamApiKey = env.get("IAM_API_KEY");
			ldapUrl = env.get("LDAP_URL");
			ldapBase = env.get("LDAP_BASE");
			ldapUser = env.get("LDAP_USER");
			ldapPassword = env.get("LDAP_PASSWORD");
			elasticSearchHost = env.get("ELASTICSEARCH_HOST");
			
			if (iamApiKey != null && ldapUrl != null && ldapBase != null && ldapUser != null && ldapPassword != null && elasticSearchHost != null) {
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

	public static String getLdapUrl() {
		return ldapUrl;
	}

	public static String getLdapBase() {
		return ldapBase;
	}

	public static String getLdapUser() {
		return ldapUser;
	}

	public static String getLdapPassword() {
		return ldapPassword;
	}

	public static String getIamApiKey() {
		return iamApiKey;
	}

	public static String getElasticSearchHost() {
		return elasticSearchHost;
	}
}
