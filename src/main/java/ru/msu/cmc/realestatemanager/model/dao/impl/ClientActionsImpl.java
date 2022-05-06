package ru.msu.cmc.realestatemanager.model.dao.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.realestatemanager.model.HibernateConfiguration;
import ru.msu.cmc.realestatemanager.model.dao.ClientActions;
import ru.msu.cmc.realestatemanager.model.entity.Client;
import ru.msu.cmc.realestatemanager.model.entity.Offer;
import ru.msu.cmc.realestatemanager.model.entity.Order;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Transactional
@Repository
public class ClientActionsImpl implements ClientActions {

    @Override
    public Collection<Order> getClientOrders(Client client) {
        Session session = HibernateConfiguration.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = builder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(builder.equal(root.get("orderedBy"), client));

        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        List<Order> result = session.createQuery(criteriaQuery).getResultList();
        session.getTransaction().commit();
        return result;
    }

    @Override
    public Collection<Offer> getClientOffers(Client client) {
        Session session = HibernateConfiguration.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Offer> criteriaQuery = builder.createQuery(Offer.class);
        Root<Offer> root = criteriaQuery.from(Offer.class);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(builder.equal(root.get("offeredBy"), client));

        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        List<Offer> result = session.createQuery(criteriaQuery).getResultList();
        session.getTransaction().commit();
        return result;
    }
}
