package com.chinmay.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class JsonUtils {
    private JsonUtils() {
        // Private constructor to prevent instantiation
    }

    public static List<ConcurrentHashMap<String, String>> readJsonFile(String filePath) {
        List<ConcurrentHashMap<String, String>> resultList = new ArrayList<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(new File(filePath));

            if (jsonNode.isObject()) {
                // If it's a single object, convert it to a ConcurrentHashMap and add it to the list
                resultList.add(convertJsonNodeToMap(jsonNode));
            } else if (jsonNode.isArray()) {
                // If it's an array, iterate through the elements and convert them to ConcurrentHashMaps
                for (JsonNode element : jsonNode) {
                    resultList.add(convertJsonNodeToMap(element));
                }
            }
        } catch (IOException e) {
            // Handle any IO exceptions that may occur
            e.printStackTrace();
        }

        return resultList;
    }

    private static ConcurrentHashMap<String, String> convertJsonNodeToMap(JsonNode jsonNode) {
        ConcurrentHashMap<String, String> concurrentMap = new ConcurrentHashMap<>();
        jsonNode.fields().forEachRemaining(entry -> {
            String key = entry.getKey();
            String value = entry.getValue().asText();
            concurrentMap.put(key, value);
        });
        return concurrentMap;
    }
}