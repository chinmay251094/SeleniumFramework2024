package com.chinmay.exceptions;

@SuppressWarnings("serial")
public class FrameworkTestException extends RuntimeException {
    public FrameworkTestException(String message) {
        super(message);
    }

    public FrameworkTestException(String message, Throwable cause) {
        super(message, cause);
    }
}
