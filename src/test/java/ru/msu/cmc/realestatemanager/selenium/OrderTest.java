package ru.msu.cmc.realestatemanager.selenium;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.dialect.DataDirectOracle9Dialect;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import ru.msu.cmc.realestatemanager.model.DAOFactory;
import ru.msu.cmc.realestatemanager.model.dao.OrderDAO;
import ru.msu.cmc.realestatemanager.model.entity.Offer;
import ru.msu.cmc.realestatemanager.model.entity.Order;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public class OrderTest {
  private WebDriver driver;
  private Map<String, Object> vars;
  JavascriptExecutor js;
  OrderDAO orderDAO;

  @BeforeEach
  public void setUp() {
    driver = new ChromeDriver();
    js = (JavascriptExecutor) driver;
    vars = new HashMap<String, Object>();
    orderDAO = DAOFactory.getInstance().getOrderDAO();
  }

  @AfterEach
  public void tearDown() {
    driver.quit();
  }

  @Test
  public void orderAddSearchDelete() {
    driver.get("http://localhost:8080/");
    driver.manage().window().setSize(new Dimension(1728, 1152));
    driver.findElement(By.id("addClientButton")).click();
    driver.findElement(By.cssSelector("form")).click();
    driver.findElement(By.id("name")).click();
    {
      WebElement element = driver.findElement(By.id("name"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).clickAndHold().perform();
    }
    {
      WebElement element = driver.findElement(By.id("name"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).perform();
    }
    {
      WebElement element = driver.findElement(By.id("name"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).release().perform();
    }
    driver.findElement(By.id("name")).click();
    driver.findElement(By.id("name")).sendKeys("Test Testovich");
    driver.findElement(By.id("email")).click();
    driver.findElement(By.id("email")).sendKeys("test@m.com");
    driver.findElement(By.id("phoneNumber")).click();
    driver.findElement(By.id("phoneNumber")).sendKeys("+777777777");
    driver.findElement(By.id("submitButton")).click();

    // added client

    driver.findElement(By.id("addClientOrderButton")).click();
    driver.findElement(By.id("ct")).click();
    driver.findElement(By.id("ct")).sendKeys("test");
    driver.findElement(By.id("bs")).click();
    driver.findElement(By.id("bs")).sendKeys("test");
    driver.findElement(By.id("minf")).click();
    driver.findElement(By.id("minf")).sendKeys("10");
    driver.findElement(By.id("maxf")).click();
    driver.findElement(By.id("maxf")).sendKeys("11");
    driver.findElement(By.id("mp")).click();
    driver.findElement(By.id("mp")).sendKeys("123456789");
    driver.findElement(By.id("net")).click();
    driver.findElement(By.id("net")).sendKeys("test");
    driver.findElement(By.id("addEstateType")).click();
    driver.findElement(By.id("net")).click();
    driver.findElement(By.id("net")).sendKeys("not test");
    driver.findElement(By.id("addEstateType")).click();
    driver.findElement(By.id("id__not test")).click();
    driver.findElement(By.id("nef")).click();
    driver.findElement(By.id("nef")).sendKeys("test");
    driver.findElement(By.id("addEstateFacade")).click();
    driver.findElement(By.id("nef")).click();
    driver.findElement(By.id("nef")).sendKeys("not test");
    driver.findElement(By.id("addEstateFacade")).click();
    driver.findElement(By.name("requestedEstateFacades__not test")).click();
    driver.findElement(By.id("nl")).click();
    driver.findElement(By.id("nl")).sendKeys("test");
    driver.findElement(By.id("addLocation")).click();
    driver.findElement(By.id("nl")).click();
    driver.findElement(By.id("nl")).sendKeys("not test");
    driver.findElement(By.id("addLocation")).click();
    driver.findElement(By.name("requestedLocations__not test")).click();
    driver.findElement(By.id("nc")).click();
    driver.findElement(By.id("nc")).sendKeys("test");
    driver.findElement(By.id("addCommodity")).click();
    driver.findElement(By.id("nc")).click();
    driver.findElement(By.id("nc")).sendKeys("not test");
    driver.findElement(By.id("addCommodity")).click();
    driver.findElement(By.name("requestedCommodities__not test")).click();
    driver.findElement(By.id("nr")).click();
    driver.findElement(By.id("nr")).sendKeys("test");
    driver.findElement(By.id("addRoom")).click();
    driver.findElement(By.id("nr")).click();
    driver.findElement(By.id("nr")).sendKeys("not test");
    driver.findElement(By.id("addRoom")).click();
    driver.findElement(By.name("requestedSpaceMin__test")).click();
    driver.findElement(By.name("requestedSpaceMin__test")).sendKeys("11");
    driver.findElement(By.id("nt")).click();
    driver.findElement(By.id("nt")).sendKeys("test");
    driver.findElement(By.id("addTransport")).click();
    driver.findElement(By.id("nt")).click();
    driver.findElement(By.id("nt")).sendKeys("not test");
    driver.findElement(By.id("addTransport")).click();
    driver.findElement(By.cssSelector("fieldset:nth-child(13)")).click();
    driver.findElement(By.name("requestedTransportMax__test")).click();
    driver.findElement(By.name("requestedTransportMax__test")).sendKeys("11");
    driver.findElement(By.id("submitButton")).click();

    // check that order has actually been added
    Integer currentOrderId;
    Integer newOrderId = Integer.parseInt(driver.findElement(By.id("orderId")).getAttribute("value"));
    Order order = orderDAO.getById(newOrderId);
    assertNotEquals(null, order);

    driver.findElement(By.id("searchOrder")).click();
    driver.findElement(By.id("ct")).click();
    driver.findElement(By.id("ct")).sendKeys("test");
    driver.findElement(By.id("ct")).sendKeys(Keys.ENTER);
    driver.findElement(By.id("openOrderButton")).click();

    // check that it is correct order
    currentOrderId = Integer.parseInt(driver.findElement(By.id("orderId")).getAttribute("value"));
    assertEquals(newOrderId, currentOrderId);

    driver.findElement(By.id("searchOrder")).click();
    driver.findElement(By.id("et")).click();
    driver.findElement(By.id("et")).sendKeys("test");
    driver.findElement(By.id("et")).sendKeys(Keys.ENTER);
    driver.findElement(By.id("openOrderButton")).click();

    // check that it is correct order
    currentOrderId = Integer.parseInt(driver.findElement(By.id("orderId")).getAttribute("value"));
    assertEquals(newOrderId, currentOrderId);

    driver.findElement(By.id("searchOrder")).click();
    driver.findElement(By.id("ef")).click();
    driver.findElement(By.id("ef")).sendKeys("test");
    driver.findElement(By.id("ef")).sendKeys(Keys.ENTER);
    driver.findElement(By.id("openOrderButton")).click();

    // check that it is correct order
    currentOrderId = Integer.parseInt(driver.findElement(By.id("orderId")).getAttribute("value"));
    assertEquals(newOrderId, currentOrderId);

    driver.findElement(By.id("searchOrder")).click();
    driver.findElement(By.id("bs")).click();
    driver.findElement(By.id("bs")).sendKeys("test");
    driver.findElement(By.id("bs")).sendKeys(Keys.ENTER);
    driver.findElement(By.id("openOrderButton")).click();

    // check that it is correct order
    currentOrderId = Integer.parseInt(driver.findElement(By.id("orderId")).getAttribute("value"));
    assertEquals(newOrderId, currentOrderId);

    driver.findElement(By.id("searchOrder")).click();
    driver.findElement(By.id("l")).click();
    driver.findElement(By.id("l")).sendKeys("test");
    driver.findElement(By.id("l")).sendKeys(Keys.ENTER);
    driver.findElement(By.id("openOrderButton")).click();
    driver.findElement(By.id("searchOrder")).click();
    driver.findElement(By.id("f")).click();
    driver.findElement(By.id("f")).sendKeys("11");
    driver.findElement(By.id("f")).sendKeys(Keys.ENTER);
    driver.findElement(By.cssSelector("tr:nth-child(2) #openOrderButton")).click();

    // check that it is correct order
    currentOrderId = Integer.parseInt(driver.findElement(By.id("orderId")).getAttribute("value"));
    assertEquals(newOrderId, currentOrderId);

    driver.findElement(By.id("searchOrder")).click();
    driver.findElement(By.id("sp")).click();
    driver.findElement(By.id("sp")).sendKeys("123456789");
    driver.findElement(By.id("sp")).sendKeys(Keys.ENTER);
    driver.findElement(By.id("openOrderButton")).click();

    // check that it is correct order
    currentOrderId = Integer.parseInt(driver.findElement(By.id("orderId")).getAttribute("value"));
    assertEquals(newOrderId, currentOrderId);

    driver.findElement(By.id("searchOrder")).click();
    driver.findElement(By.id("nc")).click();
    driver.findElement(By.id("nc")).sendKeys("test");
    driver.findElement(By.id("addCommodity")).click();
    driver.findElement(By.id("nc")).sendKeys("not test");
    driver.findElement(By.id("addCommodity")).click();
    driver.findElement(By.id("id__not test")).click();
    driver.findElement(By.id("nr")).click();
    driver.findElement(By.id("nr")).sendKeys("test");
    driver.findElement(By.id("addRoom")).click();
    driver.findElement(By.id("nr")).sendKeys("not test");
    driver.findElement(By.id("addRoom")).click();
    driver.findElement(By.name("roomSpace__test")).sendKeys("11");
    driver.findElement(By.id("nt")).click();
    driver.findElement(By.id("nt")).sendKeys("test");
    driver.findElement(By.id("nt")).sendKeys(Keys.ENTER);
    driver.findElement(By.id("nt")).click();
    driver.findElement(By.id("nt")).sendKeys("not test");
    driver.findElement(By.id("addTransport")).click();
    driver.findElement(By.name("transportDistance__test")).sendKeys("11");
    driver.findElement(By.id("submitButton")).click();
    driver.findElement(By.cssSelector("tr:nth-child(6) #openOrderButton")).click();

    // check that it is correct order
    currentOrderId = Integer.parseInt(driver.findElement(By.id("orderId")).getAttribute("value"));
    assertEquals(newOrderId, currentOrderId);

    driver.findElement(By.id("deleteButton")).click();

    // check that order has actually been deleted
    assertThrows(ObjectNotFoundException.class, () -> assertNull(orderDAO.getById(newOrderId)));

    driver.findElement(By.id("searchClient")).click();
    driver.findElement(By.id("name")).click();
    driver.findElement(By.id("name")).sendKeys("Test Testovich");
    driver.findElement(By.id("name")).sendKeys(Keys.ENTER);
    driver.findElement(By.cssSelector("a > span")).click();
    driver.findElement(By.id("deleteButton")).click();
  }
}
