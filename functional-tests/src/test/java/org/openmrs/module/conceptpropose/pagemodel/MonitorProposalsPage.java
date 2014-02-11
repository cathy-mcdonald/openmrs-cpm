package org.openmrs.module.conceptpropose.pagemodel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.List;

public class MonitorProposalsPage extends BaseCpmPage {

    private By resultsSelector = By.className("results");

    public MonitorProposalsPage(WebDriver driver) {
        super(driver);
    }

    public String getLastDraftProposalName() throws InterruptedException {
        return getLastDraftProposalRow().findElements(By.className("ng-binding")).get(0).getText();
    }

    public String getLastDraftProposalDescription() throws InterruptedException {
         return getLastDraftProposalRow().findElements(By.className("ng-binding")).get(1).getText();
    }

    public EditProposalPage navigateToEditLastProposalLink() throws InterruptedException {
        getLastDraftProposalRow().click();
        return new EditProposalPage(driver);
    }

    // Returns row for proposal closest to bottom of list with "Draft" in Status column
    private WebElement getLastDraftProposalRow() throws InterruptedException {
        defaultWait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver input) {
                return input.findElements(resultsSelector).size() > 0;
            }
        });
        Thread.sleep(100);

        List <WebElement> resultRowsElement = driver.findElements(By.className("ng-scope"));
        WebElement lastDraftProposalRow = null;
        for (int i = resultRowsElement.size()-1; (i >= 0) && (lastDraftProposalRow == null); i--) {
            WebElement resultRow =  resultRowsElement.get(i);
            if (resultRow.findElements(By.className("ng-binding")).get(3).getText().equals("Draft"))
                lastDraftProposalRow = resultRow;
        }

        return lastDraftProposalRow;
    }
}
