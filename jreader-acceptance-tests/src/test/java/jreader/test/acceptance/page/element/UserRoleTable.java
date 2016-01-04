package jreader.test.acceptance.page.element;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class UserRoleTable {
    
    @FindBy(id = "user-role")
    private WebElement table;
    
    public UserRoleTable(WebDriver browser) {
        PageFactory.initElements(browser, this);
    }
    
    public boolean isDisplayed() {
        return table.isDisplayed();
    }
    
    private WebElement findRow(String username) {
        return table.findElement(By.xpath("tbody/tr[td[contains(@class, 'username') and .='" + username + "']]"));
    }
    
    public boolean containsUsername(String username) {
        return findRow(username) != null;
    }
    
    public String getRole(String username) {
        return findRow(username).findElement(By.xpath("td[contains(@class, 'role')]")).getText();
    }
    
    public WebElement findForm(String username, String targetRole) {
        return findRow(username).findElement(By.xpath("td[contains(@class, 'action')]/form[contains(@class, '" + targetRole + "')]"));
    }
    
    public boolean containsForm(String username, String targetRole) {
        return findForm(username, targetRole) != null;
    }
    
    public void changeRole(String username, String role) {
        findForm(username, role).findElement(By.xpath("input[@type='submit']")).click();
    }

}
