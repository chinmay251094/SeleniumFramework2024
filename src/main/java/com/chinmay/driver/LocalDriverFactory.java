package com.chinmay.driver;

import com.chinmay.exceptions.FrameworkTestException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import static com.chinmay.driver.BrowserOptions.*;

public class LocalDriverFactory {
    private LocalDriverFactory() {
    }

    protected static WebDriver getLocalDriver(String browser, String headless) {
        return switch (browser.toLowerCase()) {
            case "chrome" -> headless.equalsIgnoreCase("yes") ?
                    new ChromeDriver(getChromeHeadlessOptions()) :
                    new ChromeDriver(getChromeOptions());
            case "firefox" -> headless.equalsIgnoreCase("yes") ?
                    new FirefoxDriver(getFirefoxHeadlessOptions()) :
                    new FirefoxDriver(getFirefoxOptions());
            case "edge" -> headless.equalsIgnoreCase("yes") ?
                    new EdgeDriver(getEdgeHeadlessOptions()) :
                    new EdgeDriver(getEdgeOptions());
            case "safari" -> headless.equalsIgnoreCase("yes") ?
                    new SafariDriver(getSafariHeadlessOptions()) :
                    new SafariDriver(getSafariOptions());
            default -> throw new FrameworkTestException("Invalid browser selected.");
        };
    }
}
