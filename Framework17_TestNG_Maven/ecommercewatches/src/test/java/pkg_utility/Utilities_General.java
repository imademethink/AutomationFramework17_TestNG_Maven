package pkg_utility;

import com.cucumber.listener.Reporter;
import org.apache.commons.mail.*;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import pkg_Global.GlobalObjects;
import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Utilities_General extends GlobalObjects {

    private static final Logger myLog = Logger.getLogger(Utilities_General.class);
    public WebDriver LocalDriver      = null;

    public void GetCommandLineParam(){
        hmGlobalData.put("env",          System.getProperty("env"));             // -Denv=SIT
        hmGlobalData.put("browserType",  System.getProperty("browserType"));    // -Dbrowsertype=chrome
        hmGlobalData.put("baseUrl",      System.getProperty("baseUrl"));        // -DbaseUrl=http://sit.flipcart.com:8900
        hmGlobalData.put("dbAutomation", System.getProperty("dbAutomation"));  // -DdbAutomation=true
        // optional
//        hmGlobalData.put("inputDataFolder", System.getProperty("inputDataFolder"));
//        hmGlobalData.put("userCount",    System.getProperty("userCount"));
//        hmGlobalData.put("testPriority", System.getProperty("testPriority"));
    }

    public void DbConnectionInit(){
        try {
            Class.forName("org.sqlite.JDBC");
            sqliteConn = DriverManager.getConnection(sDbUrl);
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    public void DbConnectionClose(){
        try {
            sqliteConn.close();
        }catch (SQLException eDb) {
            Assert.fail(eDb.getMessage());
        }
    }

    public void GenerateReport(){
        try{
            Reporter.loadXMLConfig(new File(System.getProperty("user.dir") + "/src/test/resources/extent-config.xml"));
            Reporter.setSystemInfo("Test User", "Tony Stark");
            Reporter.setSystemInfo("Operating System Type", "Windows");
            Reporter.setSystemInfo("Build version", "v 1.2.3");
            Reporter.setTestRunnerOutput("Extent Report for Regression");

            myLog.info("Log: Html report generated");
        }catch(Exception eReport){
            myLog.info("Log: Html report NOT generated");
        }
    }

    public void SendReportViaEmail(){
        if(hmGlobalData.get("param_email_report").contains("false")) return;

        myLog.info("\nLog: Please make sure to disable 2 step login for your GMAIL\n");
        myLog.info("\nLog: Please make sure to enable Less secure app login from your GMAIL\n");

        try{
            String sHost      = "smtp.gmail.com";
            int nPort         = 465;
            String sGmailuser = hmGlobalData.get("param_email_report_from_email");
            String sGmailPwd  = hmGlobalData.get("param_email_report_gmail_pwd");

            String sEmailAddrFrom=hmGlobalData.get("param_email_report_from_email");
            String sEmailAddrTo  =hmGlobalData.get("param_email_report_to_email");
            String sEmailSubject = "Automation Test Report";
            String sEmailBody    = "Please find attached Automation Test Report";

            HtmlEmail email      = new HtmlEmail ();
            email.setHostName(sHost);
            email.setSmtpPort(nPort);
            email.setAuthenticator(new DefaultAuthenticator(sGmailuser, sGmailPwd));
            email.setSSLOnConnect(true);
            email.setFrom(sEmailAddrFrom);
            email.setSubject(sEmailSubject);
            email.setMsg(sEmailBody);
            email.addTo(sEmailAddrTo);

            if(hmGlobalData.get("param_email_attachment").contains("true") ){
                String sReportFilePath = System.getProperty("user.dir") + "\\target\\html\\extent_report.html";
                EmailAttachment attachment = new EmailAttachment();
                attachment.setPath(sReportFilePath);
                attachment.setDisposition(EmailAttachment.ATTACHMENT);
                attachment.setName("report.html");
                email.attach(attachment);
            }

            email.send();
            myLog.info("Log: Sending report via email success");

        }catch(Exception eReportEmail){
            eReportEmail.printStackTrace();
            myLog.info("Log: Sending report via email failed");
        }
    }





}
