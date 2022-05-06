package ru.msu.cmc.realestatemanager.model.dao;

import ru.msu.cmc.realestatemanager.model.entity.Client;
import ru.msu.cmc.realestatemanager.model.entity.Offer;
import ru.msu.cmc.realestatemanager.model.entity.Order;

import java.util.Collection;

public interface ClientActions {

    Collection<Order> getClientOrders(Client client);

    Collection<Offer> getClientOffers(Client client);
}
