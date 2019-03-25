package com.uno.zoo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class loginTest {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
	  // Change this path to your local chrome webdriver path!
	System.setProperty("webdriver.chrome.driver", "C:\\Users\\Zach\\Documents\\aSpring2019\\Capstone\\chromedriver_win32\\chromedriver.exe");
	driver = new ChromeDriver();
    baseUrl = "http://localhost:4200/login";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  /* 
   * Can copy and paste different commands here from katalon recorder
   * extension for Chrome.
   * */
  @Test
  public void testPGBDemo() throws Exception {
	  	driver.get("http://localhost:4200/login");
	    driver.findElement(By.id("mat-input-0")).click();
	    driver.findElement(By.id("mat-input-0")).clear();
	    driver.findElement(By.id("mat-input-0")).sendKeys("updog");
	    Thread.sleep(500);
	    driver.findElement(By.id("mat-input-1")).click();
	    driver.findElement(By.id("mat-input-1")).clear();
	    driver.findElement(By.id("mat-input-1")).sendKeys("updog");
	    Thread.sleep(500);
	    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='The Henry Doorly Zoo Behavior Enrichment Program'])[1]/following::mat-card[1]")).click();
	    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Sign Up'])[1]/following::span[1]")).click();
	    Thread.sleep(2000);
	    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Admin'])[1]/following::span[2]")).click();
	    Thread.sleep(2000);
	    assertEquals("Login", driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='The Henry Doorly Zoo Behavior Enrichment Program'])[1]/following::mat-card-title[1]")).getText());
	    Thread.sleep(2000);
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}