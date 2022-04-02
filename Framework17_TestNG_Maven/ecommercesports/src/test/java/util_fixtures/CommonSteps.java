package util_fixtures;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import pkg_Global.GlobalObjects;

import java.io.*;
import java.util.ArrayList;

public class CommonSteps extends GlobalObjects {
    private static final Logger myLog = Logger.getLogger(CommonSteps.class);

    @BeforeSuite
    public void CommonBeforeSuite(){
        myLog.info("=CommonBeforeSuite==");
        sSearchTermsValid   = ReadRowContent(System.getProperty("user.dir") +
                                "\\src\\test\\resources\\search_term.csv", 0);
        sSearchTermsInValid = ReadRowContent(System.getProperty("user.dir") +
                                "\\src\\test\\resources\\search_term.csv", 1);
    }

    @AfterSuite
    public void CommonAfterSuite(){
        myLog.info("=CommonAfterSuite==");
    }


    @BeforeTest
    public void CommonBeforeTest(){
        myLog.info("==CommonBeforeTest==");
    }

    @AfterTest
    public void CommonAfterTest(){
        myLog.info("==CommonAfterTest==");
    }


    public static String ReadRowContent(String sFilePath, int nRequiredLineCnt){
        File oFile              = new File(sFilePath);
        if (!oFile.exists()) {
            org.junit.Assert.fail("UtilLog: Given csv file does not exists " + sFilePath);
        }

        BufferedReader oBufRd   = null;
        String sOneLine         = null;
        int  nActualLineNo      = -1;
        try{
            oBufRd = new BufferedReader(new FileReader(oFile));
            while (  (sOneLine = oBufRd.readLine()) != null ){
                nActualLineNo ++;
                if(nRequiredLineCnt == nActualLineNo){ break; }
            }
            oBufRd.close();
        }catch(FileNotFoundException exFile){
            org.junit.Assert.fail("UtilLog: Given csv file not found by buffered reader " + sFilePath);
        }catch(IOException exFile){
            org.junit.Assert.fail("UtilLog: Given csv file line reading error " + sFilePath);
        }
        if(nRequiredLineCnt > nActualLineNo){
            org.junit.Assert.fail("UtilLog: Given csv file line index not found " + sFilePath);
        }

        return sOneLine;
    }

    public static String[][] ReadExcelcontent2D(String sFilePath) {
        try {
            FileInputStream excelFile = new FileInputStream(new File(sFilePath));
            XSSFWorkbook workbook     = new XSSFWorkbook(excelFile);
            XSSFSheet worksheet       = workbook.getSheetAt(0);
            int totalNoOfRows         = 1 + worksheet.getLastRowNum();
            int totalNoOfCols         = worksheet.getRow(0).getLastCellNum();
            String[][] arrayExcelData = new String[totalNoOfRows][totalNoOfCols];
            for (int i= 0 ; i < totalNoOfRows; i++) {
                XSSFRow row = worksheet.getRow(i);
                for (int j=0; j < totalNoOfCols; j++) {
                    arrayExcelData[i][j] = row.getCell(j).toString();
                }
            }
            workbook.close();
            return arrayExcelData;
        }catch(Exception g){g.printStackTrace(); }
        return null;
    }


}
