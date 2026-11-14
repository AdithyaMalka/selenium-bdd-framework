package core.data.envdata;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import utils.ConfigParser;

import static utils.Constants.ENV_CONFIG_PATH;
@Slf4j
public class EnvConfiguration {

    private static volatile EnvConfiguration instance;

    private final JsonNode propertyNode;
    @Getter
    private final String env;

    private EnvConfiguration() {
        this.env = ConfigParser.getEnvironmentName();
        this.propertyNode =
                ConfigParser.loadJsonFile(getConfigFileLocation(env));
    }

    public static EnvConfiguration get() {
        EnvConfiguration localInstance = instance;

        if (localInstance == null) {
            synchronized (EnvConfiguration.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new EnvConfiguration();
                }
            }
        }
        return localInstance;
    }

    private String getConfigFileLocation(String env) {
        return String.format(ENV_CONFIG_PATH, env);
    }

    private String getProperty(String propertyName) {
        return ConfigParser.getProperty(this.propertyNode, propertyName);
    }

    public String getWorkstationUrl() {
        return getProperty("workstationUrl");
    }

    public String getClaimCenterUrl() {
        return getProperty("claim-center");
    }

    public String getClaimCenter1Url() {
        return getProperty("claim-center-1");
    }

    public String getBillingCenterUrl() {
        return getProperty("billing-center");
    }

    public String getBillingCenterUrl1() {
        return getProperty("billing-center-1");
    }

    public String getContactManagerUrl1() {
        return getProperty("contact-manager-1");
    }

    public String getPolicyCenterUrl() {
        return getProperty("policy-center");
    }

    public String getPolicyCenter1Url() {
        return getProperty("policy-center-1");
    }

    public String getPolicyCenterTTUrl() {
        return getProperty("policy-center-tt");
    }

    public String getBillingCenterTTUrl() {
        return getProperty("billing-center-tt");
    }

    public String getContactManagerUrl() {
        return getProperty("contact-manager");
    }

    public String getPolicyCenterSmokeUrl() {
        return getProperty("policy-center-smoke");
    }

    public String getClaimCenterSmokeUrl() {
        return getProperty("claim-center-smoke");
    }

    public String getBillingCenterSmokeUrl() {
        return getProperty("billing-center-smoke");
    }

    public String getContactManagerSmokeUrl() {
        return getProperty("contact-manager-smoke");
    }

    public String getUserId() {
        return ConfigParser.getUserId();
    }

    public String getUserPassword() {
        return ConfigParser.getUserPassword();
    }

    public String getClaimCenter() {
        return getProperty("ClaimCenter");
    }

    public String getSubmission() {
        return getProperty("submission");
    }

    public String getOmayoBlogspotUrl() {
        return getProperty("omayo-blogspot");
    }

}
