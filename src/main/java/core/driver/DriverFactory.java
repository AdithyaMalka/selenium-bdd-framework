package core.driver;

import core.driver.browser.Chrome;
import core.driver.browser.Edge;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import utils.ConfigParser;
import utils.Constants;

import java.time.Duration;

@UtilityClass
public class DriverFactory {

    static WebDriver createDriver() {
        WebDriver driver;
        String browserType = ConfigParser.getBrowserType();

        if (Browser.EDGE.is(browserType)) {
            Edge edgeDriver = new Edge();
            driver = edgeDriver.getDriverConfiguration();
        } else {
            Chrome chromeDriver = new Chrome();
            driver = chromeDriver.getDriverConfiguration();
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(100));

        if (Constants.REGRESSION_MODE) {
            driver.manage().window().setSize(new Dimension(3200, 1800));
        } else {
            driver.manage().window().maximize();
        }

        return driver;
    }
}