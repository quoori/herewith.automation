package com.qa.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class HomeScreenPO extends PageBase {
    public HomeScreenPO(ThreadLocal<AppiumDriver> driver) {
        super(driver.get());
    }

    @AndroidFindBy(xpath = "//*[@resource-id='Profile_Label']")
    private WebElement menuIcon;

    public void clickMenu(String menu) {
        Assert.assertTrue(clickElement(menu, menuIcon, false), "Unable to click the " + menu + " in the Home screen");
    }

}