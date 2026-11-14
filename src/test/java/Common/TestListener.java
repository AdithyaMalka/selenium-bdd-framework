package Common;

import core.reporter.ExtendedReporter;
import lombok.extern.slf4j.Slf4j;
import org.testng.ITestContext;
import org.testng.ITestListener;
import utils.Constants;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static Common.Hooks.createTimeTakenFile;

@Slf4j
public class TestListener implements ITestListener {

    @Override
    public void onFinish(ITestContext context) {
        log.info("Execution of tests ended, generating report");
        ExtendedReporter.generateReport();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy_HH-mm");
        String formattedNow = now.format(formatter);

        String reportFileName = Constants.TEST_TAG + "_" + formattedNow;
        String backupFileName = "testRun" + "_" + formattedNow;

        try {
            renameReport(reportFileName);
        } catch (Exception e) {
            try {
                renameReport(backupFileName);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        createTimeTakenFile(reportFileName);
    }

    public void renameReport(String reportFileName) throws IOException {

        System.out.println("Test tag: " + Constants.TEST_TAG);

        String defaultReportPathHtml =
                Constants.REPORTER_PATH + "cucumber\\cucumber.html";
        String newReportPathHtml =
                Constants.REPORTER_PATH + "cucumber\\" + reportFileName + ".html";

        Files.move(
                Paths.get(defaultReportPathHtml),
                Paths.get(newReportPathHtml),
                StandardCopyOption.REPLACE_EXISTING
        );

        try {
            String defaultReportPathJson =
                    Constants.REPORTER_PATH + "cucumber\\cucumber.json";
            String newReportPathJson =
                    Constants.REPORTER_PATH + "cucumber\\" + reportFileName + ".json";

            Files.move(
                    Paths.get(defaultReportPathJson),
                    Paths.get(newReportPathJson),
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (Exception e) {
            try {
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter =
                        DateTimeFormatter.ofPattern("dd-MM-yy_HH-mm");
                String formattedNow = now.format(formatter);

                String defaultReportPathJson =
                        Constants.REPORTER_PATH + "cucumber\\cucumber.json";
                String newReportPathJson =
                        Constants.REPORTER_PATH + "cucumber\\" + "testRun"
                                + formattedNow + ".json";

                Files.move(
                        Paths.get(defaultReportPathJson),
                        Paths.get(newReportPathJson),
                        StandardCopyOption.REPLACE_EXISTING
                );
            } catch (Exception f) {
                // ignored
            }
        }
    }
}