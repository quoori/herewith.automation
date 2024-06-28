package com.qa.stepdefinitions;

import com.qa.base.DriverManager;
import com.qa.pages.LoginScreenPO;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

public class LoginStepDef {

    LoginScreenPO loginScreenPO = new LoginScreenPO(DriverManager.driver);

    @Given("Click the terms and conditions in the HomeScreen")
    public void click_the_terms_and_conditions_in_the_home_screen() {
    loginScreenPO.clickAgreeButton();
    }

    @When("Click get started button")
    public void click_get_started_button() {
    loginScreenPO.clickGetStarted();
    }

}
