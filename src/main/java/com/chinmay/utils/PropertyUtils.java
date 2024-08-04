package com.chinmay.utils;

import com.chinmay.constants.FrameworkConstants;
import com.chinmay.enums.ConfigProperties;
import com.chinmay.exceptions.ConfigPropertyEnumException;
import com.chinmay.exceptions.InvalidFilePathException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public final class PropertyUtils {
    private static final Map<String, String> CONFIGMAP = new HashMap<>();
    private static final Properties property = new Properties();

    static {
        FileInputStream file;
        try {
            file = new FileInputStream(FrameworkConstants.getConfigPath());
            property.load(file);

            for (Map.Entry<Object, Object> entry : property.entrySet()) {
                CONFIGMAP.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()).trim());
            }
        } catch (IOException e) {
            throw new InvalidFilePathException("File is not found at the provided path.");
        }
    }

    private PropertyUtils() {
    }

    public static String get(ConfigProperties key) {
        if (Objects.isNull(key) || Objects.isNull(CONFIGMAP.get(key.name().toLowerCase()))) {
            throw new ConfigPropertyEnumException("Property name " + key + " is no found. Check config.properties");
        }
        return CONFIGMAP.get(key.name().toLowerCase());
    }
}
