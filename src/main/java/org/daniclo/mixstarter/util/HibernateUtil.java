package org.daniclo.mixstarter.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    private HibernateUtil(){}

    private static SessionFactory buildSessionFactory(){
        Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
        Properties properties = new Properties();
        try {
            properties.load(Files.newBufferedReader(Path.of("datasource.properties")));

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(properties)
                    .configure("org/daniclo/mixstarter/META-INF/hibernate.cfg.xml").build();

            Metadata metadata = new MetadataSources(serviceRegistry).getMetadataBuilder().build();
            return metadata.buildSessionFactory();
        } catch (IOException e) {
            System.err.println("Properties files was not found.");
        }
        return null;
    }

    public static SessionFactory getSessionFactory(){
        if (sessionFactory == null){
            sessionFactory = buildSessionFactory();
        }
        return sessionFactory;
    }
}
