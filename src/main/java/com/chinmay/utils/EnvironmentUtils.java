package com.chinmay.utils;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import static com.chinmay.driver.DriverManager.getWebDriver;

public final class EnvironmentUtils {
    private EnvironmentUtils() {
    }

    private static String mode;
    private static String url;
    private static String browser;
    private static String isHeadlessMode;

    public static String getMode() {
        return mode;
    }

    public static void setMode(String mode) {
        EnvironmentUtils.mode = mode;
    }

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        EnvironmentUtils.url = url;
    }

    public static String getBrowserIcon(boolean isVersionNeeded) {
        Capabilities capabilities = ((RemoteWebDriver) getWebDriver()).getCapabilities();

        if (browser.equalsIgnoreCase("chrome")) {
            if (isVersionNeeded) {
                return "<i class='fa fa-chrome fa-lg' aria-hidden='true'></i>" + " <b>CHROME</b> " + capabilities.getBrowserVersion();
            } else {
                return "<i class='fa fa-chrome' aria-hidden='true'></i>" + " CHROME: ";
            }
        } else if (browser.equalsIgnoreCase("firefox")) {
            if (isVersionNeeded) {
                return "<i class='fa fa-firefox fa-lg' aria-hidden='true'></i>" + " <b>FIREFOX</b> " + capabilities.getBrowserVersion();
            } else {
                return "<i class='fa fa-firefox' aria-hidden='true'></i>" + " FIREFOX: ";
            }
        } else if (browser.equalsIgnoreCase("edge")) {
            if (isVersionNeeded) {
                return "<i class='fas fa-edge fa-lg'></i>" + " <b>EDGE</b> " + capabilities.getBrowserVersion();
            } else {
                return "<i class='fas fa-edge'></i>" + " EDGE: ";
            }
        } else {
            return "Unknown browser";
        }
    }

    public static void setBrowser(String browser) {
        EnvironmentUtils.browser = browser;
    }

    public static String getOSInfo() {
        return System.getProperty("os.name");
    }

    public static String getBrowser() {
        return browser;
    }

    public static String isIsHeadlessMode() {
        return isHeadlessMode;
    }

    public static void setIsHeadlessMode(String isHeadlessMode) {
        if (isHeadlessMode.equalsIgnoreCase("yes")) {
            EnvironmentUtils.isHeadlessMode = "\uD83D\uDEAB️" + " NO USER INTERFACE mode";
        } else {
            EnvironmentUtils.isHeadlessMode = "\uD83D\uDCBB" + " USER INTERFACE mode";
        }
    }
}
