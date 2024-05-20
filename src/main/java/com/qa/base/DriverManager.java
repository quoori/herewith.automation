package com.qa.base;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import java.io.IOException;
import java.net.URL;

public class DriverManager extends BaseSetup {

    public void initDriver() {
        AppiumDriver driver = null;
        if (driver == null) {
            try {
                logger().info("initialising driver");
                String driverUrl = new ServerManager().getServer().getUrl().toString();
                switch (getPlatformName()) {
                    case "Android":
                        driver = new AndroidDriver(new URL(driverUrl), new CapabilitiesManager().androidOptions());
                        break;
                    case "iOS":
                        driver = new IOSDriver(new URL(driverUrl), new CapabilitiesManager().iosOptions());
                        break;
                }
                if (driver == null) {
                    throw new Exception("Issue in initializing the Appium driver: " + getPlatformName());
                }
                this.setDriver(driver);
                logger().info("Driver initialization is done: " + getPlatformName());
            } catch (IOException e) {
                e.printStackTrace();
                logger().fatal("Driver initialization is failed. ABORT!!!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
