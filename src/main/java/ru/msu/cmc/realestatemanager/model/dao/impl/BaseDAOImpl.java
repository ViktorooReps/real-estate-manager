package ru.msu.cmc.realestatemanager.model.dao.impl;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import ru.msu.cmc.realestatemanager.model.HibernateConfiguration;
import ru.msu.cmc.realestatemanager.model.dao.BaseDAO;

public class BaseDAOImpl<SomeEntity> implements BaseDAO<SomeEntity> {

    Class<SomeEntity> entityClass;

    public BaseDAOImpl(Class<SomeEntity> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public void add(SomeEntity entity) throws HibernateException {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(entity);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void update(SomeEntity entity) throws HibernateException {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        session.beginTransaction();
        session.update(entity);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void delete(SomeEntity entity) throws HibernateException {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(entity);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public SomeEntity getById(Integer id) throws HibernateException {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        return session.load(this.entityClass, id);
    }
}
