package ru.msu.cmc.realestatemanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.msu.cmc.realestatemanager.model.DAOFactory;
import ru.msu.cmc.realestatemanager.model.dao.OfferDAO;
import ru.msu.cmc.realestatemanager.model.entity.Offer;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class RealEstateManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RealEstateManagerApplication.class, args);
		DAOFactory factory = DAOFactory.getInstance();
		OfferDAO offerDAO = factory.getOfferDAO();
		System.out.println(offerDAO.getById(1));

		Map<String, Boolean> commodities = new HashMap<>();
		commodities.put("furniture", true);

		System.out.println(offerDAO.getOffersByFilter(
				OfferDAO.getFilterBuilder()
						.requestedCommodities(commodities)
						.build()));
	}

}
