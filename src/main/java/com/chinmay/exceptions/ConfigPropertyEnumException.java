package com.chinmay.exceptions;

@SuppressWarnings("serial")
public class ConfigPropertyEnumException extends FrameworkTestException {
    public ConfigPropertyEnumException(String message) {
        super(message);
    }

    public ConfigPropertyEnumException(String message, Throwable cause) {
        super(message, cause);
    }
}
