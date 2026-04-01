package com.training.steps;

import java.time.Duration;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.training.factory.DriverFactory;
import com.training.reports.ExtentLogger;
import com.training.utils.ExcelReader;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LoginSteps {

    private WebDriver driver;
    private Map<String, String> rowData;

    @Given("user launches the application")
    public void user_launches_the_application() {
        ExtentLogger.step("Launching the application");

        driver = DriverFactory.getDriver();

        // ✅ Defensive check (helps debugging)
        if (driver == null) {
            throw new RuntimeException("WebDriver is NULL. Did @Before hook run?");
        }

        driver.get("https://the-internet.herokuapp.com/login");
    }

    @When("user logs in using excel row {int}")
    public void user_logs_in_using_excel_row(Integer rowNumber) {

        ExtentLogger.step("Reading data from Excel row: " + rowNumber);

        rowData = ExcelReader.getRowData(
                "testdata/LoginData.xlsx",
                "Sheet1",
                rowNumber
        );

        String username = rowData.get("username");
        String password = rowData.get("password");

        if (username == null || password == null) {
            throw new RuntimeException("Excel data invalid. Username or password is null.");
        }

        ExtentLogger.step("Entering username");
        driver.findElement(By.id("username")).sendKeys(username);

        ExtentLogger.step("Entering password");
        driver.findElement(By.id("password")).sendKeys(password);

        ExtentLogger.step("Clicking login button");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }

    @Then("user should see expected result from excel")
    public void user_should_see_expected_result_from_excel() {

        String expectedMessage = rowData.get("expectedMessage");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement flashMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("flash"))
        );

        String actualText = flashMessage.getText();

        ExtentLogger.step("Actual message displayed: " + actualText);

        Assert.assertTrue(
                actualText.contains(expectedMessage),
                "Expected message not displayed. Expected: " + expectedMessage +
                        " Actual: " + actualText
        );
    }
}
