package com.chinmay.exceptions;

public class BrowserInvocationFailedException extends FrameworkTestException {

    public BrowserInvocationFailedException(String message) {
        super(message);
    }

    public BrowserInvocationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
