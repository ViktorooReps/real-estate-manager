package ru.msu.cmc.realestatemanager.model;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateConfiguration {
    private static final SessionFactory sessionFactory;
    static { sessionFactory = new Configuration().configure().buildSessionFactory(); }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
