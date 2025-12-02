/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Utility class for managing Hibernate SessionFactory.
 * This class ensures that a single SessionFactory is created and shared across the application.
 *
 * @author PC-MATT
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory;
	static {
		try {
			// Create the SessionFactory from hibernate.cfg.xml
			sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        }
		catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

    /**
     * Gets the singleton instance of the SessionFactory.
     * @return The SessionFactory instance.
     */
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

}
