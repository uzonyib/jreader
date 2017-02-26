package jreader.test.acceptance.page.element;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jreader.test.acceptance.util.Constants;

@Component
public class BackupForm {

    private static final String JSON_FIELD_XPATH = "//textarea[@id='export-import']";

    private WebDriver browser;

    @FindBy(xpath = "//form/button[@title='Export']")
    private WebElement exportButton;

    @FindBy(xpath = "//form/button[@title='Import']")
    private WebElement importButton;

    @FindBy(xpath = JSON_FIELD_XPATH)
    private WebElement jsonField;

    @Autowired
    public BackupForm(WebDriver browser) {
        this.browser = browser;
        PageFactory.initElements(browser, this);
    }

    public void export() {
        exportButton.click();
        new WebDriverWait(browser, Constants.WAIT_TIMEOUT).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return jsonField.getAttribute("value").length() > 0;
            }
        });
    }

    public String getJson() {
        return jsonField.getAttribute("value");
    }

    public void enterJson(String json) {
        jsonField.clear();
        jsonField.sendKeys(json);
        new WebDriverWait(browser, Constants.WAIT_TIMEOUT).until(ExpectedConditions.textToBePresentInElementValue(jsonField, json));
    }

    public void importJson() {
        importButton.click();
    }

}
