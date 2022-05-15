package ru.msu.cmc.realestatemanager.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import ru.msu.cmc.realestatemanager.model.dao.OfferDAO;
import ru.msu.cmc.realestatemanager.model.dao.OrderDAO;
import ru.msu.cmc.realestatemanager.model.entity.Client;
import ru.msu.cmc.realestatemanager.model.entity.Offer;
import ru.msu.cmc.realestatemanager.model.entity.Order;

import java.util.*;


@Controller
public class OfferController {

    @Autowired
    private final OfferDAO offerDAO = DAOFactory.getInstance().getOfferDAO();

    @Autowired
    private final OrderDAO orderDAO = DAOFactory.getInstance().getOrderDAO();

    @Autowired
    private final ClientDAO clientDAO = DAOFactory.getInstance().getClientDAO();

    @GetMapping("/offer")
    public String offerPage(@RequestParam Map<String, String> allParams, Model model) {
        Integer offerId = null;
        Map<String, Boolean> includeAttributes = new HashMap<>();
        for (String key : allParams.keySet()) {
            String value = allParams.get(key);
            if (key.startsWith("offerId"))
                offerId = Integer.parseInt(value);

            if (key.startsWith("includeAttribute")) {
                String attributeName = key.split("__")[1];
                includeAttributes.put(attributeName, Objects.equals(value, "True"));
            }
        }

        if (offerId == null) {
            model.addAttribute("error_msg", "Cannot show offer details with no offer id!");
            return "errorPage";
        }

        try {
            Offer offer = offerDAO.getById(offerId);
            if (offer == null) {
                model.addAttribute("error_msg", "No offer in database with ID = " + offerId);
                return "errorPage";
            }

            OrderDAO.Filter.FilterBuilder filterBuilder = OrderDAO.Filter.builder();
            if (includeAttributes.getOrDefault("ContractType", true))
                filterBuilder.contractType(offer.getContractType());
            if (includeAttributes.getOrDefault("EstateType", true))
                filterBuilder.estateType(offer.getEstateType());
            if (includeAttributes.getOrDefault("EstateFacade", true))
                filterBuilder.estateFacade(offer.getEstateFacade());
            if (includeAttributes.getOrDefault("BuildingState", true))
                filterBuilder.buildingState(offer.getBuildingState());
            if (includeAttributes.getOrDefault("Location", true))
                filterBuilder.location(offer.getLocation());
            if (includeAttributes.getOrDefault("Floor", true))
                filterBuilder.floor(offer.getFloor());
            if (includeAttributes.getOrDefault("StartingPrice", true))
                filterBuilder.startingPrice(offer.getStartingPrice());
            if (includeAttributes.getOrDefault("Commodities", true))
                filterBuilder.commodities(Converter.getMap(offer.getCommodities()));
            if (includeAttributes.getOrDefault("Space", true))
                filterBuilder.space(Converter.getIntMap(offer.getSpace()));
            if (includeAttributes.getOrDefault("Transport", true))
                filterBuilder.transport(Converter.getIntMap(offer.getTransport()));

            Collection<Order> suitableOrders = orderDAO.getOrdersByFilter(filterBuilder.build());

            model.addAttribute("offer", offer);
            model.addAttribute("offerService", offerDAO);
            model.addAttribute("conversionService", new Converter());

            model.addAttribute("includeAttributes", includeAttributes);
            model.addAttribute("suitableOrders", suitableOrders);
            return "offer";
        } catch (HibernateException e) {
            model.addAttribute("error_msg", e.getMessage());
            return "errorPage";
        }
    }

    @GetMapping("/editOffer")
    public String editOfferPage(@RequestParam Map<String, String> allParams, Model model) {
        String contractType = null;
        String estateType = null;
        String estateFacade = null;
        String buildingState = null;
        String location = null;
        String address = null;
        Integer offerId = null;
        Integer offeredById = null;
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
            if (key.startsWith("address"))
                address = value;

            if (key.startsWith("offerId"))
                offerId = Integer.parseInt(value);
            if (key.startsWith("offeredById"))
                offeredById = Integer.parseInt(value);
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

        Offer offer;
        if (offerId == null) {
            if (offeredById == null) {
                model.addAttribute("error_msg", "Cannot create offer with no author");
                return "errorPage";
            }
            Client offeredBy = clientDAO.getById(offeredById);
            if (offeredBy == null) {
                model.addAttribute("error_msg", "No client with ID = " + offeredById);
                return "errorPage";
            }
            offer = new Offer();
            offer.setOfferedBy(offeredBy);
        } else {
            try {
                offer = offerDAO.getById(offerId);

                if (offer == null) {
                    model.addAttribute("error_msg", "No offer in database with ID = " + offerId);
                    return "errorPage";
                }

                if (contractType == null)
                    contractType = offer.getContractType();
                if (estateType == null)
                    estateType = offer.getEstateType();
                if (estateFacade == null)
                    estateFacade = offer.getEstateFacade();
                if (buildingState == null)
                    buildingState = offer.getBuildingState();
                if (location == null)
                    location = offer.getLocation();
                if (address == null)
                    address = offer.getAddress();
                if (offeredById == null)
                    offeredById = offer.getOfferedBy().getId();
                if (floor == null)
                    floor = offer.getFloor();
                if (startingPrice == null)
                    startingPrice = offer.getStartingPrice();

                Map<String, Boolean> commoditiesMap = Converter.getMap(offer.getCommodities());
                for (String commodity : commoditiesMap.keySet()) {
                    if (!commodities.containsKey(commodity))
                        commodities.put(commodity, commoditiesMap.get(commodity));
                }

                Map<String, Double> spaceMap = Converter.getMap(offer.getSpace());
                for (String roomName : spaceMap.keySet()) {
                    if (!roomSpace.containsKey(roomName))
                        roomSpace.put(roomName, spaceMap.get(roomName).intValue());
                }

                Map<String, Double> transportMap = Converter.getMap(offer.getTransport());
                for (String transportName : transportMap.keySet()) {
                    if (!transportDistance.containsKey(transportName))
                        transportDistance.put(transportName, transportMap.get(transportName).intValue());
                }
            } catch (HibernateException e) {
                model.addAttribute("error_msg", e.getMessage());
                return "errorPage";
            }
        }

        model.addAttribute("clientService", clientDAO);

        model.addAttribute("contractType", contractType);
        model.addAttribute("estateType", estateType);
        model.addAttribute("estateFacade", estateFacade);
        model.addAttribute("buildingState", buildingState);
        model.addAttribute("location", location);
        model.addAttribute("address", address);
        model.addAttribute("offerId", offerId);
        model.addAttribute("offeredById", offeredById);
        model.addAttribute("floor", floor);
        model.addAttribute("startingPrice", startingPrice);
        model.addAttribute("commodities", commodities);
        model.addAttribute("roomSpace", roomSpace);
        model.addAttribute("transportDistance", transportDistance);
        return "editOffer";
    }

    @GetMapping("/searchOffer")
    public String searchOfferPage(@RequestParam Map<String, String> allParams, Model model) {
        String contractType = null;
        String buildingState = null;
        Integer floorMin = null;
        Integer floorMax = null;
        Integer priceMax = null;
        Map<String, Boolean> estateTypes = new LinkedHashMap<>();
        Map<String, Boolean> estateFacades = new LinkedHashMap<>();
        Map<String, Boolean> commodities = new LinkedHashMap<>();
        Map<String, Integer> minRoomSpace = new LinkedHashMap<>();
        Map<String, Integer> transportDistance = new LinkedHashMap<>();

        String newEstateType = null;
        String newEstateFacade = null;
        String newCommodity = null;
        String newRoom = null;
        String newTransport = null;

        for (String key : allParams.keySet()) {
            String value = allParams.get(key);

            if (key.startsWith("contractType"))
                contractType = value;
            if (key.startsWith("buildingState"))
                buildingState = value;

            if (Objects.equals(value, ""))
                continue;

            if (key.startsWith("floorMin"))
                floorMin = Integer.parseInt(value);
            if (key.startsWith("floorMax"))
                floorMax = Integer.parseInt(value);
            if (key.startsWith("priceMax"))
                priceMax = Integer.parseInt(value);

            if (key.startsWith("estateTypes")) {
                String estateTypeName = key.split("__")[1];
                estateTypes.put(estateTypeName, Objects.equals(value, "True"));
            }
            if (key.startsWith("estateFacades")) {
                String estateFacadeName = key.split("__")[1];
                estateFacades.put(estateFacadeName, Objects.equals(value, "True"));
            }
            if (key.startsWith("commodities")) {
                String commodityName = key.split("__")[1];
                commodities.put(commodityName, Objects.equals(value, "True"));
            }

            if (key.startsWith("minRoomSpace")) {
                String roomName = key.split("__")[1];
                minRoomSpace.put(roomName, Integer.parseInt(value));
            }
            if (key.startsWith("transportDistance")) {
                String transport = key.split("__")[1];
                transportDistance.put(transport, Integer.parseInt(value));
            }

            if (key.startsWith("newEstateType"))
                newEstateType = value;
            if (key.startsWith("newEstateFacade"))
                newEstateFacade = value;
            if (key.startsWith("newCommodity"))
                newCommodity = value;
            if (key.startsWith("newRoom"))
                newRoom = value;
            if (key.startsWith("newTransport"))
                newTransport = value;
        }

        if (newEstateType != null)
            estateTypes.put(newEstateType, Boolean.TRUE);
        if (newEstateFacade != null)
            estateFacades.put(newEstateFacade, Boolean.TRUE);
        if (newCommodity != null)
            commodities.put(newCommodity, Boolean.TRUE);
        if (newRoom != null)
            minRoomSpace.put(newRoom, 1);
        if (newTransport != null)
            transportDistance.put(newTransport, 1);

        Collection<Offer> offers;
        try {
            offers = offerDAO.getOffersByFilter(OfferDAO.getFilterBuilder()
                    .contractType(contractType)
                    .requestedEstateTypes((estateTypes.size() > 0)? estateTypes : null)
                    .requestedEstateFacades((estateFacades.size() > 0)? estateFacades : null)
                    .requestedSpaceMin((minRoomSpace.size() > 0)? minRoomSpace : null)
                    .requestedCommodities((commodities.size() > 0)? commodities : null)
                    .floorMin(floorMin)
                    .floorMax(floorMax)
                    .buildingState(buildingState)
                    .requestedTransportMax((transportDistance.size() > 0)? transportDistance : null)
                    .startingPrice(priceMax)
                    .build());
        } catch (HibernateException e) {
            model.addAttribute("error_msg", e.getMessage());
            return "errorPage";
        }
        model.addAttribute("offers", offers);
        model.addAttribute("conversionService", new Converter());

        model.addAttribute("contractType", contractType);
        model.addAttribute("buildingState", buildingState);
        model.addAttribute("floorMin", floorMin);
        model.addAttribute("floorMax", floorMax);
        model.addAttribute("priceMax", priceMax);
        model.addAttribute("estateTypes", (estateTypes.size() > 0)? estateTypes : null);
        model.addAttribute("estateFacades", (estateFacades.size() > 0)? estateFacades : null);
        model.addAttribute("commodities", (commodities.size() > 0)? commodities : null);
        model.addAttribute("minRoomSpace", (minRoomSpace.size() > 0)? minRoomSpace : null);
        model.addAttribute("transportDistance", (transportDistance.size() > 0)? transportDistance : null);
        return "searchOffer";
    }

    @PostMapping("/saveOffer")
    public String saveOfferPage(@RequestParam Map<String, String> allParams, Model model) {
        String contractType = null;
        String estateType = null;
        String estateFacade = null;
        String buildingState = null;
        String location = null;
        String address = null;
        Integer offerId = null;
        Integer offeredById = null;
        Integer floor = null;
        Integer startingPrice = null;
        Map<String, Boolean> commodities = new LinkedHashMap<>();
        Map<String, Integer> roomSpace = new LinkedHashMap<>();
        Map<String, Integer> transportDistance = new LinkedHashMap<>();

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
            if (key.startsWith("address"))
                address = value;

            if (key.startsWith("offerId"))
                offerId = Integer.parseInt(value);
            if (key.startsWith("offeredById"))
                offeredById = Integer.parseInt(value);
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
        }

        Offer offer;
        try {
            Client offeredBy = clientDAO.getById(offeredById);
            if (offeredBy == null) {
                model.addAttribute("error_msg", "No client with ID = " + offeredById);
                return "errorPage";
            }
            if (offerId == null) {
                offer = new Offer();

                offer.setOfferedBy(offeredBy);
                offer.setContractType(contractType);
                offer.setEstateType(estateType);
                offer.setEstateFacade(estateFacade);
                offer.setSpace(Converter.getJson(roomSpace));
                offer.setCommodities(Converter.getJson(commodities));
                offer.setFloor(floor);
                offer.setBuildingState(buildingState);
                offer.setTransport(Converter.getJson(transportDistance));
                offer.setLocation(location);
                offer.setStartingPrice(startingPrice);
                offer.setAddress(address);

                offerDAO.add(offer);
            } else {
                offer = offerDAO.getById(offerId);

                if (offer == null) {
                    model.addAttribute("error_msg", "No offer in database with ID = " + offerId);
                    return "errorPage";
                }

                offer.setOfferedBy(offeredBy);
                offer.setContractType(contractType);
                offer.setEstateType(estateType);
                offer.setEstateFacade(estateFacade);
                offer.setSpace(Converter.getJson(roomSpace));
                offer.setCommodities(Converter.getJson(commodities));
                offer.setFloor(floor);
                offer.setBuildingState(buildingState);
                offer.setTransport(Converter.getJson(transportDistance));
                offer.setLocation(location);
                offer.setStartingPrice(startingPrice);
                offer.setAddress(address);

                offerDAO.update(offer);
            }
        } catch (HibernateException e) {
            model.addAttribute("error_msg", e.getMessage());
            return "errorPage";
        }

        model.addAttribute("offer", offer);
        model.addAttribute("conversionService", new Converter());
        return "redirect:/offer?offerId=" + offer.getId();
    }

    @PostMapping("/removeOffer")
    public String removeOfferPage(@RequestParam(name = "offerId") Integer offerId, Model model) {
        try {
            Offer offer = offerDAO.getById(offerId);

            if (offer == null) {
                model.addAttribute("error_msg", "No offer in database with ID = " + offerId);
                return "errorPage";
            }

            offerDAO.delete(offer);
        } catch (HibernateException e) {
            model.addAttribute("error_msg", e.getMessage());
            return "errorPage";
        }

        return "redirect:/searchOffer";
    }
}
