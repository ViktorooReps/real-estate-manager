package ru.msu.cmc.realestatemanager.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.msu.cmc.realestatemanager.model.dao.BaseDAO;

import java.util.ArrayList;
import java.util.List;

public class BaseDAOTest {
    List<BaseDAO> daoList;


    @BeforeEach
    public void setUp() {
        this.daoList = new ArrayList<>();
        this.daoList.add(DAOFactory.getInstance().getClientDAO());
        this.daoList.add(DAOFactory.getInstance().getOfferDAO());
        this.daoList.add(DAOFactory.getInstance().getOrderDAO());
    }

    @AfterEach
    public void cleanUp() {
        this.daoList = null;
    }

    @Test
    public void testAdd() {

    }

    @Test
    public void testUpdate() {

    }

    @Test
    public void testDelete() {

    }
}
