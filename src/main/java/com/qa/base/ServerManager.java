package com.qa.base;

import com.qa.utils.SystemProperties;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServerHasNotBeenStartedLocallyException;
import io.appium.java_client.service.local.AppiumServiceBuilder;

import java.io.File;

public class ServerManager extends BaseSetup {
    private static String nodePath = null;
    private static String appiumServerPath = null;
    private static final ThreadLocal<AppiumDriverLocalService> server = new ThreadLocal<>();

    public static AppiumDriverLocalService getAppiumServiceDefault() {
        return AppiumDriverLocalService.buildDefaultService();
    }

    public AppiumDriverLocalService getCustomAppiumService() {
        logger().info("Global Parameters inside Server Manager");
        if (SystemProperties.getOperatingSystemName().toLowerCase().contains("windows")) {
            nodePath = "C:\\Program Files\\nodejs\\node.exe";
            appiumServerPath = "node_modules/appium/build/lib/main.js";
        } else if (SystemProperties.getOperatingSystemName().toLowerCase().contains("mac")) {
            nodePath = "/usr/local/bin/node";
            appiumServerPath = "/Applications/Appium.app/Contents/Resources/app/node_modules/appium/build/lib/main.js";
        }
        return AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
                .usingDriverExecutable(new File(nodePath))
                .withAppiumJS(new File(appiumServerPath))
                .withIPAddress("127.0.0.1").usingPort(4723)
                .withLogFile(new File(getPlatformName() + UNDERSCORE + getDeviceName() + FILE_SEPARATOR + "Server.log")));
    }

    public AppiumDriverLocalService getServer() {
        return server.get();
    }

    public void startServer() {
        logger().info("Starting appium server");
        AppiumDriverLocalService localServer = getCustomAppiumService();
        //If Appium server is already running, we have to stop it

        boolean isServerStopped = (getServer() != null) ? stopServer() : false;

        if (!isServerStopped) {
            localServer.start();
            if (localServer == null || !localServer.isRunning()) {
                logger().fatal("Unable to start server. aborting.");
                throw new AppiumServerHasNotBeenStartedLocallyException("Appium server not started. Aborting.");
            }
        }
        localServer.clearOutPutStreams();
        server.set(localServer);
        logger().info("Appium server started");
    }

    public Boolean stopServer() {
        boolean isStopped = false;
        try {
            server.get().stop();
            isStopped = true;
            logger().info("Appium Server Stopped");
        } catch (Exception e) {
            logger().error("Unable to stop the Appium Server: " + e.getMessage());
        }
        return isStopped;
    }
}
