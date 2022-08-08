package com.example.sample;


import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AndroidTest {
    private AndroidDriver driver;

    @BeforeEach
    public void setUp() throws MalformedURLException {
        // Please inject these env vars when you run these tests
        String apkFilePath = System.getenv("ANDROID_APK_FILE_PATH"); // Relative path to the APK file
        // String apkFilePath = "binary/android/app-release.apk";
        String packageName = System.getenv("ANDROID_PACKAGE_NAME"); // App package name described in AndroidManifest.xml
        // String packageName = "com.example.webviewlinksample";
        String launchActivityName = System.getenv("ANDROID_LAUNCH_ACTIVITY_NAME"); // Activity name where App is launched
        // String launchActivityName = ".MainActivity";

        File app = new File(apkFilePath);

        // Ref: https://appium.io/docs/en/writing-running-appium/caps/
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Emulator");
        desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
        desiredCapabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "12");
        desiredCapabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, packageName);
        desiredCapabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, launchActivityName);

        driver = new AndroidDriver(new URL("http://0.0.0.0:4723/wd/hub"), desiredCapabilities);
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
        // Note that androidx.appcompat.widget.Toolbar implicitly creates an element of android.widget.TextView as a child
        WebElement toolbar = driver.findElement(By.id("toolbar")).findElement(By.className("android.widget.TextView"));
        String actual = toolbar.getText();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("The text you input in the search bar is shown in the below area as a link which is tappable")
    public void inputStringIsShownAsLink() {
        // Search button in the SearchView
        WebElement searchButton = driver.findElement(By.id("searchbar")).findElement(By.className("android.widget.ImageView"));

        // Link text area
        WebElement searchSrcText = driver.findElement(By.id("link_text"));

        // Verify no texts are input in the search bar
        String expectedBeforeInput = "";
        String actualBeforeInput = searchSrcText.getText();

        assertEquals(expectedBeforeInput, actualBeforeInput);

        // Tap the search button
        searchButton.click();

        // Type the text
        Actions action = new Actions(driver);
        action.sendKeys("https://point.rakuten.co.jp").perform();

        // Verify the text is really input in the search bar
        String expectedAfterInput = "https://point.rakuten.co.jp";
        String actualAfterInput = searchSrcText.getText();

        assertEquals(expectedAfterInput, actualAfterInput);
    }
}