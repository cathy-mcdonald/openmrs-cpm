package org.openmrs.module.conceptpropose.functionaltest.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openmrs.module.conceptpropose.pagemodel.AdminPage;
import org.openmrs.module.conceptpropose.pagemodel.CreateProposalPage;
import org.openmrs.module.conceptpropose.pagemodel.EditProposalPage;
import org.openmrs.module.conceptpropose.pagemodel.MonitorProposalsPage;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class ProposalStepDefs {
    private CreateProposalPage createProposalPage;
    private MonitorProposalsPage monitorProposalsPage;
    private AdminPage adminPage;
    private EditProposalPage editProposalPage;

    @Given("^I have a saved draft proposal$")
    public void navigate_to_page() throws IOException, InterruptedException {
        loadProposalMonitorPage();

        editProposalPage = monitorProposalsPage.navigateToEditLastProposalLink();
        assert(editProposalPage.showsDraftProposal());
    }

    @When("^I submit the proposal$")
    public void submit_proposal(){

    }

    @Then("^the proposal is sent to the dictionary manager$")
    public void check_the_dictionary_manager() throws InterruptedException{
        Thread.sleep(5000);

    }

    @Given("^I have a new proposal with all necessary details$")
     public void fill_a_new_proposal() throws IOException {
        loadCreateProposalPage();
        createProposalPage.enterNewProposal("Some Name","email@example.com","Some Comments");
    }

    @When("^I save$")
    public void save_new_proposal() throws InterruptedException{
        createProposalPage.saveNewProposal();
        Thread.sleep(500);
    }

    @Then("^the proposal is stored with the details$")
    public void check_the_details() throws InterruptedException, IOException {
        loadProposalMonitorPage();
        assertThat(monitorProposalsPage.getLastDraftProposalName(), equalTo("Some Name"));
        assertThat(monitorProposalsPage.getLastDraftProposalDescription(), equalTo("Some Comments"));
    }

    @When("^I change the details and save$")
    public void change_details_and_save_proposal() throws InterruptedException {
        editProposalPage.enterNewName("New Name");
        editProposalPage.saveChanges();
        Thread.sleep(500);
    }

    @Then("^the proposal is stored with the new details$")
    public void check_the_new_details() throws InterruptedException, IOException {
        loadProposalMonitorPage();
        assertThat(monitorProposalsPage.getLastDraftProposalName(), equalTo("New Name"));
    }

    public void loadAdminPage() throws IOException {
        Login login = new Login();
        adminPage = login.login();
    }

    private void loadCreateProposalPage() throws IOException {
        if (adminPage == null)
            loadAdminPage();
        createProposalPage= adminPage.navigateToCreateProposalPage();
    }

    private void loadProposalMonitorPage() throws IOException {
        if (adminPage == null)
            loadAdminPage();
        monitorProposalsPage = adminPage.navigateToMonitorProposals();
    }


}
