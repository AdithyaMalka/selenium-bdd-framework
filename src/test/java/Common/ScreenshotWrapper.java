package Common;

import static core.driver.DriverManager.setCurrentStepName;


public class ScreenshotWrapper {

    public static void entryExitScreenshots(String stepName, Runnable actions) {
        setCurrentStepName(stepName);
        Hooks.onFailScreenshot("Entry- " + stepName);

        actions.run();

        // Removed as creates duplicate screenshots.
        // (end = entry of next so both show the same thing)
        // Hooks.onFailScreenshot("Exit-" + stepName);
    }
}