package ru.msu.cmc.realestatemanager.model;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.msu.cmc.realestatemanager.model.dao.ClientDAO;
import ru.msu.cmc.realestatemanager.model.entity.Client;

public class ClientDAOTest {
    ClientDAO dao;
    Client clientTest1;
    Client clientTest2;

    @BeforeEach
    public void setUp() {
        this.dao = DAOFactory.getInstance().getClientDAO();

        this.clientTest1 = new Client();
        this.clientTest1.setClientName("TestName1");
        this.clientTest1.setEmail("test1@email.com");
        this.clientTest1.setPhoneNumber("+12345123412");

        this.clientTest2 = new Client();
        this.clientTest2.setClientName("TestName2");
        this.clientTest2.setEmail("test2@email.com");
        this.clientTest2.setPhoneNumber("+23451234512");

        this.dao.add(clientTest1);
        this.dao.add(clientTest2);
    }

    @AfterEach
    public void cleanUp() {
        this.dao.delete(this.clientTest1);
        this.dao.delete(this.clientTest2);

        this.dao = null;
        this.clientTest1 = null;
        this.clientTest2 = null;
    }

    @Test
    public void test() {

    }
}
