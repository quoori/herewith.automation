package com.qa.base;

import com.qa.utils.MobileUtils;
import com.qa.utils.SystemProperties;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.PageFactory;

import java.awt.geom.Point2D;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;

public class PageObjectBase extends BaseSetup {
    protected static boolean isPasswordField = false;

    public PageObjectBase(AppiumDriver driver) {
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    @Step("Clicking on '{elementName}'...")
    protected boolean clickElement(String elementName, WebElement elementToClick, boolean waitForPageLoad) {
        boolean isSuccess = false;
        MobileUtils.hideKeyboard(getDriver(),getPlatformName());
        isSuccess = MobileUtils.scrollToElement(getDriver(), elementToClick);
        int retryCount = 3;
        if (isSuccess) {
            isSuccess = false;
            for (int itr = 1; !isSuccess && (itr <= retryCount); itr++) {
                try {
                    MobileUtils.waitForElementToBeClickable(getDriver(), elementToClick);
                    elementToClick.click();
                    isSuccess = true;
                } catch (Exception e) {
                    Allure.step("Iteration " + itr + ": Exception: " + e.getMessage());
                    if (itr >= retryCount) {
                        throw e;
                    }
                    elementToClick = MobileUtils.refreshWebElement(getDriver(), elementToClick);
                }
            }
            if (waitForPageLoad) {
                MobileUtils.waitUntilProgressBarFinished(getDriver());
            }
        }
        Allure.step("In PageObjectBase: clickElement- " + elementName + " Returning: " + isSuccess);
        return isSuccess;
    }

    @Step("Setting input element '{elementName}' to '{webElementValue}'...")
    protected boolean setInputText(String elementName, WebElement webElement, String webElementValue) {
        boolean isSuccess = false;
        MobileUtils.hideKeyboard(getDriver(),getPlatformName());
        boolean isFound = MobileUtils.scrollToElement(getDriver(), webElement);
        if (isFound) {
            MobileUtils.waitForElementToBeClickable(getDriver(), webElement);
            int retryCount = 3;
            for (int itr = 1; !isSuccess && (itr <= retryCount); itr++) {
                try {
                    webElement.clear();
                    webElement.sendKeys(webElementValue);
                    String getText = getInputText(webElement);
                    if (getText.equals(webElementValue) || isPasswordField) {
                        isPasswordField = false;
                        isSuccess = true;
                    }
                } catch (Exception e) {
                    Allure.step("Iteration " + itr + ": Exception: " + e.getMessage());
                    if (itr >= retryCount) {
                        throw e;
                    }
                    webElement = MobileUtils.refreshWebElement(getDriver(), webElement);
                }
            }
        }
        Allure.step("In PageObjectBase: Entering the text " + webElementValue + " in the element - " + elementName + ", Returning: " + isSuccess);
        return isSuccess;
    }

    protected String getInputText(WebElement webElement) {
        String webElementValue = null;
        MobileUtils.hideKeyboard(getDriver(),getPlatformName());
        boolean isFound = MobileUtils.scrollToElement(getDriver(), webElement);
        if (isFound) {
            webElementValue = webElement.getText();
            Allure.step("Element returning text: " + webElementValue);
        }
        return webElementValue;
    }

    @Step("Validate Toast Message: '{toastMessage}'...")
    protected boolean getToastMessage(WebElement webElement, String toastMessage) {
        boolean isSucess = false;
        String webElementValue = null;
        MobileUtils.hideKeyboard(getDriver(),getPlatformName());
        boolean isFound = MobileUtils.waitForElementToBeVisible(getDriver(), webElement);
        if (isFound) {
            webElementValue = webElement.getText();
            if (webElementValue.equals(toastMessage)) {
                isFound = MobileUtils.waitForElementToBeInvisible(getDriver(), webElement);
                isSucess = true;
            }
        }
        Allure.step("Toast Message displayed: " + webElementValue);
        return isSucess && isFound;
    }

    @Step("Verifying the element visibility - '{elementName}'")
    protected boolean isElementVisible(String elementName, WebElement webElement) {
        MobileUtils.hideKeyboard(getDriver(),getPlatformName());
        boolean isSucess = MobileUtils.isElementVisible(getDriver(), webElement);
        Allure.step(" Status of the element: " + elementName + " = Visiblity: " + isSucess);
        return isSucess;
    }

    public void tapElement(int x, int y) {
        Dimension size = getDriver().manage().window().getSize();
        int height = (int) (size.getHeight() * 0.5);
        int width = (int) (size.getWidth() * 0.5);
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence sequence = new Sequence(finger, 1)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        getDriver().perform(Arrays.asList(sequence));
    }


}
