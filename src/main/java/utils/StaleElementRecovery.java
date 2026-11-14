package utils;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.function.Consumer;

import static core.driver.DriverManager.getDriver;


@Slf4j
@UtilityClass
public class StaleElementRecovery {

    public static void expectedStaleElementActions(
            Object page,
            WebElement element,
            Consumer<WebElement> actions
    ) {
        int attempts = 0;

        while (attempts < 3) {
            try {
                actions.accept(element);
                System.out.println("SUCCEEDED WITH " + attempts + " ATTEMPTS");
                break;
            } catch (StaleElementReferenceException e) {
                System.out.println("RE-INIT WEB ELEMENTS");
                PageFactory.initElements(getDriver(), page);
                attempts++;
            }
        }
    }

    public static void expectedStaleElementsActions(
            Object page,
            List<WebElement> elements,
            Consumer<WebElement> actions
    ) {
        int attempts = 0;

        while (attempts < 3) {
            try {
                for (WebElement element : elements) {
                    actions.accept(element);
                }
                System.out.println("SUCCEEDED WITH " + attempts + " ATTEMPTS");
                break;
            } catch (StaleElementReferenceException e) {
                System.out.println("RE-INIT WEB ELEMENTS");
                PageFactory.initElements(getDriver(), page);
                attempts++;
            }
        }
    }
}