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
import ru.msu.cmc.realestatemanager.model.dao.ClientActions;
import ru.msu.cmc.realestatemanager.model.dao.ClientDAO;
import ru.msu.cmc.realestatemanager.model.entity.Client;

import java.util.Collection;

@Controller
public class ClientController {

    @Autowired
    private final ClientDAO clientDAO = DAOFactory.getInstance().getClientDAO();

    @Autowired
    private final ClientActions clientActions = DAOFactory.getInstance().getClientActions();

    @GetMapping("/client")
    public String clientPage(@RequestParam(name = "clientId") Integer clientId, Model model) {
        try {
            Client client = clientDAO.getById(clientId);
            if (client == null) {
                model.addAttribute("error_msg", "No client in database with ID = " + clientId);
                return "errorPage";
            }

            model.addAttribute("client", client);
            model.addAttribute("clientService", clientActions);
            model.addAttribute("conversionService", new Converter());
            return "client";
        } catch (HibernateException e) {
            model.addAttribute("error_msg", e.getMessage());
            return "errorPage";
        }
    }

    @GetMapping("/editClient")
    public String editClientPage(@RequestParam(name = "clientId", required = false) Integer clientId, Model model) {
        Client client;

        if (clientId == null) {
            client = new Client();
        } else {
            try {
                client = clientDAO.getById(clientId);

                if (client == null) {
                    model.addAttribute("error_msg", "No client in database with ID = " + clientId);
                    return "errorPage";
                }
            } catch (HibernateException e) {
                model.addAttribute("error_msg", e.getMessage());
                return "errorPage";
            }
        }

        model.addAttribute("client", client);
        model.addAttribute("clientService", clientDAO);
        return "editClient";
    }

    @GetMapping({"/searchClient", "/"})
    public String searchClientPage(@RequestParam(name = "name", required = false) String clientName,
                                   @RequestParam(name = "email", required = false) String email,
                                   @RequestParam(name = "phoneNumber", required = false) String phoneNumber,
                                   Model model) {
        try {
            if (clientName == null) {
                clientName = "";
            }
            if (email == null) {
                email = "";
            }
            if (phoneNumber == null) {
                phoneNumber = "";
            }

            model.addAttribute("searchName", clientName);
            model.addAttribute("searchEmail", email);
            model.addAttribute("searchPhone", phoneNumber);

            Collection<Client> clients = clientDAO.getClientsByFilter(ClientDAO.getFilterBuilder()
                    .clientName(clientName)
                    .email(email)
                    .phoneNumber(phoneNumber)
                    .build());
            model.addAttribute("clients", clients);
            model.addAttribute("clientService", clientDAO);
            return "searchClient";
        } catch (HibernateException e) {
            model.addAttribute("error_msg", e.getMessage());
            return "errorPage";
        }
    }

    @PostMapping("/saveClient")
    public String saveClientPage(@RequestParam(name = "clientId", required = false) Integer clientId,
                                 @RequestParam(name = "name") String name,
                                 @RequestParam(name = "email") String email,
                                 @RequestParam(name = "phoneNumber") String phoneNumber,
                                 Model model) {
        Client client;
        try {
            if (clientId == null) {
                client = new Client(clientId, name, email, phoneNumber);
                clientDAO.add(client);
            } else {
                client = clientDAO.getById(clientId);

                if (client == null) {
                    model.addAttribute("error_msg", "No client in database with ID = " + clientId);
                    return "errorPage";
                }

                client.setClientName(name);
                client.setEmail(email);
                client.setPhoneNumber(phoneNumber);
                clientDAO.update(client);
            }
        } catch (HibernateException e) {
            model.addAttribute("error_msg", e.getMessage());
            return "errorPage";
        }

        model.addAttribute("client", client);
        model.addAttribute("clientService", clientDAO);
        return "redirect:/client?clientId=" + client.getId();
    }

    @PostMapping("/removeClient")
    public String removeClientPage(@RequestParam(name = "clientId") Integer clientId, Model model) {
        try {
            Client client = clientDAO.getById(clientId);

            if (client == null) {
                model.addAttribute("error_msg", "No client in database with ID = " + clientId);
                return "errorPage";
            }

            clientDAO.delete(client);
        } catch (HibernateException e) {
            model.addAttribute("error_msg", e.getMessage());
            return "errorPage";
        }

        return "redirect:/searchClient";
    }
}
