package com.chinmay.driver;

import com.chinmay.enums.ConfigProperties;
import com.chinmay.utils.PropertyUtils;

import java.util.Objects;

import static com.chinmay.driver.BrowserOptions.launchUrl;
import static com.chinmay.driver.BrowserOptions.maximizeWindow;
import static com.chinmay.driver.DriverManager.*;
import static com.chinmay.utils.TestUtils.log;
import static com.chinmay.utils.UtilityService.gmailUtils;

public final class Driver {
    private Driver() {
    }

    public static void initDriver(String browser, String url, String mode, String version, String description, String headless) {
        if (Objects.isNull(getWebDriver())) {
            if (log().isInfoEnabled()) {
                log().info("{} is under test.", description);
                log().info(String.format("Server used: %s", url));
                log().info(String.format("Run mode used: %s", mode));
                log().info(String.format("Browser used: %s", browser));
                if (mode.equalsIgnoreCase("remote")) {
                    log().info(String.format("Version used: %s", version));
                }
            }
            setDriver(DriverFactory.get(mode, browser, version, headless));
            maximizeWindow();
            launchUrl(url);
        }
    }

    public static void quitDriver() {
        if (Objects.nonNull(getWebDriver())) {
            getWebDriver().quit();
            unload();
        }

        if (PropertyUtils.get(ConfigProperties.SEND_REPORTS_MAIL).equalsIgnoreCase("yes")) {
            try {
                gmailUtils();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
