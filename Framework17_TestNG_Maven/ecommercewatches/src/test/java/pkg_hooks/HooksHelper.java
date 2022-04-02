package pkg_hooks;

import com.cucumber.listener.Reporter;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import pkg_Global.GlobalObjects;
import pkg_utility.Utilities_General;
import pkg_utility.Utility_Filehandler;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HooksHelper extends GlobalObjects {

    public WebDriver LocalDriver      = null;
    private static final Logger myLog = Logger.getLogger(HooksHelper.class);

    @Before  // before each scenario
    public void beforeEachScenario(){
        myLog.info("Log: Before every Scenario hook");
        LocalDriver       = new GlobalObjects().getDriver();
        UtilFileHandler    = new Utility_Filehandler();
        UtilGeneralHandler = new Utilities_General();
    }

    @After  // after each scenario
    public void afterEachScenarioWithScreenShot(Scenario scenario){
        myLog.info("Log: After the every Scenario hook");

        // Add screenshot only if scenario fails
        if (scenario.isFailed()) {
            myLog.info("Log: Attaching screenshot for failed scenario "+ scenario.getName());
            String sDDMMYY = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
            File src       = ((TakesScreenshot) LocalDriver).getScreenshotAs(OutputType.FILE);
            String dest    = System.getProperty("user.dir") + "//target//html//"+"screenshot_"+sDDMMYY+".jpg";
            File finalDestination = new File(dest);

            try {
                FileUtils.copyFile(src, finalDestination);
                Reporter.addScreenCaptureFromPath(finalDestination.getAbsolutePath());
            }catch (IOException eScreenshot) {
                eScreenshot.printStackTrace();
            }
        }
        LocalDriver.quit();
        LocalDriver = null;
        bBrowserInvoked = false;
        myLog.info("Log: Chrome browser is closed");
    }


}
