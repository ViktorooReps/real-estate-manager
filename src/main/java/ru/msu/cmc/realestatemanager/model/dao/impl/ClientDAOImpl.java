package ru.msu.cmc.realestatemanager.model.dao.impl;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import ru.msu.cmc.realestatemanager.model.HibernateConfiguration;
import ru.msu.cmc.realestatemanager.model.dao.ClientDAO;
import ru.msu.cmc.realestatemanager.model.entity.Client;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Transactional
public class ClientDAOImpl extends BaseDAOImpl<Client> implements ClientDAO {

    public ClientDAOImpl() {
        super(Client.class);
    }

    @Override
    public Collection<Client> getClientsByFilter(Filter filter) throws HibernateException {
        Session session = HibernateConfiguration.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Client> criteriaQuery = builder.createQuery(Client.class);
        Root<Client> root = criteriaQuery.from(Client.class);

        List<Predicate> predicates = new ArrayList<>();
        if (filter.getClientName() != null) {
            String pattern = "%" + filter.getClientName() + "%"; // Any name containing substring
            predicates.add(builder.like(root.get("clientName"), pattern));
        }
        if (filter.getEmail() != null) {
            String pattern = "%" + filter.getEmail() + "%"; // Any email containing substring
            predicates.add(builder.like(root.get("email"), pattern));
        }
        if (filter.getPhoneNumber() != null) {
            String pattern = "%" + filter.getPhoneNumber() + "%"; // Any number containing substring
            predicates.add(builder.like(root.get("phoneNumber"), pattern));
        }

        if (predicates.size() != 0)
            criteriaQuery.where(predicates.toArray(new Predicate[0]));

        List<Client> result = session.createQuery(criteriaQuery).getResultList();
        session.getTransaction().commit();
        return result;
    }
}
