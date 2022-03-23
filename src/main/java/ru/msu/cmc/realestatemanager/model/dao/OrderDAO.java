package ru.msu.cmc.realestatemanager.model.dao;

import lombok.Builder;
import lombok.Getter;
import ru.msu.cmc.realestatemanager.model.entity.Order;

import java.util.Collection;
import java.util.Map;

public interface OrderDAO extends BaseDAO<Order> {

    // filter represents potential offer
    @Builder
    @Getter
    class Filter {
        String contractType;
        String estateType;
        String estateFacade;
        Map<String, Integer> space;
        Map<String, Boolean> commodities;
        Integer floor;
        String buildingState;
        Map<String, Integer> transport;
        String location;
        Integer startingPrice;
    }

    static Filter.FilterBuilder getFilterBuilder() {
        return Filter.builder();
    }

    Collection<Order> getOrdersByFilter(Filter filter);
}
