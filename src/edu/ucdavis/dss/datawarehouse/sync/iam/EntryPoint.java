package edu.ucdavis.dss.datawarehouse.sync.iam;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntryPoint {
	public static String iamApiKey;
	static Logger logger = LoggerFactory.getLogger("EntryPoint");

	/**
	 * Main entry point for IAM sync. Run as a console application when
	 * needed. Scheduling once a day via cron is recommended.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		/**
		 * Load the IAM API key from ~/.data-warehouse/settings.properties.
		 * We expect a key to exist called IAM_API_KEY.
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
			
			logger.info("Settings file '" + filename + "' found.");
		} catch (FileNotFoundException e) {
			logger.warn("Could not find " + filename + ".");
			return;
		} catch (IOException e) {
			logger.error("An IOException occurred while loading " + filename);
			return;
		}


	}

}
