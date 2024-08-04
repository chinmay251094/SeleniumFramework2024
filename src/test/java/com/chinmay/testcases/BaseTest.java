package com.chinmay.testcases;

import com.chinmay.annotations.MustExtendBaseTest;
import com.chinmay.driver.Driver;
import com.chinmay.reports.AllureManager;
import lombok.SneakyThrows;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.util.Map;

import static com.chinmay.driver.Driver.initDriver;
import static com.chinmay.utils.EnvironmentUtils.*;
import static com.chinmay.utils.UtilityService.deleteFile;

@MustExtendBaseTest
public class BaseTest {
    protected BaseTest() {
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    @BeforeMethod(alwaysRun = true)
    protected void setUp(Object[] data) {
        Map<String, String> map = (Map<String, String>) data[0];
        initDriver(map.get("browser"), map.get("url"), map.get("mode"), map.get("version"), map.get("Test Description"), map.get("headless"));
        setMode(map.get("mode")); //For extent reports
        setUrl(map.get("url")); //For extent reports
        setBrowser(map.get("browser")); //For extent reports
        setIsHeadlessMode(map.get("headless")); //For extent reports
        AllureManager.setAllureEnvironmentInformation();
        AllureManager.addBrowserInformationOnAllureReport();
    }

    @AfterMethod(alwaysRun = true)
    protected void tearDown() {
        Driver.quitDriver();
        deleteFile();
    }
}
