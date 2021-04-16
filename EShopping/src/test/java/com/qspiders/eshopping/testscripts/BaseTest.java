package com.qspiders.eshopping.testscripts;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.qspiders.eshopping.genericlibs.AutoConstants;
import com.qspiders.eshopping.genericlibs.FrameworkUtility;
import com.qspiders.eshopping.genericlibs.WebActionUtil;
import com.qspiders.eshopping.pages.HomePage;
import com.qspiders.eshopping.pages.LoginPage;

import io.github.bonigarcia.wdm.WebDriverManager;

//All Common Pre-Condition and Post-Condition
public class BaseTest implements AutoConstants {
	
	public WebDriver driver;
	public WebActionUtil webActionUtil;
	public HomePage homePage;
	
	@Parameters({"browserName","appUrl","implict","explict"})
	@BeforeClass(alwaysRun=true)
	public void openApp(@Optional(DEFAULT_BROWSER)String browserName,
						@Optional(APP_URL)String appUrl,
						@Optional(ITO)String implict,
						@Optional(ETO)String explict) {
		if(browserName.equalsIgnoreCase("chrome")) {
			WebDriverManager.chromedriver().setup();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--disable-notifications");
			driver = new ChromeDriver(options);
		} else if(browserName.equalsIgnoreCase("firefox")) {
			WebDriverManager.firefoxdriver().setup();
			FirefoxOptions options = new FirefoxOptions();
			options.addPreference("dom.webnotifications.enabled", false);
			driver = new FirefoxDriver(options);
		} else {
			Assert.fail(browserName+" not Supported");
		}
		
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Long.parseLong(implict), TimeUnit.SECONDS);
		driver.get(appUrl);
		webActionUtil = new WebActionUtil(driver, Long.parseLong(explict));
	}
	
	@Parameters({"usernameData", "passwordData"})
	@BeforeMethod(alwaysRun=true)
	public void login(@Optional(DEFAULT_USER)String usernameData,
					  @Optional(DEFAULT_PASSWORD)String passwordData) {
		LoginPage loginPage = new LoginPage(driver, webActionUtil);
		homePage = loginPage.signIn(usernameData, passwordData);
	}
	
	@AfterMethod(alwaysRun=true)
	public void logout(ITestResult result) {
		if(result.getStatus()==ITestResult.FAILURE) {
			System.out.println(FrameworkUtility.getScreenshot(driver, result.getName()));
		}
		
		homePage.signOut();
	}
	
	@AfterClass(alwaysRun=true)
	public void closeApp() {
		driver.quit();
	}
}
