package ru.msu.cmc.realestatemanager.model;


import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.msu.cmc.realestatemanager.model.dao.ClientDAO;
import ru.msu.cmc.realestatemanager.model.dao.OfferDAO;
import ru.msu.cmc.realestatemanager.model.dao.OrderDAO;
import ru.msu.cmc.realestatemanager.model.entity.Client;
import ru.msu.cmc.realestatemanager.model.entity.Offer;
import ru.msu.cmc.realestatemanager.model.entity.Order;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderDAOTest {
    OrderDAO dao;
    String runId;
    Client client1;
    Client client2;
    Order order1;
    Order order2;

    @BeforeEach
    public void setUp() {
        this.dao = DAOFactory.getInstance().getOrderDAO();
        this.runId = UUID.randomUUID().toString();

        this.client1 = new Client();
        this.client1.setClientName("TestName1" + this.runId);
        this.client1.setEmail("test1" + this.runId + "@mail.com");
        this.client1.setPhoneNumber("+1" + this.runId);

        this.order1 = new Order();
        this.order1.setOrderedBy(client1);
        this.order1.setContractType("ct1" + this.runId);
        this.order1.setRequestedEstateTypes((
                new JSONObject(
                        new HashMap<>(){{
                            put("et1" + runId, true);
                            put("et2" + runId, true);
                        }}
                )
        ).toString());
        this.order1.setRequestedEstateFacades((
                new JSONObject(
                        new HashMap<>(){{
                            put("ef1" + runId, true);
                            put("ef2" + runId, true);
                        }}
                )
        ).toString());
        this.order1.setRequestedSpaceMin((
                new JSONObject(
                        new HashMap<>(){{ put("k" + runId, 1); }}
                )
        ).toString());
        this.order1.setRequestedCommodities((
                new JSONObject(
                        new HashMap<>(){{
                            put("c1" + runId, true);
                            put("c2" + runId, true);
                        }}
                )
        ).toString());
        this.order1.setFloorMin(1);
        this.order1.setFloorMax(2);
        this.order1.setBuildingState("bs1" + this.runId);
        this.order1.setRequestedTransportMax((
                new JSONObject(
                        new HashMap<>(){{ put("k" + runId, 1); }}
                )
        ).toString());
        this.order1.setRequestedLocations((
                new JSONObject(
                        new HashMap<>(){{
                            put("l1" + runId, true);
                            put("l2" + runId, true);
                        }}
                )
        ).toString());
        this.order1.setPriceMax(1);

        this.client2 = new Client();
        this.client2.setClientName("TestName2" + this.runId);
        this.client2.setEmail("test2" + this.runId + "@mail.com");
        this.client2.setPhoneNumber("+2" + this.runId);

        this.order2 = new Order();
        this.order2.setOrderedBy(client2);
        this.order2.setContractType("ct2" + this.runId);
        this.order2.setRequestedEstateTypes((
                new JSONObject(
                        new HashMap<>(){{
                            put("et1" + runId, false);
                            put("et2" + runId, true);
                        }}
                )
        ).toString());
        this.order2.setRequestedEstateFacades((
                new JSONObject(
                        new HashMap<>(){{
                            put("ef1" + runId, false);
                            put("ef2" + runId, true);
                        }}
                )
        ).toString());
        this.order2.setRequestedSpaceMin((
                new JSONObject(
                        new HashMap<>(){{ put("k" + runId, 2); }}
                )
        ).toString());
        this.order2.setRequestedCommodities((
                new JSONObject(
                        new HashMap<>(){{
                            put("c1" + runId, false);
                            put("c2" + runId, true);
                        }}
                )
        ).toString());
        this.order2.setFloorMin(2);
        this.order2.setFloorMax(3);
        this.order2.setBuildingState("bs2" + this.runId);
        this.order2.setRequestedTransportMax((
                new JSONObject(
                        new HashMap<>(){{ put("k" + runId, 2); }}
                )
        ).toString());
        this.order2.setRequestedLocations((
                new JSONObject(
                        new HashMap<>(){{
                            put("l1" + runId, false);
                            put("l2" + runId, true);
                        }}
                )
        ).toString());
        this.order2.setPriceMax(2);

        ClientDAO clientDAO = DAOFactory.getInstance().getClientDAO();
        clientDAO.add(this.client1);
        clientDAO.add(this.client2);

        this.dao.add(this.order1);
        this.dao.add(this.order2);
    }

    @AfterEach
    public void cleanUp() {
        this.dao.delete(this.order1);
        this.dao.delete(this.order2);

        ClientDAO clientDAO = DAOFactory.getInstance().getClientDAO();
        clientDAO.delete(this.client1);
        clientDAO.delete(this.client2);

        this.dao = null;
        this.runId = null;
        this.client1 = null;
        this.client2 = null;
        this.order1 = null;
        this.order2 = null;
    }

    @Test
    public void testGetByContractType() {
        Collection<Order> all = this.dao.getOrdersByFilter(
                OrderDAO.getFilterBuilder()
                        .contractType(this.runId)
                        .build()
        );

        Set<Order> expected = new HashSet<>();
        expected.add(this.order1);
        expected.add(this.order2);

        Set<Order> got = new HashSet<>(all);

        assertEquals(expected, got);

        Collection<Order> onlyTest1 = this.dao.getOrdersByFilter(
                OrderDAO.getFilterBuilder()
                        .contractType("ct1" + this.runId)
                        .build()
        );

        expected = new HashSet<>();
        expected.add(this.order1);

        got = new HashSet<>(onlyTest1);

        assertEquals(expected, got);
    }

    @Test
    public void testGetByEstateType() {
        Collection<Order> all = this.dao.getOrdersByFilter(
                OrderDAO.getFilterBuilder()
                        .estateType("et2" + this.runId)
                        .build()
        );

        Set<Order> expected = new HashSet<>();
        expected.add(this.order1);
        expected.add(this.order2);

        Set<Order> got = new HashSet<>(all);

        assertEquals(expected, got);

        Collection<Order> onlyTest1 = this.dao.getOrdersByFilter(
                OrderDAO.getFilterBuilder()
                        .estateType("et1" + this.runId)
                        .build()
        );

        expected = new HashSet<>();
        expected.add(this.order1);

        got = new HashSet<>(onlyTest1);

        assertEquals(expected, got);
    }

    @Test
    public void testGetByEstateFacade() {
        Collection<Order> all = this.dao.getOrdersByFilter(
                OrderDAO.getFilterBuilder()
                        .estateFacade("ef2" + this.runId)
                        .build()
        );

        Set<Order> expected = new HashSet<>();
        expected.add(this.order1);
        expected.add(this.order2);

        Set<Order> got = new HashSet<>(all);

        assertEquals(expected, got);

        Collection<Order> onlyTest1 = this.dao.getOrdersByFilter(
                OrderDAO.getFilterBuilder()
                        .estateFacade("ef1" + this.runId)
                        .build()
        );

        expected = new HashSet<>();
        expected.add(this.order1);

        got = new HashSet<>(onlyTest1);

        assertEquals(expected, got);
    }

    @Test
    public void testGetBySpace() {
        Collection<Order> all = this.dao.getOrdersByFilter(
                OrderDAO.getFilterBuilder()
                        .space(new HashMap<>() {{ put("k" + runId, 2); }})
                        .build()
        );

        Set<Order> expected = new HashSet<>();
        expected.add(this.order1);
        expected.add(this.order2);

        Set<Order> got = new HashSet<>(all);

        assertEquals(expected, got);

        Collection<Order> onlyTest1 = this.dao.getOrdersByFilter(
                OrderDAO.getFilterBuilder()
                        .space(new HashMap<>() {{ put("k" + runId, 1); }})
                        .build()
        );

        expected = new HashSet<>();
        expected.add(this.order1);

        got = new HashSet<>(onlyTest1);

        assertEquals(expected, got);
    }

    @Test
    public void testGetByCommodities() {
        Collection<Order> all = this.dao.getOrdersByFilter(
                OrderDAO.getFilterBuilder()
                        .commodities(new HashMap<>() {{ put("c2" + runId, true); }})
                        .build()
        );

        Set<Order> expected = new HashSet<>();
        expected.add(this.order1);
        expected.add(this.order2);

        Set<Order> got = new HashSet<>(all);

        assertEquals(expected, got);

        Collection<Order> onlyTest1 = this.dao.getOrdersByFilter(
                OrderDAO.getFilterBuilder()
                        .commodities(new HashMap<>() {{ put("c1" + runId, true); }})
                        .build()
        );

        expected = new HashSet<>();
        expected.add(this.order1);

        got = new HashSet<>(onlyTest1);

        assertEquals(expected, got);
    }

    @Test
    public void testGetByFloor() {
        Collection<Order> all = this.dao.getOrdersByFilter(
                OrderDAO.getFilterBuilder()
                        .floor(2)
                        .build()
        );

        Set<Order> expected = new HashSet<>();
        expected.add(this.order1);
        expected.add(this.order2);

        Set<Order> got = new HashSet<>(all);

        assertEquals(expected, got);

        Collection<Order> onlyTest1 = this.dao.getOrdersByFilter(
                OrderDAO.getFilterBuilder()
                        .floor(1)
                        .build()
        );

        expected = new HashSet<>();
        expected.add(this.order1);

        got = new HashSet<>(onlyTest1);

        assertEquals(expected, got);
    }

    @Test
    public void testGetByBuildingState() {
        Collection<Order> all = this.dao.getOrdersByFilter(
                OrderDAO.getFilterBuilder()
                        .buildingState(this.runId)
                        .build()
        );

        Set<Order> expected = new HashSet<>();
        expected.add(this.order1);
        expected.add(this.order2);

        Set<Order> got = new HashSet<>(all);

        assertEquals(expected, got);

        Collection<Order> onlyTest1 = this.dao.getOrdersByFilter(
                OrderDAO.getFilterBuilder()
                        .buildingState("bs1" + this.runId)
                        .build()
        );

        expected = new HashSet<>();
        expected.add(this.order1);

        got = new HashSet<>(onlyTest1);

        assertEquals(expected, got);
    }

    @Test
    public void testGetByTransport() {
        Collection<Order> all = this.dao.getOrdersByFilter(
                OrderDAO.getFilterBuilder()
                        .transport(new HashMap<>(){{ put("k" + runId, 1); }})
                        .build()
        );

        Set<Order> expected = new HashSet<>();
        expected.add(this.order1);
        expected.add(this.order2);

        Set<Order> got = new HashSet<>(all);

        assertEquals(expected, got);

        Collection<Order> onlyTest2 = this.dao.getOrdersByFilter(
                OrderDAO.getFilterBuilder()
                        .transport(new HashMap<>(){{ put("k" + runId, 2); }})
                        .build()
        );

        expected = new HashSet<>();
        expected.add(this.order2);

        got = new HashSet<>(onlyTest2);

        assertEquals(expected, got);
    }

    @Test
    public void testGetByLocation() {
        Collection<Order> all = this.dao.getOrdersByFilter(
                OrderDAO.getFilterBuilder()
                        .location("l2" + runId)
                        .build()
        );

        Set<Order> expected = new HashSet<>();
        expected.add(this.order1);
        expected.add(this.order2);

        Set<Order> got = new HashSet<>(all);

        assertEquals(expected, got);

        Collection<Order> onlyTest1 = this.dao.getOrdersByFilter(
                OrderDAO.getFilterBuilder()
                        .location("l1" + runId)
                        .build()
        );

        expected = new HashSet<>();
        expected.add(this.order1);

        got = new HashSet<>(onlyTest1);

        assertEquals(expected, got);
    }

    @Test
    public void testGetByStartingPrice() {
        Collection<Order> all = this.dao.getOrdersByFilter(
                OrderDAO.getFilterBuilder()
                        .startingPrice(1)
                        .build()
        );

        Set<Order> expected = new HashSet<>();
        expected.add(this.order1);
        expected.add(this.order2);

        Set<Order> got = new HashSet<>(all);

        assertEquals(expected, got);

        Collection<Order> onlyTest2 = this.dao.getOrdersByFilter(
                OrderDAO.getFilterBuilder()
                        .startingPrice(2)
                        .build()
        );

        expected = new HashSet<>();
        expected.add(this.order2);

        got = new HashSet<>(onlyTest2);

        assertEquals(expected, got);
    }
}
