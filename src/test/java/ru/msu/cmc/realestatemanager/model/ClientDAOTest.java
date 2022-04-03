package ru.msu.cmc.realestatemanager.model;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.msu.cmc.realestatemanager.model.dao.ClientDAO;
import ru.msu.cmc.realestatemanager.model.entity.Client;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientDAOTest {
    ClientDAO dao;
    Client clientTest1;
    Client clientTest2;
    String runId;

    @BeforeEach
    public void setUp() {
        this.dao = DAOFactory.getInstance().getClientDAO();
        this.runId = UUID.randomUUID().toString();

        this.clientTest1 = new Client();
        this.clientTest1.setClientName("TestName1" + this.runId);
        this.clientTest1.setEmail("test1" + this.runId + "@email.com");
        this.clientTest1.setPhoneNumber("+1" + this.runId);

        this.clientTest2 = new Client();
        this.clientTest2.setClientName("TestName2" + this.runId);
        this.clientTest2.setEmail("test2" + this.runId + "@email.com");
        this.clientTest2.setPhoneNumber("+2" + this.runId);

        this.dao.add(clientTest1);
        this.dao.add(clientTest2);
    }

    @AfterEach
    public void cleanUp() {
        this.dao.delete(this.clientTest1);
        this.dao.delete(this.clientTest2);

        this.dao = null;
        this.runId = null;
        this.clientTest1 = null;
        this.clientTest2 = null;
    }

    @Test
    public void testGetByName() {
        Collection<Client> allTestNames = this.dao.getClientsByFilter(
                ClientDAO.getFilterBuilder()
                        .clientName(this.runId)
                        .build()
        );

        Set<Client> expected = new HashSet<>();
        expected.add(this.clientTest1);
        expected.add(this.clientTest2);

        Set<Client> got = new HashSet<>(allTestNames);

        assertEquals(expected, got);

        Collection<Client> onlyTest1 = this.dao.getClientsByFilter(
                ClientDAO.getFilterBuilder()
                        .clientName("TestName1" + this.runId)
                        .build()
        );

        expected = new HashSet<>();
        expected.add(this.clientTest1);

        got = new HashSet<>(onlyTest1);

        assertEquals(expected, got);
    }

    @Test
    public void testGetByEmail() {
        Collection<Client> allTestNames = this.dao.getClientsByFilter(
                ClientDAO.getFilterBuilder()
                        .email(this.runId)
                        .build()
        );

        Set<Client> expected = new HashSet<>();
        expected.add(this.clientTest1);
        expected.add(this.clientTest2);

        Set<Client> got = new HashSet<>(allTestNames);

        assertEquals(expected, got);

        Collection<Client> onlyTest1 = this.dao.getClientsByFilter(
                ClientDAO.getFilterBuilder()
                        .email("test1" + this.runId)
                        .build()
        );

        expected = new HashSet<>();
        expected.add(this.clientTest1);

        got = new HashSet<>(onlyTest1);

        assertEquals(expected, got);
    }

    @Test
    public void testGetByPhoneNumber() {
        Collection<Client> allTestNames = this.dao.getClientsByFilter(
                ClientDAO.getFilterBuilder()
                        .phoneNumber(this.runId)
                        .build()
        );

        Set<Client> expected = new HashSet<>();
        expected.add(this.clientTest1);
        expected.add(this.clientTest2);

        Set<Client> got = new HashSet<>(allTestNames);

        assertEquals(expected, got);

        Collection<Client> onlyTest1 = this.dao.getClientsByFilter(
                ClientDAO.getFilterBuilder()
                        .phoneNumber("+1" + this.runId)
                        .build()
        );

        expected = new HashSet<>();
        expected.add(this.clientTest1);

        got = new HashSet<>(onlyTest1);

        assertEquals(expected, got);
    }
}
