package core.driver.browser;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import static utils.Constants.*;

public class Edge extends BrowserConfig<EdgeOptions> {

    @Override
    public WebDriver getDriverConfiguration() {
        return new EdgeDriver(getDriverOptions());
    }

    @Override
    protected EdgeOptions getDriverOptions() {
        EdgeOptions options = new EdgeOptions();

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
