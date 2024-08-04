package com.chinmay.exceptions;

@SuppressWarnings("serial")
public class InvalidFilePathException extends FrameworkTestException {
    public InvalidFilePathException(String message) {
        super(message);
    }

    public InvalidFilePathException(String message, Throwable cause) {
        super(message, cause);
    }
}
