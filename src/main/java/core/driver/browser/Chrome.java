package core.driver.browser;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static utils.Constants.*;

public class Chrome extends BrowserConfig<ChromeOptions> {

    @Override
    public WebDriver getDriverConfiguration() {
        return new ChromeDriver(getDriverOptions());
    }

    @Override
    protected ChromeOptions getDriverOptions() {
        ChromeOptions options = new ChromeOptions();

        options.addArguments(NO_SANDBOX);

        if (REGRESSION_MODE) {
            options.addArguments(HEADLESS);
        }

        if (REGRESSION_MODE && PARALLEL_EXECUTION) {
            options.addArguments(DISABLE_GPU);
        }

        options.addArguments(IGNORE_CERTIFICATE_ERRORS);
        options.addArguments(WINDOW_SIZE);
        options.addArguments(DISABLE_POPUP_BLOCKING);
        options.addArguments(INCOGNITO);
        options.addArguments(LANG_EN_US);
        options.addArguments(REMOTE_ALLOW_ORIGINS);
        options.addArguments(TIME_ZONE_FOR_TESTING_GMT);
        options.addArguments(DISABLE_DEV_SHM_USAGE);

        return options;
    }
}