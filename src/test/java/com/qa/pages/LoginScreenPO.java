package com.qa.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class LoginScreenPO extends PageBase {
    public LoginScreenPO(ThreadLocal<AppiumDriver> driver) {
        super(driver.get());
    }

    @AndroidFindBy(xpath = "//*[@resource-id= 'radio_agree_button']")
    private WebElement agreeButton;

    @AndroidFindBy(xpath = "//*[@resource-id= 'get_started_button']")
    private WebElement getStarted;

    public void clickAgreeButton() {
        Assert.assertTrue(clickElement("agreeButton", agreeButton, false), "Unable to click the reviewApplicants button in the Home screen");
    }

    public void clickGetStarted(){
        Assert.assertTrue(clickElement("getStarted",getStarted,false),"Unable to click get started button in home Screen");
    }



}