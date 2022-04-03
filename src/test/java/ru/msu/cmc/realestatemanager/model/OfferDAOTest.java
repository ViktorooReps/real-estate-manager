package ru.msu.cmc.realestatemanager.model;


import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.msu.cmc.realestatemanager.model.dao.ClientDAO;
import ru.msu.cmc.realestatemanager.model.dao.OfferDAO;
import ru.msu.cmc.realestatemanager.model.entity.Client;
import ru.msu.cmc.realestatemanager.model.entity.Offer;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OfferDAOTest {
    OfferDAO dao;
    String runId;
    Client client1;
    Client client2;
    Offer offer1;
    Offer offer2;

    @BeforeEach
    public void setUp() {
        this.dao = DAOFactory.getInstance().getOfferDAO();
        this.runId = UUID.randomUUID().toString();

        this.client1 = new Client();
        this.client1.setClientName("TestName1" + this.runId);
        this.client1.setEmail("test1" + this.runId + "@mail.com");
        this.client1.setPhoneNumber("+1" + this.runId);

        this.offer1 = new Offer();
        this.offer1.setOfferedBy(client1);
        this.offer1.setContractType("ct1" + this.runId);
        this.offer1.setEstateType("et1" + this.runId);
        this.offer1.setEstateFacade("ef1" + this.runId);
        this.offer1.setSpace((new JSONObject(new HashMap<>() {{ put("k" + runId, 1); }})).toString());
        this.offer1.setCommodities((new JSONObject(new HashMap<>() {{ put("k" + runId, true); }})).toString());
        this.offer1.setFloor(1);
        this.offer1.setBuildingState("bs1" + runId);
        this.offer1.setTransport((new JSONObject(new HashMap<>() {{ put("k" + runId, 1); }})).toString());
        this.offer1.setLocation("l1" + this.runId);
        this.offer1.setStartingPrice(1);
        this.offer1.setAddress("a1" + this.runId);

        this.client2 = new Client();
        this.client2.setClientName("TestName2" + this.runId);
        this.client2.setEmail("test2" + this.runId + "@mail.com");
        this.client2.setPhoneNumber("+2" + this.runId);

        this.offer2 = new Offer();
        this.offer2.setOfferedBy(client2);
        this.offer2.setContractType("ct2" + this.runId);
        this.offer2.setEstateType("et2" + this.runId);
        this.offer2.setEstateFacade("ef2" + this.runId);
        this.offer2.setSpace((new JSONObject(new HashMap<>() {{ put("k" + runId, 2); }})).toString());
        this.offer2.setCommodities((new JSONObject(new HashMap<>() {{ put("k" + runId, false); }})).toString());
        this.offer2.setFloor(2);
        this.offer2.setBuildingState("bs2" + runId);
        this.offer2.setTransport((new JSONObject(new HashMap<>() {{ put("k" + runId, 2); }})).toString());
        this.offer2.setLocation("l2" + this.runId);
        this.offer2.setStartingPrice(2);
        this.offer2.setAddress("a2" + this.runId);

        ClientDAO clientDAO = DAOFactory.getInstance().getClientDAO();
        clientDAO.add(this.client1);
        clientDAO.add(this.client2);

        this.dao.add(this.offer1);
        this.dao.add(this.offer2);
    }

    @AfterEach
    public void cleanUp() {
        this.dao.delete(this.offer1);
        this.dao.delete(this.offer2);

        ClientDAO clientDAO = DAOFactory.getInstance().getClientDAO();
        clientDAO.delete(this.client1);
        clientDAO.delete(this.client2);

        this.dao = null;
        this.runId = null;
        this.client1 = null;
        this.client2 = null;
        this.offer1 = null;
        this.offer2 = null;
    }

    @Test
    public void testGetByContractType() {
        Collection<Offer> all = this.dao.getOffersByFilter(
                OfferDAO.getFilterBuilder()
                        .contractType(this.runId)
                        .build()
        );

        Set<Offer> expected = new HashSet<>();
        expected.add(this.offer1);
        expected.add(this.offer2);

        Set<Offer> got = new HashSet<>(all);

        assertEquals(expected, got);

        Collection<Offer> onlyTest1 = this.dao.getOffersByFilter(
                OfferDAO.getFilterBuilder()
                        .contractType("ct1" + this.runId)
                        .build()
        );

        expected = new HashSet<>();
        expected.add(this.offer1);

        got = new HashSet<>(onlyTest1);

        assertEquals(expected, got);
    }

    @Test
    public void testGetByEstateType() {
        Collection<Offer> all = this.dao.getOffersByFilter(
                OfferDAO.getFilterBuilder()
                        .requestedEstateTypes(new HashMap<>() {{
                            put("et1" + runId, true);
                            put("et2" + runId, true);
                        }})
                        .build()
        );

        Set<Offer> expected = new HashSet<>();
        expected.add(this.offer1);
        expected.add(this.offer2);

        Set<Offer> got = new HashSet<>(all);

        assertEquals(expected, got);

        Collection<Offer> onlyTest1 = this.dao.getOffersByFilter(
                OfferDAO.getFilterBuilder()
                        .requestedEstateTypes(new HashMap<>() {{ put("et1" + runId, true); }})
                        .build()
        );

        expected = new HashSet<>();
        expected.add(this.offer1);

        got = new HashSet<>(onlyTest1);

        assertEquals(expected, got);
    }

    @Test
    public void testGetByEstateFacade() {
        Collection<Offer> all = this.dao.getOffersByFilter(
                OfferDAO.getFilterBuilder()
                        .requestedEstateFacades(new HashMap<>() {{
                            put("ef1" + runId, true);
                            put("ef2" + runId, true);
                        }})
                        .build()
        );

        Set<Offer> expected = new HashSet<>();
        expected.add(this.offer1);
        expected.add(this.offer2);

        Set<Offer> got = new HashSet<>(all);

        assertEquals(expected, got);

        Collection<Offer> onlyTest1 = this.dao.getOffersByFilter(
                OfferDAO.getFilterBuilder()
                        .requestedEstateFacades(new HashMap<>() {{ put("ef1" + runId, true); }})
                        .build()
        );

        expected = new HashSet<>();
        expected.add(this.offer1);

        got = new HashSet<>(onlyTest1);

        assertEquals(expected, got);
    }

    @Test
    public void testGetBySpace() {
        Collection<Offer> all = this.dao.getOffersByFilter(
                OfferDAO.getFilterBuilder()
                        .requestedSpaceMin(new HashMap<>() {{ put("k" + runId, 1); }})
                        .build()
        );

        Set<Offer> expected = new HashSet<>();
        expected.add(this.offer1);
        expected.add(this.offer2);

        Set<Offer> got = new HashSet<>(all);

        assertEquals(expected, got);

        Collection<Offer> onlyTest2 = this.dao.getOffersByFilter(
                OfferDAO.getFilterBuilder()
                        .requestedSpaceMin(new HashMap<>() {{ put("k" + runId, 2); }})
                        .build()
        );

        expected = new HashSet<>();
        expected.add(this.offer2);

        got = new HashSet<>(onlyTest2);

        assertEquals(expected, got);
    }

    @Test
    public void testGetByCommodities() {
        Collection<Offer> all = this.dao.getOffersByFilter(
                OfferDAO.getFilterBuilder()
                        .requestedCommodities(new HashMap<>() {})
                        .build()
        );

        Set<Offer> expected = new HashSet<>();
        expected.add(this.offer1);
        expected.add(this.offer2);

        Set<Offer> got = new HashSet<>(all);

        assertEquals(expected, got);

        Collection<Offer> onlyTest1 = this.dao.getOffersByFilter(
                OfferDAO.getFilterBuilder()
                        .requestedCommodities(new HashMap<>() {{ put("k" + runId, true); }})
                        .build()
        );

        expected = new HashSet<>();
        expected.add(this.offer1);

        got = new HashSet<>(onlyTest1);

        assertEquals(expected, got);
    }

    @Test
    public void testGetByFloor() {
        Collection<Offer> all = this.dao.getOffersByFilter(
                OfferDAO.getFilterBuilder()
                        .floorMin(1)
                        .build()
        );

        Set<Offer> expected = new HashSet<>();
        expected.add(this.offer1);
        expected.add(this.offer2);

        Set<Offer> got = new HashSet<>(all);

        assertEquals(expected, got);

        Collection<Offer> onlyTest1 = this.dao.getOffersByFilter(
                OfferDAO.getFilterBuilder()
                        .floorMax(1)
                        .build()
        );

        expected = new HashSet<>();
        expected.add(this.offer1);

        got = new HashSet<>(onlyTest1);

        assertEquals(expected, got);
    }

    @Test
    public void testGetByBuildingState() {
        Collection<Offer> all = this.dao.getOffersByFilter(
                OfferDAO.getFilterBuilder()
                        .buildingState(this.runId)
                        .build()
        );

        Set<Offer> expected = new HashSet<>();
        expected.add(this.offer1);
        expected.add(this.offer2);

        Set<Offer> got = new HashSet<>(all);

        assertEquals(expected, got);

        Collection<Offer> onlyTest1 = this.dao.getOffersByFilter(
                OfferDAO.getFilterBuilder()
                        .buildingState("bs1" + this.runId)
                        .build()
        );

        expected = new HashSet<>();
        expected.add(this.offer1);

        got = new HashSet<>(onlyTest1);

        assertEquals(expected, got);
    }

    @Test
    public void testGetByTransport() {
        Collection<Offer> all = this.dao.getOffersByFilter(
                OfferDAO.getFilterBuilder()
                        .requestedTransportMax(new HashMap<>() {{ put("k" + runId, 10); }})
                        .build()
        );

        Set<Offer> expected = new HashSet<>();
        expected.add(this.offer1);
        expected.add(this.offer2);

        Set<Offer> got = new HashSet<>(all);

        assertEquals(expected, got);

        Collection<Offer> onlyTest1 = this.dao.getOffersByFilter(
                OfferDAO.getFilterBuilder()
                        .requestedTransportMax(new HashMap<>() {{ put("k" + runId, 1); }})
                        .build()
        );

        expected = new HashSet<>();
        expected.add(this.offer1);

        got = new HashSet<>(onlyTest1);

        assertEquals(expected, got);
    }

    @Test
    public void testGetByLocation() {
        Collection<Offer> all = this.dao.getOffersByFilter(
                OfferDAO.getFilterBuilder()
                        .location(this.runId)
                        .build()
        );

        Set<Offer> expected = new HashSet<>();
        expected.add(this.offer1);
        expected.add(this.offer2);

        Set<Offer> got = new HashSet<>(all);

        assertEquals(expected, got);

        Collection<Offer> onlyTest1 = this.dao.getOffersByFilter(
                OfferDAO.getFilterBuilder()
                        .location("l1" + this.runId)
                        .build()
        );

        expected = new HashSet<>();
        expected.add(this.offer1);

        got = new HashSet<>(onlyTest1);

        assertEquals(expected, got);
    }

    @Test
    public void testGetByStartingPrice() {
        Collection<Offer> all = this.dao.getOffersByFilter(
                OfferDAO.getFilterBuilder()
                        .startingPrice(10)
                        .build()
        );

        Set<Offer> expected = new HashSet<>();
        expected.add(this.offer1);
        expected.add(this.offer2);

        Set<Offer> got = new HashSet<>(all);

        assertEquals(expected, got);

        Collection<Offer> onlyTest1 = this.dao.getOffersByFilter(
                OfferDAO.getFilterBuilder()
                        .startingPrice(1)
                        .build()
        );

        expected = new HashSet<>();
        expected.add(this.offer1);

        got = new HashSet<>(onlyTest1);

        assertEquals(expected, got);
    }
}