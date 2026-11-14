package runners;

import Common.TestListener;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import utils.Constants;

@Listeners(TestListener.class)
@CucumberOptions(
        // !! DO NOT UPDATE!! Update TEST_TAG in Constants directly
        tags = Constants.TEST_TAG,
        features = { "src/test/resources/features" },
        glue = { "stepdefinitions" },
        plugin = {
                "pretty",
                "html:build/test-report/cucumber/cucumber.html",
                "json:build/test-report/cucumber/cucumber.json"
        }
)
public class TestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = Constants.PARALLEL_EXECUTION)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}