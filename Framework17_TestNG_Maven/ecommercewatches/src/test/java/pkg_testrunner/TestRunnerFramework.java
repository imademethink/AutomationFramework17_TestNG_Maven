package pkg_testrunner;
import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import pkg_utility.Utilities_General;
import pkg_utility.Utility_Filehandler;

@CucumberOptions(
    features 	= {"."},
    glue 	 	= {"pkg_stepdefinition","pkg_hooks"},
    plugin 		= {
            "html:target/cucumber",
            "json:target/cucumber/cucumber.json",
            "com.cucumber.listener.ExtentCucumberFormatter:target/html/extent_report.html"
    },
//    tags        = {"@login"},
    tags        = {"@login_valid"},
//    tags        = {"@dummy"},
    dryRun 	 	= false,
    strict 	 	= false,
    monochrome  = true
)

public class TestRunnerFramework extends AbstractTestNGCucumberTests {
    private static final Logger myLog = Logger.getLogger(TestRunnerFramework.class);

    @BeforeClass
    public static void setupBeforeClass() {
        //GlobalInit
        new Utilities_General().GetCommandLineParam();

        new Utility_Filehandler().ExcelDataReaderInit();

        new Utility_Filehandler().PropertiesDataReaderInit();

        new Utility_Filehandler().CsvDataReaderInit();

        new Utilities_General().DbConnectionInit();

        // Making required environment web-app up and running - optional step

        // Launching web browser - To be done before cucumber every scenario
    }

    @AfterClass
    public static void teardownAfterClass() {
        //GlobalTearDown
        // Shutting down web browser - To be done after cucumber every scenario

        // Making required environment web-app down - optional step

        new Utilities_General().DbConnectionClose();

        new Utility_Filehandler().ExcelDataReaderClose();

        new Utilities_General().GenerateReport();

        // Extra step for JVM shutdown
        Runtime runtime = Runtime.getRuntime();
        // Java shutdown hook are handy to run some code when program exit
        // e.g. clean up resources, send reports etc
        runtime.addShutdownHook(new Thread(){
                                    public void run(){
                                        try {new Utilities_General().SendReportViaEmail();}
                                        catch (Exception e) { e.printStackTrace();}
                                    }
                                }
        );
        try{Thread.sleep(5000);}catch (Exception e){}
    }
}



//  Helping hands - additional activities - value addition
//    - BeforeClass/AfterClass
//    - GetCommandLineParam, ExcelDataReaderInit, PropertiesDataReaderInit, CsvDataReaderInit
//    - Database connection and handling queries
//    - Docker up/ down
//    - Sending HTML test report using email
//      As attachment

//  Logging using org.apache.log4j (others are 1) SLF4J, 2) Built in java.util.logging package)
//      Why not SYSOUT? - to have a permanent place of logs stored for further analysis
//                      - also useful in multi threaded application logging
//      need this file at specific location src/test/resources/log4j.properties
//      log level     debug < info < warn < error < fatal
//
//  Full set of steps to be done BeforeClass/ AfterClass
//

//  Page Object Model concept
//      Object Creation
//         - At the time of init
//         - Need basis



