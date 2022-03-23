package ru.msu.cmc.realestatemanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.msu.cmc.realestatemanager.model.DAOFactory;
import ru.msu.cmc.realestatemanager.model.dao.OfferDAO;
import ru.msu.cmc.realestatemanager.model.entity.Offer;

@SpringBootApplication
public class RealEstateManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RealEstateManagerApplication.class, args);
		DAOFactory factory = DAOFactory.getInstance();
		OfferDAO offerDAO = factory.getOfferDAO();
		System.out.println(offerDAO.getById(1L));
	}

}
