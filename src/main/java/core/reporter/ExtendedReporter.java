package core.reporter;


import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import utils.Constants;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class ExtendedReporter {

    public static void generateReport() {
        File reportOutputDir = new File(Constants.REPORTER_PATH);
        List<String> jsonFiles =
                Collections.singletonList(Constants.CUCUMBER_JSON);

        Configuration configuration =
                new Configuration(reportOutputDir, Constants.PROJECT_NAME);

        ReportBuilder reportBuilder =
                new ReportBuilder(jsonFiles, configuration);

        reportBuilder.generateReports();
    }
}