package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableSet;
import core.driver.Browser;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.nio.file.Files.newInputStream;

@Slf4j
@UtilityClass
public class ConfigParser {

    private static final Set<String> ENVS =
            ImmutableSet.of(Constants.MQ_ENV);

    private static final Set<String> BROWSERS =
            ImmutableSet.of(
                    Browser.CHROME.browserName(),
                    Browser.EDGE.browserName()
            );

    public static JsonNode loadJsonFile(String configPath) {
        try {
            InputStream input = newInputStream(Paths.get(configPath));
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(input);
        } catch (IOException e) {
            // log.error("Error loading file: {}", configPath, e);
            throw new RuntimeException(e);
        }
    }

    public static JsonNode loadAndCombineJsonFilesFromFolder(String basePath) {
        JsonNode baseNode = null;
        File baseFolder = new File(basePath);
        File[] listOfFiles = baseFolder.listFiles();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (!file.isFile()) continue;

                JsonNode localNode = loadJsonFile(file.getPath());
                baseNode = mergeJsonNodeOverwriteBase(baseNode, localNode);
            }
        }

        return baseNode;
    }

    public static JsonNode mergeJsonNodeOverwriteBase(
            JsonNode baseNode,
            JsonNode updateNode
    ) {
        if (baseNode == null || baseNode.isEmpty()) {
            return updateNode;
        }

        if (updateNode == null || updateNode.isEmpty()) {
            return baseNode;
        }

        ObjectNode outputNode = baseNode.deepCopy();
        ObjectNode updateUseNode = updateNode.deepCopy();

        Set<String> topLevelUpdateSet = new HashSet<>();
        Set<String> topLevelBaseSet = new HashSet<>();

        updateUseNode.fields().forEachRemaining(
                entry -> topLevelUpdateSet.add(entry.getKey())
        );

        baseNode.fields().forEachRemaining(
                entry -> topLevelBaseSet.add(entry.getKey())
        );

        HashMap<String, JsonNode> updatedValues = new HashMap<>();
        HashMap<String, JsonNode> replaceValues = new HashMap<>();

        for (String entry : topLevelUpdateSet) {
            if (topLevelBaseSet.contains(entry)) {

                ObjectNode internalNode =
                        (ObjectNode) baseNode.get(entry);

                updateUseNode.get(entry).fields()
                        .forEachRemaining(
                                internalEntry ->
                                        internalNode.set(
                                                internalEntry.getKey(),
                                                internalEntry.getValue()
                                        )
                        );

                outputNode.set(entry, internalNode);
                replaceValues.put(entry, updateUseNode.get(entry));

            } else {
                updatedValues.put(entry, updateUseNode.get(entry));
            }
        }

        for (String str : updatedValues.keySet()) {
            outputNode.set(str, updatedValues.get(str));
        }

        return outputNode;
    }

    public static <T> T getTestData(
            JsonNode dataNode,
            Class<T> classRef
    ) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.treeToValue(dataNode, classRef);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getProperty(JsonNode propertyNode, String propertyName) {
        return Optional.ofNullable(propertyNode.findValue(propertyName))
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                String.format(
                                        "Property with '%s' name not found",
                                        propertyName
                                )
                        )
                )
                .textValue();
    }

    public static String getEnvironmentName() {
        String env = System.getProperty(
                Constants.ENV_VARIABLE,
                Constants.MQ_ENV
        );

        if (ENVS.contains(env)) {
            // log.info("Using environment: {}", env);
        } else {
            // log.info(
            //     "Unknown environment: {} provided, using default one: {}",
            //     env,
            //     Constants.HQ_ENV
            // );
        }

        return env;
    }

    public static String getBrowserType() {
        String browserType =
                System.getProperty(
                        Constants.BROWSER_TYPE_VARIABLE,
                        Browser.CHROME.browserName()
                );

        if (BROWSERS.contains(browserType)) {
            log.info("Using browser: {}", browserType);
            return browserType;
        } else {
            log.info(
                    "Unknown browser type: {} provided, using default one: {}",
                    browserType,
                    Browser.CHROME.browserName()
            );
        }

        return browserType;
    }

    public static String getUserId() {
        String userId =
                System.getProperty(Constants.USER_ID_VARIABLE);

        if (userId == null || userId.isEmpty() || userId.isBlank()) {
            throw new InvalidParameterException("User Id is not provided");
        }

        return userId;
    }

    public static String getUserPassword() {
        String password =
                System.getProperty(Constants.PASSWORD_VARIABLE);

        if (password == null || password.isEmpty() || password.isBlank()) {
            throw new InvalidParameterException("Password is not provided");
        }

        return password;
    }
}