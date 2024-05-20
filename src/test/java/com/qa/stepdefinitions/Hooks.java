package com.qa.stepdefinitions;

import com.qa.base.BaseSetup;
import com.qa.base.DriverManager;
import com.qa.listeners.RetryAnalyzer;
import com.qa.utils.VideoManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * hooks to start the appium server
 * to initialise the appium driver
 * <p>
 * to stop appium server
 * to quit the driver
 * <p>
 * Executed before and after every cucumber scenario
 */
public class Hooks {
    private final BaseSetup baseSetup = new BaseSetup();

    //executes before every scenario
    @Before
    public void initialhook(Scenario scenario) throws IOException, InterruptedException {
        baseSetup.logger().info("Hooks: Before");
        if (baseSetup.getDriver() == null) {
            new DriverManager().initDriver();
        }
        if (!baseSetup.isAppActivated) {
            baseSetup.isAppActivated = baseSetup.activateApp();
            baseSetup.isAppTerminated = false;
        }
        new VideoManager().startRecording();
    }

    //executes after every scenario
    @After
    public void quit(Scenario scenario) throws Exception {
        baseSetup.logger().info("Hooks: After");
//        if (scenario.isFailed() && !RetryAnalyzer.isRetrying()) {
            byte[] screenshot = baseSetup.getDriver().getScreenshotAs(OutputType.BYTES);
            Allure.attachment(scenario.getName(), new ByteArrayInputStream(screenshot));
            String dir = baseSetup.getPlatformName() + baseSetup.UNDERSCORE + baseSetup.getDeviceName() + baseSetup.FILE_SEPARATOR + "Videos";
            String fileName = baseSetup.FILE_SEPARATOR + scenario.getName() + baseSetup.VIDEO_FILE_EXTENSION;
            byte[] videoBytes = new VideoManager().stopRecording(dir, fileName);
            Allure.attachment(scenario.getName() + "Video", new ByteArrayInputStream(videoBytes));
//        }
        if (baseSetup.isAppActivated && !baseSetup.isAppTerminated) {
            baseSetup.isAppTerminated = baseSetup.terminateApp();
            baseSetup.isAppActivated = false;
        }
        if (baseSetup.getDriver() != null) {
            baseSetup.getDriver().quit();
            baseSetup.setDriver(null);
        }
    }
}
