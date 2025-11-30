/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import java.io.InputStream;
import java.util.Properties;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;

/**
 *
 * @author PC-MATT
 */
public class HibernateUtil {
    
    private static final SessionFactory sessionFactory;
	static {
		try {
			// TODO: IMPORTANT SECURITY RISK - The Jasypt master password is hardcoded here.
			// This password MUST be externalized and secured in a production environment.
			// Consider using environment variables, system properties, or a secure vault.
			String masterPassword = "pizzamdp_master_password";

			StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
			encryptor.setPassword(masterPassword);
			encryptor.setAlgorithm("PBEWithMD5AndDES"); // Ensure algorithm matches encryption method

			Properties properties = new Properties();
			// Load database configuration from database.properties file
			try (InputStream is = HibernateUtil.class.getClassLoader().getResourceAsStream("database.properties")) {
				if (is == null) {
					throw new RuntimeException("Cannot find database.properties in classpath");
				}
				properties.load(is);
			}

			// Wrap properties with Jasypt for decryption
			EncryptableProperties encryptableProperties = new EncryptableProperties(properties, encryptor);

			// Load database connection details from database.properties
			// The password in database.properties is expected to be in ENC(...) format.
			String username = encryptableProperties.getProperty("database.username");
			String password = encryptableProperties.getProperty("database.password"); // Jasypt decrypts this automatically

			Configuration cfg = new Configuration().configure("hibernate.cfg.xml");
			cfg.setProperty("hibernate.connection.username", username);
			cfg.setProperty("hibernate.connection.password", password);

			sessionFactory = cfg.buildSessionFactory();
                
                
        		}
		catch (Throwable ex) {//NOPMD
			//LoggerManager.logger.error("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

}
