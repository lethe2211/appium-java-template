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
        //
        // When you run the tests on Android Emulator,
        // you need to make sure there is a running emulator whose OS is the same as "appium:platformVersion"
        // $ $ANDROID_HOME/platform-tools/adb devices -l
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "12");
        desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Emulator");
        desiredCapabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
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
        // Link text area
        WebElement linkText = driver.findElement(By.id("link_text"));

        // Verify no texts are input in the below text area
        String expectedBeforeInput = "";
        String actualBeforeInput = linkText.getText();

        assertEquals(expectedBeforeInput, actualBeforeInput);

        // Search button in the SearchView
        WebElement searchButton = driver.findElement(By.id("searchbar")).findElement(By.className("android.widget.ImageView"));

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