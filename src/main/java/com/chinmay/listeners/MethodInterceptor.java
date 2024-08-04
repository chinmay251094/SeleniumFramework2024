package com.chinmay.listeners;

import com.chinmay.constants.FrameworkConstants;
import com.chinmay.enums.ConfigProperties;
import com.chinmay.exceptions.FrameworkTestException;
import com.chinmay.utils.ExcelUtils;
import com.chinmay.utils.JsonUtils;
import com.chinmay.utils.PropertyUtils;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MethodInterceptor implements IMethodInterceptor {
    private static final List<String> ordered = new ArrayList<>();

    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
        String source = getSource();
        List<ConcurrentHashMap<String, String>> list = getSourceData(source);

        List<IMethodInstance> result = new ArrayList<>();
        for (IMethodInstance method : methods) {
            String methodName = method.getMethod().getMethodName();
            list.stream()
                    .filter(hashMap -> methodName.equalsIgnoreCase(hashMap.get("Test Name"))
                            && "yes".equalsIgnoreCase(hashMap.get("Execute")))
                    .findFirst()
                    .ifPresent(hashMap -> {
                        setMethodDetails(method, hashMap);
                        result.add(method);
                    });
        }

        result.sort(Comparator.comparingInt(m -> m.getMethod().getPriority()));

        methods.forEach(m -> ordered.add(m.getMethod().getInstance().getClass().getName() + "." + m.getMethod().getMethodName()));

        return result;
    }

    private void setMethodDetails(IMethodInstance method, Map<String, String> hashMap) {
        method.getMethod().setDescription(hashMap.get("Test Description"));
        method.getMethod().setInvocationCount(Integer.parseInt(hashMap.get("Count")));
        method.getMethod().setPriority(Integer.parseInt(hashMap.get("Priority")));
    }

    private String getSource() {
        return PropertyUtils.get(ConfigProperties.DATA_SOURCE);
    }

    private List<ConcurrentHashMap<String, String>> getSourceData(String source) {
        try {
            if ("excel".equalsIgnoreCase(source)) {
                return ExcelUtils.getTestDetails(FrameworkConstants.getRunmanagersheet());
            } else if ("json".equalsIgnoreCase(source)) {
                return JsonUtils.readJsonFile(FrameworkConstants.getJsonspath("TestCases.json"));
            } else {
                throw new FrameworkTestException("No valid data source selected.");
            }
        } catch (Exception ex) {
            throw new FrameworkTestException("An error occurred while fetching data source.");
        }
    }
}


