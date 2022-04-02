package pkg_Global;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pkg_utility.Utilities_General;
import pkg_utility.Utility_Filehandler;
import java.io.FileInputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class GlobalObjects {

    // Logger
    private static final Logger myLog = Logger.getLogger(GlobalObjects.class);

    // Browser instance
    public static WebDriver RealDriver                    = null;
    public static boolean bBrowserInvoked                 = false;

    // A hash-map of all data items
    public static HashMap<String, String> hmGlobalData    = new HashMap<>();

    // User data read from Excel table
    public static FileInputStream exlInStream             = null;
    public static XSSFWorkbook exlWorkBook                = null;
    public static XSSFSheet exlWorkSheet                  = null;
    public static final String sSheetName                = "user_data";
    public static final String sUserDataExcelPath        =
            System.getProperty("user.dir") + "\\src\\test\\resources\\externalData\\UserInfo.xlsx";
    public static HashMap<String, Object> hmUserData = new HashMap<>();

    // Properties file where basic 1 time properties stored
    public static final String sAutomationPropertiesPath =
            System.getProperty("user.dir") + "\\src\\test\\resources\\externalData\\Config.properties";

    // Csv file e.g. E-commerce search items stored in single line
    public static final String sSearchtermCsvFilePath =
            System.getProperty("user.dir") + "\\src\\test\\resources\\externalData\\Searchterm.csv";

    // Db connection
    public static Connection sqliteConn                   = null;
    public static final String sDbUrl                    =
            "jdbc:sqlite:" + System.getProperty("user.dir") + "\\src\\test\\resources\\simple_sqlite\\Tony.db";

    // Utility handler objects
    public static Utility_Filehandler UtilFileHandler     = null;
    public static Utilities_General UtilGeneralHandler    = null;




    // Urls
    public static String sUrlHome      = "http://automationpractice.com/index.php";
    public static String sUrlLogin     = "http://automationpractice.com/index.php?controller=authentication&back=my-account";
    public static String sUrlSearch    = "http://automationpractice.com/index.php";
    public static String sUrlRegister  = "http://automationpractice.com/index.php?controller=authentication&back=my-account";
    public static String sStackOverflow= "https://stackoverflow.com/";




    // Launch browser instance -- Currently only Chrome is supported
    // Note by default image loading is disabled to speedup the operation
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

}
