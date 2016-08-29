package edu.ucdavis.dss.datawarehouse.sync.iam;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.ucdavis.dss.iam.client.IamClient;
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
		//SessionFactory sessionFactory = null;
		EntityManagerFactory entityManagerFactory = null;
		
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
//		// A SessionFactory is set up once for an application!
//		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
//				.configure() // configures settings from hibernate.cfg.xml
//				.build();
//		try {
//			sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
//		} catch (Exception e) {
//			// The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
//			// so destroy it manually.
//			StandardServiceRegistryBuilder.destroy( registry );
//			e.printStackTrace();
//			return;
//		}
		entityManagerFactory = Persistence.createEntityManagerFactory( "edu.ucdavis.dss.datawarehouse.sync.iam" );
		
		/**
		 * Initialize IAM client
		 */
		IamClient iamClient = new IamClient(iamApiKey);
		
		Session session = null;

		/**
		 * Extract and load all departments from IAM
		 */
		List<IamDepartment> departments = iamClient.getAllDepartments();
//		Session session = sessionFactory.openSession();
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		for(IamDepartment department : departments) {
//			session.beginTransaction();
			entityManager.persist( department );
//			session.save( department );
//			session.getTransaction().commit();
		}
		entityManager.getTransaction().commit();
		entityManager.close();
//		session.close();
		
		/**
		 * Extract and load all associations by department from IAM
		 */
//		session = sessionFactory.openSession();
//		for(IamDepartment department : departments) {
//			List<IamAssociation> associations = iamClient.getAllAssociationsForDepartment(department.getDeptCode());
//			for(IamAssociation association : associations) {
//				session.beginTransaction();
//				try {
//					session.save( association );
//				} catch (DataException e) {
//					logger.debug("Exception while saving object: " + association);
//					e.printStackTrace();
//				}
//				session.getTransaction().commit();
//			}
//		}
//		session.close();
		
		/**
		 * Use all known associations to fetch contact information, login IDs, etc.
		 */
		

		/**
		 * Close Hibernate
		 */
//		if ( sessionFactory != null ) {
//			sessionFactory.close();
//		}
		entityManagerFactory.close();
	}
}
