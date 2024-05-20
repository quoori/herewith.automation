package com.qa.utils;

import com.qa.base.BaseSetup;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Allure;
import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import static java.time.Duration.ofSeconds;

public class MobileUtils {

    public static final int SECOND_TIMEOUT = 1;
    public static final int HALF_MINUTE_TIMEOUT = 30;
    public static final int MINUTE_TIMEOUT = 60;
    public static final int DEFAULT_TIMEOUT = MINUTE_TIMEOUT * 3;

    public static final double DEFAULT_MIN_SIMILARITY = 0.8;
    public static final String LOADING_SPINNER = "//*[@resource-id='Loading_Spinner']";

    public static boolean scrollToElement(AppiumDriver driver, WebElement elementToLocate, WebElement scrollElement) {
        boolean isSuccess = true;
        String scrollCondition = null;
        try {
            Pair<String, String> locatorPair = getLocatorFromWebElement(driver, elementToLocate);
            String locatorExpression = locatorPair.getRight();
            if (locatorExpression.contains("@content-desc")) {
                scrollCondition = (locatorExpression.split("@content-desc=")[1]).replaceAll("[^\\w\\s]", "");
                scrollCondition = "description(\"" + scrollCondition + "\")";

            }
            if (locatorExpression.contains("@resource-id")) {
                scrollCondition = locatorExpression.split("@resource-id=")[1].replaceAll("[^\\w\\s]", "");
                scrollCondition = "resourceId(\"" + scrollCondition + "\")";
            }
            WebElement scroll = scrollElement
                    .findElement(AppiumBy
                            .androidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector()." + scrollCondition + ".instance(0))"));

        } catch (Exception e) {
            isSuccess = false;
        }
        return isSuccess;
    }

    public static boolean scrollToElement(AppiumDriver driver, WebElement elementToLocate) {
        boolean isSuccess = false;
        String matcher = "resource-id=";
        for (int idx = 0; idx < 5 && !isSuccess; idx++) {
            try {
                if (!isElementVisible(driver, elementToLocate)) {
                    String pageSource = driver.getPageSource();
                    for (int i = -1; (i = pageSource.indexOf(matcher, i + 1)) != -1; i++) {
                        String matchExp = "//*[@" + matcher + "'" + pageSource.substring(i).split("\"*\"")[1] + "']";
                        WebElement element = driver.findElement(By.xpath(matchExp));
                        scrollToElement(driver, elementToLocate, element);
                        if (isElementVisible(driver, elementToLocate)) {
                            isSuccess = true;
                            break;
                        }
                    }
                } else {
                    isSuccess = true;
                    break;
                }
            } catch (Exception e) {
                isSuccess = false;
            }
        }
        return isSuccess;
    }

    public static boolean isElementVisible(AppiumDriver driver, By webelementLocator) {
        boolean isSuccess = true;
        try {
            getWebelementFromBy(driver, webelementLocator);
        } catch (Exception e) {
            isSuccess = false;
        }
        return isSuccess;
    }

    public static boolean isElementVisible(AppiumDriver driver, WebElement webElement) {
        return isElementVisible(driver, getByLocatorFromWebElement(driver, webElement));
    }

    public static boolean waitForElementToBeVisible(AppiumDriver driver, WebElement webElement, int timeOutInSeconds) {
        boolean isVisible = true;
        try {
            new WebDriverWait(driver, ofSeconds(timeOutInSeconds))
                    .until(ExpectedConditions.visibilityOf(webElement));
        } catch (Exception e) {
            isVisible = false;
        }
        return isVisible;
    }

    public static boolean waitForElementToBeVisible(WebDriver driver, By webElementLocator, int timeOutInSeconds) {
        boolean isVisible = true;
        try {
            new WebDriverWait(driver, ofSeconds(timeOutInSeconds))
                    .until(ExpectedConditions.visibilityOfElementLocated(webElementLocator));
        } catch (Exception e) {
            isVisible = false;
        }
        return isVisible;
    }

    public static boolean waitForElementToBeVisible(AppiumDriver driver, WebElement webElement) {
        return waitForElementToBeVisible(driver, webElement, DEFAULT_TIMEOUT);
    }

    public static boolean waitForElementToBeVisible(AppiumDriver driver, By webElementLocator) {
        return waitForElementToBeVisible(driver, webElementLocator, DEFAULT_TIMEOUT);
    }

    public static boolean waitForElementToBeInvisible(AppiumDriver driver, WebElement webElement, int timeOutInSeconds) {
        boolean isSuccess = false;
        try {
            new WebDriverWait(driver, ofSeconds(timeOutInSeconds))
                    .until(ExpectedConditions.invisibilityOf(webElement));
            isSuccess = true;
        } catch (Exception e) {
            isSuccess = false;
        }
        return isSuccess;
    }

    public static boolean waitForElementToBeInvisible(WebDriver driver, By webElementLocator, int timeOutInSeconds) {
        boolean isSuccess = false;
        try {
            new WebDriverWait(driver, ofSeconds(timeOutInSeconds))
                    .until(ExpectedConditions.invisibilityOfElementLocated(webElementLocator));
            isSuccess = true;
        } catch (Exception e) {
            isSuccess = false;
        }
        return isSuccess;
    }

    public static boolean waitForElementToBeInvisible(AppiumDriver driver, WebElement webElement) {
        return waitForElementToBeInvisible(driver, webElement, DEFAULT_TIMEOUT);
    }

    public static boolean waitForElementToBeInvisible(AppiumDriver driver, By webElementLocator) {
        return waitForElementToBeInvisible(driver, webElementLocator, DEFAULT_TIMEOUT);
    }

    public static boolean waitForElementToBeClickable(WebDriver driver, WebElement locator, int timeOutInSeconds) {
        boolean isClickable = true;
        try {
            new WebDriverWait(driver, ofSeconds(timeOutInSeconds))
                    .until(ExpectedConditions.elementToBeClickable(locator));
        } catch (Exception e) {
            isClickable = false;
        }
        return isClickable;
    }

    public static boolean waitForElementToBeClickable(AppiumDriver driver, WebElement webElement) {
        return waitForElementToBeClickable(driver, webElement, DEFAULT_TIMEOUT);
    }

    public static void navigateBack(AppiumDriver driver) {
        Allure.step("Navigating to previous page.");
        driver.navigate().back();
    }

    public static void forwardPage(AppiumDriver driver) {
        Allure.step("Navigating to forward page.");
        driver.navigate().forward();
    }

    public static By getByLocatorFromString(String locatorType, String locatorExpression) {
        By byLocator = null;
        switch (locatorType) {
            case "xpath":
                byLocator = By.xpath(locatorExpression);
                break;
            case "id":
                byLocator = By.id(locatorExpression);
                break;
            default:
                throw new UnsupportedOperationException("Ask QA to implement By locator method.");
        }
        return byLocator;
    }

    public static By getByLocatorFromWebElement(AppiumDriver driver, WebElement webElement) {
        By byLocator = null;
        Pair<String, String> locatorPair = getLocatorFromWebElement(driver, webElement);
        String locatorType = locatorPair.getLeft();
        String locatorExpression = locatorPair.getRight();
        byLocator = getByLocatorFromString(locatorType, locatorExpression);
        return byLocator;
    }

    public static Pair<String, String> getLocatorFromWebElement(AppiumDriver driver, WebElement webElement) {
        String locatorExpression = webElement.toString();
        String locatorType = null;
        // Drop the outer square braces
        locatorExpression = (locatorExpression.startsWith("[") && locatorExpression.endsWith("]")) ? locatorExpression.substring(1, locatorExpression.length() - 1) : locatorExpression;
        if (locatorExpression.contains("->")) {
            locatorExpression = locatorExpression.split("->")[1].trim();    // Get string following the "->"
        } else if (locatorExpression.contains("Located by By.chained")) {
            locatorExpression = locatorExpression.replace("Located by By.chained", "");
            locatorExpression = (locatorExpression.startsWith("({") && locatorExpression.endsWith("})")) ? locatorExpression.substring(2, locatorExpression.length() - 2) : locatorExpression;
            if (locatorExpression.startsWith("By.")) {
                locatorExpression = locatorExpression.replace("By.", "");
            }
        }
        locatorType = locatorExpression.split(":")[0].trim();
        locatorExpression = locatorExpression.replaceFirst(locatorType.concat(":"), "").trim();
        return Pair.of(locatorType, locatorExpression);
    }

    public static WebElement getWebelementFromBy(AppiumDriver driver, By bylocator) {
        WebElement webElement = null;
        try {
            webElement = driver.findElement(bylocator);
        } catch (Exception e) {
            throw e;
        }
        return webElement;
    }

    public static WebElement refreshWebElement(AppiumDriver driver, WebElement webElement) {
        Allure.step("In PageObjectBase: refreshWebElement");
        BaseSetup.logger().info("Refreshing the WebElement");
        By locator = MobileUtils.getByLocatorFromWebElement(driver, webElement);
        MobileUtils.waitForElementToBeVisible(driver, locator, SECOND_TIMEOUT * 5);
        webElement = driver.findElement(locator);
        return webElement;
    }

    public static boolean waitUntilProgressBarFinished(AppiumDriver driver) {
        return waitUntilProgressBarFinished(driver, 3, 10);
    }

    public static boolean waitUntilProgressBarFinished(AppiumDriver driver, int pollInterval, int numberOfPolls) {
        boolean isFinished = false;
        for (int i = 1; (!isFinished && (i <= numberOfPolls)); i++) {
            boolean isExistsProgressBar = isExistsProgressBar(driver);
            if (isExistsProgressBar) {
                try {
                    BaseSetup.logger().info(i + " Waiting " + pollInterval + " seconds...");
                    Thread.sleep(SECOND_TIMEOUT * pollInterval * 1000);
                } catch (InterruptedException ie) {
                }
            } else {
                isFinished = true;
                break;
            }
        }
        return isFinished;
    }

    public static boolean hideKeyboard(AppiumDriver driver, String platform) {
        boolean isKeyboardHidden = false;
        if (platform.equalsIgnoreCase("android")) {
            AndroidDriver driver1 = (AndroidDriver) driver;
            if (driver1.isKeyboardShown()) {
                driver1.hideKeyboard();
                Allure.step("Dismissing the Keyboard to focus the webelement");
                isKeyboardHidden = true;
            }
        }
        return isKeyboardHidden;
    }

public static boolean isExistsProgressBar(AppiumDriver driver) {
    boolean isExistsLoadingSpinner = false;
    if (isElementVisible(driver, By.xpath(LOADING_SPINNER))) {
        isExistsLoadingSpinner = true;
        BaseSetup.logger().debug("We found the GENERIC LOADING SPINNER ICON...");
    }
    return isExistsLoadingSpinner;
}

//Helper Methods:
public static String getAttributeFromLocator(AppiumDriver driver, WebElement webElement) {
    Pair<String, String> locatorPair = getLocatorFromWebElement(driver, webElement);
    String locatorExpression = locatorPair.getRight();
    locatorExpression = (locatorExpression.replaceAll("[^\\w^\\=^\\'\\s]", "")).replaceAll("'", "'\\'");
    return locatorExpression;
}
}



