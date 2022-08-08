# Appium sample test codes

## Android

### Dependencies

* Node.js
* NPM
* JDK
* Android SDK (Android Studio 2021.1.1)

### Preparation

```bash
# Verify if node is installed
$ node -v
  # v12.22.1

# Verify if npm is installed
$ npm -v
  # 8.7.0

# Verify if JDK is installed (JDK 8 or 11 is preferable)
$ java -version
  # openjdk version "11.0.11" 2021-04-20
  # OpenJDK Runtime Environment AdoptOpenJDK-11.0.11+9 (build 11.0.11+9)
  # Eclipse OpenJ9 VM AdoptOpenJDK-11.0.11+9 (build openj9-0.26.0, JRE 11 Mac OS X amd64-64-Bit Compressed References 20210421_957 (JIT enabled, AOT enabled)
  # OpenJ9   - b4cc246d9
  # OMR      - 162e6f729
  # JCL      - 7796c80419 based on jdk-11.0.11+9)

# Verify if JAVA_HOME is set
$ echo $JAVA_HOME
  # /Users/$USER/.sdkman/candidates/java/current

# Verify if ANDROID_HOME is set
$ echo $ANDROID_HOME
 # /Users/$USER/Library/Android/sdk

# [Only you want to run the tests with Android emulator]
# Verify if there's any Android emulator that runs with Android 12
$ $ANDROID_HOME/platform-tools/adb devices -l
  # If not, run it from Android Studio

# Install Appium server
$ npm install -g appium
$ npx appium -v
  # 1.22.3

# Verify if all of the above prerequisites are met
$ npx appium-doctor --android
  # Check if "### Diagnostic for necessary dependencies completed, no fix needed. ##" is shown in the console

# Run the Appium server on 127.0.0.1:4723
$ npx appium
  # Check if the server really runs without any problem
  
# Alternatively, we could install and use Appium Desktop https://github.com/appium/appium-desktop as Appium server
```

### How to run the tests

```bash
# Inject necessary env vars
$ export ANDROID_APK_FILE_PATH="binary/android/app-release.apk"
$ export ANDROID_PACKAGE_NAME="com.example.webviewlinksample"
$ export ANDROID_LAUNCH_ACTIVITY_NAME=".MainActivity"

$ ./gradlew test
```

## iOS

TBD