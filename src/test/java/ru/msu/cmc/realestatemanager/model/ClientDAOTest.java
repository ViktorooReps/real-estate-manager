package ru.msu.cmc.realestatemanager.model;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.msu.cmc.realestatemanager.model.dao.ClientDAO;
import ru.msu.cmc.realestatemanager.model.entity.Client;

public class ClientDAOTest {
    @BeforeEach
    public void setUp() {

    }

    @AfterEach
    public void cleanUp() {

    }

    @Test
    public void test() {
        ClientDAO dao = DAOFactory.getInstance().getClientDAO();
        Client client = dao.getById(1);
        System.out.println(client);
    }
}
