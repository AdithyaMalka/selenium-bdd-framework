package stepdefinitions;

import io.cucumber.java.ParameterType;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import pages.LaunchWorkStationPage;
import pages.LoginPage;

import static Common.ScreenshotWrapper.entryExitScreenshots;

public class LoginSteps {
    LaunchWorkStationPage launchWorkStationPage = new LaunchWorkStationPage();
    LoginPage loginPage = new LoginPage();
    @Given("Login into the application")
    public void loginIntoTheApplication() {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("verify the login is successful")
    public void verifyTheLoginIsSuccessful() {
      loginPage.isOmayoHeaderDisplayed();

    }

    @ParameterType("Omayo|Workstation")
    public String env(String envOption) {
        return envOption;
    }

    @Given("User Login into {env} Application")
    public void userLoginIntoApplication(String env) {
       entryExitScreenshots("LoginSteps",()->{;
              String url = launchWorkStationPage.launchTravelersAdminPage(env);
              System.out.println("Launched URL: " + url);

       });
    }
}
