package ru.msu.cmc.realestatemanager.model.dao.impl;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import ru.msu.cmc.realestatemanager.model.HibernateConfiguration;
import ru.msu.cmc.realestatemanager.model.dao.BaseDAO;

import javax.transaction.Transactional;

@Transactional
public class BaseDAOImpl<SomeEntity> implements BaseDAO<SomeEntity> {

    Class<SomeEntity> entityClass;

    public BaseDAOImpl(Class<SomeEntity> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public void add(SomeEntity entity) throws HibernateException {
        Session session = HibernateConfiguration.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.save(entity);
        session.getTransaction().commit();
    }

    @Override
    public void update(SomeEntity entity) throws HibernateException {
        Session session = HibernateConfiguration.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.update(entity);
        session.getTransaction().commit();
    }

    @Override
    public void delete(SomeEntity entity) throws HibernateException {
        Session session = HibernateConfiguration.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.delete(entity);
        session.getTransaction().commit();
    }

    @Override
    public SomeEntity getById(Integer id) throws HibernateException {
        Session session = HibernateConfiguration.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        SomeEntity result = session.load(this.entityClass, id);
        session.getTransaction().commit();
        return result;
    }
}
