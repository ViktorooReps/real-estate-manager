package ru.msu.cmc.realestatemanager.model.dao;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import ru.msu.cmc.realestatemanager.model.entity.Offer;

import java.util.Collection;
import java.util.Map;

public interface OfferDAO extends BaseDAO<Offer> {

    // filter represents potential order
    @Builder
    @Getter
    @ToString
    class Filter {
        String contractType;
        Map<String, Boolean> requestedEstateTypes;
        Map<String, Boolean> requestedEstateFacades;
        Map<String, Integer> requestedSpaceMin;
        Map<String, Boolean> requestedCommodities;
        Integer floorMin;
        Integer floorMax;
        String buildingState;
        Map<String, Integer> requestedTransportMax;
        String location;
        Integer startingPrice;
    }

    static Filter.FilterBuilder getFilterBuilder() {
        return Filter.builder();
    }

    Collection<Offer> getOffersByFilter(Filter filter);
}
