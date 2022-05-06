package ru.msu.cmc.realestatemanager.controller;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.msu.cmc.realestatemanager.controller.util.Converter;
import ru.msu.cmc.realestatemanager.model.DAOFactory;
import ru.msu.cmc.realestatemanager.model.dao.ClientDAO;
import ru.msu.cmc.realestatemanager.model.dao.OrderDAO;
import ru.msu.cmc.realestatemanager.model.entity.Client;
import ru.msu.cmc.realestatemanager.model.entity.Order;

import java.util.*;

@Controller
public class OrderController {

    @Autowired
    private final OrderDAO orderDAO = DAOFactory.getInstance().getOrderDAO();

    @Autowired
    private final ClientDAO clientDAO = DAOFactory.getInstance().getClientDAO();

    @GetMapping("/order")
    public String orderPage(@RequestParam(name = "orderId") Integer orderId, Model model) {
        try {
            Order order = orderDAO.getById(orderId);
            if (order == null) {
                model.addAttribute("error_msg", "No order in database with ID = " + orderId);
                return "errorPage";
            }

            model.addAttribute("order", order);
            model.addAttribute("conversionService", new Converter());
            return "order";
        } catch (HibernateException e) {
            model.addAttribute("error_msg", e.getMessage());
            return "errorPage";
        }
    }

    @GetMapping("/editOrder")
    public String editOrderPage(@RequestParam Map<String, String> allParams, Model model) {
        String contractType = null;
        String buildingState = null;
        Integer floorMin = null;
        Integer floorMax = null;
        Integer orderId = null;
        Integer orderedById = null;
        Integer priceMax = null;
        Map<String, Boolean> requestedEstateTypes = new HashMap<>();
        Map<String, Boolean> requestedEstateFacades = new HashMap<>();
        Map<String, Boolean> requestedLocations = new HashMap<>();
        Map<String, Boolean> requestedCommodities = new HashMap<>();
        Map<String, Integer> requestedSpaceMin = new HashMap<>();
        Map<String, Integer> requestedTransportMax = new HashMap<>();

        String newEstateType = null;
        String newEstateFacade = null;
        String newLocation = null;
        String newCommodity = null;
        String newRoom = null;
        String newTransport = null;

        for (String key : allParams.keySet()) {
            String value = allParams.get(key);
            if (Objects.equals(value, ""))
                continue;

            if (key.startsWith("contractType"))
                contractType = value;
            if (key.startsWith("buildingState"))
                buildingState = value;

            if (key.startsWith("floorMin"))
                floorMin = Integer.parseInt(value);
            if (key.startsWith("floorMax"))
                floorMax = Integer.parseInt(value);
            if (key.startsWith("orderId"))
                orderId = Integer.parseInt(value);
            if (key.startsWith("orderedById"))
                orderedById = Integer.parseInt(value);
            if (key.startsWith("priceMax"))
                priceMax = Integer.parseInt(value);

            if (key.startsWith("requestedEstateTypes")) {
                String estateType = key.split("__")[1];
                requestedEstateTypes.put(estateType, Objects.equals(value, "True"));
            }
            if (key.startsWith("requestedEstateFacades")) {
                String estateFacade = key.split("__")[1];
                requestedEstateFacades.put(estateFacade, Objects.equals(value, "True"));
            }
            if (key.startsWith("requestedLocations")) {
                String location = key.split("__")[1];
                requestedLocations.put(location, Objects.equals(value, "True"));
            }
            if (key.startsWith("requestedCommodities")) {
                String commodity = key.split("__")[1];
                requestedCommodities.put(commodity, Objects.equals(value, "True"));
            }

            if (key.startsWith("requestedSpaceMin")) {
                String roomName = key.split("__")[1];
                requestedSpaceMin.put(roomName, Integer.parseInt(value));
            }
            if (key.startsWith("requestedTransportMax")) {
                String transport = key.split("__")[1];
                requestedTransportMax.put(transport, Integer.parseInt(value));
            }

            if (key.startsWith("newEstateType") && !Objects.equals(value, ""))
                newEstateType = value;
            if (key.startsWith("newEstateFacade") && !Objects.equals(value, ""))
                newEstateFacade = value;
            if (key.startsWith("newLocation") && !Objects.equals(value, ""))
                newLocation = value;
            if (key.startsWith("newCommodity") && !Objects.equals(value, ""))
                newCommodity = value;
            if (key.startsWith("newRoom") && !Objects.equals(value, ""))
                newRoom = value;
            if (key.startsWith("newTransport") && !Objects.equals(value, ""))
                newTransport = value;
        }

        if (newEstateType != null)
            requestedEstateTypes.put(newEstateType, Boolean.TRUE);
        if (newEstateFacade != null)
            requestedEstateFacades.put(newEstateFacade, Boolean.TRUE);
        if (newLocation != null)
            requestedLocations.put(newLocation, Boolean.TRUE);
        if (newCommodity != null)
            requestedCommodities.put(newCommodity, Boolean.TRUE);
        if (newRoom != null)
            requestedSpaceMin.put(newRoom, 1);
        if (newTransport != null)
            requestedTransportMax.put(newTransport, 1);

        Order order;
        if (orderId == null) {
            if (orderedById == null) {
                model.addAttribute("error_msg", "Cannot create order with no author");
                return "errorPage";
            }
            Client orderedBy = clientDAO.getById(orderedById);
            if (orderedBy == null) {
                model.addAttribute("error_msg", "No client with ID = " + orderedById);
                return "errorPage";
            }
            order = new Order();
            order.setOrderedBy(orderedBy);
        } else {
            try {
                order = orderDAO.getById(orderId);

                if (order == null) {
                    model.addAttribute("error_msg", "No order in database with ID = " + orderId);
                    return "errorPage";
                }

                if (contractType == null)
                    contractType = order.getContractType();
                if (buildingState == null)
                    buildingState = order.getBuildingState();
                if (floorMin == null)
                    floorMin = order.getFloorMin();
                if (floorMax == null)
                    floorMax = order.getFloorMax();
                if (orderedById == null)
                    orderedById = order.getOrderedBy().getId();
                if (priceMax == null)
                    priceMax = order.getPriceMax();

                Map<String, Boolean> estatesTypesMap = Converter.getMap(order.getRequestedEstateTypes());
                for (String estateType : estatesTypesMap.keySet()) {
                    if (!requestedEstateTypes.containsKey(estateType))
                        requestedEstateTypes.put(estateType, estatesTypesMap.get(estateType));
                }

                Map<String, Boolean> estateFacadesMap = Converter.getMap(order.getRequestedEstateFacades());
                for (String estateFacade : estateFacadesMap.keySet()) {
                    if (!requestedEstateFacades.containsKey(estateFacade))
                        requestedEstateFacades.put(estateFacade, estateFacadesMap.get(estateFacade));
                }

                Map<String, Boolean> locationsMap = Converter.getMap(order.getRequestedLocations());
                for (String location : locationsMap.keySet()) {
                    if (!requestedLocations.containsKey(location))
                        requestedLocations.put(location, locationsMap.get(location));
                }

                Map<String, Boolean> commoditiesMap = Converter.getMap(order.getRequestedCommodities());
                for (String commodity : commoditiesMap.keySet()) {
                    if (!requestedCommodities.containsKey(commodity))
                        requestedCommodities.put(commodity, commoditiesMap.get(commodity));
                }

                Map<String, Double> spaceMap = Converter.getMap(order.getRequestedSpaceMin());
                for (String roomName : spaceMap.keySet()) {
                    if (!requestedSpaceMin.containsKey(roomName))
                        requestedSpaceMin.put(roomName, spaceMap.get(roomName).intValue());
                }

                Map<String, Double> transportMap = Converter.getMap(order.getRequestedTransportMax());
                for (String transportName : transportMap.keySet()) {
                    if (!requestedTransportMax.containsKey(transportName))
                        requestedTransportMax.put(transportName, transportMap.get(transportName).intValue());
                }
            } catch (HibernateException e) {
                model.addAttribute("error_msg", e.getMessage());
                return "errorPage";
            }
        }

        model.addAttribute("clientService", clientDAO);

        model.addAttribute("contractType", contractType);
        model.addAttribute("buildingState", buildingState);
        model.addAttribute("floorMin", floorMin);
        model.addAttribute("floorMax", floorMax);
        model.addAttribute("orderId", orderId);
        model.addAttribute("orderedById", orderedById);
        model.addAttribute("priceMax", priceMax);
        model.addAttribute("requestedEstateTypes", requestedEstateTypes);
        model.addAttribute("requestedEstateFacades", requestedEstateFacades);
        model.addAttribute("requestedLocations", requestedLocations);
        model.addAttribute("requestedCommodities", requestedCommodities);
        model.addAttribute("requestedSpaceMin", requestedSpaceMin);
        model.addAttribute("requestedTransportMax", requestedTransportMax);
        return "editOrder";
    }

    @GetMapping("/searchOrder")
    public String searchOrderPage(@RequestParam Map<String, String> allParams, Model model) {
        String contractType = null;
        String estateType = null;
        String estateFacade = null;
        String buildingState = null;
        String location = null;
        Integer floor = null;
        Integer startingPrice = null;
        Map<String, Boolean> commodities = new LinkedHashMap<>();
        Map<String, Integer> roomSpace = new LinkedHashMap<>();
        Map<String, Integer> transportDistance = new LinkedHashMap<>();

        String newCommodity = null;
        String newRoom = null;
        String newTransport = null;

        for (String key : allParams.keySet()) {
            String value = allParams.get(key);
            if (Objects.equals(value, ""))
                continue;

            if (key.startsWith("contractType"))
                contractType = value;
            if (key.startsWith("estateType"))
                estateType = value;
            if (key.startsWith("estateFacade"))
                estateFacade = value;
            if (key.startsWith("buildingState"))
                buildingState = value;
            if (key.startsWith("location"))
                location = value;

            if (Objects.equals(value, ""))
                continue;

            if (key.startsWith("floor"))
                floor = Integer.parseInt(value);
            if (key.startsWith("startingPrice"))
                startingPrice = Integer.parseInt(value);

            if (key.startsWith("commodities")) {
                String commodityName = key.split("__")[1];
                commodities.put(commodityName, Objects.equals(value, "True"));
            }

            if (key.startsWith("roomSpace")) {
                String roomName = key.split("__")[1];
                roomSpace.put(roomName, Integer.parseInt(value));
            }
            if (key.startsWith("transportDistance")) {
                String transport = key.split("__")[1];
                transportDistance.put(transport, Integer.parseInt(value));
            }

            if (key.startsWith("newCommodity") && !Objects.equals(value, ""))
                newCommodity = value;
            if (key.startsWith("newRoom") && !Objects.equals(value, ""))
                newRoom = value;
            if (key.startsWith("newTransport") && !Objects.equals(value, ""))
                newTransport = value;
        }

        if (newCommodity != null)
            commodities.put(newCommodity, Boolean.TRUE);
        if (newRoom != null)
            roomSpace.put(newRoom, 1);
        if (newTransport != null)
            transportDistance.put(newTransport, 1);

        Collection<Order> orders;
        try {
            orders = orderDAO.getOrdersByFilter(OrderDAO.getFilterBuilder()
                    .contractType(contractType)
                    .estateType(estateType)
                    .estateFacade(estateFacade)
                    .buildingState(buildingState)
                    .location(location)
                    .floor(floor)
                    .startingPrice(startingPrice)
                    .commodities(commodities)
                    .space(roomSpace)
                    .transport(transportDistance)
                    .build());
        } catch (HibernateException e) {
            model.addAttribute("error_msg", e.getMessage());
            return "errorPage";
        }
        model.addAttribute("orders", orders);
        model.addAttribute("conversionService", new Converter());

        model.addAttribute("contractType", contractType);
        model.addAttribute("estateType", estateType);
        model.addAttribute("estateFacade", estateFacade);
        model.addAttribute("buildingState", buildingState);
        model.addAttribute("location", location);
        model.addAttribute("floor", floor);
        model.addAttribute("startingPrice", startingPrice);
        model.addAttribute("commodities", commodities);
        model.addAttribute("roomSpace", roomSpace);
        model.addAttribute("transportDistance", transportDistance);
        return "searchOrder";
    }

    @PostMapping("/saveOrder")
    public String saveOrderPage(@RequestParam Map<String, String> allParams, Model model) {
        String contractType = null;
        String buildingState = null;
        Integer floorMin = null;
        Integer floorMax = null;
        Integer orderId = null;
        Integer orderedById = null;
        Integer priceMax = null;
        Map<String, Boolean> requestedEstateTypes = new HashMap<>();
        Map<String, Boolean> requestedEstateFacades = new HashMap<>();
        Map<String, Boolean> requestedLocations = new HashMap<>();
        Map<String, Boolean> requestedCommodities = new HashMap<>();
        Map<String, Integer> requestedSpaceMin = new HashMap<>();
        Map<String, Integer> requestedTransportMax = new HashMap<>();

        for (String key : allParams.keySet()) {
            String value = allParams.get(key);
            if (Objects.equals(value, ""))
                continue;

            if (key.startsWith("contractType"))
                contractType = value;
            if (key.startsWith("buildingState"))
                buildingState = value;

            if (key.startsWith("floorMin"))
                floorMin = Integer.parseInt(value);
            if (key.startsWith("floorMax"))
                floorMax = Integer.parseInt(value);
            if (key.startsWith("orderId"))
                orderId = Integer.parseInt(value);
            if (key.startsWith("orderedById"))
                orderedById = Integer.parseInt(value);
            if (key.startsWith("priceMax"))
                priceMax = Integer.parseInt(value);

            if (key.startsWith("requestedEstateTypes")) {
                String estateType = key.split("__")[1];
                requestedEstateTypes.put(estateType, Objects.equals(value, "True"));
            }
            if (key.startsWith("requestedEstateFacades")) {
                String estateFacade = key.split("__")[1];
                requestedEstateFacades.put(estateFacade, Objects.equals(value, "True"));
            }
            if (key.startsWith("requestedLocations")) {
                String location = key.split("__")[1];
                requestedLocations.put(location, Objects.equals(value, "True"));
            }
            if (key.startsWith("requestedCommodities")) {
                String commodity = key.split("__")[1];
                requestedCommodities.put(commodity, Objects.equals(value, "True"));
            }

            if (key.startsWith("requestedSpaceMin")) {
                String roomName = key.split("__")[1];
                requestedSpaceMin.put(roomName, Integer.parseInt(value));
            }
            if (key.startsWith("requestedTransportMax")) {
                String transport = key.split("__")[1];
                requestedTransportMax.put(transport, Integer.parseInt(value));
            }
        }

        Order order;
        try {
            Client orderedBy = clientDAO.getById(orderedById);
            if (orderedBy == null) {
                model.addAttribute("error_msg", "No client with ID = " + orderedById);
                return "errorPage";
            }
            if (orderId == null) {
                order = new Order();

                order.setContractType(contractType);
                order.setBuildingState(buildingState);
                order.setFloorMin(floorMin);
                order.setFloorMax(floorMax);
                order.setOrderedBy(orderedBy);
                order.setPriceMax(priceMax);
                order.setRequestedEstateTypes(Converter.getJson(requestedEstateTypes));
                order.setRequestedEstateFacades(Converter.getJson(requestedEstateFacades));
                order.setRequestedLocations(Converter.getJson(requestedLocations));
                order.setRequestedCommodities(Converter.getJson(requestedCommodities));
                order.setRequestedSpaceMin(Converter.getJson(requestedSpaceMin));
                order.setRequestedTransportMax(Converter.getJson(requestedTransportMax));

                orderDAO.add(order);
            } else {
                order = orderDAO.getById(orderId);

                if (order == null) {
                    model.addAttribute("error_msg", "No order in database with ID = " + orderId);
                    return "errorPage";
                }

                order.setContractType(contractType);
                order.setBuildingState(buildingState);
                order.setFloorMin(floorMin);
                order.setFloorMax(floorMax);
                order.setOrderedBy(orderedBy);
                order.setPriceMax(priceMax);
                order.setRequestedEstateTypes(Converter.getJson(requestedEstateTypes));
                order.setRequestedEstateFacades(Converter.getJson(requestedEstateFacades));
                order.setRequestedLocations(Converter.getJson(requestedLocations));
                order.setRequestedCommodities(Converter.getJson(requestedCommodities));
                order.setRequestedSpaceMin(Converter.getJson(requestedSpaceMin));
                order.setRequestedTransportMax(Converter.getJson(requestedTransportMax));

                orderDAO.update(order);
            }
        } catch (HibernateException e) {
            model.addAttribute("error_msg", e.getMessage());
            return "errorPage";
        }

        model.addAttribute("order", order);
        model.addAttribute("conversionService", new Converter());
        return "redirect:/order?orderId=" + order.getId();
    }

    @PostMapping("/removeOrder")
    public String removeOrderPage(@RequestParam(name = "orderId") Integer orderId, Model model) {
        try {
            Order order = orderDAO.getById(orderId);

            if (order == null) {
                model.addAttribute("error_msg", "No order in database with ID = " + orderId);
                return "errorPage";
            }

            orderDAO.delete(order);
        } catch (HibernateException e) {
            model.addAttribute("error_msg", e.getMessage());
            return "errorPage";
        }

        return "redirect:/searchOrder";
    }
}
