package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {

    public LoginPage() {
        super();
    }
    @FindBy(xpath ="//h1[contains(text(),'omayo (QAFox.com)')]")
    private WebElement omayoHeader;

    public void isOmayoHeaderDisplayed() {
       smallWaitForElement(omayoHeader);
        scrollToBottomOfPage();
       softAssertContains(omayoHeader.getText(), "omayo (QAFox.com)");
    }
}
