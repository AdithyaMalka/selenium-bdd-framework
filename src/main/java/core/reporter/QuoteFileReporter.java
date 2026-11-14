package core.reporter;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static utils.Constants.*;

@Slf4j
public class QuoteFileReporter {

    public static String filePath;

    public static void createNewFile(String fileName) {
        filePath = REPORTER_PATH
                + TEST_DATA_OUTPUT_PATH
                + fileName
                + TXT_EXTENSION;

        try {
            Files.createDirectories(
                    Paths.get(REPORTER_PATH + TEST_DATA_OUTPUT_PATH)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        File file = new File(filePath);
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeIntoFile(String description, String value) {

        Path path = Paths.get(filePath);

        try (BufferedWriter writer =
                     Files.newBufferedWriter(
                             path,
                             StandardOpenOption.APPEND)) {

            writer.write(description + ": " + value);
            writer.newLine();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Path> getAllFiles() {
        String filesPath =
                REPORTER_PATH + TEST_DATA_OUTPUT_PATH;

        List<Path> paths = new ArrayList<>();

        try {
            paths =
                    Files.list(Paths.get(filesPath))
                            .filter(Files::isRegularFile)
                            .collect(Collectors.toList());
        } catch (IOException e) {
            log.info("No output data have been found");
        }

        return paths;
    }
}