package com.chinmay.factories;

import com.chinmay.constants.FrameworkConstants;
import com.chinmay.enums.WaitStrategy;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static com.chinmay.constants.FrameworkConstants.getExplicitwait;
import static com.chinmay.driver.DriverManager.getWebDriver;

public final class ExplicitWaitFactory {
    private ExplicitWaitFactory() {
    }

    public static WebElement performExplicitWait(WaitStrategy wait, By by) {
        WebElement element = null;
        if (wait == WaitStrategy.CLICKABLE) {
            element = new WebDriverWait(getWebDriver(), getExplicitwait())
                    .until(ExpectedConditions.elementToBeClickable(by));
        } else if (wait == WaitStrategy.VISIBLE) {
            element = new WebDriverWait(getWebDriver(), getExplicitwait())
                    .until(ExpectedConditions.visibilityOfElementLocated(by));
        } else if (wait == WaitStrategy.PRESENCE) {
            element = new WebDriverWait(getWebDriver(), getExplicitwait())
                    .until(ExpectedConditions.presenceOfElementLocated(by));
        } else if (wait == WaitStrategy.NONE) {
            element = getWebDriver().findElement(by);
        }
        return element;
    }

    public static WebElement performExplicitWait(WaitStrategy wait, WebElement ele) {
        WebElement element = null;
        if (wait == WaitStrategy.CLICKABLE) {
            element = new WebDriverWait(getWebDriver(), getExplicitwait())
                    .until(ExpectedConditions.elementToBeClickable(ele));
        } else if (wait == WaitStrategy.VISIBLE) {
            element = new WebDriverWait(getWebDriver(), getExplicitwait())
                    .until(ExpectedConditions.visibilityOf(ele));
        } else if (wait == WaitStrategy.NONE) {
            return ele;
        }
        return element;
    }

    public static List<WebElement> performExplicitWaitForList(List<WebElement> list) {
        List<WebElement> element = null;
        element = new WebDriverWait(getWebDriver(), getExplicitwait())
                .until(ExpectedConditions.visibilityOfAllElements(list));
        return element;
    }

    public static WebElement waitForElementToBePresent(By locator) {
        WebDriverWait wait = new WebDriverWait(getWebDriver(), getExplicitwait());
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static List<WebElement> waitForElementsToBePresent(By locator) {
        WebDriverWait wait = new WebDriverWait(getWebDriver(), getExplicitwait());
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    public static List<WebElement> waitForElementsToBeVisible(By locator) {
        WebDriverWait wait = new WebDriverWait(getWebDriver(), getExplicitwait());
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public static List<WebElement> waitForElementsToBeVisible(By locator, Duration minutes) {
        WebDriverWait wait = new WebDriverWait(getWebDriver(), minutes);
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public static List<WebElement> waitForElementsToBeLoaded(By by) {
        WebDriverWait wait = new WebDriverWait(getWebDriver(), getExplicitwait());
        return wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(by, 0));
    }

    public static void waitForElementInvisibility(By locator) {
        WebDriverWait wait = new WebDriverWait(getWebDriver(), getExplicitwait());
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static List<WebElement> waitForElementsToNotBeStale(By locator) {
        WebDriverWait wait = new WebDriverWait(getWebDriver(), getExplicitwait());
        return wait.until(driver -> {
            try {
                List<WebElement> elements = driver.findElements(locator);
                if (!elements.isEmpty() && elements.stream().allMatch(WebElement::isDisplayed)) {
                    return elements;
                }
            } catch (StaleElementReferenceException e) {
                // Handle StaleElementReferenceException by re-fetching the elements
                System.out.println("StaleElementReferenceException occurred. Retrying...");
            }
            return null;
        });
    }

    // Method to safely find element with retry mechanism
    public static List<WebElement> findStaleElementSafe(By locator) {
        WebDriverWait wait = new WebDriverWait(getWebDriver(), getExplicitwait());
        List<WebElement> elements = null;
        try {
            elements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        } catch (org.openqa.selenium.StaleElementReferenceException e) {
            // Element is stale, retrying to find it
            elements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        }
        return elements;
    }

    public static WebElement waitForElementToNotBeStale(By locator) {
        WebDriverWait wait = new WebDriverWait(getWebDriver(), FrameworkConstants.getExplicitwait());
        return wait.until(driver -> {
            try {
                WebElement element = driver.findElement(locator);
                if (element != null && element.isDisplayed()) {
                    return element;
                }
            } catch (StaleElementReferenceException e) {
                // Handle StaleElementReferenceException by re-fetching the element
                System.out.println("StaleElementReferenceException occurred. Retrying...");
            }
            return null;
        });
    }

    public static WebElement waitForElementToBeClickable(WebElement element) {
        WebDriverWait wait = new WebDriverWait(getWebDriver(), getExplicitwait());
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public static WebElement waitForElementToBeVisible(WebElement element) {
        WebDriverWait wait = new WebDriverWait(getWebDriver(), getExplicitwait());
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    public static WebElement waitToAvoidStaleElement(By locator) {
        FluentWait<WebDriver> wait = new FluentWait<>(getWebDriver())
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(2))
                .ignoring(StaleElementReferenceException.class);

        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static WebElement waitToAvoidNoSuchElement(By locator) {
        FluentWait<WebDriver> wait = new FluentWait<>(getWebDriver())
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(2))
                .ignoring(NoSuchElementException.class);

        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static List<WebElement> retryFindElements(By locator) {
        List<WebElement> elements = null;
        int attempts = 0;
        while (attempts < 2) { // Retry up to 2 times
            try {
                elements = getWebDriver().findElements(locator);
                break;
            } catch (StaleElementReferenceException e) {
                // Handle StaleElementReferenceException, wait and retry
                e.printStackTrace();
            }
            attempts++;
        }
        return elements;
    }
}
