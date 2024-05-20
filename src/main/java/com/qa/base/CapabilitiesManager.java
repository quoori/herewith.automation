package com.qa.base;

import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.options.XCUITestOptions;

import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class CapabilitiesManager extends BaseSetup {

    public UiAutomator2Options androidOptions() throws IOException {
        logger().info("Android Ui Automator Options being set");
        Properties properties = new PropertyManager().getProperties(CONFIG_FILENAME);
        UiAutomator2Options options = new UiAutomator2Options();
        try {
            options.setUdid(getUdid());
            options.setDeviceName(getDeviceName());
            options.setAppPackage(properties.getProperty("androidAppPackage"));
            options.setAppActivity(properties.getProperty("androidAppActivity"));
            options.setAppWaitForLaunch(true);
            options.setNewCommandTimeout(Duration.ofSeconds(22000));
            options.setNoReset(true);
            options.ignoreHiddenApiPolicyError();
            options.autoGrantPermissions();
        } catch (Exception e) {
            e.printStackTrace();
            logger().fatal("Android Ui Automator Options issue: " + e.getLocalizedMessage());
        }
        return options;
    }

    public XCUITestOptions iosOptions() throws IOException {
        logger().info("iOS XCUITest Options being set");
        Properties properties = new PropertyManager().getProperties(CONFIG_FILENAME);
        XCUITestOptions options = new XCUITestOptions();
        try {
            options.setUdid(getUdid());
            options.setDeviceName(getDeviceName());
            options.setBundleId(properties.getProperty("iOSBundleId"));
            options.setWdaLocalPort(getWdaLocalPort());
            options.setNewCommandTimeout(Duration.ofSeconds(22000));
        } catch (Exception e) {
            e.printStackTrace();
            logger().fatal("iOS XCUITest Options issue: " + e.getLocalizedMessage());
        }
        return options;
    }
}

