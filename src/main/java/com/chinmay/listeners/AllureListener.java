package com.chinmay.listeners;

import com.chinmay.driver.DriverManager;
import com.chinmay.reports.AllureManager;
import io.qameta.allure.Allure;
import io.qameta.allure.listener.TestLifecycleListener;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.TestResult;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;

public class AllureListener implements TestLifecycleListener {

    @Override
    public void beforeTestSchedule(TestResult result) {
    }

    @Override
    public void afterTestSchedule(TestResult result) {
    }

    @Override
    public void beforeTestUpdate(TestResult result) {
    }

    @Override
    public void afterTestUpdate(TestResult result) {
    }

    @Override
    public void beforeTestStart(TestResult result) {
        AllureManager.setAllureEnvironmentInformation();
        AllureManager.addBrowserInformationOnAllureReport();
    }

    @Override
    public void afterTestStart(TestResult result) {
    }

    @Override
    public void beforeTestStop(TestResult result) {
        if (result.getStatus() == Status.FAILED || result.getStatus() == Status.SKIPPED || result.getStatus() == Status.BROKEN) {
            if (DriverManager.getWebDriver() != null) {
                Allure.addAttachment(result.getName() + "_Failed_Screenshot", new ByteArrayInputStream(((TakesScreenshot) DriverManager.getWebDriver()).getScreenshotAs(OutputType.BYTES)));
            }
            AllureManager.addAttachmentVideoAVI();
        }
    }

    @Override
    public void afterTestStop(TestResult result) {
    }

    @Override
    public void beforeTestWrite(TestResult result) {
    }

    @Override
    public void afterTestWrite(TestResult result) {
    }

}
