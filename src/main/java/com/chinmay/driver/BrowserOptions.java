package com.chinmay.driver;

import com.chinmay.constants.FrameworkConstants;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.safari.SafariOptions;

import java.time.Duration;
import java.util.HashMap;

import static com.chinmay.driver.DriverManager.getWebDriver;

public class BrowserOptions {
    private BrowserOptions() {
    }

    protected static ChromeOptions getChromeOptions() {
        HashMap<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", FrameworkConstants.getDownloadpath());

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--blink-settings=imagesEnabled=false");
        chromeOptions.addArguments("--disable-extensions");
        chromeOptions.addArguments("--disable-infobars");
        chromeOptions.addArguments("--disable-notifications");
        chromeOptions.setExperimentalOption("prefs", prefs);
        return chromeOptions;
    }

    protected static FirefoxOptions getFirefoxOptions() {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("browser.download.folderList", 2);
        profile.setPreference("browser.download.dir", FrameworkConstants.getDownloadpath());
        profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "text/csv");
        firefoxOptions.setProfile(profile);
        return firefoxOptions;
    }

    protected static EdgeOptions getEdgeOptions() {
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.addArguments("--disable-extensions");
        edgeOptions.addArguments("--disable-infobars");
        edgeOptions.addArguments("--disable-notifications");
        edgeOptions.addArguments("--inprivate");
        return edgeOptions;
    }

    protected static SafariOptions getSafariOptions() {
        SafariOptions safariOptions = new SafariOptions();

        // Set download directory
        safariOptions.setCapability("browser.download.dir", FrameworkConstants.getDownloadpath());
        safariOptions.setCapability("browser.download.folderList", 2);

        // Set MIME types to automatically save
        safariOptions.setCapability("browser.helperApps.neverAsk.saveToDisk", "text/csv");

        // Additional options specific to Safari can be set here

        return safariOptions;
    }

    public static void maximizeWindow() {
        getWebDriver().manage().window().maximize();
    }

    public static void launchUrl(String url) {
        getWebDriver().get(url);
    }

    public static void waitForElementsLoaded(Duration duration) {
        getWebDriver().manage().timeouts().implicitlyWait(duration);
    }

    protected static ChromeOptions getChromeHeadlessOptions() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--window-size=1800,900");
        chromeOptions.addArguments("--disable-gpu");
        return chromeOptions;
    }

    protected static FirefoxOptions getFirefoxHeadlessOptions() {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.addArguments("--headless");
        firefoxOptions.addArguments("--window-size=1800,900");
        return firefoxOptions;
    }

    protected static EdgeOptions getEdgeHeadlessOptions() {
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.addArguments("--headless");
        edgeOptions.addArguments("--window-size=1800,900");
        return edgeOptions;
    }

    protected static SafariOptions getSafariHeadlessOptions() {
        SafariOptions safariOptions = new SafariOptions();
        safariOptions.setCapability("headless", true);
        return safariOptions;
    }
}
