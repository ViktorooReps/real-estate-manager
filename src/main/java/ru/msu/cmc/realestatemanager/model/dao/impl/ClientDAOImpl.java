package ru.msu.cmc.realestatemanager.model.dao.impl;

import org.hibernate.Session;
import ru.msu.cmc.realestatemanager.model.HibernateConfiguration;
import ru.msu.cmc.realestatemanager.model.dao.ClientDAO;
import ru.msu.cmc.realestatemanager.model.entity.Client;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ClientDAOImpl extends BaseDAOImpl<Client> implements ClientDAO {

    public ClientDAOImpl() {
        super(Client.class);
    }

    @Override
    public Collection<Client> getClientsByFilter(Filter filter) {
        try (Session session = HibernateConfiguration.getSessionFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Client> criteriaQuery = builder.createQuery(Client.class);
            Root<Client> root = criteriaQuery.from(Client.class);

            List<Predicate> predicates = new ArrayList<>();
            if (filter.getClientName() != null) {
                String pattern = "%" + filter.getClientName() + "%"; // Any name containing substring
                predicates.add(builder.like(root.get("client_name"), pattern));
            }
            if (filter.getEmail() != null) {
                String pattern = "%" + filter.getEmail() + "%"; // Any email containing substring
                predicates.add(builder.like(root.get("email"), pattern));
            }
            if (filter.getPhoneNumber() != null) {
                String pattern = "%" + filter.getPhoneNumber() + "%"; // Any number containing substring
                predicates.add(builder.like(root.get("phone_number"), pattern));
            }

            if (predicates.size() != 0)
                criteriaQuery.where(predicates.toArray(new Predicate[0]));

            return session.createQuery(criteriaQuery).getResultList();
        }
    }
}
