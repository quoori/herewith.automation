package com.qa.base;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.InteractsWithApps;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseSetup {

    private static final ThreadLocal<String> platformName = new ThreadLocal<String>();
    private static final ThreadLocal<String> udid = new ThreadLocal<String>();
    private static final ThreadLocal<String> deviceName = new ThreadLocal<String>();
    private static final ThreadLocal<String> systemPort = new ThreadLocal<String>();
    private static final ThreadLocal<String> chromeDriverPort = new ThreadLocal<String>();
    private static final ThreadLocal<Integer> wdaLocalPort = new ThreadLocal<Integer>();
    private static final ThreadLocal<String> webkitDebugProxyPort = new ThreadLocal<String>();

    public static boolean isAppActivated = false;

    public static boolean isAppTerminated = false;
    public static final long WAIT_TEN_SEC = 10L;
    public static final String FILE_EXTENSION = ".png";

    public static final String IMAGE_PAGE_OBJECT = "\\src\\test\\resources\\PageObjectsImage\\";
    public static final String CONFIG_FILENAME = "config.properties";
    public static final String UNDERSCORE = "_";
    public static final String FILE_SEPARATOR = File.separator;
    public static final String VIDEO_FILE_EXTENSION = ".mp4";
    public static final String ROUTING_KEY = "ROUTINGKEY";

    public static ThreadLocal<AppiumDriver> driver = new ThreadLocal<>();

    public String getPlatformName() {
        return platformName.get();
    }

    public void setPlatformName(String platformname) {
        platformName.set(platformname);
    }

    public String getUdid() {
        return udid.get();
    }

    public void setUdid(String pUdid) {
        udid.set(pUdid);
    }

    public String getDeviceName() {
        return deviceName.get();
    }

    public void setDeviceName(String devicename) {
        deviceName.set(devicename);
    }

    public String getSystemPort() {
        return systemPort.get();
    }

    public void setSystemPort(String systemport) {
        systemPort.set(systemport);
    }

    public String getChromeDriverPort() {
        return chromeDriverPort.get();
    }

    public void setChromeDriverPort(String chromedriverport) {
        chromeDriverPort.set(chromedriverport);
    }

    public Integer getWdaLocalPort() {
        return wdaLocalPort.get();
    }

    public void setWdaLocalPort(Integer wdaport) {
        wdaLocalPort.set(wdaport);
    }

    public ThreadLocal<String> getWebkitDebugProxyPort() {
        return webkitDebugProxyPort;
    }

    public void setWebkitDebugProxyPort(String webkitdebugproxyport) {
        webkitDebugProxyPort.set(webkitdebugproxyport);
    }

    public AppiumDriver getDriver() {
        return driver.get();
    }

    public void setDriver(AppiumDriver driver1) {
        driver.set(driver1);
    }

    public static Logger logger() {
        return LogManager.getLogger(Thread.currentThread().getStackTrace()[2].getClassName());
    }

    public void initializeGlobalParams() {
        setPlatformName(System.getProperty("platformName", "Android"));
        setUdid(System.getProperty("udid", getConnectedAndroidDevices().get(0)));
        setDeviceName(System.getProperty("deviceName", getDeviceModel(getConnectedAndroidDevices().get(0))));
        switch (getPlatformName()) {
            case "Android":
                setSystemPort(System.getProperty("systemPort", "9999"));
                setChromeDriverPort(System.getProperty("chromeDriverPort", "9998"));
                break;
            case "iOS":
                setWebkitDebugProxyPort(System.getProperty("webkitDebugProxyPort", "10001"));
                setWdaLocalPort(Integer.valueOf(System.getProperty("wdaLocalPort", String.valueOf(11001))));
                break;
        }
    }

    public boolean activateApp() throws IOException, InterruptedException {
        boolean isSucess = false;
        try {
            ((InteractsWithApps) getDriver()).activateApp(new PropertyManager().getProperties(CONFIG_FILENAME).getProperty("androidAppPackage"));
            isSucess = true;
        } catch (Exception e) {
            throw e;
        }
        return isSucess;
    }

    public boolean terminateApp() throws IOException, InterruptedException {
        boolean isSucess = false;
        for(int i=0;i<3 && !isSucess;i++) {
            try {
                ((InteractsWithApps) getDriver()).terminateApp(new PropertyManager().getProperties(CONFIG_FILENAME).getProperty("androidAppPackage"));
                isSucess = true;
            }catch(Exception e){
                Thread.sleep(5000);
            }
        }
        return isSucess;
    }

    protected ArrayList<String> getConnectedAndroidDevices() {
        ArrayList<String> deviceList = new ArrayList<String>();
        String line = null;
        Matcher matcher = null;
        try {
            Process process = Runtime.getRuntime().exec("adb devices");
            BufferedReader deviceReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            process.waitFor();
            Pattern pattern = Pattern.compile("^([a-zA-Z0-9\\-]+)(\\s+)(device)");
            while ((line = deviceReader.readLine()) != null) {
                if (line.matches(pattern.pattern())) {
                    matcher = pattern.matcher(line);
                    if (matcher.find())
                        deviceList.add(matcher.group(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceList;
    }

    protected String getDeviceModel(String udid) {
        String deviceModel = null;
        try {
            Process process = Runtime.getRuntime().exec("adb -s " + udid + " shell getprop ro.product.model");
            BufferedReader modelReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            process.waitFor();
            while (modelReader.ready()) {
                deviceModel = modelReader.readLine();
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceModel;
    }
}
