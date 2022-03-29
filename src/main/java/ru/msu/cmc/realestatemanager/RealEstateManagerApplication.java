package ru.msu.cmc.realestatemanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.msu.cmc.realestatemanager.model.DAOFactory;
import ru.msu.cmc.realestatemanager.model.dao.ClientDAO;
import ru.msu.cmc.realestatemanager.model.dao.OfferDAO;
import ru.msu.cmc.realestatemanager.model.entity.Client;
import ru.msu.cmc.realestatemanager.model.entity.Offer;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class RealEstateManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RealEstateManagerApplication.class, args);
		ClientDAO dao = DAOFactory.getInstance().getClientDAO();
		System.out.println(dao.getClientsByFilter(ClientDAO.getFilterBuilder().build()));
	}

}
