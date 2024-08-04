package com.chinmay.listeners;

import com.chinmay.annotations.AutomationTeam;
import com.chinmay.enums.ConfigProperties;
import com.chinmay.exceptions.BrowserInvocationFailedException;
import com.chinmay.exceptions.FrameworkTestException;
import com.chinmay.exceptions.FrameworkTestSkipException;
import com.chinmay.reports.AllureManager;
import com.chinmay.reports.FrameworkReports;
import com.chinmay.utils.PropertyUtils;
import com.chinmay.utils.ScreenRecoderUtils;
import lombok.SneakyThrows;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.*;

import java.awt.*;
import java.io.IOException;

import static com.chinmay.driver.DriverManager.getWebDriver;
import static com.chinmay.pageobjects.PageActions.addScreenshotToAllureReport;
import static com.chinmay.reports.FrameworkReportLogger.*;
import static com.chinmay.reports.FrameworkReports.*;
import static com.chinmay.utils.ELKUtils.sendDetailsToElk;
import static com.chinmay.utils.UtilityService.sendEmailWithAttachment;
import static com.chinmay.utils.UtilityService.takeScreenshot;

public class ListenerClass implements ITestListener, ISuiteListener {
    private final ScreenRecoderUtils screenRecorder;

    @SneakyThrows
    public ListenerClass() {
        try {
            screenRecorder = new ScreenRecoderUtils(); // Initialize the screenRecorder object
        } catch (IOException | AWTException e) {
            throw new FrameworkTestException("Unable to initialize screen recorder");
        }
    }

    @Override
    public void onStart(ISuite suite) {
        initReports();
    }

    @Override
    public void onFinish(ISuite suite) {
        flushReports();
        if (PropertyUtils.get(ConfigProperties.SEND_REPORTS_MAIL).equalsIgnoreCase("yes")) {
            sendEmailWithAttachment();
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        FrameworkReports.createTest(result.getMethod().getDescription());
        String testDescription = result.getMethod().getDescription();
        addAuthorsAndCategory(result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(AutomationTeam.class));
        setInfo();
        if (PropertyUtils.get(ConfigProperties.VIDEO_RECORD).trim().equalsIgnoreCase("yes") && testDescription != null && !testDescription.isEmpty()) {
            screenRecorder.startRecording(testDescription);
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        pass("<span style='background-color: #008000; color: white;'><b>" + result.getMethod().getDescription() + " has passed successfully. 😊</b></span>", false);
        AllureManager.saveTextLog("Test case: " + result.getMethod().getDescription() + " has passed successfully.");
        if (getWebDriver().getClass() == RemoteWebDriver.class && PropertyUtils.get(ConfigProperties.SEND_RESULTS_TO_DASHBOARD).equalsIgnoreCase("yes")) {
            sendDetailsToElk(result.getMethod().getDescription(), "pass");
        }
        handleScreenshotsAndVideo(PropertyUtils.get(ConfigProperties.SCREENSHOT_ALL_STEPS), PropertyUtils.get(ConfigProperties.TAKE_SCREENSHOT_PASS), "PASSED_" + result.getMethod().getDescription());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        try {
            if (result.getThrowable() instanceof FrameworkTestException || result.getThrowable() instanceof BrowserInvocationFailedException) {
                handleFailure("<span style='background-color: red; color: white;'><b>" + result.getThrowable().getMessage() + " 😔</b></span>", true);
                AllureManager.saveTextLog("Test case: " + result.getThrowable().getMessage());
            } else {
                handleFailure("<span style='background-color: red; color: white;'><b>" + result.getMethod().getDescription() + " has failed the test. 😔</span></b>", true);
                failWithDetails("<b><font color='red'> Reasons for test failure stated below</font></b>", result.getThrowable());
                AllureManager.saveTextLog("Test case: " + result.getMethod().getDescription() + " has failed the test.");
            }
            if (getWebDriver().getClass() == RemoteWebDriver.class && PropertyUtils.get(ConfigProperties.SEND_RESULTS_TO_DASHBOARD).equalsIgnoreCase("yes")) {
                sendDetailsToElk(result.getMethod().getDescription(), "fail");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        handleScreenshotsAndVideo(PropertyUtils.get(ConfigProperties.SCREENSHOT_ALL_STEPS), PropertyUtils.get(ConfigProperties.TAKE_SCREENSHOT_FAIL), "FAILED_" + result.getMethod().getDescription());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        if (result.getStatus() == ITestResult.SKIP) {
            handleSkipped(result);
        }
    }

    private void handleSkipped(ITestResult result) {
        if (result.getThrowable() instanceof FrameworkTestSkipException) {
            handleSkipped("<span style='background-color: #FF8C00; color: white;'><b>" + result.getThrowable().getMessage() + "</b></span> 👋", true);
            AllureManager.saveTextLog("Test case: " + result.getThrowable().getMessage());
        } else {
            handleSkipped("<span style='color: #FF8C00'><b>" + result.getMethod().getDescription() + " has been skipped.</b></span>", true);
            AllureManager.saveTextLog("Test case: " + result.getMethod().getDescription() + " has been skipped.");
        }
        handleScreenshotsAndVideo(PropertyUtils.get(ConfigProperties.SCREENSHOT_ALL_STEPS), PropertyUtils.get(ConfigProperties.TAKE_SCREENSHOT_SKIP), "SKIPPED_" + result.getMethod().getDescription());
    }

    private void handleScreenshotsAndVideo(String screenshotAllSteps, String takeScreenshot, String screenshotName) {
        if (screenshotAllSteps.equalsIgnoreCase("yes")) {
            addScreenshotToAllureReport();
        }
        if (takeScreenshot.equalsIgnoreCase("yes")) {
            takeScreenshot(screenshotName);
        }
        if (PropertyUtils.get(ConfigProperties.VIDEO_RECORD).equalsIgnoreCase("yes")) {
            screenRecorder.stopRecording(true);
        }
    }

    private void handleFailure(String message, boolean saveTextLog) {
        fail(message, true);
        if (saveTextLog) {
            AllureManager.saveTextLog("Test case: " + message);
        }
    }

    private void handleSkipped(String message, boolean saveTextLog) {
        skip(message, true);
        if (saveTextLog) {
            AllureManager.saveTextLog("Test case: " + message);
        }
    }

    private void addAuthorsAndCategory(AutomationTeam teamAutomation) {
        if (teamAutomation != null) {
            addAuthors(teamAutomation.author());
            addCategory(teamAutomation.category());
        }
    }

    private void setInfo() {
        setBrowserInfo();
        setExecutionEnvironmentInfo();
        setExecutionModeInfo();
        setUrlInfo();
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        /* Not in use for now */

    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        /* Not in use for now */

    }

    @Override
    public void onStart(ITestContext context) {
        /* Not in use for now */

    }

    @Override
    public void onFinish(ITestContext context) {
        /* Not in use for now */

    }
}
