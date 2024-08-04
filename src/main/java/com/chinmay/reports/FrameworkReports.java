package com.chinmay.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.chinmay.driver.DriverManager;
import com.chinmay.enums.TestCategory;
import com.chinmay.enums.Tester;
import com.chinmay.utils.EnvironmentUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import tech.grasshopper.reporter.ExtentPDFReporter;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static com.chinmay.constants.FrameworkConstants.getExtentReportFilepath;
import static com.chinmay.utils.EnvironmentUtils.getUrl;
import static com.chinmay.utils.EnvironmentUtils.isIsHeadlessMode;

public final class FrameworkReports {
    private static ExtentReports extentReports;

    private FrameworkReports() {
    }

    public static void initReports() {
        if (Objects.isNull(extentReports)) {
            extentReports = new ExtentReports();
            ExtentSparkReporter spark = new ExtentSparkReporter(getExtentReportFilepath());
            extentReports.attachReporter(spark);
            spark.config().setTheme(Theme.STANDARD);
            spark.config().setDocumentTitle("Test Reports");
            spark.config().setReportName("Automation Test Reports");
        }

        ExtentPDFReporter pdf = new ExtentPDFReporter(getExtentReportFilepath());
        extentReports.attachReporter(pdf);
    }

    public static void flushReports() {
        if (Objects.nonNull(extentReports)) {
            extentReports.flush();
        }
        FrameworkReportManager.unload();

        try {
            Desktop.getDesktop().browse(new File(getExtentReportFilepath()).toURI());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createTest(String testcasename) {
        FrameworkReportManager.setExtent(extentReports.createTest(EnvironmentUtils.getBrowserIcon(false) + " " + testcasename));
    }

    public static void addAuthors(Tester[] authors) {
        for (Tester tester : authors) {
            FrameworkReportManager.getExtent().assignAuthor(tester.toString());
        }
    }

    public static void addCategory(TestCategory[] categories) {
        for (TestCategory category : categories) {
            FrameworkReportManager.getExtent().assignCategory(category.toString());
        }
    }

    private static String getBrowser() {
        Capabilities cap = ((RemoteWebDriver) DriverManager.getWebDriver()).getCapabilities();
        String browserName = cap.getBrowserName().toLowerCase();
        return StringUtils.capitalize(browserName);
    }

    private static String getVersion() {
        Capabilities cap = ((RemoteWebDriver) DriverManager.getWebDriver()).getCapabilities();
        return cap.getBrowserVersion();
    }

    public static void setBrowserInfo() {
        FrameworkReportManager.getExtent().assignDevice(getBrowser() + getVersion());
    }

    public static void setExecutionEnvironmentInfo() {
        FrameworkReportManager.getExtent().info("<font color='blue'><b>" + "\uD83D\uDCBB "
                + System.getProperty("os.name").replace(" ", "_") + "</b></font>" + " --------- " + EnvironmentUtils.getBrowserIcon(true));
    }

    public static void setUrlInfo() {
        FrameworkReportManager.getExtent().info("<font color='blue'><b>" + "\uD83C\uDF10 " + getUrl() + "</b></font>");
    }

    public static void setExecutionModeInfo() {
        FrameworkReportManager.getExtent().info("<font color='blue'><b>" + isIsHeadlessMode() + "</b></font>");
    }
}
