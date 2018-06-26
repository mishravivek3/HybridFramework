package testscript;

import java.util.Hashtable;

import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import keywords.Keywords;
import util.Constants;
import util.DataUtil;
import util.Xls_Reader;

public class VerifyPolicyoverview extends BaseTest {

@BeforeTest	
	public void init(){
		xls = new Xls_Reader(Constants.ProductSuite_XLS);
		testName = "VerifyPolicyoverview";
	}


@Test(dataProvider="getData")
public void VerifyPolicyoverview(Hashtable<String,String> data){
	test = rep.startTest(testName+"Session");
	test.log(LogStatus.INFO, data.toString());
	
	if(DataUtil.isSkip(xls, testName) || data.get("Runmode").equals("N")){
		test.log(LogStatus.SKIP, "Skipping the test as runmode is N");
		throw new SkipException("Skipping the test as runmode is N");
	}
	
	test.log(LogStatus.INFO, "Starting "+testName);		
	app = new Keywords(test);
	test.log(LogStatus.INFO, "Executing keywords");
	app.executeKeywords(testName, xls,data);
	test.log(LogStatus.PASS, "PASS");
	app.getGenericKeyWords().takeScreenShot();
	}


}
