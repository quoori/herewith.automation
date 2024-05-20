package com.qa.stepdefinitions;

import com.qa.base.DriverManager;
import com.qa.pages.LoginScreenPO;
import io.cucumber.java.en.Given;

public class LoginStepDef {

    LoginScreenPO loginScreenPO = new LoginScreenPO(DriverManager.driver);

    @Given("Click the terms and conditions in the HomeScreen")
    public void click_icon_homescreen() throws InterruptedException {
        Thread.sleep(30000);
    }

}
