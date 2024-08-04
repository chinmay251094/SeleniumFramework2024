package com.chinmay.exceptions;

import org.testng.SkipException;

public class FrameworkTestSkipException extends SkipException {
    public FrameworkTestSkipException(String message) {
        super(message);
    }
}

