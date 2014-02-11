package org.openmrs.module.conceptpropose.pagemodel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class EditProposalPage extends BaseCpmPage {
    private By nameSelector = By.name("name");
    public EditProposalPage(WebDriver driver) {
        super(driver);
    }

    public boolean showsDraftProposal() {
        defaultWait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver input) {
                return input.findElements(nameSelector).size() > 0;
            }
        });

        java.util.List<WebElement> paragraphList = driver.findElements(By.tagName("p"));
        for (WebElement paragraph: paragraphList) {
            if (paragraph.getText().contains("Status: Draft"))
                return true;
        }

        return false;
    }

    public void enterNewName(String someName) {
        defaultWait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver input) {
                return input.findElements(nameSelector).size() > 0;
            }
        });
        final WebElement nameElement = driver.findElement(nameSelector);
        nameElement.clear();
        nameElement.sendKeys(someName);
    }

    public void saveChanges() {
        java.util.List<WebElement> buttonList = driver.findElements(By.tagName("Button"));

        WebElement saveButton = null;
        for (WebElement button : buttonList) {
            if(button.getText().equals("Save Draft"))
                saveButton = button;
        }

        saveButton.click();
    }
}
