package com.chinmay.utils;

import com.chinmay.driver.DriverManager;
import com.chinmay.enums.Locator;
import com.chinmay.pageobjects.PageActions;
import org.assertj.core.api.Assertions;
import org.testng.Assert;

public final class VerificationUtils {
    private VerificationUtils() {
    }

    public static void runAndVerifyMandatoryPass(Runnable action, String failureMessage) {
        try {
            action.run();
        } catch (Exception e) {
            // Log or print the exception details if needed
            e.printStackTrace();

            // Fail the test explicitly using TestNG or JUnit
            Assert.fail(failureMessage);
        }
    }

    public static void validateElementExists(String value, Locator locator) {
        Assertions.assertThat(PageActions.getWebElements(value, locator)).size().isNotZero();
    }

    public static void validate(Object actual, Object expected, String message) {
        try {
            logFile(message);
            logFile(actual, expected);
            Assert.assertEquals(actual, expected, message);
        } catch (AssertionError assertionError) {
            Assert.fail(message);
        }
    }

    public static void validateUrl(Object expected, String message) {
        try {
            String url = DriverManager.getWebDriver().getCurrentUrl();
            logFile(message);
            logFile(url, expected);
            Assert.assertEquals(url, expected, message);
        } catch (AssertionError assertionError) {
            Assert.fail(message);
        }
    }

    private static void logFile(Object actual, Object expected) {
        LogUtils.log().info("Actual: " + actual);
        LogUtils.log().info("Expected: " + expected);
    }

    private static void logFile(String message) {
        LogUtils.log().info(message);
    }
}
