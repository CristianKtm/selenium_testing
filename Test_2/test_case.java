package test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class test_2 {

	ChromeDriver driver;
	Actions actions;
	JavascriptExecutor jse;

	@BeforeTest
	public void setUp() throws InterruptedException {

		System.setProperty("webdriver.chrome.driver", "C:\\browserdriver\\chromedriver.exe");	

		driver = new ChromeDriver();
		actions = new Actions(driver);
		jse = (JavascriptExecutor)driver;

		//Open bayut.com
		driver.get("https://www.bayut.com/");      
		driver.manage().window().maximize();

	}


	@DataProvider(name = "test-data")
	public Object[][] dataProvFunc() throws InterruptedException{

		List<WebElement> list = null;


		//scroll down to section  
		jse.executeScript("window.scrollBy(0,1800)");

		Thread.sleep(1000);
		//click on rent tab   
		driver.findElement(By.xpath("//div[@class='d8530318']")).click();


		Thread.sleep(2000);

		//get the wrapping div to search in it 	
		WebElement div = driver.findElement(By.xpath("//div[@class='fc910dcd']"));
		//expand button	
		WebElement expand = div.findElement(By.xpath(".//div[@class='_2f838ff4 _5b112776 _29dd7f18']"));
		expand.click();

		//search for dubai apartments section and get all the li s inside	
		WebElement div1 = div.findElement(By.xpath("//div[@class='_5a12e6f6 _9b01d0a7']"));
		list = div1.findElements(By.xpath(".//li[contains(@class, '_76ddbf32 af2d23c9')]"));

		Thread.sleep(1000);

		//use aux vars to find information on the loaded page	
		WebElement aux1,aux2,aux3;

		//assign the maximum number of tests	
		Object[][] obj = new Object [30][4];

		for(int i = 0 ;i < list.size() ; i++) {

			String[] split = null;

			// refresh the web elements 
			div = driver.findElement(By.xpath("//div[@class='fc910dcd']"));
			expand = div.findElement(By.xpath(".//div[@class='_2f838ff4 _5b112776 _29dd7f18']"));
			expand.click();

			Thread.sleep(1000);

			div1 = div.findElement(By.xpath(".//div[@class='_5a12e6f6 _9b01d0a7']"));
			list = div1.findElements(By.xpath(".//li[contains(@class, '_76ddbf32 af2d23c9')]"));

			String location_before_click = list.get(i).findElement(By.xpath(".//a[@class='_78d325fa ']")).getText();

			list.get(i).findElement(By.xpath(".//a[@class='_78d325fa ']")).click();

			//after page is loaded, search inside navigation bar	
			aux1 = driver.findElement(By.xpath("//div[@class='ae0af9df']"));
			aux2 = aux1.findElement(By.xpath("//div[@class='_3a42e70b _1246bfc1']"));
			aux3 = aux1.findElement(By.xpath("//div[@class='_3a42e70b dec16dfe']"));

			String purpose = aux2.findElement(By.xpath(".//span[@class='fontCompensation']")).getText();
			String location = aux1.findElement(By.xpath(".//span[@class='_0ab46ba6']")).getText();
			String type = aux3.findElement(By.xpath(".//span[@class='fontCompensation']")).getText();

			split = location_before_click.split("-()");

			if(split != null)
				location_before_click = split[0];

			//prepare input for test method	
			obj[i][0] = purpose;
			obj[i][1] = location;
			obj[i][2] = type;
			obj[i][3] = location_before_click;


			//go back to home page  
			Thread.sleep(2000);
			driver.get("https://www.bayut.com/");      
			jse.executeScript("window.scrollBy(0,2000)");
			Thread.sleep(1000);
			driver.findElement(By.xpath("//div[@class='d8530318']")).click();
			Thread.sleep(2000);


		}

		//assign new object with exactly defined dimension	
		Object[][] obj_new = new Object [list.size()][4];

		for(int k = 0; k< list.size() ; k++) {
			obj_new[k][0] = obj[k][0];
			obj_new[k][1] = obj[k][1];
			obj_new[k][2] = obj[k][2];
			obj_new[k][3] = obj[k][3];
		}

		return obj_new;

	}


	@Test(dataProvider ="test-data")
	public void load_search_test(String purpose, String location, String type, String actualLocation) throws InterruptedException {


		Assert.assertTrue(purpose.contains("Rent"));
		Assert.assertTrue(location.contains(actualLocation));
		Assert.assertTrue(type.contains("Apartment"));


	}

	@AfterTest
	public void tearDown(){

		driver.quit();

	}



}
