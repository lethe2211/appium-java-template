package com.example.sample;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IOSTest {
    private IOSDriver driver;

    @BeforeEach
    public void setUp() throws MalformedURLException {
        // Please inject these env vars when you run these tests
        String appFilePath = System.getenv("IOS_APP_FILE_PATH"); // Relative path to the .app directory
        // String appFilePath = "binary/ios/webviewlinksample.app";

        File app = new File(appFilePath);

        // Ref: https://appium.io/docs/en/writing-running-appium/caps/
        //
        // When you run the tests on iOS Simulator,
        // you need to choose the OS/device which is listed in the output of
        // $ xcrun xctrace list devices
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.IOS);
        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "15.5");
        desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone 8");
        desiredCapabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());

        driver = new IOSDriver(new URL("http://0.0.0.0:4723/wd/hub"), desiredCapabilities);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Toolbar has `WebViewLink Sample` as text")
    public void toolbarHasCorrectTitle() {
        String expected = "WebViewLink Sample";

        // Fetch the title of the toolbar
        // Note that the title of NavigationBar will automatically be the same as its Accessibility ID and we don't have a control to change it from the code
        WebElement toolbar = driver.findElement(new AppiumBy.ByAccessibilityId("WebViewLink Sample"));

        // In case of iOS, toolbar (= NavigationBarTitle) is contained "name" attribute
        String actual = toolbar.getAttribute("name");

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("The text you input in the search bar is shown in the below area as a link which is tappable")
    public void inputStringIsShownAsLink() {
        // Link text area
        WebElement linkText = driver.findElement(By.id("link_text"));

        // Verify no texts are input in the below text area
        String expectedBeforeInput = "";
        String actualBeforeInput = linkText.getText();

        assertEquals(expectedBeforeInput, actualBeforeInput);

        // Search button
        // TODO: Get the locator in a more proper way
        WebElement searchButton = driver.findElement(By.xpath("//XCUIElementTypeTextField[@name=\"searchbar\"]"));

        // Tap the search button to get its focus
        searchButton.click();

        // Type some characters in the search bar
        Actions action = new Actions(driver);
        action.sendKeys("https://google.com").perform();

        // Verify the text it inputted to the search bar is really shown in the below text area
        String expectedAfterInput = "https://google.com";
        String actualAfterInput = linkText.getText();

        assertEquals(expectedAfterInput, actualAfterInput);

        // Tap the generated link
        linkText.click();
    }
}
