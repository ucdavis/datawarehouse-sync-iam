package edu.ucdavis.dss.datawarehouse.sync.iam;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
		String filename = System.getProperty("user.home") + File.separator + ".data-warehouse" + File.separator + "settings.properties";
		File propsFile = new File(filename);

		InputStream is;
		try {
			is = new FileInputStream(propsFile);

			Properties prop = new Properties();

			prop.load(is);
			is.close();

			iamApiKey = prop.getProperty("IAM_API_KEY");
			ldapUrl = prop.getProperty("LDAP_URL");
			ldapBase = prop.getProperty("LDAP_BASE");
			ldapUser = prop.getProperty("LDAP_USER");
			ldapPassword = prop.getProperty("LDAP_PASSWORD");
			elasticSearchHost = prop.getProperty("ELASTICSEARCH_HOST");
			
			logger.debug("Settings file '" + filename + "' found.");

			return true;
		} catch (FileNotFoundException e) {
			logger.warn("Could not find " + filename + ".");
			return false;
		} catch (IOException e) {
			logger.error("An IOException occurred while loading " + filename);
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
