package com.training.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SecureAreaPage {

    private WebDriver driver;

    private By secureAreaHeader = By.xpath("//h2[text()='Secure Area']");
    private By logoutButton = By.xpath("//a[@href='/logout']");
    private By successMessage = By.id("flash");   // ✅ new locator

    public SecureAreaPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isSecureAreaDisplayed() {
        return driver.findElement(secureAreaHeader).isDisplayed();
    }

    public boolean isLogoutButtonDisplayed() {
        return driver.findElement(logoutButton).isDisplayed();
    }

    public String getSuccessMessage() {
        return driver.findElement(successMessage).getText().trim();
    }
}