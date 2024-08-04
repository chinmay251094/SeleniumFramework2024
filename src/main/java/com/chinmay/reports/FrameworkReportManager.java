package com.chinmay.reports;

import com.aventstack.extentreports.ExtentTest;

public class FrameworkReportManager {
    private static final ThreadLocal<ExtentTest> extTest = new ThreadLocal<ExtentTest>();

    private FrameworkReportManager() {
    }

    static ExtentTest getExtent() {
        return extTest.get();
    }

    static void setExtent(ExtentTest extentTest) {
        extTest.set(extentTest);
    }

    static void unload() {
        extTest.remove();
    }
}
