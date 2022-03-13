package Model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateConnection {
    public static final String HIBERNATE_CONFIG_FILE = "hibernate.cfg.xml";
    private static SessionFactory sessionFactory = getSessionFactory();

    private static SessionFactory getSessionFactory() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure(HIBERNATE_CONFIG_FILE).build();
        Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();

        return metadata.getSessionFactoryBuilder().build();
    }

    public static synchronized Session getSession() {
        return sessionFactory.openSession();
    }
}
