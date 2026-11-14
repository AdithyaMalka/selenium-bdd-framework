package core.reporter;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.OutputType;
import java.io.File;
import org.apache.commons.io.FileUtils;
import java.io.IOException;
import org.openqa.selenium.UnhandledAlertException;
import core.driver.DriverManager;

import static utils.Constants.*;


@Slf4j
public final class ScreenshotManager {

    private static final ThreadLocal<ScreenshotManager> THREAD_LOCAL =
            new ThreadLocal<>();

    private final String screenshotFolder;
    private final AtomicInteger screenshotCounter = new AtomicInteger();

    private ScreenshotManager(String screenshotFolder) {
        this.screenshotFolder = screenshotFolder;
        this.screenshotCounter.set(1);
    }

    public static void initScreenShotManager(String filename) {
        THREAD_LOCAL.set(new ScreenshotManager(filename));
    }

    public static ScreenshotManager getInstance() {
        final ScreenshotManager instance = THREAD_LOCAL.get();
        if (instance != null) {
            return instance;
        }

        throw new IllegalStateException(
                "Instance of ScreenShotManager not initialised. " +
                        "Try to initialize ScreenShotManager by calling initScreenShotManager()"
        );
    }

    public static void clear() {
        THREAD_LOCAL.remove();
    }

    public synchronized byte[] takeScreenshot(String screenshotName) {

        String screenShotName =
                String.format("%02d - ",
                        screenshotCounter.getAndIncrement())
                        + screenshotName
                        + SCREENSHOTS_EXTENSION;

        String screenshotsFolder =
                REPORTER_PATH
                        + SCREENSHOTS_PATH
                        + this.screenshotFolder;

        try {

            TakesScreenshot screenShot =
                    ((TakesScreenshot) DriverManager.getDriver());

            byte[] screenShotAsBytes =
                    screenShot.getScreenshotAs(OutputType.BYTES);

            File screenshotDestination =
                    new File(screenshotsFolder, screenShotName);

            try {
                FileUtils.writeByteArrayToFile(
                        screenshotDestination,
                        screenShotAsBytes
                );
            } catch (IOException e) {
                log.error("ScreenShot moving error");
                e.printStackTrace();
            }

            return screenShotAsBytes;

        } catch (UnhandledAlertException e) {
            log.error("Unhandled alert on attempt screenshot");
            return new byte[0];
        }
    }
}
