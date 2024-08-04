package com.chinmay.driver;

import com.chinmay.enums.ConfigProperties;
import com.chinmay.utils.PropertyUtils;
import lombok.SneakyThrows;
import org.openqa.selenium.WebDriver;

import java.net.URL;

public final class DriverFactory {
    private DriverFactory() {
    }

    @SneakyThrows
    public static WebDriver get(String mode, String browser, String version, String headless) {
        String docker = "remote";
        URL url = new URL(PropertyUtils.get(ConfigProperties.REMOTE_URL));

        if (mode.equalsIgnoreCase(docker)) {
            return RemoteDriverFactory.getRemoteDriver(browser, version, url);
        } else {
            return LocalDriverFactory.getLocalDriver(browser, headless);
        }
    }
}