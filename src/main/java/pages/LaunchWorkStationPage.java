package pages;

import core.data.envdata.EnvConfiguration;
import lombok.extern.slf4j.Slf4j;

import static core.driver.DriverManager.getDriver;
import static core.driver.DriverManager.setGlobalStringStoreValue;

@Slf4j
public class LaunchWorkStationPage extends BasePage {

    public LaunchWorkStationPage() {
        super();
    }

    public void launchTravelersAdminPage() {
        log.info("Launching workstation page");
        // driver = new ChromeDriver();
        // getDriver().get(get().getWorkstationUrl());
        getDriver().get(EnvConfiguration.get().getWorkstationUrl());
    }

    public String launchTravelersAdminPage(String envOption) {
        log.info("Launching workstation page");
        // driver = new ChromeDriver();
        // getDriver().get(get().getWorkstationUrl());

        String url = "Undefined";
        setGlobalStringStoreValue("envOption", envOption);

        if (envOption.equals("Omayo")) {
            getDriver().get(EnvConfiguration.get().getOmayoBlogspotUrl());
            url = EnvConfiguration.get().getOmayoBlogspotUrl();
        }
        return url;

    }
}