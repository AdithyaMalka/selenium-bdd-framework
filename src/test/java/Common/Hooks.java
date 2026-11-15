package Common;

import core.data.testdata.DataManager;
import core.driver.DriverManager;
import core.reporter.ScreenshotManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import lombok.extern.slf4j.Slf4j;
import utils.Constants;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static core.driver.DriverManager.*;
import static core.reporter.QuoteFileReporter.getAllFiles;
import static utils.QuoteTabCleanup.closeAllQuoteTabs;

@Slf4j
public class Hooks {

    private static final ThreadLocal<Scenario> scenario =
            new ThreadLocal<>();

    private static String timeStampedText;

    private static final ThreadLocal<List<ScreenshotObject>> screenshotObjectList =
            ThreadLocal.withInitial(ArrayList::new);

    private static final ThreadLocal<Long> startTime =
            new ThreadLocal<>();

    private static final List<TimeTakenObject> timeTakenObjectList =
            Collections.synchronizedList(new ArrayList<>());

    public static void addFailureScreenshot(HashMap<String, byte[]> screenshotFailureMap) {
        for(String key : screenshotFailureMap.keySet()) {
            try{
                Hooks.scenario.get().attach(screenshotFailureMap.get(key), "image/png", "Assert failure: " + key);
            } catch (InvalidPathException e) {
                System.out.println("Invalid path: " + e.getMessage());
                Hooks.scenario.get().attach(screenshotFailureMap.get(key), "image/png", "Assert failure: invalid screenshot name");
            }
        }
    }

    public static void addScreenshot(String screenshotName) {
        try {
            final byte[] screenshot =
                    ScreenshotManager.getInstance()
                            .takeScreenshot(screenshotName);

            Hooks.scenario.get().attach(
                    screenshot,
                    "image/png",
                    screenshotName
            );

        } catch (InvalidPathException e) {
            System.out.println("Invalid path: " + e.getMessage());

            screenshotName = "invalid screenshot name";
            final byte[] screenshot =
                    ScreenshotManager.getInstance()
                            .takeScreenshot(screenshotName);

            Hooks.scenario.get().attach(
                    screenshot,
                    "image/png",
                    screenshotName
            );

        } catch (IllegalStateException e) {
            System.out.println("Illegal State: " + e.getMessage());
        }
    }

    public static void debugScreenshot(String screenshotName) {
        if (Constants.DEBUG_MODE) {
            try {
                final byte[] screenshot =
                        ScreenshotManager.getInstance()
                                .takeScreenshot(screenshotName);

                Hooks.scenario.get().attach(
                        screenshot,
                        "image/png",
                        screenshotName
                );

            } catch (InvalidPathException e) {
                System.out.println("Invalid path: " + e.getMessage());

                screenshotName = "invalid screenshot name";
                final byte[] screenshot =
                        ScreenshotManager.getInstance()
                                .takeScreenshot(screenshotName);

                Hooks.scenario.get().attach(
                        screenshot,
                        "image/png",
                        screenshotName
                );

            } catch (IllegalStateException e) {
                System.out.println("Illegal State: " + e.getMessage());
            }
        }
    }

    public static void onFailScreenshot(String screenshotName) {
        if (Constants.LARGE_TEST) {
            return;
        }

        try {
            final byte[] screenshot =
                    ScreenshotManager.getInstance()
                            .takeScreenshot(screenshotName);

            ScreenshotObject localScreenshotObject =
                    new ScreenshotObject(
                            screenshot,
                            "image/png",
                            screenshotName
                    );

            screenshotObjectList.get().add(localScreenshotObject);

        } catch (InvalidPathException e) {
            try {
                System.out.println("Invalid path: " + e.getMessage());

                screenshotName = "invalid screenshot name";
                final byte[] screenshot =
                        ScreenshotManager.getInstance()
                                .takeScreenshot(screenshotName);

                ScreenshotObject localScreenshotObject =
                        new ScreenshotObject(
                                screenshot,
                                "image/png",
                                screenshotName
                        );

                screenshotObjectList.get().add(localScreenshotObject);

            } catch (InvalidPathException e1) {
                System.out.println(
                        "Unable to take screenshot due to scenario name"
                );
            }

        } catch (IllegalStateException e) {
            System.out.println("Illegal State: " + e.getMessage());
        }
    }

    public static void publishFailureScreenshots() {
        for (ScreenshotObject screenshotObject :
                screenshotObjectList.get()) {

            try {
                Hooks.scenario.get().attach(
                        screenshotObject.getScreenshotValue(),
                        screenshotObject.getMediaType(),
                        screenshotObject.getScreenshotName()
                );

            } catch (InvalidPathException e) {
                System.out.println("Invalid path: " + e.getMessage());

                Hooks.scenario.get().attach(
                        screenshotObject.getScreenshotValue(),
                        screenshotObject.getMediaType(),
                        screenshotObject.getScreenshotName()
                );
            }
        }
    }

    public static void clearFailureScreenshots() {
        screenshotObjectList.get().clear();
    }

    public static void addTextToReport(
            String fieldValue,
            String fieldDescription
    ) {
        Hooks.scenario.get().attach(
                fieldValue,
                "text/plain",
                String.format(
                        "Test data '%s' field value:",
                        fieldDescription
                )
        );
    }

    public static void addTimesToTrackObject(
            String scenarioName,
            String timeTaken,
            boolean isFailed,
            Collection<String> tagNames
    ) {
        TimeTakenObject timeTakenObject1 =
                new TimeTakenObject();

        StringBuilder tagName = new StringBuilder();
        for (String str : tagNames) {
            tagName.append(str).append("---");
        }

        timeTakenObject1.setTagName(tagName.toString());
        timeTakenObject1.setScenarioName(scenarioName);
        timeTakenObject1.setTimeTaken(timeTaken);

        if (isFailed) {
            timeTakenObject1.setPassFail("FAIL");
        } else {
            timeTakenObject1.setPassFail("PASS");
        }

        timeTakenObjectList.add(timeTakenObject1);
    }


    public static void createTimeTakenFile(String fileName) {
        File reportOutputDir = new File(
                Constants.REPORTER_PATH + "cucumber"
        );

        String csvFilePath = reportOutputDir + "\\" + fileName + "TestTimes.csv";
        System.out.println("Creating file at : " + csvFilePath);

        try (FileWriter writer = new FileWriter(csvFilePath)) {
            writer.append("Tag Names,Scenario Name,Time Taken,Pass / Fail\n");

            synchronized (Hooks.class) {
                for (TimeTakenObject timeTakenObject : timeTakenObjectList) {
                    writer.append(timeTakenObject.getTagName())
                            .append(',')
                            .append(timeTakenObject.getScenarioName())
                            .append(',')
                            .append(timeTakenObject.getTimeTaken())
                            .append(',')
                            .append(timeTakenObject.getPassFail())
                            .append('\n');
                }
            }

        } catch (IOException e) {
            System.out.println("Got exception on generate file");
        }
    }

    private void addQuoteReferenceFilesToReport() {
        for (Path p : getAllFiles()) {
            byte[] data = new byte[0];

            try {
                data = Files.readAllBytes(p);
                Hooks.scenario.get().attach(
                        data,
                        "text/plain",
                        p.getFileName().toString()
                );
                Files.deleteIfExists(p);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void addFailedCommentToReport(String scenarioName) {
        if (getFailureMap().isEmpty()) return;

        StringBuilder stringBuilder = new StringBuilder();

        HashMap<String, List<String>> failureMap = getFailureMap();
        for (String step : failureMap.keySet()) {
            stringBuilder.append("**Step: ")
                    .append(step)
                    .append("\n");

            for (String str : failureMap.get(step)) {
                stringBuilder.append(str).append("\n");
            }
        }

        Hooks.scenario.get().attach(
                stringBuilder.toString(),
                "text/plain",
                "Assertion Failures"
        );
        if(Constants.SCREENSHOT_ON_ASSERTION_FAIL) {
            addFailureScreenshot(getScreenshotFailureMap());
        }
    }

    public static void addData(String description, String value) {
        addTextToReport(value, description);
    }

    private static String timeStampText() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    @BeforeAll
    public static void beforeAll() {
        timeStampedText = timeStampText();
        log.info("Setting time stamped text " + timeStampedText);
    }

    @Before
    public void setUpTest(Scenario scn) {
        Hooks.scenario.set(scn);

        if (Constants.REGRESSION_MODE) {
            Hooks.addData(
                    "Start time",
                    String.valueOf(System.currentTimeMillis())
            );
        }

        ScreenshotManager.initScreenShotManager(scn.getName());
        log.info("Execution of {} scenario started", scn.getName());

        setGlobalTimeStamp(timeStampedText);
        log.info("Set time stamp text in thread " + timeStampedText);

        startTime.set(System.currentTimeMillis());
    }

    @After
    public void afterTest(Scenario scn) {

        // do check first as otherwise the cleanup will clear the failure map
        if ((!getFailureMap().isEmpty()
                && Constants.SOFT_ASSERTIONS
                && !Constants.PASS_ON_ASSERTION_FAILURE)) {

            performCleanup(scn);
            throw new AssertionError(
                    "Test failed due to Assertion during the run. Check report for details."
            );
        } else {
            performCleanup(scn);
        }
    }

    private void performCleanup(Scenario scn) {

        long durationSeconds =
                (System.currentTimeMillis() - startTime.get()) / 1000;

        long minutes = durationSeconds / 60;
        long seconds = durationSeconds % 60;

        System.out.println(
                "Test Time: " + minutes + "m " + seconds + "s"
        );

        if (Constants.REGRESSION_MODE) {
            addTextToReport(
                    minutes + "m " + seconds + "s",
                    "Time taken"
            );

            addTimesToTrackObject(
                    scn.getName(),
                    minutes + "m " + seconds + "s",
                    scn.isFailed(),
                    scn.getSourceTagNames()
            );

            Hooks.addData(
                    "End time",
                    String.valueOf(System.currentTimeMillis())
            );
        }

        if (scn.isFailed() || Constants.SOFT_ASSERTIONS) {
            addFailedCommentToReport(scn.getName());
        }

        if (scn.isFailed()) {
            addScreenshot("failed");
        }

        if (scn.isFailed() || Constants.DEBUG_MODE) {
            publishFailureScreenshots();
            clearFailureScreenshots();
        } else {
            clearFailureScreenshots();
        }

        addQuoteReferenceFilesToReport();
        closeAllQuoteTabs();
        DriverManager.closeDriver();
        ScreenshotManager.clear();
        DataManager.clear();

        log.info("Execution of {} scenario ended", scn.getName());
    }

}