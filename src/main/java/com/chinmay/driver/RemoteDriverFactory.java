package com.chinmay.driver;

import com.chinmay.exceptions.FrameworkTestException;
import lombok.SneakyThrows;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

import static com.chinmay.driver.BrowserOptions.*;

public class RemoteDriverFactory {
    private RemoteDriverFactory() {
    }

    @SneakyThrows
    protected static WebDriver getRemoteDriver(String browser, String version, URL url) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.BROWSER_NAME, browser);
        capabilities.setCapability(CapabilityType.BROWSER_VERSION, version);

        if (browser.equalsIgnoreCase("chrome")) {
            capabilities.setCapability(ChromeOptions.CAPABILITY, getChromeOptions());
        } else if (browser.equalsIgnoreCase("firefox")) {
            capabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, getFirefoxOptions());
        } else if (browser.equalsIgnoreCase("edge")) {
            capabilities.setCapability(EdgeOptions.CAPABILITY, getEdgeOptions());
        } else {
            throw new FrameworkTestException("Invalid browser selected.");
        }
        return new RemoteWebDriver(url, capabilities);
    }
}
