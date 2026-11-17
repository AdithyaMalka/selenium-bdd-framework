package utils;

public class Constants {

    // Test Tag
    public static final String TEST_TAG =
            "@Demo";

    // Run Type Constants -> comments show the effect of setting each to true
    public static final boolean REGRESSION_MODE = false;
    // will run in headless mode and increase wait times

    public static final boolean PARALLEL_EXECUTION = false;
    // enables parallel execution of 10 tests at once

    public static final boolean SOFT_ASSERTIONS = false;
    // will continue test execution on assertion failures

    public static final boolean SCREENSHOT_ON_ASSERTION_FAIL = false;
    // attach screenshots on assertion failures

    public static final boolean PASS_ON_ASSERTION_FAILURE = false;
    // will mark test as passed even if there are assertion failures

    public static final boolean VALIDATE_ALL = false;
    // will enter FLS validation contained inside normal steps. Used for Smoke/SIT

    public static final boolean LARGE_TEST = false;
    // will skip the entry/exit screenshots. Stops OutOfMemory errors

    public static final boolean DEBUG_MODE = false;
    // Prints debug logs and debugScreenshot. Prints all screenshots in entryExit

    public static final boolean DISABLE_ZOOM = false;
    // disables zoom methods

    public static final boolean SSO_DISABLED = true;
    // run login logic, for when SSO does not automatically log in

    // ********************************

    // driver waits type constants
    public static final int SMALL_WAIT_TIME = 5;
    public static final int MEDIUM_WAIT_TIME = 35;
    public static final int LONG_WAIT_TIME = 120;
    public static final int REGRESSION_WAIT_TIME = 180;

    // screenshot & report constants
    public static final String SCREENSHOTS_PATH = "screenshots/";
    public static final String EDI_PATH = "edi/";
    public static final String TEST_DATA_OUTPUT_PATH = "test_output_data/";
    public static final String CUCUMBER_JSON =
            "build/test-report/cucumber/cucumber.json";
    public static final String REPORTER_PATH = "build/test-report/";
    public static final String PROJECT_NAME = "TRV Test Automation";
    public static final String SCREENSHOTS_EXTENSION = ".png";
    public static final String TXT_EXTENSION = ".txt";

    // env constants
    public static final String ENV_VARIABLE = "ExecutionEnv";
    public static final String MQ_ENV = "mo";
    public static final String ENV_CONFIG_PATH =
            "src/test/resources/config/env/%s.json";

    public static final String USER_ID_VARIABLE = "UserId";
    public static final String PASSWORD_VARIABLE = "Password";

    // user data constants
    public static final String CORE_DATA_PATH =
            "src/test/resources/config/testdata/no/%s/";
    public static final String DATA_PATH =
            "src/test/resources/config/testdata/%s/%s/%s.json";

    // driver type constants
    public static final String BROWSER_TYPE_VARIABLE = "BrowserType";

    // web driver options constants
    public static final String DISABLE_POPUP_BLOCKING =
            "--disable-popup-blocking";
    public static final String HEADLESS = "--headless";
    public static final String INCOGNITO = "--incognito";
    public static final String LANG_EN_US = "--lang=en-US";
    public static final String NO_SANDBOX = "--no-sandbox";
    public static final String REMOTE_ALLOW_ORIGINS =
            "--remote-allow-origins=*";
    public static final String START_MAXIMIZED = "start-maximized";
    public static final String WINDOW_SIZE = "--window-size=1920,1080";
    public static final String TIME_ZONE_FOR_TESTING_GMT =
            "--time-zone-for-testing=GMT";
    public static final String IGNORE_CERTIFICATE_ERRORS =
            "--ignore-certificate-errors";
    public static final String DISABLE_DEV_SHM_USAGE =
            "--disable-dev-shm-usage";
    public static final String DISABLE_GPU = "--disable-gpu";
}
