package edu.ucdavis.dss.datawarehouse.sync.iam;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.ucdavis.dss.iam.client.IamClient;
import edu.ucdavis.dss.iam.dtos.IamAssociation;
import edu.ucdavis.dss.iam.dtos.IamDepartment;

public class EntryPoint {
	public static String iamApiKey, localDBUrl, localDBUser, localDBPass;
	static Logger logger = LoggerFactory.getLogger("EntryPoint");

	/**
	 * Main entry point for IAM sync. Run as a console application when
	 * needed. Scheduling once a day via cron is recommended.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SessionFactory sessionFactory = null;
		
		/**
		 * Load the IAM API key from ~/.data-warehouse/settings.properties.
		 * We expect a few keys to exist; see prop.getProperty() lines.
		 */
		String filename = System.getProperty("user.home") + File.separator + ".data-warehouse" + File.separator + "settings.properties";
		File propsFile = new File(filename);

		try {
			InputStream is = new FileInputStream(propsFile);

			Properties prop = new Properties();

			prop.load(is);
			is.close();

			localDBUrl = prop.getProperty("LOCAL_MYSQL_URL");
			localDBUser = prop.getProperty("LOCAL_MYSQL_USER");
			localDBPass = prop.getProperty("LOCAL_MYSQL_PASS");
			iamApiKey = prop.getProperty("IAM_API_KEY");
			
			logger.info("Settings file '" + filename + "' found.");
		} catch (FileNotFoundException e) {
			logger.warn("Could not find " + filename + ".");
			return;
		} catch (IOException e) {
			logger.error("An IOException occurred while loading " + filename);
			return;
		}

		/**
		 * Set up Hibernate
		 */
		// A SessionFactory is set up once for an application!
		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
				.configure() // configures settings from hibernate.cfg.xml
				.build();
		try {
			sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
		} catch (Exception e) {
			// The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
			// so destroy it manually.
			StandardServiceRegistryBuilder.destroy( registry );
			e.printStackTrace();
			return;
		}
		
		/**
		 * Initialize IAM client
		 */
		IamClient iamClient = new IamClient(iamApiKey);

		/**
		 * Extract and load all departments from IAM
		 */
		List<IamDepartment> departments = iamClient.getAllDepartments();
		Session session = sessionFactory.openSession();
		for(IamDepartment department : departments) {
			session.beginTransaction();
			session.save( department );
			session.getTransaction().commit();
		}
		session.close();
		
		/**
		 * Extract and load all people by department from IAM
		 */
		session = sessionFactory.openSession();
		for(IamDepartment department : departments) {
			List<IamAssociation> associations = iamClient.getAllAssociationsForDepartment(department.getDeptCode());
			for(IamAssociation association : associations) {
				session.beginTransaction();
				session.save( association );
				session.getTransaction().commit();
			}
		}
		session.close();

		/**
		 * Close Hibernate
		 */
		if ( sessionFactory != null ) {
			sessionFactory.close();
		}
	}
}
