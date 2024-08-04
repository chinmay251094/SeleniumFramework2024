/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package com.chinmay.reports;


import com.chinmay.driver.DriverManager;
import com.chinmay.enums.ConfigProperties;
import com.chinmay.utils.EnvironmentUtils;
import com.chinmay.utils.FileUtils;
import com.chinmay.utils.PropertyUtils;
import com.github.automatedowl.tools.AllureEnvironmentWriter;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;

import static org.openqa.selenium.OutputType.BYTES;

public class AllureManager {

    private AllureManager() {
    }

    private static final Logger logger = LogManager.getLogger(AllureManager.class);

    public static void setAllureEnvironmentInformation() {
        AllureEnvironmentWriter.allureEnvironmentWriter(
                ImmutableMap.<String, String>builder().
                        put("Test URL", EnvironmentUtils.getUrl()).
                        put("Target Execution", EnvironmentUtils.getMode()).
                        put("Headless Mode", "no").
                        put("Local Browser", EnvironmentUtils.getBrowser()).
                        put("Remote URL", PropertyUtils.get(ConfigProperties.REMOTE_URL)).
                        put("Remote Port", "4444").
                        build());

        System.out.println("Allure Reports is installed.");

    }

    @Attachment(value = "Failed test Screenshot", type = "image/png")
    public static byte[] takeScreenshotToAttachOnAllureReport() {
        try {
            return ((TakesScreenshot) DriverManager.getWebDriver()).getScreenshotAs(BYTES);
        } catch (Exception ex) {
            logger.error("Error taking screenshot for Allure report:", ex); // Log the exception
            return new byte[0]; // Return an empty byte array to indicate the failure
        }
    }

    @Attachment(value = "Take step Screenshot", type = "image/png")
    public static byte[] takeScreenshotStep() {
        try {
            return ((TakesScreenshot) DriverManager.getWebDriver()).getScreenshotAs(BYTES);
        } catch (Exception ex) {
            logger.error("Error taking step screenshot for Allure report:", ex); // Log the exception
            return new byte[0]; // Return an empty byte array to indicate the failure
        }
    }

    @Attachment(value = "Browser Information", type = "text/plain")
    public static String addBrowserInformationOnAllureReport() {
        return EnvironmentUtils.getOSInfo();
    }


    //Text attachments for Allure
    @Attachment(value = "{0}", type = "text/plain")
    public static String saveTextLog(String message) {
        return message;
    }

    public static void logStep(String message) {
        Allure.step(message);
    }

    //HTML attachments for Allure
    @Attachment(value = "{0}", type = "text/html")
    public static String attachHtml(String html) {
        return html;
    }

    public static void addAttachmentVideoAVI() {
        if (PropertyUtils.get(ConfigProperties.VIDEO_RECORD).toLowerCase().trim().equals("yes")) {
            try {
                //Get file Last Modified in folder
                File video = FileUtils.getFileLastModified("ExportData/Videos");
                if (video != null) {
                    Allure.addAttachment("Failed test Video record AVI", "video/avi", Files.asByteSource(video).openStream(), ".avi");
                } else {
                    logger.warn("Video record not found.");
                    logger.warn("Can not attachment Video in Allure report");
                }

            } catch (IOException e) {
                logger.error("Can not attachment Video in Allure report");
                e.printStackTrace();
            }
        }
    }

    public static void addAttachmentVideoMP4() {
        try {
            //Get file Last Modified in folder
            File video = FileUtils.getFileLastModified("ExportData/Videos");
            if (video != null) {
                Allure.addAttachment("Failed test Video record MP4", "video/mp4", Files.asByteSource(video).openStream(), ".mp4");
            } else {
                logger.warn("Video record not found.");
                logger.warn("Can not attachment Video in Allure report");
            }

        } catch (IOException e) {
            logger.error("Can not attachment Video in Allure report");
            e.printStackTrace();
        }
    }

}
