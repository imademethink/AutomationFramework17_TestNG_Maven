package pkg_Global;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.apache.log4j.Logger;
import java.util.HashMap;
import java.util.Map;

public class GlobalObjects {

    public static WebDriver RealDriver    = null;
    public static boolean bBrowserInvoked = false;
    public static String sSearchTermsValid = "";
    public static String sSearchTermsInValid = "";
    private static final Logger myLog = Logger.getLogger(GlobalObjects.class);

    public WebDriver getDriver(){
        if(false == bBrowserInvoked){
            String sChromeBinary=System.getProperty("user.dir") + "\\src\\test\\resources\\browserDriver\\chromedriver.exe";
            System.setProperty("webdriver.chrome.driver", sChromeBinary);
            System.setProperty("webdriver.chrome.silentOutput", "true");

            // Disable image loading - to speedup test execution
            Map<String, Object> prefs = new HashMap<String, Object>();
            prefs.put("profile.managed_default_content_settings.images", 2);

            ChromeOptions options = new ChromeOptions();
            options.addArguments("window-size=1400,800");
            options.addArguments("disable-infobars"); // disabling infobars
            options.addArguments("--disable-extensions"); // disabling extensions
            options.addArguments("--no-sandbox"); // Bypass OS security model
            options.setExperimentalOption("useAutomationExtension", false);
            options.setExperimentalOption("prefs", prefs);

            RealDriver = new ChromeDriver(options);
            myLog.info("Log: Chrome browser is launched");
            bBrowserInvoked = true;
        }
        return RealDriver;
    }

    public static String sUrlSearch   = "http://automationpractice.com/index.php";
    public static String sUrlLogin    = "http://automationpractice.com/index.php?controller=authentication&back=my-account";
    public static String sUrlRegister = "http://automationpractice.com/index.php?controller=authentication&back=my-account";
    public static String sStackOverflow= "https://stackoverflow.com/";

}
