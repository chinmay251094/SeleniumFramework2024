package com.chinmay.utils;

import com.chinmay.constants.FrameworkConstants;
import com.chinmay.enums.ConfigProperties;
import com.chinmay.exceptions.FrameworkTestException;
import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class DataProviderUtils {
    private static List<ConcurrentHashMap<String, String>> completeTestMethodsList = new ArrayList<>();

    private DataProviderUtils() {
    }

    @DataProvider(parallel = false)
    public static Object[] getDataFromSheet(Method method) {
        String name = method.getName();
        List<Map<String, String>> executeMethodsList = new ArrayList<>();

        try {
            String source = getSource(); // Determine the data source (Excel or JSON)

            List<ConcurrentHashMap<String, String>> dataList;
            if ("excel".equalsIgnoreCase(source)) {
                dataList = getDataListFromExcel();
            } else if ("json".equalsIgnoreCase(source)) {
                dataList = getDataListFromJson();
            } else {
                throw new FrameworkTestException("No valid data source selected.");
            }

            for (ConcurrentHashMap<String, String> hashMap : dataList) {
                String testName = hashMap.get("Test Name");
                String executeFlag = hashMap.get("Execute");

                // Check if the test name matches and execute flag is "yes"
                if (name.equalsIgnoreCase(testName) && "yes".equalsIgnoreCase(executeFlag)) {
                    executeMethodsList.add(new HashMap<>(hashMap));
                }
            }
        } catch (Exception ex) {
            throw new FrameworkTestException("An error occurred while fetching data source.", ex);
        }

        return executeMethodsList.toArray();
    }

    private static String getSource() {
        // Logic to determine the source ("excel" or "json")
        return PropertyUtils.get(ConfigProperties.DATA_SOURCE);
    }

    private static List<ConcurrentHashMap<String, String>> getDataListFromExcel() {
        if (completeTestMethodsList.isEmpty()) {
            completeTestMethodsList = ExcelUtils.getTestDetails(FrameworkConstants.getDatafilesheet());
        }
        return completeTestMethodsList;
    }

    private static List<ConcurrentHashMap<String, String>> getDataListFromJson() {
        return JsonUtils.readJsonFile(FrameworkConstants.getJsonspath("TestData.json"));
    }
}

