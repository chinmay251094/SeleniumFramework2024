package com.chinmay.listeners;

import com.chinmay.enums.ConfigProperties;
import com.chinmay.utils.PropertyUtils;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryFailedTests implements IRetryAnalyzer {

    private int count = 0;
    private int retries = 1;

    public boolean retry(ITestResult result) {
        boolean value = false;
        try {
            if (PropertyUtils.get(ConfigProperties.RETRYFAILEDTEST).equalsIgnoreCase("yes")) {
                value = count < retries;
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

}
