package keywords;

import java.util.Hashtable;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import util.Constants;
import util.Xls_Reader;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class Keywords {

	ExtentTest test;
	AppKeywords app;
	
	public Keywords(ExtentTest test) {
		this.test=test;
	}

	public void executeKeywords(
			String testUnderExecution,
			Xls_Reader xls,
			Hashtable<String,String> testData){
		
		app = new AppKeywords(test);
		
		int rows = xls.getRowCount(Constants.KEYWORDS_SHEET);
		
		for(int rNum=2;rNum<=rows;rNum++){
			String tcid  = xls.getCellData(Constants.KEYWORDS_SHEET, Constants.TCID_COL, rNum);
			if(tcid.equals(testUnderExecution)){
				String keyword  = xls.getCellData(Constants.KEYWORDS_SHEET, Constants.KEYWORD_COL, rNum);
				String object  = xls.getCellData(Constants.KEYWORDS_SHEET, Constants.OBJECT_COL, rNum);
				String key  = xls.getCellData(Constants.KEYWORDS_SHEET, Constants.DATA_COL, rNum);
				String data = testData.get(key);
				test.log(LogStatus.INFO,tcid+" -- "+ keyword +" -- "+object+" --- "+data);
				String result="";
				if(keyword.equals("openBrowser"))
					result=app.openBrowser(data);
				else if(keyword.equals("navigate"))
					result=app.navigate(object);
				else if(keyword.equals("click"))
					result=app.click(object);
				else if(keyword.equals("input"))
					result=app.input(object,data);
				else if(keyword.equals("closeBrowser"))
					result=app.closeBrowser();
				else if(keyword.equals("verifyLoginDetails"))
					result=app.verifyLoginDetails(testData);
				else if(keyword.equals("verifyElementPresent"))
					result=app.verifyElementPresent(object);
				else if(keyword.equals("flipKartLogin"))
					result=app.flipKartLogin(testData);
				else if(keyword.equals("verifyFlipKartLogin"))
					result=app.verifyFlipKartLogin(testData.get("ExpectedResult"));
				else if(keyword.equals("defaultLogin"))
					result=app.defaultLogin();
				else if(keyword.equals("wait"))
					result=app.wait(key);
				else if(keyword.equals("filterMobileAndValidate"))
					result=app.filterMobileAndValidate(testData);
				else if(keyword.equals("searchMobileAndSelect"))
					result=app.searchMobileAndSelect(testData.get(key));
				else if(keyword.equals("writeInput"))
					result=app.searchMobileAndSelect(testData.get(key));
				
				// central place reporing failure
				if(!result.equals(Constants.PASS)){
					app.reportFailure(result);
					Assert.fail(result);
				}
			}
		}
		
		}
	
	public AppKeywords getGenericKeyWords(){
		return app;
	}
	
	
}
