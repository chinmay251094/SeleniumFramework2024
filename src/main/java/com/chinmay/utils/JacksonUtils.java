package com.chinmay.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;

public class JacksonUtils {
    public static <T> T deserializeJSON(String fileName, Class<T> clazz) throws IOException {
        InputStream is = JacksonUtils.class.getClassLoader().getResourceAsStream(fileName);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(is, clazz);
    }

    public static <T> T[] deserializeArray(String fileName, Class<T[]> arrayType) throws IOException {
        InputStream is = JacksonUtils.class.getClassLoader().getResourceAsStream(fileName);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(is, arrayType);
    }

    public static ConcurrentHashMap<String, String> deserializeObject(String fileName) throws IOException {
        InputStream is = JacksonUtils.class.getClassLoader().getResourceAsStream(fileName);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(is, ConcurrentHashMap.class);
    }
}
