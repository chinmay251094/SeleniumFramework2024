package com.chinmay.pageobjects;

import com.chinmay.enums.Locator;
import com.chinmay.enums.WaitStrategy;
import com.chinmay.exceptions.FrameworkTestException;
import com.chinmay.factories.ExplicitWaitFactory;
import com.chinmay.reports.AllureManager;
import com.google.common.util.concurrent.Uninterruptibles;
import io.qameta.allure.Step;
import lombok.SneakyThrows;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import static com.chinmay.constants.FrameworkConstants.getDownloadpath;
import static com.chinmay.driver.DriverManager.getWebDriver;
import static com.chinmay.factories.ExplicitWaitFactory.performExplicitWait;
import static com.chinmay.reports.FrameworkReportLogger.info;
import static com.chinmay.utils.EnvironmentUtils.getMode;
import static com.chinmay.utils.TestUtils.log;
import static com.chinmay.utils.UtilityService.dynamicElementSynchronization;

public class PageActions {
    protected static void highlightElement(WebElement element) {
        // Change the border and background color of the element to highlight it
        JavascriptExecutor jsExecutor = (JavascriptExecutor) getWebDriver();
        jsExecutor.executeScript("arguments[0].style.border='2px solid red';" +
                "arguments[0].style.backgroundColor='yellow';", element);

        // Wait for a short period to allow visual inspection (adjust as needed)
        dynamicElementSynchronization(1);

        // Reset the element's styles to their original values
        jsExecutor.executeScript("arguments[0].style.border='';" +
                "arguments[0].style.backgroundColor='';", element);
    }

    protected static void highlightElements(List<WebElement> elements) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) getWebDriver();
        for (WebElement element : elements) {
            // Change the border and background color of the element to highlight it
            jsExecutor.executeScript("arguments[0].style.border='2px solid red';" +
                    "arguments[0].style.backgroundColor='yellow';", element);

            // Wait for a short period to allow visual inspection (adjust as needed)
            dynamicElementSynchronization(1);

            // Reset the element's styles to their original values
            jsExecutor.executeScript("arguments[0].style.border='';" +
                    "arguments[0].style.backgroundColor='';", element);
        }
    }

    public static WebElement getElementByWaiting(WaitStrategy waitStrategy, By locator) {
        return performExplicitWait(waitStrategy, locator);
    }

    public static WebElement getElementByWaiting(WaitStrategy waitStrategy, WebElement element) {
        return performExplicitWait(waitStrategy, element);
    }

    protected static WebElement getWebElement(String value, Locator locator) {
        By by = switch (locator) {
            case XPATH -> By.xpath(value);
            case CSS_SELECTOR -> By.cssSelector(value);
            case ID -> By.id(value);
            case LINK_TEXT -> By.linkText(value);
            case NAME -> By.name(value);
            case CLASS_NAME -> By.className(value);
            case TAG_NAME -> By.tagName(value);
        };

        return getWebDriver().findElement(by);
    }

    protected static WebElement getWebElement(String value, Locator locator, WaitStrategy waitStrategy) {
        By by = switch (locator) {
            case XPATH -> By.xpath(value);
            case CSS_SELECTOR -> By.cssSelector(value);
            case ID -> By.id(value);
            case LINK_TEXT -> By.linkText(value);
            case NAME -> By.name(value);
            case CLASS_NAME -> By.className(value);
            case TAG_NAME -> By.tagName(value);
        };

        return performExplicitWait(waitStrategy, by);
    }

    protected static List<WebElement> getWebElements(String xpath) {
        return getWebDriver().findElements(By.xpath(xpath));
    }

    public static List<WebElement> getWebElements(String value, Locator locator) {
        By by = switch (locator) {
            case XPATH -> By.xpath(value);
            case CSS_SELECTOR -> By.cssSelector(value);
            case ID -> By.id(value);
            case LINK_TEXT -> By.linkText(value);
            case NAME -> By.name(value);
            case CLASS_NAME -> By.className(value);
            default -> throw new IllegalArgumentException("Invalid locator type: " + locator);
        };

        return getWebDriver().findElements(by);
    }

    protected static List<WebElement> getWebElements(By by) {
        return getWebDriver().findElements(by);
    }

    @Step("{1}")
    public static void clickDropdownOptionAlternate(List<WebElement> ele, String value) {
        highlightElements(ele);
        try {
            for (WebElement element : ele) {
                if (element.getAttribute("innerText").equals(value) || element.getAttribute("innerText").equalsIgnoreCase(value)) {
                    highlightElement(element);
                    element.click();
                    info("<b>" + value + "</b> is selected", false);
                    AllureManager.logStep("Selected " + value);
                    dynamicElementSynchronization(1);
                    break;
                }
            }
        } catch (Exception ex) {
            throw new FrameworkTestException("Unable to select an option");
        }
    }

    @Step("{1}")
    public static void clickDropdownOptionWithStream(List<WebElement> ele, String value) {
        highlightElements(ele);
        Optional<WebElement> matchingElement = ele.stream()
                .filter(element -> element.getAttribute("innerText").equalsIgnoreCase(value))
                .findFirst();

        matchingElement.ifPresent(element -> {
            element.click();
            info("<b>" + value + "</b> is selected from dropdown", false);
            AllureManager.logStep("Selected " + value);
        });
    }

    public static String getPageUrl() {
        return getWebDriver().getCurrentUrl();
    }

    @Step("{0}")
    public static String getWebElementText(WebElement ele) {
        highlightElement(ele);
        String text = ele.getText();
        capitalizeFully(text);
        info("<b>" + text + "</b> is retrieved", false);
        AllureManager.logStep(text + " is retrieved");
        return text;
    }

    public static boolean getElementAttribute(WebElement ele) {
        String flag = ele.getAttribute("value");
        return flag.isEmpty();
    }

    public static String getElementValue(WebElement ele) {
        return ele.getAttribute("value");
    }

    public static String getElementText(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) getWebDriver();
        return js.executeScript("return arguments[0].firstChild.textContent;", element).toString();
    }

    public static boolean checkElementVisiblity(By element) {
        return getWebDriver().findElement(element).isDisplayed();
    }

    public static void scrollDown() {
        JavascriptExecutor js = (JavascriptExecutor) getWebDriver();
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    public static Long getHeight() {
        JavascriptExecutor js = (JavascriptExecutor) getWebDriver();
        return (Long) js.executeScript("return document.body.scrollHeight");
    }

    public static WebElement retrieveChildNode(WebElement nodeParent, String xpath) {
        return nodeParent.findElement(By.xpath(xpath));
    }

    @Step("{2}")
    protected static void click(WebElement ele, WaitStrategy waitStrategy, String elementName) {
        WebElement element = performExplicitWait(waitStrategy, ele);
        highlightElement(element);
        element.click();
        info("<b>" + elementName + "</b> is clicked", false);
        AllureManager.logStep(elementName + " is clicked");
    }

    protected static void clickJS(WebElement element, String elementName) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) getWebDriver();
        highlightElement(element);
        jsExecutor.executeScript("arguments[0].click();", element);
        info("<b>" + elementName + "</b> is clicked", false);
        AllureManager.logStep("<b>" + elementName + "</b> is clicked");
    }

    protected static void clickJS(WebElement element) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) getWebDriver();
        highlightElement(element);
        jsExecutor.executeScript("arguments[0].click();", element);
    }

    protected static int generateRandomIndex(List<WebElement> options) {
        return new Random().nextInt(options.size());
    }

    protected static String capitalizeFully(String value) {
        if (value == null || value.isEmpty()) {
            return value; // Return unchanged if input is null or empty.
        }

        // Convert the first character to uppercase and the rest to lowercase.
        return value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase().trim();
    }

    public static int countFilesInDownloadDirectory() {
        String pathFolderDownload = getDownloadpath();
        File file = new File(pathFolderDownload);
        int i = 0;
        for (File listOfFiles : Objects.requireNonNull(file.listFiles())) {
            if (listOfFiles.isFile()) {
                i++;
            }
        }
        return i;
    }

    public static boolean verifyFileContainsInDownloadDirectory(String fileName) {
        boolean flag = false;
        try {
            String pathFolderDownload = getDownloadpath();
            File dir = new File(pathFolderDownload);
            File[] files = dir.listFiles();
            assert files != null;
            for (File file : files) {
                if (file.getName().contains(fileName)) {
                    flag = true;
                    break;
                }
            }
            return flag;
        } catch (Exception e) {
            e.getMessage();
            return flag;
        }
    }

    public static boolean verifyFileEqualsInDownloadDirectory(String fileName) {
        boolean flag = false;
        try {
            String pathFolderDownload = getDownloadpath();
            File dir = new File(pathFolderDownload);
            File[] files = dir.listFiles();
            assert files != null;
            for (File file : files) {
                if (file.getName().equals(fileName)) {
                    flag = true;
                    break;
                }
            }
            return flag;
        } catch (Exception e) {
            e.getMessage();
            return flag;
        }
    }

    public static Boolean verifyDownloadFileContainsName(String fileName, int timeoutSeconds) {
        int i = 0;
        while (i < timeoutSeconds) {
            boolean exist = verifyFileContainsInDownloadDirectory(fileName);
            if (exist) {
                return true;
            }
            dynamicElementSynchronization(1);
            i++;
        }
        return false;
    }

    public static Boolean verifyDownloadFileEqualsName(String fileName, int timeoutSeconds) {
        int i = 0;
        while (i < timeoutSeconds) {
            boolean exist = verifyFileEqualsInDownloadDirectory(fileName);
            if (exist) {
                return true;
            }
            dynamicElementSynchronization(1);
            i++;
        }
        return false;
    }

    public static void sendKeysOneByOne(WebElement element, String data) {
        for (char c : data.toCharArray()) {
            String singleChar = String.valueOf(c);
            highlightElement(element);
            element.sendKeys(singleChar);
        }
        info("<b>" + data + "</b> is entered in textbox", false);
        AllureManager.logStep(data + " is entered in textbox");
        if (log().isInfoEnabled()) {
            log().info("{} is entered in textbox", data);
        }
    }

    public static void addScreenshotToAllureReport() {
        AllureManager.takeScreenshotStep();
    }

    protected static void selectDifferentOptionFromCurrent(List<WebElement> list, String currentValue) {
        while (true) {
            int index = generateRandomIndex(list);
            WebElement newJobRole = list.get(index);
            String updatedRole = newJobRole.getText();

            if (!updatedRole.equals(currentValue)) {
                click(newJobRole, WaitStrategy.NONE, updatedRole);
                break;
            }
        }
    }

    private static void scrollUntilElement(By by) {
        JavascriptExecutor js = (JavascriptExecutor) getWebDriver();
        String script = "window.scrollTo(0, document.body.scrollHeight)";
        while (true) {
            js.executeScript(script);
            Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(1));

            List<WebElement> loadMoreButton = getWebDriver().findElements(by);
            if (!loadMoreButton.isEmpty()) {
                loadMoreButton.get(0).click();
            }

            int previousHeight = (int) (long) js.executeScript("return document.body.scrollHeight");
            js.executeScript(script);
            Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(1));

            int newHeight = (int) (long) js.executeScript("return document.body.scrollHeight");
            if (newHeight == previousHeight) {
                break;
            }
        }
    }

    @Step("{2}")
    public void clickDropdownOption(List<WebElement> ele, WaitStrategy waitStrategy, String value) {
        highlightElements(ele);
        try {
            for (WebElement element : ele) {
                String option = element.getText().trim();
                if (option.equalsIgnoreCase(value)) {
                    highlightElement(element);
                    click(element, waitStrategy, value);
                    AllureManager.logStep("Selected " + value);
                    dynamicElementSynchronization(1);
                    break;
                }
            }
        } catch (Exception ex) {
            throw new FrameworkTestException("Unable to select an option");
        }
    }

    @SneakyThrows
    @Step("{2}")
    protected void actionsClick(By by, WaitStrategy waitStrategy, String elementName) {
        Actions actions = new Actions(getWebDriver());
        WebElement element = performExplicitWait(waitStrategy, by);
        highlightElement(element);
        actions.moveToElement(element).click().build().perform();
        info("<b>" + elementName + "</b> is clicked", false);
        AllureManager.logStep(elementName + " is clicked");
    }

    @SneakyThrows
    @Step("{2}")
    protected void actionsClick(WebElement element, WaitStrategy waitStrategy, String elementName) {
        Actions actions = new Actions(getWebDriver());
        highlightElement(element);
        actions.moveToElement(element).click().build().perform();
        info("<b>" + elementName + "</b> is clicked", false);
        AllureManager.logStep(elementName + " is clicked");
    }

    @SneakyThrows
    @Step("'{0}' is clicked")
    protected void click(By by, WaitStrategy waitStrategy, String elementName) {
        WebElement element = performExplicitWait(waitStrategy, by);
        highlightElement(element);
        element.click();
        info("<b>" + elementName + "</b> is clicked", false);
        AllureManager.logStep(elementName + " is clicked");
    }

    protected void click(WebElement element, String elementName) {
        highlightElement(element);
        element.click();
        info("<b>" + elementName + "</b> is clicked", false);
        AllureManager.logStep(elementName + " is clicked");
    }

    protected void click(WebElement element) {
        JavascriptExecutor executor = (JavascriptExecutor) getWebDriver();
        highlightElement(element);
        executor.executeScript("arguments[0].click();", element);
        info("<b>" + element.getAttribute("innerText") + "</b> is clicked", false);
        AllureManager.logStep("<b>" + element.getAttribute("innerText") + "</b> is clicked");
    }

    protected void clickDynamic(String xpath, int index) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) getWebDriver();
        WebElement element = getWebDriver().findElement(By.xpath(String.format(xpath, "[" + (index) + "]")));
        highlightElement(element);
        String elementName = element.getAttribute("innerText");
        jsExecutor.executeScript("arguments[0].click();", element);
        info("<b>" + elementName + "</b> is clicked", false);
        AllureManager.logStep("<b>" + elementName + "</b> is clicked");
    }

    protected void clickFromList(String xpath, int indice) {
        WebElement unavailableElement = getWebDriver().findElements(By.xpath(xpath)).get(indice);
        highlightElement(unavailableElement);
        unavailableElement.click();
    }

    //@Step("'{0}' is entered in '{1}'")
    @Step("{1} is entered in textbox")
    protected void sendKeys(By by, String value, WaitStrategy waitStrategy, String elementName) {
        WebElement element = performExplicitWait(waitStrategy, by);
        highlightElement(element);
        element.sendKeys(value);
        info("<b>" + value + "</b> is entered in " + elementName, false);
        AllureManager.logStep(value + " is entered in " + elementName);
        if (log().isInfoEnabled()) {
            log().info("{} is entered in {}", value, elementName);
        }
    }

    @Step("{1} is entered in textbox")
    protected void sendKeys(WebElement element, String value, WaitStrategy waitStrategy, String elementName) {
        WebElement webElement = ExplicitWaitFactory.performExplicitWait(waitStrategy, element);
        highlightElement(webElement);
        webElement.sendKeys(value);
        info("<b>" + value + "</b> is entered in " + elementName, false);
        AllureManager.logStep(value + " is entered in " + elementName);
        if (log().isInfoEnabled()) {
            log().info("{} is entered in {}", value, elementName);
        }
    }

    public void sendKeysWithRetry(By locator, String text, String elementName, int maxRetries) {
        int retries = 0;
        while (retries < maxRetries) {
            try {
                WebElement element = getWebDriver().findElement(locator);
                highlightElement(element);
                element.sendKeys(text);
                info("<b>" + text + "</b> is entered in " + elementName, false);
                AllureManager.logStep("<b>" + text + "</b> is entered in " + elementName);
                if (log().isInfoEnabled()) {
                    log().info("{} is entered in {}", text, elementName);
                }
                return; // Exit the method if successful
            } catch (StaleElementReferenceException e) {
                // Handle the StaleElementReferenceException
                // Log the exception or perform any necessary actions
                retries++;
            }
        }
        throw new FrameworkTestException("Failed to send keys after maximum retries.");
    }

    protected void pressEnter(By by, WaitStrategy waitStrategy) {
        WebElement element = performExplicitWait(waitStrategy, by);
        highlightElement(element);
        JavascriptExecutor executor = (JavascriptExecutor) getWebDriver();
        try {
            if (element != null) {
                // Execute JavaScript to simulate pressing Enter
                executor.executeScript("var event = new KeyboardEvent('keydown', { key: 'Enter', keyCode: 13, which: 13, bubbles: true, cancelable: true }); arguments[0].dispatchEvent(event);", element);
            }
        } catch (Exception e) {
            throw new FrameworkTestException("Unable to interact with the element");
        }
    }

    protected void pressEnterWithAction(By by, WaitStrategy waitStrategy) {
        WebElement element = performExplicitWait(waitStrategy, by);
        Actions actions = new Actions(getWebDriver());
        actions.moveToElement(element).sendKeys(Keys.ENTER).perform();
    }

    protected void hoverOverElement(WebElement element) {
        String javaScript = "var evObj = document.createEvent('MouseEvents');" +
                "evObj.initMouseEvent(\"mouseover\",true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);" +
                "arguments[0].dispatchEvent(evObj);";

        ((JavascriptExecutor) getWebDriver()).executeScript(javaScript, element);
    }

    protected void clickCheckBox(String value) {
        ((JavascriptExecutor) getWebDriver()).executeScript(value);
    }

    protected void clearText(By by, WaitStrategy waitStrategy, String elementName) throws Exception {
        WebElement element = performExplicitWait(waitStrategy, by);
        highlightElement(element);
        element.clear();
        info("<b>" + elementName + "</b> is cleared", false);
    }

    protected String getElementText(By by, WaitStrategy waitStrategy, String elementName) {
        WebElement element = performExplicitWait(waitStrategy, by);
        highlightElement(element);
        info(element.getText() + " is retrieved from " + elementName, false);
        AllureManager.logStep(element.getText() + " is retrieved from " + elementName);
        return element.getText();
    }

    public WebElement getParentNode(int nodeParentIndex, String xpath) {
        WebElement node = getWebDriver().findElement(By.xpath(String.format(xpath, "[" + (nodeParentIndex + 1) + "]")));
        return (WebElement) ((JavascriptExecutor) getWebDriver()).executeScript(
                "return arguments[0].parentNode;", node);
    }

    public WebElement getNode(String xpath) {
        WebElement node = getWebDriver().findElement(By.xpath(xpath));
        return (WebElement) ((JavascriptExecutor) getWebDriver()).executeScript(
                "return arguments[0].parentNode;", node);
    }

    public WebElement getChildNodeIndex(WebElement nodeParent, String xpath, int nodeParentIndex) {
        return nodeParent.findElement(By.xpath(String.format(xpath, "[" + (nodeParentIndex - 1) + "]")));
    }

    public WebElement getChildNodeIndex(WebElement nodeParent, String xpath) {
        return nodeParent.findElement(By.xpath(xpath));
    }

    public WebElement getSiblingNode(WebElement element, String xpath, int nodeIndex) {
        return element.findElement(By.xpath(String.format(xpath, "[" + (nodeIndex + 1) + "]")));
    }

    public WebElement getChildElementByTagName(WebElement parentElement, String tagName) {
        List<WebElement> childElements = parentElement.findElements(By.tagName(tagName));
        if (!childElements.isEmpty()) {
            return childElements.get(0);
        }
        return null;
    }

    public WebElement getParentNode(WebElement element) {
        return (WebElement) ((JavascriptExecutor) getWebDriver()).executeScript(
                "return arguments[0].parentNode;", element);
    }

    public WebElement getSiblingNode(WebElement element) {
        WebElement parentNode = getParentNode(element);
        return (WebElement) ((JavascriptExecutor) getWebDriver()).executeScript(
                "return arguments[0].nextElementSibling;", parentNode);
    }

    public WebElement getPreviousSiblingNode(WebElement element) {
        WebElement parentNode = getParentNode(element);
        return (WebElement) ((JavascriptExecutor) getWebDriver()).executeScript(
                "return arguments[0].previousElementSibling;", parentNode);
    }

    public WebElement getFirstNonTextNode(WebElement parentElement) {
        List<WebElement> childElements = parentElement.findElements(By.xpath("./*[not(self::text())]"));
        highlightElements(childElements);
        if (!childElements.isEmpty()) {
            highlightElement(childElements.get(0));
            return childElements.get(0);
        }
        return null;
    }

    public WebElement findElementByIndex(String xpath, int index) {
        String indexedXpath = String.format("(%s)[%d]", xpath, index);
        return getWebDriver().findElement(By.xpath(indexedXpath));
    }

    public String getTextIgnoringChildElement(By by) {
        WebElement parentElement = getWebDriver().findElement(by);
        String parentText = parentElement.getText();
        List<WebElement> childElements = parentElement.findElements(By.xpath("./*"));
        for (WebElement childElement : childElements) {
            String childText = childElement.getText();
            parentText = parentText.replace(childText, "").trim();
        }
        return parentText.substring(0, parentText.length() - 1);
    }

    public WebElement findElementUsingIndex(By childLocator, int index) {
        String indexedLocator = "(" + childLocator.toString().replaceFirst("^By\\.(\\w+): ", "") + ")[" + index + "]";
        return getWebDriver().findElement(By.xpath(indexedLocator));
    }

    @SneakyThrows
    public WebElement findElementByLocatorUsingJavaScript(By locator) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) getWebDriver();
        String jsCode = "document.evaluate(locator, document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;";
        return (WebElement) jsExecutor.executeScript(jsCode);
    }

    public By appendIndexToLocator(By locator, int index) {
        String locatorString = locator.toString();
        String modifiedLocatorString = "(" + locatorString.replace("By.xpath: ", "") + ")[" + index + "]";
        return By.xpath(modifiedLocatorString);
    }

    public void clickUsingJavaScript(String xpathExpression) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) getWebDriver();
        String script = "var xpathResult = document.evaluate('" + xpathExpression + "', document, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);\n" +
                "var element = xpathResult.snapshotItem(0);\n" +
                "element.click();";
        jsExecutor.executeScript(script);
    }

    public void clickUsingJavaScript(By locator) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) getWebDriver();
        String script = "var element = arguments[0];" +
                "element.click();";
        WebElement element = getWebDriver().findElement(locator);
        highlightElement(element);
        jsExecutor.executeScript(script, element);
    }

    public String generateDynamicXPath(By locator, String indexStr) {
        int index = Integer.parseInt(indexStr);
        String xpathTemplate = "(%s)[%d]";
        String locatorAsString = locator.toString();

        // Remove the locator type prefix, e.g., "By.xpath: "
        locatorAsString = locatorAsString.replaceAll("^[^:]+: ", "");

        return String.format(xpathTemplate, locatorAsString, index);
    }

    protected Path downloadFile(By locator, WaitStrategy waitStrategy, String elementName, String nameOfFile) {
        String tempFolderPath;

        if (getMode().equalsIgnoreCase("remote")) {
            // Use a predefined environment variable for the remote mode
            tempFolderPath = "G:/Downloads";
        } else {
            // Use a default path for local mode
            tempFolderPath = getDownloadpath();
            try {
                Files.createDirectories(Paths.get(tempFolderPath));
            } catch (IOException e) {
                // Handle the exception
            }
        }
        highlightElement(getWebDriver().findElement(locator));
        click(locator, waitStrategy, elementName);
        dynamicElementSynchronization(2);

        // Wait for the file to be downloaded
        return Paths.get(tempFolderPath, nameOfFile);
    }

    protected void scrollTillBottomOfPage() {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) getWebDriver();
        while (true) {
            long initialHeight = (long) jsExecutor.executeScript("return document.body.scrollHeight");
            jsExecutor.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            dynamicElementSynchronization(3); // Wait for the new content to load

            long newHeight = (long) jsExecutor.executeScript("return document.body.scrollHeight");
            if (newHeight == initialHeight) {
                // Reached the end of the page
                break;
            }
        }
    }

    protected void scrollTillTopOfPage() {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) getWebDriver();
        while (true) {
            jsExecutor.executeScript("window.scrollTo(0, 0)");
            dynamicElementSynchronization(3); // Wait for any content adjustments

            long scrollPosition = (long) jsExecutor.executeScript("return window.scrollY");
            if (scrollPosition <= 0) {
                // Reached the top of the page
                break;
            }
        }
    }

    public void sendKeysOneByOneWithRetry(By locator, String text, String elementName, int maxRetries) {
        int retries = 0;
        while (retries < maxRetries) {
            try {
                WebElement element = getWebDriver().findElement(locator);
                for (char c : text.toCharArray()) {
                    String singleChar = String.valueOf(c);
                    highlightElement(element);
                    element.sendKeys(singleChar);
                    dynamicElementSynchronization(1);
                }
                info("<b>" + text + "</b> is entered in " + elementName, false);
                if (log().isInfoEnabled()) {
                    log().info("{} is entered in {}", text, elementName);
                }
                return; // Exit the method if successful
            } catch (StaleElementReferenceException e) {
                // Handle the StaleElementReferenceException
                // Log the exception or perform any necessary actions
                retries++;
            }
        }
        throw new FrameworkTestException("Failed to send keys after maximum retries.");
    }

    public void refreshScreen() {
        getWebDriver().navigate().refresh();
    }

    @Beta
    void elementSynchronization(long duration) {
        Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(duration));
    }

    public String getCurrency(String input) {
        return input.replaceAll("[^0-9]", "");
    }
}
