package pkg_testrunner;

import com.cucumber.listener.Reporter;
import cucumber.api.CucumberOptions;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import cucumber.api.testng.AbstractTestNGCucumberTests;
import java.io.File;

@CucumberOptions(
    // this is must for rerun.txt to have complete path of failed feature files
    features 	= {"."},
    glue 	 	= {"pkg_stepdefinition","pkg_hooks"},
    plugin 		= {
            "html:target2/cucumber",
            "json:target2/cucumber/cucumber.json",
            "com.cucumber.listener.ExtentCucumberFormatter:target2/html/extent_report.html"
    },
//    tags        = {"@login"},
    tags        = {"@search"},
//    tags        = {"@dummy"},
    dryRun 	 	= false,
    strict 	 	= false,
    monochrome  = true
)

public class TestRunnerFramework_SIT extends AbstractTestNGCucumberTests {
    private static final Logger myLog = Logger.getLogger(TestRunnerFramework_SIT.class);

    @BeforeClass
    public static void setupBeforeClass() {
        // empty
    }

    @AfterClass
    public static void setupAfterClass() {
        myLog.info("Generating Extent Report");
        Reporter.loadXMLConfig(new File(System.getProperty("user.dir") + "/src/test/resources/extent-config.xml"));
        Reporter.setSystemInfo("Test User", "Tony Stark");
        Reporter.setSystemInfo("Operating System Type", "Windows");
        Reporter.setSystemInfo("Build version", "v 1.2.3");
        Reporter.setTestRunnerOutput("Extent Report for Regression");
    }



}


// Maven profiles (with maven-surefire-plugin version 2.18 onwards)
//      Note the id tag in pom_2.xml
//      To run specific profile use -P <prifile_name> option e.g.
//          mvn clean test -P UAT_PROFILE
//      In current example use         mvn clean test -f pom_2.xml -P UAT_PROFILE
//      Checkout the report in target_profile2 folder

//  Note that - Extent report captures every rerun and logs the result for every re-run

//  Annotations - hierarchy is - Suite, Test, Class, Method
//      - BeforeSuite/AfterSuite - Basic activity related to suite mentioned in testng
//                                 Reading data input files e.g. csv, excel
//      - BeforeClass/AfterClass - Extent Reporting
//      - BeforeTest/AfterTest   - e.g. clear db settings etc



