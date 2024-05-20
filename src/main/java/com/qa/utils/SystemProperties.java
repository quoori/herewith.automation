package com.qa.utils;

import org.slf4j.Logger;

import java.util.Properties;

public class SystemProperties {

    private SystemProperties() {
    }

    ;

    public static String UnixNL = "\n";

    public static String getFileSeparator() {
        return System.getProperty("file.separator");
    }

    public static String getClasspath() {
        return System.getProperty("java.class.path");
    }

    public static String getJavaHome() {
        return System.getProperty("java.home");
    }

    public static String getJavaVendor() {
        return System.getProperty("java.vendor");
    }

    public static String getJavaVendorUrl() {
        return System.getProperty("java.vendor.url");
    }

    public static String getJavaVersion() {
        return System.getProperty("java.version");
    }

    public static String getLineSeparator() {
        return System.getProperty("line.separator");
    }

    public static String getOperatingSystemArchitecture() {
        return System.getProperty("os.arch");
    }

    public static String getOperatingSystemName() {
        return System.getProperty("os.name");
    }

    public static String getOperatingSystemVersion() {
        return System.getProperty("os.version");
    }

    public static String getPathSeparator() {
        return System.getProperty("path.separator");
    }

    public static String getUserDirectory() {
        return System.getProperty("user.dir");
    }

    public static String getUserHome() {
        return System.getProperty("user.home");
    }

    public static String getUserName() {
        return System.getProperty("user.name");
    }

    public static void printAllSystemProperties(Logger logger) {
        Properties systemProperties = System.getProperties();
        for (Object property : systemProperties.keySet()) {
            logger.info(property.toString() + " = " + systemProperties.get(property));
        }
    }
}
