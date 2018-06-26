package keywords;

import java.util.Hashtable;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import util.Constants;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class AppKeywords extends GenericKeywords{

		
	public AppKeywords(ExtentTest test) {
		super(test);
	}

	public String verifyLoginDetails(Hashtable<String, String> testData){
		//name
		String expectedName=testData.get("Name");
		//email
		String expectedID=testData.get("Username");
		
		// actual
		
		return Constants.PASS;
		
	}
	
	public String login(String username,String password){
		
		getElement("loginLink_xpath").click();
		getElement("userName_xpath").sendKeys(username);
		getElement("password_xpath").sendKeys(password);
		getElement("loginButton_xpath").click();
		return Constants.PASS;
		
	}
	
	public String loginLifePRO(String username,String password){
		
		//getElement("loginLink_xpath").click();
		getElement("Usernametxt").sendKeys("postd");
		getElement("Passwordtxt").sendKeys("Exl@123$");
		getElement("Submitbtn").click();
		return Constants.PASS;
		
	}

	public String flipKartLogin(Hashtable<String, String> testData) {
		test.log(LogStatus.INFO, "Logging in with "+ testData.get("Username")+"/"+testData.get("Password"));
		return login(testData.get("Username"),testData.get("Password"));
	}
	
	public String defaultLogin(){
		return login(prop.getProperty("username"),prop.getProperty("password"));
		
	}

	public String verifyFlipKartLogin(String expectedResult) {
		test.log(LogStatus.INFO, "Validating login");
		boolean result = isElementPresent("myAccount_xpath");
		String actualResult="";
		if(result)
			actualResult="Success";
		else
			actualResult="Failure";
		
		if(!actualResult.equals(expectedResult)){
			return "Failed - "+ "Got actual result as "+actualResult;
		}
		
		return Constants.PASS;
	}

	public String filterMobileAndValidate(Hashtable<String, String> testData) {
		String brandNameXPATH = testData.get("MobileCompany");
		//driver.findElement(By.xpath("//a[@title='"+brandName+"']")).click();;
		getElement(brandNameXPATH).click();
		//validate names
		List<WebElement> mobiles = driver.findElements(By.xpath(prop.getProperty("allMobiles_xpath")));
		for(int i=0;i<mobiles.size();i++){
			System.out.println(mobiles.get(i).getText());
			if(!mobiles.get(i).getText().contains("SAMSUNG")){
				return Constants.FAIL + " -  Found the mobile- " +mobiles.get(i).getText() ;

			}
		}
		
		// validate prices 
		//Rs. 5001 - Rs. 10000
		String priceRange = testData.get("PriceRange");
		driver.findElement(By.xpath("//span[@original='"+priceRange+"']")).click();
		wait("5000");
		String temp[] = priceRange.split(" ");
		int lowerCost = Integer.parseInt(temp[1]);
		int upperCost = Integer.parseInt(temp[4]);
		// extract all prices and check
		List<WebElement> prices = driver.findElements(By.xpath(prop.getProperty("allPrices_xpath")));
		for(int i=0;i<prices.size();i++){
			String price=prices.get(i).getText();
			temp = price.split(" ");
			price = temp[1].replace(",", "");
			int prodPrice = Integer.parseInt(price);
			if(prodPrice>upperCost || prodPrice<lowerCost)
				return Constants.FAIL + " Price incorrect " +prodPrice ;
			
		}
		
		
		return Constants.PASS;
		
	}

	public String searchMobileAndSelect(String itemName) {
		boolean found=false;
		List<WebElement> mobilesBeforeScroll =null;
		List<WebElement> mobilesAfterScroll =null;
		int index=-1;
		while(!found){
			mobilesBeforeScroll = driver.findElements(By.xpath(prop.getProperty("allMobiles_xpath")));
			int y_last=mobilesBeforeScroll.get(mobilesBeforeScroll.size()-1).getLocation().y;
			JavascriptExecutor js = (JavascriptExecutor)driver;

			js.executeScript("window.scrollTo(0,"+y_last+")");
			wait("3000");
			
			 mobilesAfterScroll = driver.findElements(By.xpath(prop.getProperty("allMobiles_xpath")));
			if(mobilesAfterScroll.size() == mobilesBeforeScroll.size()){
				return "FAIL - Product not found";
			}
			
			for(int i=0;i<mobilesBeforeScroll.size();i++){
				if(mobilesBeforeScroll.get(i).getText().startsWith(itemName)){
					//found
					index=i;
					System.out.println(mobilesBeforeScroll.get(i).getText());
					found=true;
				}
			}

		}
		
		// found
		int y=mobilesBeforeScroll.get(index).getLocation().y;
		JavascriptExecutor js = (JavascriptExecutor)driver;

		js.executeScript("window.scrollTo(0,"+(y-200)+")");
		mobilesBeforeScroll.get(index).click();
		String itemText = getElement("prodHeading_xpath").getText();
		
		if(!itemText.startsWith(itemName))
			return "Fail - item name did not match " + itemText;
		
		getElement("addToCart_xpath").click();
		
		return Constants.PASS;
	}

	
	
	
}
