package ru.msu.cmc.realestatemanager.model.dao;

import lombok.Builder;
import lombok.Getter;
import ru.msu.cmc.realestatemanager.model.entity.Client;

import java.util.Collection;


public interface ClientDAO extends BaseDAO<Client> {
    @Builder
    @Getter
    class Filter {
        private String clientName;
        private String email;
        private String phoneNumber;
    }

    static Filter.FilterBuilder getFilterBuilder() {
        return Filter.builder();
    }

    Collection<Client> getClientsByFilter(Filter filter);
}
