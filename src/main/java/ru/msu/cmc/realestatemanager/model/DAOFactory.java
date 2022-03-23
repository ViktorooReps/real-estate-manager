package ru.msu.cmc.realestatemanager.model;


import ru.msu.cmc.realestatemanager.model.dao.ClientDAO;
import ru.msu.cmc.realestatemanager.model.dao.OfferDAO;
import ru.msu.cmc.realestatemanager.model.dao.OrderDAO;
import ru.msu.cmc.realestatemanager.model.dao.impl.ClientDAOImpl;
import ru.msu.cmc.realestatemanager.model.dao.impl.OfferDAOImpl;
import ru.msu.cmc.realestatemanager.model.dao.impl.OrderDAOImpl;

public class DAOFactory {

    private static ClientDAOImpl clientDAO = null;
    private static OrderDAOImpl orderDAO = null;
    private static OfferDAOImpl offerDAO = null;
    private static DAOFactory instance = null;

    public static synchronized DAOFactory getInstance(){
        if (instance == null){
            instance = new DAOFactory();
        }
        return instance;
    }

    public ClientDAO getClientDAO(){
        if (clientDAO == null){
            clientDAO = new ClientDAOImpl();
        }
        return clientDAO;
    }

    public OrderDAO getOrderDAO(){
        if (orderDAO == null){
            orderDAO = new OrderDAOImpl();
        }
        return orderDAO;
    }

    public OfferDAO getOfferDAO(){
        if (offerDAO == null){
            offerDAO = new OfferDAOImpl();
        }
        return offerDAO;
    }
}
