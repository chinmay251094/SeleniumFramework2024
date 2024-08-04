package com.chinmay.pageobjects;

import com.chinmay.factories.ExplicitWaitFactory;
import org.openqa.selenium.By;

public class HomePage extends PageActions {
    private HomePage() {
    }

    public static HomePage dashboard() {
        return new HomePage();
    }

    public boolean isUserLoggedIn() {
        return ExplicitWaitFactory.waitForElementsToBeLoaded(By.cssSelector("[class='oxd-userdropdown'] [alt='profile picture']")) != null;
    }
}
