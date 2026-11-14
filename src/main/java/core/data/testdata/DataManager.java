package core.data.testdata;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import core.data.envdata.EnvConfiguration;
import core.data.testdata.model.DataModel;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

import static utils.Constants.CORE_DATA_PATH;
import static utils.Constants.DATA_PATH;

@Slf4j
public class DataManager {

    private static final ThreadLocal<DataManager> THREAD_LOCAL =
            new ThreadLocal<>();

    private final ThreadLocal<JsonNode> dataNode =
            new ThreadLocal<>();

    private DataManager(String client, String subFolder, String filename) {

        String corePath =
                String.format(CORE_DATA_PATH, "-core_");

        String dataPath =
                String.format(
                        DATA_PATH,
                        EnvConfiguration.get().getEnv(),
                        client,
                        subFolder,
                        filename
                );

        JsonNode coreJson =
                loadAndCombineJsonFilesFromFolder(corePath);

        JsonNode dataJson =
                loadJsonFile(dataPath);

        JsonNode mergedJson =
                mergeJsonNodeOverwriteBase(coreJson, dataJson);

        dataNode.set(mergedJson);
    }

    public static void loadTestData(
            String client,
            String subFolder,
            String filename
    ) {
        THREAD_LOCAL.set(
                new DataManager(client, subFolder, filename)
        );
    }

    public static DataManager getInstance() {
        return THREAD_LOCAL.get();
    }

    public static void clear() {
        THREAD_LOCAL.remove();
    }

    private static JsonNode loadJsonFile(String path) {
        try {
            return new ObjectMapper().readTree(new File(path));
        } catch (IOException e) {
            log.error("Error loading JSON file: " + path, e);
            return null;
        }
    }

    private static JsonNode loadAndCombineJsonFilesFromFolder(String folderPath) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode combined = mapper.createObjectNode();
        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));
            if (files != null) {
                for (File file : files) {
                    try {
                        JsonNode node = mapper.readTree(file);
                        combined = mergeJsonNodeOverwriteBase(combined, node);
                    } catch (IOException e) {
                        log.error("Error loading JSON file: " + file.getPath(), e);
                    }
                }
            }
        }
        return combined;
    }

    private static JsonNode mergeJsonNodeOverwriteBase(JsonNode base, JsonNode overlay) {
        if (overlay.isObject()) {
            ObjectNode result = (ObjectNode) base.deepCopy();
            overlay.fields().forEachRemaining(entry -> {
                String key = entry.getKey();
                JsonNode value = entry.getValue();
                if (value.isObject() && base.has(key) && base.get(key).isObject()) {
                    result.set(key, mergeJsonNodeOverwriteBase(base.get(key), value));
                } else {
                    result.set(key, value);
                }
            });
            return result;
        } else {
            return overlay;
        }
    }

    private static <T> T getTestData(JsonNode node, Class<T> clazz) {
        try {
            return new ObjectMapper().treeToValue(node, clazz);
        } catch (Exception e) {
            log.error("Error converting JsonNode to " + clazz.getSimpleName(), e);
            return null;
        }
    }

    public DataModel getTestingData() {
        return getTestData(this.dataNode.get(), DataModel.class);
    }
}