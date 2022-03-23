package ru.msu.cmc.realestatemanager.model.dao.impl;

import org.hibernate.Session;
import ru.msu.cmc.realestatemanager.model.HibernateConfiguration;
import ru.msu.cmc.realestatemanager.model.dao.BaseDAO;

import javax.swing.*;

public class BaseDAOImpl<SomeEntity> implements BaseDAO<SomeEntity> {

    Class<SomeEntity> entityClass;

    public BaseDAOImpl(Class<SomeEntity> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public void add(SomeEntity entity) {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(entity);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void update(SomeEntity entity) {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        session.beginTransaction();
        session.update(entity);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void delete(SomeEntity entity) {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(entity);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public SomeEntity getById(Long id) {
        Session session = HibernateConfiguration.getSessionFactory().openSession();

        try {
            SomeEntity entity = session.load(this.entityClass, id);
            session.close();
            return entity;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка 'getClientById'",
                    JOptionPane.WARNING_MESSAGE);
        }

        session.close();
        return null;
    }
}
