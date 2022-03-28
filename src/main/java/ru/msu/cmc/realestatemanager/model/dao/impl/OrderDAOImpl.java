package ru.msu.cmc.realestatemanager.model.dao.impl;


import org.hibernate.HibernateException;
import org.hibernate.Session;
import ru.msu.cmc.realestatemanager.model.HibernateConfiguration;
import ru.msu.cmc.realestatemanager.model.dao.OrderDAO;
import ru.msu.cmc.realestatemanager.model.entity.Order;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class OrderDAOImpl extends BaseDAOImpl<Order> implements OrderDAO {

    public OrderDAOImpl() {
        super(Order.class);
    }

    @Override
    public Collection<Order> getOrdersByFilter(Filter filter) throws HibernateException {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = builder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);

        List<Predicate> predicates = new ArrayList<>();
        if (filter.getContractType() != null) {
            String pattern = "%" + filter.getContractType() + "%"; // Any contract type containing substring
            predicates.add(builder.like(root.get("contractType"), pattern));
        }
        if (filter.getEstateType() != null) {
            String estateType = filter.getEstateType();
            Expression<Boolean> isNeeded = builder.function(
                    "json_extract_path_text",
                    String.class,
                    root.get("requestedEstateTypes"),
                    builder.literal(estateType)
            ).as(Boolean.class);
            predicates.add(builder.isTrue(isNeeded));
        }
        if (filter.getEstateFacade() != null) {
            String estateFacade = filter.getEstateFacade();
            Expression<Boolean> isNeeded = builder.function(
                    "json_extract_path_text",
                    String.class,
                    root.get("requestedEstateFacades"),
                    builder.literal(estateFacade)
            ).as(Boolean.class);
            predicates.add(builder.isTrue(isNeeded));
        }
        if (filter.getSpace() != null) {
            Map<String, Integer> space = filter.getSpace();
            for (String room : space.keySet()) {
                Integer roomSpace = space.get(room);
                Expression<Integer> requestedMinRoomSpace = builder.function(
                        "json_extract_path_text",
                        String.class,
                        root.get("requestedSpaceMin"),
                        builder.literal(room)
                ).as(Integer.class);
                Predicate notStated = requestedMinRoomSpace.isNull();
                Predicate moreSpace = builder.le(requestedMinRoomSpace, builder.literal(roomSpace));
                predicates.add(builder.or(notStated, moreSpace));
            }
        }
        if (filter.getCommodities() != null) {
            Map<String, Boolean> commodities = filter.getCommodities();
            for (String commodity : commodities.keySet()) {
                Boolean isPresent = commodities.get(commodity);
                Expression<Boolean> requestedCommodities = builder.function(
                        "json_extract_path_text",
                        String.class,
                        root.get("requestedCommodities"),
                        builder.literal(commodity)
                ).as(Boolean.class);
                Predicate notStated = requestedCommodities.isNull();
                Predicate matches = builder.equal(requestedCommodities, builder.literal(isPresent));
                predicates.add(builder.or(notStated, matches));
            }
        }
        if (filter.getFloor() != null) {
            Integer floor = filter.getFloor();
            Predicate moreThanMin = builder.ge(root.get("floorMin"), builder.literal(floor));
            Predicate lessThanMax = builder.le(root.get("floorMax"), builder.literal(floor));
            predicates.add(builder.and(moreThanMin, lessThanMax));
        }
        if (filter.getBuildingState() != null) {
            String pattern = "%" + filter.getBuildingState() + "%"; // Any building state containing substring
            predicates.add(builder.like(root.get("building_state"), pattern));
        }
        if (filter.getTransport() != null) {
            Map<String, Integer> transport = filter.getTransport();
            for (String transportType : transport.keySet()) {
                Integer transportDistance = transport.get(transportType);
                Expression<Integer> requestedTransportDistance = builder.function(
                        "json_extract_path_text",
                        String.class,
                        root.get("requestedTransportMax"),
                        builder.literal(transportType)
                ).as(Integer.class);
                Predicate notStated = requestedTransportDistance.isNull();
                Predicate closer = builder.le(builder.literal(transportDistance), requestedTransportDistance);
                predicates.add(builder.or(notStated, closer));
            }
        }
        if (filter.getLocation() != null) {
            String pattern = "%" + filter.getLocation() + "%"; // Any location containing substring
            predicates.add(builder.like(root.get("location"), pattern));
        }
        if (filter.getStartingPrice() != null) {
            Integer startingPrice = filter.getStartingPrice();
            predicates.add(builder.le(builder.literal(startingPrice), root.get("startingPrice")));
        }

        if (predicates.size() != 0)
            criteriaQuery.where(predicates.toArray(new Predicate[0]));

        return session.createQuery(criteriaQuery).getResultList();
    }
}
