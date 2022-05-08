package ru.msu.cmc.realestatemanager.selenium;

import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import ru.msu.cmc.realestatemanager.model.DAOFactory;
import ru.msu.cmc.realestatemanager.model.dao.OfferDAO;
import ru.msu.cmc.realestatemanager.model.entity.Client;
import ru.msu.cmc.realestatemanager.model.entity.Offer;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public class OfferTest {
  private WebDriver driver;
  private Map<String, Object> vars;
  JavascriptExecutor js;
  OfferDAO offerDAO;

  @BeforeEach
  public void setUp() {
    driver = new ChromeDriver();
    js = (JavascriptExecutor) driver;
    vars = new HashMap<String, Object>();
    offerDAO = DAOFactory.getInstance().getOfferDAO();
  }

  @AfterEach
  public void tearDown() {
    driver.quit();
  }

  @Test
  public void offerAddSearchDelete() {
    driver.get("http://localhost:8080/");
    driver.manage().window().setSize(new Dimension(1728, 1152));
    driver.findElement(By.id("searchClient")).click();
    driver.findElement(By.id("addClientButton")).click();
    driver.findElement(By.cssSelector("form")).click();
    driver.findElement(By.id("name")).click();
    driver.findElement(By.id("name")).sendKeys("Test Testovich");
    driver.findElement(By.id("email")).click();
    driver.findElement(By.id("email")).sendKeys("test@m.com");
    driver.findElement(By.id("phoneNumber")).click();
    driver.findElement(By.id("phoneNumber")).sendKeys("+777777777");
    driver.findElement(By.id("submitButton")).click();

    // added client

    driver.findElement(By.id("addClientOfferButton")).click();
    driver.findElement(By.id("ct")).click();
    driver.findElement(By.id("ct")).sendKeys("test");
    driver.findElement(By.id("ct")).sendKeys(Keys.ENTER);
    driver.findElement(By.id("et")).sendKeys("test");
    driver.findElement(By.id("et")).sendKeys(Keys.ENTER);
    driver.findElement(By.id("ef")).sendKeys("test");
    driver.findElement(By.id("ef")).sendKeys(Keys.ENTER);
    driver.findElement(By.id("bs")).sendKeys("test");
    driver.findElement(By.id("bs")).sendKeys(Keys.ENTER);
    driver.findElement(By.id("l")).sendKeys("test");
    driver.findElement(By.id("l")).sendKeys(Keys.ENTER);
    driver.findElement(By.id("a")).sendKeys("test");
    driver.findElement(By.id("a")).sendKeys(Keys.ENTER);
    driver.findElement(By.id("f")).sendKeys("11");
    driver.findElement(By.id("f")).sendKeys(Keys.ENTER);
    driver.findElement(By.id("sp")).sendKeys("11");
    driver.findElement(By.id("nc")).click();
    driver.findElement(By.id("nc")).sendKeys("test");
    driver.findElement(By.id("addCommodity")).click();
    driver.findElement(By.id("nc")).click();
    driver.findElement(By.id("nc")).sendKeys("not test");
    driver.findElement(By.cssSelector("fieldset:nth-child(6)")).click();
    driver.findElement(By.id("addCommodity")).click();
    driver.findElement(By.id("id__not test")).click();
    driver.findElement(By.id("nr")).click();
    driver.findElement(By.id("nr")).sendKeys("test");
    driver.findElement(By.id("addRoom")).click();
    driver.findElement(By.id("nr")).click();
    driver.findElement(By.id("nr")).sendKeys("not test");
    driver.findElement(By.id("addRoom")).click();
    driver.findElement(By.name("roomSpace__test")).click();
    driver.findElement(By.name("roomSpace__test")).sendKeys("11");
    driver.findElement(By.id("nt")).click();
    driver.findElement(By.id("nt")).sendKeys("test");
    driver.findElement(By.id("addTransport")).click();
    driver.findElement(By.id("nt")).click();
    driver.findElement(By.id("nt")).sendKeys("not test");
    driver.findElement(By.id("addTransport")).click();
    driver.findElement(By.name("transportDistance__test")).click();
    driver.findElement(By.name("transportDistance__test")).sendKeys("11");
    driver.findElement(By.id("submitButton")).click();

    // check that offer has actually been added
    Integer currentOfferId;
    Integer newOfferId = Integer.parseInt(driver.findElement(By.id("offerId")).getAttribute("value"));
    Offer offer = offerDAO.getById(newOfferId);
    assertNotEquals(null, offer);

    driver.findElement(By.id("searchOffer")).click();
    driver.findElement(By.id("ct")).click();
    driver.findElement(By.id("ct")).sendKeys("test");
    driver.findElement(By.id("ct")).sendKeys(Keys.ENTER);
    driver.findElement(By.id("openOfferButton")).click();

    // check that it is correct offer
    currentOfferId = Integer.parseInt(driver.findElement(By.id("offerId")).getAttribute("value"));
    assertEquals(newOfferId, currentOfferId);

    driver.findElement(By.id("searchOffer")).click();
    driver.findElement(By.id("bs")).click();
    driver.findElement(By.id("bs")).sendKeys("test");
    driver.findElement(By.id("bs")).sendKeys(Keys.ENTER);
    driver.findElement(By.id("openOfferButton")).click();

    // check that it is correct offer
    currentOfferId = Integer.parseInt(driver.findElement(By.id("offerId")).getAttribute("value"));
    assertEquals(newOfferId, currentOfferId);

    driver.findElement(By.id("searchOffer")).click();
    driver.findElement(By.id("fmin")).click();
    driver.findElement(By.id("fmin")).sendKeys("11");
    driver.findElement(By.id("fmin")).sendKeys(Keys.ENTER);
    driver.findElement(By.id("openOfferButton")).click();

    // check that it is correct offer
    currentOfferId = Integer.parseInt(driver.findElement(By.id("offerId")).getAttribute("value"));
    assertEquals(newOfferId, currentOfferId);

    driver.findElement(By.id("searchOffer")).click();
    driver.findElement(By.id("fmax")).click();
    driver.findElement(By.id("fmax")).sendKeys("11");
    driver.findElement(By.id("fmax")).sendKeys(Keys.ENTER);
    driver.findElement(By.id("fmin")).click();
    driver.findElement(By.id("fmin")).sendKeys("11");
    driver.findElement(By.id("fmin")).sendKeys(Keys.ENTER);
    driver.findElement(By.id("openOfferButton")).click();

    // check that it is correct offer
    currentOfferId = Integer.parseInt(driver.findElement(By.id("offerId")).getAttribute("value"));
    assertEquals(newOfferId, currentOfferId);

    driver.findElement(By.id("searchOffer")).click();
    driver.findElement(By.id("mp")).click();
    driver.findElement(By.id("mp")).sendKeys("11");
    driver.findElement(By.id("mp")).sendKeys(Keys.ENTER);
    driver.findElement(By.id("openOfferButton")).click();

    // check that it is correct offer
    currentOfferId = Integer.parseInt(driver.findElement(By.id("offerId")).getAttribute("value"));
    assertEquals(newOfferId, currentOfferId);

    driver.findElement(By.id("searchOffer")).click();
    driver.findElement(By.cssSelector("fieldset:nth-child(3)")).click();
    driver.findElement(By.id("net")).click();
    driver.findElement(By.id("net")).sendKeys("test");
    driver.findElement(By.id("addEstateType")).click();
    driver.findElement(By.id("net")).sendKeys("not test");
    driver.findElement(By.id("net")).sendKeys(Keys.ENTER);
    driver.findElement(By.id("id__not test")).click();
    driver.findElement(By.cssSelector(".indent > form")).click();
    driver.findElement(By.id("submitButton")).click();
    driver.findElement(By.id("openOfferButton")).click();

    // check that it is correct offer
    currentOfferId = Integer.parseInt(driver.findElement(By.id("offerId")).getAttribute("value"));
    assertEquals(newOfferId, currentOfferId);

    driver.findElement(By.id("searchOffer")).click();
    driver.findElement(By.id("nef")).click();
    driver.findElement(By.id("nef")).sendKeys("test");
    driver.findElement(By.id("addEstateFacade")).click();
    driver.findElement(By.id("nef")).click();
    driver.findElement(By.id("nef")).sendKeys("not test");
    driver.findElement(By.id("addEstateFacade")).click();
    driver.findElement(By.id("id__not test")).click();
    driver.findElement(By.id("submitButton")).click();
    driver.findElement(By.id("openOfferButton")).click();

    // check that it is correct offer
    currentOfferId = Integer.parseInt(driver.findElement(By.id("offerId")).getAttribute("value"));
    assertEquals(newOfferId, currentOfferId);

    driver.findElement(By.id("searchOffer")).click();
    driver.findElement(By.cssSelector("fieldset:nth-child(7)")).click();
    driver.findElement(By.id("nc")).click();
    driver.findElement(By.id("nc")).sendKeys("test");
    driver.findElement(By.id("addCommodity")).click();
    driver.findElement(By.id("nc")).sendKeys("not test");
    driver.findElement(By.id("addCommodity")).click();
    driver.findElement(By.id("id__not test")).click();
    driver.findElement(By.id("nr")).click();
    driver.findElement(By.id("nr")).sendKeys("test");
    driver.findElement(By.id("addRoom")).click();
    driver.findElement(By.id("nr")).click();
    driver.findElement(By.id("nr")).sendKeys("not test");
    driver.findElement(By.id("addRoom")).click();
    driver.findElement(By.name("minRoomSpace__test")).click();
    driver.findElement(By.name("minRoomSpace__test")).sendKeys("11");
    driver.findElement(By.id("nt")).click();
    driver.findElement(By.id("nt")).sendKeys("test");
    driver.findElement(By.id("addTransport")).click();
    driver.findElement(By.name("transportDistance__test")).click();
    driver.findElement(By.name("transportDistance__test")).sendKeys("11");
    driver.findElement(By.id("nt")).click();
    driver.findElement(By.id("nt")).sendKeys("not test");
    driver.findElement(By.id("addTransport")).click();
    driver.findElement(By.id("submitButton")).click();
    driver.findElement(By.cssSelector("tr:nth-child(6) #openOfferButton")).click();

    // check that it is correct offer
    currentOfferId = Integer.parseInt(driver.findElement(By.id("offerId")).getAttribute("value"));
    assertEquals(newOfferId, currentOfferId);

    driver.findElement(By.id("deleteButton")).click();

    // check that offer has actually been deleted
    assertThrows(ObjectNotFoundException.class, () -> assertNull(offerDAO.getById(newOfferId)));

    driver.findElement(By.id("searchClient")).click();
    driver.findElement(By.id("name")).click();
    driver.findElement(By.id("name")).sendKeys("Test Testovich");
    driver.findElement(By.id("name")).sendKeys(Keys.ENTER);
    driver.findElement(By.cssSelector("a > span")).click();
    driver.findElement(By.id("deleteButton")).click();
  }
}
