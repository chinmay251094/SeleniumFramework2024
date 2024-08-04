package com.chinmay.utils;

import com.chinmay.driver.DriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.io.FileHandler;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class ScreenshotUtils {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");

    private ScreenshotUtils() {
    }

    public static String getBase64Image() {
        return ((TakesScreenshot) DriverManager.getWebDriver()).getScreenshotAs(OutputType.BASE64);
    }

    public static String takeScreenshot() {
        synchronized (DATE_FORMAT) {
            String currentTime = DATE_FORMAT.format(new Date());
            String dirPath = System.getProperty("user.dir") + "/media/Screenshots/";
            String path = dirPath + "ExtentReport_" + currentTime + ".png";

            // Create directory if it doesn't exist
            File directory = new File(dirPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            try {
                File screenshotFile = ((TakesScreenshot) DriverManager.getWebDriver()).getScreenshotAs(OutputType.FILE);
                File destFile = new File(path);
                FileHandler.copy(screenshotFile, destFile);
                return path;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
