package ru.msu.cmc.realestatemanager.model.dao.impl;

import org.hibernate.Session;
import ru.msu.cmc.realestatemanager.model.HibernateConfiguration;
import ru.msu.cmc.realestatemanager.model.dao.OfferDAO;
import ru.msu.cmc.realestatemanager.model.entity.Offer;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class OfferDAOImpl extends BaseDAOImpl<Offer> implements OfferDAO {

    private Predicate getChoicePredicate(
            Map<String, Boolean> choices,
            CriteriaBuilder builder,
            Root<Offer> root,
            String column
    ) {
        List<Predicate> possible = new ArrayList<>();
        List<Predicate> required = new ArrayList<>();
        for (String choiceValue : choices.keySet()) {
            Boolean isRequested = choices.get(choiceValue);
            Predicate predicate = builder.like(root.get(column), "%" + choiceValue + "%");
            if (!isRequested) {
                required.add(builder.not(predicate));
            } else {
                possible.add(predicate);
            }
        }
        Predicate finalPossible = builder.isFalse(builder.literal(true)); // always false
        for (Predicate possibility : possible)
            finalPossible = builder.or(finalPossible, possibility);

        Predicate finalRequired = builder.isTrue(builder.literal(true)); // always true
        for (Predicate requirement : required)
            finalRequired = builder.and(finalRequired, requirement);

        // should satisfy one of true values and none of false values
        return builder.and(finalPossible, finalRequired);
    }

    public OfferDAOImpl() {
        super(Offer.class);
    }

    @Override
    public Collection<Offer> getOffersByFilter(Filter filter) {
        try (Session session = HibernateConfiguration.getSessionFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Offer> criteriaQuery = builder.createQuery(Offer.class);
            Root<Offer> root = criteriaQuery.from(Offer.class);

            List<Predicate> predicates = new ArrayList<>();
            if (filter.getContractType() != null) {
                String pattern = "%" + filter.getContractType() + "%"; // Any contract type containing substring
                predicates.add(builder.like(root.get("contractType"), pattern));
            }
            if (filter.getRequestedEstateTypes() != null) {
                Map<String, Boolean> requestedTypes = filter.getRequestedEstateTypes();
                predicates.add(getChoicePredicate(requestedTypes, builder, root, "estateType"));
            }
            if (filter.getRequestedEstateFacades() != null) {
                Map<String, Boolean> requestedFacades = filter.getRequestedEstateFacades();
                predicates.add(getChoicePredicate(requestedFacades, builder, root, "estateFacade"));
            }
            if (filter.getRequestedSpaceMin() != null) {
                Map<String, Integer> requestedSpace = filter.getRequestedSpaceMin();
                for (String room : requestedSpace.keySet()) {
                    Integer roomSpace = requestedSpace.get(room);
                    Expression<Integer> actualRoomSpace = builder.function(
                            "json_extract_path_text",
                            String.class,
                            root.get("space"),
                            builder.literal(room)
                    ).as(Integer.class);
                    Predicate notStated = actualRoomSpace.isNull();
                    Predicate moreSpace = builder.ge(actualRoomSpace, builder.literal(roomSpace));
                    predicates.add(builder.or(notStated, moreSpace));
                }
            }
            if (filter.getRequestedCommodities() != null) {
                Map<String, Boolean> requestedCommodities = filter.getRequestedCommodities();
                for (String commodity : requestedCommodities.keySet()) {
                    Boolean isRequested = requestedCommodities.get(commodity);
                    Expression<Boolean> actualCommodities = builder.function(
                            "json_extract_path_text",
                            String.class,
                            root.get("commodities"),
                            builder.literal(commodity)
                    ).as(Boolean.class);
                    Predicate notStated = actualCommodities.isNull();
                    Predicate isPresent = builder.isTrue(actualCommodities);
                    if (!isRequested) {
                        predicates.add(builder.or(notStated, builder.not(isPresent)));
                    } else {
                        predicates.add(builder.or(notStated, isPresent));
                    }
                }
            }
            if (filter.getFloorMin() != null) {
                Integer floorMin = filter.getFloorMin();
                predicates.add(builder.ge(root.get("floor"), builder.literal(floorMin)));
            }
            if (filter.getFloorMax() != null) {
                Integer floorMax = filter.getFloorMax();
                predicates.add(builder.le(root.get("floor"), builder.literal(floorMax)));
            }
            if (filter.getBuildingState() != null) {
                String pattern = "%" + filter.getBuildingState() + "%"; // Any building state containing substring
                predicates.add(builder.like(root.get("buildingState"), pattern));
            }
            if (filter.getRequestedTransportMax() != null) {
                Map<String, Integer> requestedTransport = filter.getRequestedTransportMax();
                for (String transport : requestedTransport.keySet()) {
                    Integer transportDistance = requestedTransport.get(transport);
                    Expression<Integer> actualTransportDistance = builder.function(
                            "json_extract_path_text",
                            String.class,
                            root.get("transport"),
                            builder.literal(transport)
                    ).as(Integer.class);
                    Predicate notStated = actualTransportDistance.isNull();
                    Predicate closer = builder.le(actualTransportDistance, builder.literal(transportDistance));
                    predicates.add(builder.or(notStated, closer));
                }
            }
            if (filter.getLocation() != null) {
                String pattern = "%" + filter.getLocation() + "%"; // Any location containing substring
                predicates.add(builder.like(root.get("location"), pattern));
            }
            if (filter.getStartingPrice() != null) {
                Integer startingPrice = filter.getStartingPrice();
                predicates.add(builder.le(root.get("startingPrice"), builder.literal(startingPrice)));
            }

            if (predicates.size() != 0)
                criteriaQuery.where(predicates.toArray(new Predicate[0]));

            return session.createQuery(criteriaQuery).getResultList();
        }
    }
}
