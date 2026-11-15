package pages;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.asserts.SoftAssert;
import utils.Constants;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static core.driver.DriverManager.*;
import static utils.StaleElementRecovery.expectedStaleElementActions;

@Slf4j
public abstract class BasePage {

    protected WebDriver driver;
    protected SoftAssert softAssert = new SoftAssert();

    @FindBy(xpath = "//body[@class='js-click-trap']")
    private WebElement loadingPageSpinner;

    @FindBy(id = "gw-click-overlay")
    private WebElement gwOverlay;

    protected BasePage() {
        this.driver = getDriver();
        PageFactory.initElements(driver, this);
    }

    /* =====================================================
       DROPDOWN UTILITIES
     ===================================================== */

    protected List<String> getListOfDropdownOptions(WebElement element) {
        return new Select(element).getOptions()
                .stream().map(WebElement::getText).collect(Collectors.toList());
    }

    protected void verifyDropdownListEquals(List<String> expected, List<String> actual) {
        softAssert.assertEquals(actual, expected);
    }

    protected void verifyDropdownListNotContained(List<String> expected, List<String> actual) {
        expected.forEach(e -> softAssert.assertFalse(actual.contains(e), e + " present"));
    }

    protected void verifyListAlphabeticallyOrdered(List<String> list) {
        List<String> sorted = new ArrayList<>(list);
        Collections.sort(sorted, String.CASE_INSENSITIVE_ORDER);
        softAssert.assertEquals(list, sorted);
    }

    protected void changeDropdownSelectedOption(WebElement dropdown) {
        Select select = new Select(dropdown);
        select.selectByIndex(1);
    }

    protected void makeDropdownNotNone(WebElement dropdown) {
        Select select = new Select(dropdown);
        if (select.getFirstSelectedOption().getText().equalsIgnoreCase("none")) {
            select.selectByIndex(1);
        }
    }

    protected void setInputValue(WebElement element, String value) {
        debugLog("Setting value in element to " + value);
        expectedStaleElementActions(this, element, actions -> {
            safeClick(element);
            element.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        });
        element.sendKeys(value);
        try{
            clickOffElement(element);
        } catch (Exception e) {

        }
    }

    /* =====================================================
       WAITS & INTERACTION
     ===================================================== */

    protected void smallWaitForElement(WebElement element) {
        getSmallWait().until(d -> element.isDisplayed());
    }

    protected void smallWaitForElements(List<WebElement> elements) {
        getSmallWait().until(ExpectedConditions.visibilityOfAllElements(elements));
    }

    protected void mediumWaitForElement(WebElement element) {
        getMediumWait().until(d -> element.isDisplayed());
    }

    protected void safeClick(WebElement element) {
        try {
            element.click();
        } catch (Exception e) {
            scrollDownToElement(element);
            element.click();
        }
    }

    protected void clickOffElement(WebElement element) {
        debugLog("Clicking off Elememt");
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("arguments[0].blur();", element);
    }

    protected void clickOffsetElement(WebElement element) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", element);
    }

    protected void pressTabKey() {
        new Actions(driver).sendKeys(Keys.TAB).perform();
    }

    protected void zoomInPage() {
        ((JavascriptExecutor) driver).executeScript("document.body.style.zoom='110%'");
    }

    protected void zoomOutPage() {
        ((JavascriptExecutor) driver).executeScript("document.body.style.zoom='90%'");
    }

    /* =====================================================
       TABLE UTILITIES
     ===================================================== */

    protected List<WebElement> getAllRows(WebElement table) {
        return table.findElements(By.tagName("tr"));
    }

    protected int getNumberOfColumns(WebElement table) {
        return table.findElements(By.tagName("th")).size();
    }

    protected void tableClickCellContains(WebElement table, String text) {
        table.findElements(By.xpath(".//td[contains(text(),'" + text + "')]"))
                .get(0).click();
    }

    protected Map<String, String> tablePopulateRowMap(WebElement table, String rowText) {
        Map<String, String> map = new LinkedHashMap<>();
        List<WebElement> headers = table.findElements(By.tagName("th"));
        WebElement row = table.findElement(By.xpath(".//tr[td[contains(text(),'" + rowText + "')]]"));
        List<WebElement> cells = row.findElements(By.tagName("td"));
        for (int i = 0; i < headers.size(); i++) {
            map.put(headers.get(i).getText(), cells.get(i).getText());
        }
        return map;
    }

    /* =====================================================
       TEXT & ELEMENT VALIDATIONS
     ===================================================== */

    protected boolean verifyTextNotPresentOnPage(String text) {
        return driver.findElements(By.xpath("//*[contains(text(),'" + text + "')]")).isEmpty();
    }

    protected boolean webElementTextExists(String text) {
        return !driver.findElements(By.xpath("//*[text()='" + text + "']")).isEmpty();
    }

    protected boolean isCheckBoxChecked(WebElement element) {
        return element.isSelected();
    }

    protected boolean isElementReadOnly(WebElement element) {
        return Boolean.parseBoolean(element.getAttribute("readonly"));
    }

    /* =====================================================
       NUMBER EXTRACTION
     ===================================================== */

    protected int extractIntFromText(String text) {
        return Integer.parseInt(text.replaceAll("\\D", ""));
    }

    protected double extractDoubleFromText(String text) {
        return Double.parseDouble(text.replaceAll("[^0-9.]", ""));
    }

    protected BigDecimal extractBigDecimalFromText(String text) {
        return new BigDecimal(text.replaceAll("[^0-9.]", ""));
    }

    protected int determineNumberOfDecimalPlaces(String text) {
        if (!text.contains(".")) return 0;
        return text.length() - text.indexOf('.') - 1;
    }

    protected boolean validateDecimalPlacesOnText(String text, int places) {
        return determineNumberOfDecimalPlaces(text) == places;
    }

    /* =====================================================
       ASSERTIONS (SOFT)
     ===================================================== */

    protected void softAssertTrue(boolean value, String name) {
        softAssert.assertTrue(value, name);
    }

    protected void softAssertEquals(String actual, String expected) {
        softAssert.assertEquals(actual, expected);
    }

    protected void softAssertEquals(BigDecimal actual, BigDecimal expected) {
        softAssert.assertEquals(actual, expected);
    }

    protected void softAssertContains(String actual, String expected) {
        softAssert.assertTrue(actual.contains(expected));
    }

    protected void softAssertRegex(String text, String regex) {
        softAssert.assertTrue(Pattern.matches(regex, text));
    }

    protected void assertAll() {
        softAssert.assertAll();
    }

    /* =====================================================
       MISC
     ===================================================== */

    protected void scrollDownToElement(WebElement element) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);", element);
    }

    protected void scrollToBottomOfPage() {
        ((JavascriptExecutor) driver)
                .executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    protected void hardSleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException ignored) {
        }
    }

    protected void acceptPopup() {
        try {
            driver.switchTo().alert().accept();
        } catch (NoAlertPresentException ignored) {
        }
    }

    protected void dismissPopup() {
        try {
            driver.switchTo().alert().dismiss();
        } catch (NoAlertPresentException ignored) {
        }
    }

    protected void waitForLoadingOver() {
        try {
            if(loadingPageSpinner.isDisplayed()) {
                getMediumWait().until(ExpectedConditions.invisibilityOf(loadingPageSpinner));
            }
        } catch (TimeoutException | NoSuchElementException exception) {
            debugLog("loading spinner is not displayed first");
        }
        try {
            if(gwOverlay.isDisplayed()) {
                getMediumWait().until(ExpectedConditions.invisibilityOf(gwOverlay));
            }
        } catch (TimeoutException | NoSuchElementException exception) {
            debugLog("loading spinner is not displayed second");
        }
    }

    protected void debugLog(String message) {
        if(Constants.DEBUG_MODE) {
            log.info(message);
        }
    }

    public String getCurrentTab() {
        return getDriver().getWindowHandle();
    }

    protected void switchTabFrom(String currentTab) {
        for(String childWindow : getDriver().getWindowHandles()) {
            if(!childWindow.equals(currentTab)) {
                getDriver().switchTo().window(childWindow);
            }
        }
    }

    public void openNewTab() {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("window.open()");
        waitForLoadingOver();
        switchToNewTab();
        waitForLoadingOver();
    }

    public void switchToNewTab() {
        String currentTab = getCurrentTab();
        switchTabFrom(currentTab);
    }

}