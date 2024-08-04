package com.chinmay.pageobjects;

import com.chinmay.annotations.FunctionalStep;
import com.chinmay.annotations.PageElement;
import com.chinmay.annotations.Step;
import com.chinmay.enums.WaitStrategy;
import com.chinmay.pojo.LoginData;
import org.openqa.selenium.By;

import static com.chinmay.pageobjects.HomePage.dashboard;

public class LoginPage extends PageActions {
    @PageElement(purpose = "Textbox for email")
    private final By textBoxUserName = By.cssSelector("[name='username']");
    @PageElement(purpose = "Textbox for password")
    private final By textBoxPassword = By.cssSelector("[name='password']");
    @PageElement(purpose = "Button to login to system")
    private final By buttonSignIn = By.cssSelector("[type=submit]");

    private LoginPage() {
    }

    public static LoginPage authenticate() {
        return new LoginPage();
    }

    @Step(purpose = "To enter username into textbox")
    public LoginPage enterUserName(String username) {
        sendKeys(textBoxUserName, username, WaitStrategy.VISIBLE, "Email");
        return this;
    }

    @Step(purpose = "To enter password into textbox")
    public LoginPage enterPassword(String password) {
        sendKeys(textBoxPassword, password, WaitStrategy.NONE, "Password");
        return this;
    }

    @Step(purpose = "To click Sign in button")
    public void clickSignInButton() {
        click(buttonSignIn, WaitStrategy.NONE, "Sign In Button");
    }

    @FunctionalStep(purpose = "To Sign in to web portal")
    public HomePage loginWith(LoginData loginData) {
        enterUserName(loginData.getEmail())
                .enterPassword(loginData.getPassword())
                .clickSignInButton();
        return dashboard();
    }
}
