package com.chinmay.constants;

import com.chinmay.enums.ConfigProperties;
import com.chinmay.utils.DateTimeUtils;
import com.chinmay.utils.PropertyUtils;

import java.time.Duration;

public final class FrameworkConstants {

    private static final String RESOURCEPATH = System.getProperty("user.dir") + "/src/test/resources/";
    private static final String CONFIGPATH = RESOURCEPATH + "/config/config.properties";
    private static final String IMAGEPATH = RESOURCEPATH + "/images/";
    private static final String EXCELSHEETSPATH = RESOURCEPATH + "/excelsheets/";
    private static final String JSONSPATH = RESOURCEPATH + "/json/";
    private static final String EXTENTREPORTSPATH = System.getProperty("user.dir") + "/extent-test-output/";
    private static final Duration EXPLICITWAIT = Duration.ofSeconds(30);
    private static final String RUNMANAGERSHEET = "RunManager";
    private static final String DATAFILESHEET = "DataFile";
    private static final String DOWNLOADPATH = System.getProperty("user.dir") + "\\downloads";
    private static final String EXPORT_VIDEO_PATH = "/local-execution-videos";
    private static String extentReportFilepath = "";

    private FrameworkConstants() {
    }

    public static String getDownloadpath() {
        return DOWNLOADPATH;
    }

    public static String getResourcepath() {
        return RESOURCEPATH;
    }

    public static String getDatafilesheet() {
        return DATAFILESHEET;
    }

    public static String getRunmanagersheet() {
        return RUNMANAGERSHEET;
    }

    public static String getExtentReportFilepath() {
        if (extentReportFilepath.isEmpty()) {
            extentReportFilepath = createReportPath();
        }
        return extentReportFilepath;
    }

    public static String createReportPath() {
        if (PropertyUtils.get(ConfigProperties.OVERRIDEREPORTS).equalsIgnoreCase("no")) {
            return EXTENTREPORTSPATH + "/" + "Framework_Ver1.0_" + DateTimeUtils.getDateTime() + ".html";
        } else {
            return EXTENTREPORTSPATH + "/" + "index.html";
        }
    }

    public static String getJsonspath(String fileName) {
        return JSONSPATH + "\\" + fileName + "";
    }

    public static String getExcelsheetspath() {
        return EXCELSHEETSPATH;
    }

    public static String getImagepath() {
        return IMAGEPATH;
    }

    public static Duration getExplicitwait() {
        return EXPLICITWAIT;
    }

    public static String getConfigPath() {
        return CONFIGPATH;
    }

    public static String getVideoRecordingFilePathFilePath() {
        return EXPORT_VIDEO_PATH.replace(":", "-");
    }
}
