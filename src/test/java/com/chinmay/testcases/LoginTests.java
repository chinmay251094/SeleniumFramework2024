package com.chinmay.testcases;

import com.chinmay.annotations.AutomationTeam;
import com.chinmay.enums.TestCategory;
import com.chinmay.enums.Tester;
import com.chinmay.pojo.LoginData;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.qameta.allure.testng.Tag;
import org.testng.annotations.Test;

import java.util.Map;

import static com.chinmay.pageobjects.HomePage.dashboard;
import static com.chinmay.pageobjects.LoginPage.authenticate;
import static com.chinmay.utils.VerificationUtils.validate;
import static com.chinmay.utils.VerificationUtils.validateUrl;

public class LoginTests extends BaseTest {
    private LoginData loginData;

    private LoginTests() {
    }

    // Annotations indicating test metadata and attributes
    @AutomationTeam(author = Tester.CHINMAY, category = {TestCategory.SMOKE, TestCategory.SANITY})
    @Description("To test user is able to login with valid credentials")
    @Story("Successful login to application")
    @Severity(SeverityLevel.NORMAL)
    @Tag("Smoke")
    @Test
    // Method to test valid login functionality with provided login credentials
    public void testValidLogin(Map<String, String> map) {
        // Creating a new instance of LoginData
        loginData = new LoginData();
        // Setting email and password from the JSON file
        loginData.setEmail(map.get("username")).setPassword(map.get("password"));

        // Authenticating and logging in with the provided credentials
        authenticate().loginWith(loginData);

        // Checking if the upload invoice button exists on the dashboard
        boolean buttonExists = dashboard().isUserLoggedIn();

        // Validating the URL against the expected URL
        validateUrl(map.get("expectedUrl"), "URL is as expected");
        // Validating if the upload button exists on the dashboard after sign in
        validate(buttonExists, true, "Upload button exists on dashboard when user signs in.");
    }
}
