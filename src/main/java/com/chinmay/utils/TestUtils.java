package com.chinmay.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestUtils {
    /**
     * Returns a logger object for the current class.
     *
     * @return A Logger object for the class of the calling method.
     */
    public static Logger log() {
        return LogManager.getLogger(Thread.currentThread().getStackTrace()[2].getClassName());
    }
}
