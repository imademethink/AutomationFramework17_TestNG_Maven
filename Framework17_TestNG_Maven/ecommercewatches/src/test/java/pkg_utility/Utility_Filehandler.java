package pkg_utility;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import pkg_Global.GlobalObjects;
import java.io.*;
import java.util.Properties;
import java.util.Set;

public class Utility_Filehandler extends GlobalObjects {

    private static final Logger myLog = Logger.getLogger(Utility_Filehandler.class);

    public void CsvDataReaderInit(){
        File oFile              = new File(sSearchtermCsvFilePath);
        String sOneLine         = null;
        try{
            BufferedReader oBufRd = new BufferedReader(new FileReader(oFile));
            // Assuming all valid search terms are on line 0, seperated by comma
            // e.g. dress,shows,toys,watch
            sOneLine              = oBufRd.readLine();
            hmGlobalData.put("Searchterm_Valid", sOneLine);
            // Assuming all invalid valid search terms are on line 1, seperated by comma
            // e.g. candle
            sOneLine              = oBufRd.readLine();
            hmGlobalData.put("Searchterm_Invalid", sOneLine);
            oBufRd.close();
        }catch(FileNotFoundException exFile){
            Assert.fail("Log: Given csv file not found by buffered reader " + sSearchtermCsvFilePath);
        }catch(IOException exFile){
            Assert.fail("Log: Given csv file line reading error " + sSearchtermCsvFilePath);
        }
    }

    public void PropertiesDataReaderInit(){
        File oFile              = new File(sAutomationPropertiesPath);
        Properties oProp        = new Properties();
        try{
            oProp.load(new FileInputStream(sAutomationPropertiesPath));
            Set<String> stAllPropertyNames = oProp.stringPropertyNames();
            for (String sOnePropertyName : stAllPropertyNames) {
                hmGlobalData.put(sOnePropertyName, oProp.getProperty(sOnePropertyName));
            }
        }catch(FileNotFoundException exFile){
            Assert.fail("Log: Given properties file not found by FileInputStream " + sAutomationPropertiesPath);
        }catch(IOException exFile){
            Assert.fail("Log: Given properties file reading error " + sAutomationPropertiesPath);
        }
    }

    public void ExcelDataReaderInit(){
        try {
            exlInStream   = new FileInputStream(new File(sUserDataExcelPath));
            exlWorkBook   = new XSSFWorkbook(exlInStream);
            exlWorkSheet  = exlWorkBook.getSheet(sSheetName);
        }catch(IOException eXl){
            Assert.fail("Log: Given excel file parsing error " + sUserDataExcelPath);
        }
    }

    public void ExcelDataReaderClose(){
        try {
            exlWorkBook.close();
            exlInStream.close();
        }catch(IOException eXl){
            Assert.fail("Log: Given excel file closing error " + sUserDataExcelPath);
        }
    }

    public void ExcelReadDataRowWithUniqueId(String sUniqueId){
            int nRowIndex             = -1;
            for (int k=0; k< exlWorkSheet.getLastRowNum(); k++){
                if(exlWorkSheet.getRow(k).getCell(0).toString().matches(sUniqueId)){
                    nRowIndex = k;
                    break;
                }
            }
            for (int k=0; k< exlWorkSheet.getRow(nRowIndex).getLastCellNum(); k++){
                hmUserData.put(
                        exlWorkSheet.getRow(0).getCell(k).toString(), // key
                        exlWorkSheet.getRow(nRowIndex).getCell(k)              // value
                );
            }
    }

    public static XSSFCell ExcelSetCellValue(int nRowNum, int nColNum, Object objCellValue){
        XSSFCell oRequiredCellValue = null;
        try {
            boolean bFreshWorksheet    = false;
            if(null == exlWorkSheet){
                exlWorkBook.createSheet(sSheetName);
                exlWorkSheet = exlWorkBook.getSheet(sSheetName);
                exlWorkSheet.createRow(nRowNum);
                exlWorkSheet.getRow(nRowNum).createCell(nColNum);
                bFreshWorksheet = true;
            }
            if(nRowNum > exlWorkSheet.getLastRowNum() && !bFreshWorksheet){
                exlWorkSheet.createRow(nRowNum);
            }
            if(nColNum >= exlWorkSheet.getRow(nRowNum).getLastCellNum() && !bFreshWorksheet){
                exlWorkSheet.getRow(nRowNum).createCell(nColNum);
            }
            oRequiredCellValue = exlWorkSheet.getRow(nRowNum).getCell(nColNum);

            if( (objCellValue instanceof Integer ) || (objCellValue instanceof Long ) ){
                oRequiredCellValue.setCellType(CellType.NUMERIC);
                oRequiredCellValue.setCellValue((int)objCellValue); // int or long
            } else
            if( (objCellValue instanceof Float ) || (objCellValue instanceof Double ) ){
                oRequiredCellValue.setCellType(CellType.NUMERIC);
                oRequiredCellValue.setCellValue((float)objCellValue); // float or double
            } else
            if(    (objCellValue instanceof String) && ( ! objCellValue.toString().isEmpty())   ){
                oRequiredCellValue.setCellType(CellType.STRING);
                oRequiredCellValue.setCellValue(objCellValue.toString());
            } else
            if(    (objCellValue instanceof String) && ( objCellValue.toString().isEmpty())     ){
                oRequiredCellValue.setCellType(CellType.BLANK);
                oRequiredCellValue.setCellValue("");
            } else
            if (objCellValue instanceof Boolean){
                oRequiredCellValue.setCellType(CellType.BOOLEAN);
                oRequiredCellValue.setCellValue((boolean)objCellValue);
            }
            // also need to add for objCellValue instanceof Date

            exlInStream.close();

            FileOutputStream exlOutStream = new FileOutputStream(new File(sUserDataExcelPath));
            exlWorkBook.write(exlOutStream);
            exlOutStream.close();
            exlOutStream.close();

            new Utility_Filehandler().ExcelDataReaderInit();
        }catch(IOException eXl){
            eXl.printStackTrace();
            Assert.fail("Log: Given excel file parsing error");
        }
        return oRequiredCellValue;
    }

}
