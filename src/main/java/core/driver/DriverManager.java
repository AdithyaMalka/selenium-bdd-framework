package core.driver;

import core.data.testdata.customGenerated.*;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.Constants;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@UtilityClass
public class DriverManager {

    private static final ThreadLocal<WebDriver> driverPool = new ThreadLocal<>();

    private static final ThreadLocal<WebDriverWait> smallWait  = new ThreadLocal<>();
    private static final ThreadLocal<WebDriverWait> mediumWait = new ThreadLocal<>();
    private static final ThreadLocal<WebDriverWait> longWait   = new ThreadLocal<>();

    private static final ThreadLocal<String> timeStamp = new ThreadLocal<>();

    private static final ThreadLocal<HashMap<String, String>> globalStringStore =
            new ThreadLocal<>();
    private static final ThreadLocal<HashMap<String, List<String>>> globalStringListStore =
            new ThreadLocal<>();
    private static final ThreadLocal<HashMap<String, Object>> globalObjectStore =
            new ThreadLocal<>();

    private static final ThreadLocal<StampRefData> stampRefData =
            new ThreadLocal<>();

    private static final ThreadLocal<HashMap<Integer, String>> generatedSubmissionMap =
            new ThreadLocal<>();
    private static final ThreadLocal<HashMap<Integer, String>> generatedPolicyMap =
            new ThreadLocal<>();
    private static final ThreadLocal<HashMap<Integer, String>> generatedAccountMap =
            new ThreadLocal<>();

    private static final ThreadLocal<HashSet<String>> policyHistoryEvents =
            new ThreadLocal<>();

    private static final ThreadLocal<GenericSubmission> genericSubmissionThreadLocal =
            new ThreadLocal<>();

    // @GuardedBy("this")
    // private static String genericSubmissionNumber;

    private static final ThreadLocal<PricingTermSubmission> pricingTermSubmissionThreadLocal =
            new ThreadLocal<>();
    private static final ThreadLocal<PremiumModifiedSubmission> premiumModifiedSubmissionThreadLocal =
            new ThreadLocal<>();
    private static final ThreadLocal<GenericAccount> genericAccountThreadLocal =
            new ThreadLocal<>();

    private static final ThreadLocal<HashMap<String, List<String>>> assertionFailureMap =
            new ThreadLocal<>();
    private static final ThreadLocal<String> currentStepName =
            new ThreadLocal<>();
    private static final ThreadLocal<List<String>> allAssertValidations =
            new ThreadLocal<>();

    public static synchronized WebDriver getDriver() {

        WebDriver driver = driverPool.get();

        if (driver == null) {
            driver = DriverFactory.createDriver();
            driverPool.set(driver);

            smallWait.set(
                    new WebDriverWait(
                            driver,
                            Duration.ofSeconds(Constants.SMALL_WAIT_TIME)
                    )
            );

            mediumWait.set(
                    new WebDriverWait(
                            driver,
                            Duration.ofSeconds(
                                    Constants.REGRESSION_MODE
                                            ? Constants.REGRESSION_WAIT_TIME
                                            : Constants.MEDIUM_WAIT_TIME
                            )
                    )
            );

            longWait.set(
                    new WebDriverWait(
                            driver,
                            Duration.ofSeconds(Constants.LONG_WAIT_TIME)
                    )
            );

            currentStepName.set("START");
            assertionFailureMap.set(new HashMap<>());
            policyHistoryEvents.set(new HashSet<>());
            allAssertValidations.set(new ArrayList<>());

            generatedSubmissionMap.set(new HashMap<>());
            generatedPolicyMap.set(new HashMap<>());

            timeStamp.set(timeStampText());
        }

        return driver;
    }

    public synchronized static void closeDriver() {
        WebDriver driver = driverPool.get();

        if (driver != null) {
            driver.quit();

            driverPool.remove();
            smallWait.remove();
            mediumWait.remove();
            longWait.remove();

            timeStamp.remove();
            globalStringStore.remove();
            globalObjectStore.remove();
            stampRefData.remove();

            genericSubmissionThreadLocal.remove();
            pricingTermSubmissionThreadLocal.remove();
            premiumModifiedSubmissionThreadLocal.remove();
            genericAccountThreadLocal.remove();

            assertionFailureMap.remove();
            policyHistoryEvents.remove();
            allAssertValidations.remove();
        }
    }

    public static WebDriverWait getSmallWait() {
        return smallWait.get();
    }

    public static WebDriverWait getMediumWait() {
        return mediumWait.get();
    }

    public static WebDriverWait getLongWait() {
        return longWait.get();
    }

    public static HashMap<String, List<String>> getFailureMap() {
        return assertionFailureMap.get();
    }

    public static HashSet<String> getPolicyHistoryEvents() {
        return policyHistoryEvents.get();
    }

    public static String getGlobalTimeStamp() {
        return timeStamp.get();
    }

    public static void setGlobalTimeStamp(String timeStampLocal) {
        timeStamp.set(timeStampLocal);
    }

    public static void initStampRefData() {
        StampRefData stampRefData1 = StampRefData.builder().build();
        stampRefData.set(stampRefData1);
    }

    public static StampRefData getStampRefData() {
        if (stampRefData.get() == null) {
            initStampRefData();
        }
        return stampRefData.get();
    }

    public static void setStampRefData(StampRefData stampRefDataLocal) {
        stampRefData.set(stampRefDataLocal);
    }

    public static void initGenericSubmissionThreadLocalData() {
        GenericSubmission genericSubmission =
                GenericSubmission.builder().build();
        genericSubmissionThreadLocal.set(genericSubmission);
    }

    // public synchronized static String getGenericSubmissionNumber() {
    //     return genericSubmissionNumber;
    // }
    //
    // public synchronized static void setGenericSubmissionNumber(String submissionNumber) {
    //     if (genericSubmissionNumber == null) {
    //         genericSubmissionNumber = submissionNumber;
    //     }
    // }
    public static GenericSubmission getGenericSubmissionThreadLocalData() {
        if (genericSubmissionThreadLocal.get() == null) {
            initGenericSubmissionThreadLocalData();
        }
        return genericSubmissionThreadLocal.get();
    }

    public static void setGenericSubmissionThreadLocalData(
            GenericSubmission genericSubmissionLocal) {
        genericSubmissionThreadLocal.set(genericSubmissionLocal);
    }
    // Pricing Term
    public static void initPricingTermSubmissionThreadLocalData() {
        PricingTermSubmission pricingTermSubmission =
                PricingTermSubmission.builder().build();
        pricingTermSubmissionThreadLocal.set(pricingTermSubmission);
    }

    public static PricingTermSubmission getPricingTermSubmissionThreadLocalData() {
        if (pricingTermSubmissionThreadLocal.get() == null) {
            initPricingTermSubmissionThreadLocalData();
        }
        return pricingTermSubmissionThreadLocal.get();
    }

    public static void setPricingTermSubmissionThreadLocalData(
            PricingTermSubmission pricingTermSubmissionLocal) {
        pricingTermSubmissionThreadLocal.set(pricingTermSubmissionLocal);
    }
    // PremiumMod
    public static void initPremiumModifiedSubmissionThreadLocalData() {
        PremiumModifiedSubmission premiumModifiedSubmission =
                PremiumModifiedSubmission.builder().build();
        premiumModifiedSubmissionThreadLocal.set(premiumModifiedSubmission);
    }

    public static PremiumModifiedSubmission
    getPremiumModifiedSubmissionThreadLocalData() {
        if (premiumModifiedSubmissionThreadLocal.get() == null) {
            initPremiumModifiedSubmissionThreadLocalData();
        }
        return premiumModifiedSubmissionThreadLocal.get();
    }

    public static void setPremiumModifiedSubmissionThreadLocalData(
            PremiumModifiedSubmission premiumModifiedSubmissionLocal) {
        premiumModifiedSubmissionThreadLocal.set(premiumModifiedSubmissionLocal);
    }
    // Account
    public static void initGenericAccountThreadLocalData() {
        GenericAccount genericAccount =
                GenericAccount.builder().build();
        genericAccountThreadLocal.set(genericAccount);
    }

    public static GenericAccount getGenericAccountThreadLocalData() {
        if (genericAccountThreadLocal.get() == null) {
            initGenericAccountThreadLocalData();
        }
        return genericAccountThreadLocal.get();
    }

    public static void setGenericAccountThreadLocalData(
            GenericAccount genericAccountLocal) {
        genericAccountThreadLocal.set(genericAccountLocal);
    }

    public static String getGlobalStringStoreValue(String key) {
        try {
            return globalStringStore.get().get(key);
        } catch (NullPointerException e) {
            System.out.println("Tried to get from empty data store");
            return "";
        }
    }

    public static List<String> getGlobalStringListStoreValue(String key) {
        try {
            return globalStringListStore.get().get(key);
        } catch (NullPointerException e) {
            System.out.println("Tried to get from empty list data store");
            return new ArrayList<>();
        }
    }

    public static void setGeneratedSubmissionMap(
            int hashCode, String submissionNumber) {

        HashMap<Integer, String> localMap;

        if (generatedSubmissionMap.get() == null) {
            localMap = new HashMap<>();
        } else {
            localMap = generatedSubmissionMap.get();
        }

        localMap.put(hashCode, submissionNumber);
        generatedSubmissionMap.set(localMap);
    }

    public static String getGeneratedSubmissionMap(int hashCode) {
        HashMap<Integer, String> localMap = generatedSubmissionMap.get();
        if (localMap == null) {
            return "";
        }
        return localMap.getOrDefault(hashCode, "");
    }
    public static void setGeneratedAccountMap(int hashCode, String accountNumber) {
        HashMap<Integer, String> localMap;

        if (generatedAccountMap.get() == null) {
            localMap = new HashMap<>();
        } else {
            localMap = generatedAccountMap.get();
        }

        localMap.put(hashCode, accountNumber);
        generatedAccountMap.set(localMap);
    }

    public static String getGeneratedAccountMap(int hashCode) {
        HashMap<Integer, String> localMap = generatedAccountMap.get();
        if (localMap == null) {
            return "";
        }

        return localMap.getOrDefault(hashCode, "");
    }
    public static void setGlobalStringStoreValue(String key, String value) {
        HashMap<String, String> localMap;

        if (globalStringStore.get() == null) {
            localMap = new HashMap<>();
        } else {
            localMap = globalStringStore.get();
        }

        localMap.put(key, value);
        globalStringStore.set(localMap);
    }
    public static void setGlobalStringListStoreValue(
            String key,
            List<String> value) {

        HashMap<String, List<String>> localMap;

        if (globalStringListStore.get() == null) {
            localMap = new HashMap<>();
        } else {
            localMap = globalStringListStore.get();
        }

        localMap.put(key, value);
        globalStringListStore.set(localMap);
    }
    public static Object getGlobalObjectStoreValue(String key) {
        return globalObjectStore.get().get(key);
    }

    public static synchronized void setGlobalObjectStoreValue(
            String key,
            Object value) {

        HashMap<String, Object> localMap;

        if (globalObjectStore.get() == null) {
            localMap = new HashMap<>();
        } else {
            localMap = globalObjectStore.get();
        }

        localMap.put(key, value);
        globalObjectStore.set(localMap);
    }
    public static void setCurrentStepName(String stepName) {
        currentStepName.set(stepName);
    }

    public static String getCurrentStepName() {
        return currentStepName.get();
    }
    public static void setAllAssertValidations(String validation) {
        List<String> allValidations = getAllAssertValidations();

        if (allValidations == null) {
            allValidations = new ArrayList<>();
        }

        allValidations.add(validation);
        allAssertValidations.set(allValidations);
    }

    public static List<String> getAllAssertValidations() {
        return allAssertValidations.get();
    }

    private static String timeStampText() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

}

