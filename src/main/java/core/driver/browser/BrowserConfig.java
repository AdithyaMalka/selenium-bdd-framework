package core.driver.browser;

import org.openqa.selenium.WebDriver;

public abstract class BrowserConfig<T> {

    public abstract WebDriver getDriverConfiguration();

    protected abstract T getDriverOptions();

}