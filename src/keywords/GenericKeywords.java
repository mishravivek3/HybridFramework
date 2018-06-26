package keywords;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;



import util.Constants;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class GenericKeywords {
	public WebDriver driver;
	public Properties prop;
	ExtentTest test;
	public GenericKeywords(ExtentTest test){
		this.test=test;
		
		prop = new Properties();
		try {
			FileInputStream fs = new FileInputStream(System.getProperty("user.dir")+"//src//resources//project.properties");
			prop.load(fs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String openBrowser(String browserType){
		test.log(LogStatus.INFO, "Opening Browser");
		if(prop.getProperty("grid").equals("Y")){
			DesiredCapabilities cap=null;
			if(browserType.equals("Mozilla")){
				cap = DesiredCapabilities.firefox();
				cap.setBrowserName("firefox");
				cap.setJavascriptEnabled(true);
				cap.setPlatform(org.openqa.selenium.Platform.WINDOWS);
				
			}else if(browserType.equals("Chrome")){
				 cap = DesiredCapabilities.chrome();
				 cap.setBrowserName("chrome");
				 cap.setPlatform(org.openqa.selenium.Platform.WINDOWS);
			}
			
			try {
				driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), cap);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{		
				if(browserType.equals("Mozilla")){
					System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")+"\\Exe\\geckodriver.exe");
					driver = new FirefoxDriver();
				}else if(browserType.equals("Chrome")){
					System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"\\Exe\\chromedriver.exe");
					driver =  new ChromeDriver();
				}else if(browserType.equals("ie")){
					System.setProperty("webdriver.ie.driver", System.getProperty("user.dir")+"\\Exe\\IEDriverServer.exe");
					driver =  new InternetExplorerDriver();
				}
		}
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		return Constants.PASS;
	}
	
	public String navigate(String urlKey){
		test.log(LogStatus.INFO, "Navigating to "+ prop.getProperty(urlKey));
		driver.get(prop.getProperty(urlKey));
		return Constants.PASS;
	}
	
	public String click(String locatorKey){
		test.log(LogStatus.INFO,"Clicking on "+ prop.getProperty(locatorKey));
		WebElement e = getElement(locatorKey);
		e.click();
		return Constants.PASS;
		
	}
	
	public String input(String locatorKey, String data){
		test.log(LogStatus.INFO,"Typing in "+ prop.getProperty(locatorKey));

		WebElement e = getElement(locatorKey);
		e.sendKeys(data);
		return Constants.PASS;
	}
	
	public String writeInput(String object, String data){
		wait(object);
		// currently taking the input data from Config properties file

		System.out.println("Input parameter ::-> "+data);
		try{
		driver.findElement(By.xpath(prop.getProperty(object))).clear();
		driver.findElement(By.xpath(prop.getProperty(object))).sendKeys(data);
			return Constants.PASS;
		}catch(Exception e){
			return Constants.FAIL+e.getMessage();
		}
}

	
	public String closeBrowser() {
		test.log(LogStatus.INFO,"Closing browser");
		driver.quit();
		return Constants.PASS;
		
	}
	
	/***************************Validation keywords*********************************/
	public String verifyText(String locatorKey,String expectedText){
		WebElement e = getElement(locatorKey);
		String actualText = e.getText();
		if(actualText.equals(expectedText))
			return Constants.PASS;
		else
			return Constants.FAIL+" - Could not find expected Text"+ expectedText;
	}
	
	public String verifyElementPresent(String locatorKey){
		boolean result= isElementPresent(locatorKey);
		if(result)
			return Constants.PASS;
		else
			return Constants.FAIL+" - Could not find Element "+ locatorKey;
	}
	
/*	public String verifyElementNotPresent(String locatorKey){
		boolean result= isElementPresent(locatorKey);
		if(!result)
			return Constants.PASS;
		else
			return Constants.FAIL+" - Could find Element "+ locatorKey;
	}
*/	
	
	public String wait(String timeout) {

	try {
		Thread.sleep(5000);
		//Thread.sleep(Integer.parseInt(timeout));
	} catch (Exception  e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		return Constants.PASS;
	}
	
	/************************Utility Functions********************************/
	public WebElement getElement(String locatorKey){
		WebElement e = null;
		try{
			if(locatorKey.endsWith("_id"))
				e = driver.findElement(By.id(prop.getProperty(locatorKey)));
			else if(locatorKey.endsWith("_xpath"))
				e = driver.findElement(By.xpath(prop.getProperty(locatorKey)));
			else if(locatorKey.endsWith("_name"))
				e = driver.findElement(By.name(prop.getProperty(locatorKey)));
			else if(locatorKey.endsWith("_css"))
				e = driver.findElement(By.cssSelector(prop.getProperty(locatorKey)));
			
		}catch(Exception ex){
			reportFailure("Failure in Element Extraction - "+ locatorKey);
			Assert.fail("Failure in Element Extraction - "+ locatorKey);
		}
		
		return e;

	}
	
	public boolean isElementPresent(String locatorKey){
		List<WebElement> e = null;
		
		if(locatorKey.endsWith("_id"))
			e = driver.findElements(By.id(prop.getProperty(locatorKey)));
		else if(locatorKey.endsWith("_xpath"))
			e = driver.findElements(By.xpath(prop.getProperty(locatorKey)));
		else if(locatorKey.endsWith("_name"))
			e = driver.findElements(By.name(prop.getProperty(locatorKey)));
		else if(locatorKey.endsWith("_css"))
			e = driver.findElements(By.cssSelector(prop.getProperty(locatorKey)));
		if(e.size()==0)
			return false;
		else 
			return true;
	}
	/******************************Reporting functions******************************/
		
	public void reportFailure(String failureMessage){
		takeScreenShot();
		test.log(LogStatus.FAIL,failureMessage);
	}
	public void takeScreenShot(){
		// decide name - time stamp
		Date d = new Date();
		String screenshotFile=d.toString().replace(":", "_").replace(" ","_")+".png";
		String path=System.getProperty("user.dir")+Constants.SCREENSHOT_PATH+screenshotFile;
		// take screenshot
		File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(srcFile, new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		test.log(LogStatus.INFO, test.addScreenCapture(path) + path);
		
	}
	
}
