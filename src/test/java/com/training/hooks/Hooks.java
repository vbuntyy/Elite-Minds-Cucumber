package com.training.hooks;

import com.aventstack.extentreports.*;
import com.training.factory.DriverFactory;
import com.training.reports.ExtentManager;
import com.training.reports.ExtentTestHolder;
import io.cucumber.java.*;

public class Hooks {

    private static final ExtentReports extent = ExtentManager.getExtent();

    @Before
    public void beforeScenario(Scenario scenario) {

        // ✅ Initialize WebDriver FIRST
        DriverFactory.initDriver();

        // ✅ Then initialize Extent test
        ExtentTest test = extent.createTest(scenario.getName());
        ExtentTestHolder.set(test);
    }

    @After
    public void afterScenario(Scenario scenario) {

        ExtentTest test = ExtentTestHolder.get();

        if (scenario.isFailed()) {
            test.fail("Scenario FAILED ❌");
        } else {
            test.pass("Scenario PASSED ✅");
        }

        // ✅ Quit browser safely
        DriverFactory.quitDriver();
        ExtentTestHolder.remove();
    }

    @AfterAll
    public static void afterAll() {
        extent.flush();
    }
}