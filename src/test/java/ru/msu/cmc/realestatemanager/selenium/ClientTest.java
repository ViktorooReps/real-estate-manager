package ru.msu.cmc.realestatemanager.selenium;

import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import ru.msu.cmc.realestatemanager.model.DAOFactory;
import ru.msu.cmc.realestatemanager.model.dao.ClientDAO;
import ru.msu.cmc.realestatemanager.model.entity.Client;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ClientTest {
  private WebDriver driver;
  private Map<String, Object> vars;
  JavascriptExecutor js;

  ClientDAO clientDAO;

  @BeforeEach
  public void setUp() {
    driver = new ChromeDriver();
    js = (JavascriptExecutor) driver;
    vars = new HashMap<String, Object>();
    clientDAO = DAOFactory.getInstance().getClientDAO();
  }

  @AfterEach
  public void tearDown() {
    driver.quit();
  }

  @Test
  public void clientAddSearchDelete() {
    driver.get("http://localhost:8080/");
    driver.manage().window().setSize(new Dimension(1728, 1152));
    driver.findElement(By.id("addClientButton")).click();
    driver.findElement(By.id("name")).click();
    driver.findElement(By.id("name")).sendKeys("Test Testovich");
    driver.findElement(By.id("email")).click();
    driver.findElement(By.id("email")).click();
    driver.findElement(By.id("email")).sendKeys("test@m.com");
    driver.findElement(By.id("phoneNumber")).click();
    driver.findElement(By.id("phoneNumber")).sendKeys("+777777777");
    driver.findElement(By.id("submitButton")).click();

    // check that client has actually been added
    Integer currentClientId;
    Integer newClientId = Integer.parseInt(driver.findElement(By.id("clientId")).getAttribute("value"));
    Client client = clientDAO.getById(newClientId);
    assertNotEquals(null, client);

    driver.findElement(By.id("searchClient")).click();
    driver.findElement(By.id("name")).click();
    driver.findElement(By.id("name")).sendKeys("Test Testovich");
    driver.findElement(By.id("name")).sendKeys(Keys.ENTER);
    driver.findElement(By.cssSelector("a > span")).click();

    // check that it is correct client
    currentClientId = Integer.parseInt(driver.findElement(By.id("clientId")).getAttribute("value"));
    assertEquals(newClientId, currentClientId);

    driver.findElement(By.id("searchClient")).click();
    driver.findElement(By.id("email")).click();
    driver.findElement(By.id("email")).sendKeys("test");
    driver.findElement(By.id("submitButton")).click();
    driver.findElement(By.cssSelector("a > span")).click();

    // check that it is correct client
    currentClientId = Integer.parseInt(driver.findElement(By.id("clientId")).getAttribute("value"));
    assertEquals(newClientId, currentClientId);

    driver.findElement(By.id("searchClient")).click();
    driver.findElement(By.cssSelector("form:nth-child(1)")).click();
    driver.findElement(By.id("phoneNumber")).click();
    driver.findElement(By.id("phoneNumber")).sendKeys("7777777");
    driver.findElement(By.id("submitButton")).click();
    driver.findElement(By.id("email")).click();
    driver.findElement(By.id("email")).sendKeys("t");
    driver.findElement(By.id("submitButton")).click();
    driver.findElement(By.cssSelector("a > span")).click();

    // check that it is correct client
    currentClientId = Integer.parseInt(driver.findElement(By.id("clientId")).getAttribute("value"));
    assertEquals(newClientId, currentClientId);

    driver.findElement(By.id("deleteButton")).click();

    // check that client has actually been deleted
    assertThrows(ObjectNotFoundException.class, () -> assertNull(clientDAO.getById(newClientId)));
  }
}
