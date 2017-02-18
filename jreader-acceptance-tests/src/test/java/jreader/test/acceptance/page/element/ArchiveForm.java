package jreader.test.acceptance.page.element;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ArchiveForm {

    @FindBy(css = "#archive-form input[name='title']")
    private WebElement titleField;

    @FindBy(css = "#archive-form button[type='submit']")
    private WebElement createButton;

    @Autowired
    public ArchiveForm(WebDriver browser) {
        PageFactory.initElements(browser, this);
    }

    public void enterTitle(String title) {
        titleField.clear();
        titleField.sendKeys(title);
    }

    public void clickCreateButton() {
        createButton.click();
    }

    public WebElement getTitleField() {
        return titleField;
    }

    public WebElement getCreateButton() {
        return createButton;
    }

}
