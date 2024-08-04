package com.chinmay.reports;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.chinmay.enums.ConfigProperties;
import com.chinmay.utils.ScreenshotUtils;

import java.util.Objects;

import static com.chinmay.reports.FrameworkReportManager.getExtent;
import static com.chinmay.utils.PropertyUtils.get;

public final class FrameworkReportLogger {
    private FrameworkReportLogger() {
    }

    public static void pass(String message) {
        logWithScreenshotIfRequired(message, Status.PASS);
    }

    public static void fail(String message) {
        logWithScreenshotIfRequired(message, Status.FAIL);
    }

    public static void skip(String message) {
        logWithScreenshotIfRequired(message, Status.SKIP);
    }

    public static void info(String message) {
        getExtent().info(message);
    }

    public static void pass(String message, String validationReason) {
        pass(message + " - " + validationReason);
    }

    public static void pass(String message, boolean isScreenshotNeeded) {
        logWithScreenshotIfRequired(message, Status.PASS, isScreenshotNeeded);
    }

    public static void info(String message, boolean isScreenshotNeeded) {
        if (isScreenshotNeeded) {
            logWithScreenshotIfRequired(message, Status.INFO, true);
        } else {
            info(message);
        }
    }

    public static void fail(String message, boolean isScreenshotNeeded) {
        logWithScreenshotIfRequired(message, Status.FAIL, isScreenshotNeeded);
    }

    public static void failWithDetails(String message, Throwable throwable) {
        ExtentTest test = getExtent();
        test.log(Status.FAIL, message);
        test.log(Status.FAIL, MarkupHelper.createLabel("Details:", ExtentColor.RED));
        test.log(Status.FAIL, throwable);
    }

    public static void skip(String message, boolean isScreenshotNeeded) {
        logWithScreenshotIfRequired(message, Status.SKIP, isScreenshotNeeded);
    }

    private static void logWithScreenshotIfRequired(String message, Status status) {
        logWithScreenshotIfRequired(message, status, true);
    }

    private static void logWithScreenshotIfRequired(String message, Status status, boolean isScreenshotNeeded) {
        if (isScreenshotNeeded && shouldTakeScreenshot()) {
            getExtent().log(status, message, MediaEntityBuilder.createScreenCaptureFromPath(Objects.requireNonNull(ScreenshotUtils.takeScreenshot())).build());
        } else {
            getExtent().log(status, message);
        }
    }

    private static boolean shouldTakeScreenshot() {
        String passedScreenshotConfig = get(ConfigProperties.PASSEDSCREENSHOT);
        String failedScreenshotConfig = get(ConfigProperties.FAILEDSCREENSHOT);
        String skippedScreenshotConfig = get(ConfigProperties.SKIPPPEDSCREENSHOT);
        return (passedScreenshotConfig.equalsIgnoreCase("yes") || failedScreenshotConfig.equalsIgnoreCase("yes") || skippedScreenshotConfig.equalsIgnoreCase("yes"));
    }
}
