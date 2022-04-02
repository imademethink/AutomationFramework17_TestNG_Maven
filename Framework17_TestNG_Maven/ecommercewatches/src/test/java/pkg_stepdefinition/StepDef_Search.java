package pkg_stepdefinition;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import pkg_Global.GlobalObjects;

import java.util.concurrent.TimeUnit;

public class StepDef_Search extends GlobalObjects {

    private static final Logger myLog = Logger.getLogger(StepDef_Search.class);
    public WebDriver LocalDriver      = null;

    @When("^User attempts to search for \"(.*?)\" item$")
    public void User_attempts_to_search_for_item(String sSearchType){
        myLog.info("Log: Navigating to " + sUrlSearch);
        LocalDriver = new GlobalObjects().getDriver();
        LocalDriver.get(sUrlSearch);
        LocalDriver.manage().timeouts().implicitlyWait(8000, TimeUnit.MILLISECONDS);

        myLog.info("Log: Searching for " + sSearchType + " items");
        String sSearchItem = "dress";
        if(sSearchType.toLowerCase().contains("invalid")){
            sSearchItem = "candle";
        }
        LocalDriver.findElement(By.id("search_query_top")).sendKeys(sSearchItem);
        LocalDriver.findElement(By.name("submit_search")).click();
        LocalDriver.manage().timeouts().implicitlyWait(10000, TimeUnit.MILLISECONDS);
    }

    @Then("^Successful search results \"(.*?)\" shown")
    public void Successful_search_results_shown(String sExpectedSearchResult){
        String sResultCount = LocalDriver.findElement(By.cssSelector("span[class='heading-counter']")).getText();

        if(sExpectedSearchResult.contains("should NOT be") &&
           sResultCount.contains("0 results have been found"))
        {
            myLog.info("Log: Item search criteria met");
            return;
        }


        if(sExpectedSearchResult.contains("should be") &&
           sResultCount.contains("7 results have been found"))
        {
            myLog.info("Log: Item search criteria met");
            return;
        }

        Assert.fail("Log: Item search criteria NOT met");
    }
}