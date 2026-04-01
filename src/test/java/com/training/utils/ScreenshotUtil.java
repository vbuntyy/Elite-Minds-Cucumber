package com.training.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScreenshotUtil {

    private static final Logger logger =
            LoggerFactory.getLogger(ScreenshotUtil.class);

    private ScreenshotUtil() {
    }

    // Screenshot as bytes (useful for reports like Allure)
    public static byte[] captureScreenshotAsBytes(WebDriver driver) {
        logger.info("Capturing screenshot as bytes");
        try {
            byte[] screenshot =
                    ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            logger.info("Screenshot successfully captured as bytes");
            return screenshot;
        } catch (Exception e) {
            logger.error("Failed to capture screenshot as bytes", e);
            throw new RuntimeException("Unable to capture screenshot as bytes", e);
        }
    }

    // Screenshot saved to file
    public static String captureScreenshotToFile(WebDriver driver, String scenarioName) {
        logger.info("Attempting to capture screenshot for scenario: {}", scenarioName);

        try {
            String timeStamp =
                    new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            String safeName =
                    scenarioName.replaceAll("[^a-zA-Z0-9-_]", "_");

            Path screenshotDir = Paths.get("target", "screenshots");
            Files.createDirectories(screenshotDir);

            String fileName = safeName + "_" + timeStamp + ".png";
            Path destination = screenshotDir.resolve(fileName);

            File sourceFile =
                    ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            Files.copy(sourceFile.toPath(), destination);

            logger.info("Screenshot captured successfully");
            logger.info("Screenshot saved at: {}", destination.toAbsolutePath());

            return destination.toString();

        } catch (IOException e) {
            logger.error("Failed to save screenshot to file for scenario: {}",
                    scenarioName, e);
            throw new RuntimeException("Unable to save screenshot file", e);
        }
    }
}