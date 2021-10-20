package test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class test_1 {

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

		Thread.sleep(1000);

		//Select Dubai Marina as a location
		WebElement input =  driver.findElement(By.xpath("//input[contains(@class, 'a41c4dcc _6a3a3de9')]"));
		input.sendKeys("D");
		Thread.sleep(250);
		input.sendKeys("u");
		Thread.sleep(250);
		input.sendKeys("b");
		Thread.sleep(250);
		input.sendKeys("a");
		Thread.sleep(250);
		input.sendKeys("i");
		Thread.sleep(250);
		input.sendKeys(" ");
		Thread.sleep(250);
		input.sendKeys("M");
		Thread.sleep(250);
		input.sendKeys("a");
		Thread.sleep(250);
		input.sendKeys("r");
		Thread.sleep(250);
		input.sendKeys("i");
		Thread.sleep(250);
		input.sendKeys("n");
		Thread.sleep(250);
		input.sendKeys("a");

		Thread.sleep(1000);

		driver.findElement(By.xpath("//button[contains(@class, '_0e756b14')]")).click();

		//Select properties For Sale
		WebElement dropdownBtn = driver.findElement(By.xpath("//div[contains(@class, 'e7c6503c')]")); 
		dropdownBtn.click();

		Thread.sleep(1000);

		WebElement buy_btn = driver.findElement(By.xpath("//span[contains(@class, 'd92d11c7')]")); 
		buy_btn.click();

		driver.findElement(By.xpath("//a[contains(@class, 'c3901770 f6d94e28')]")).click();



	}

	@DataProvider(name = "test-data")
	public Object[][] dataProvFunc() throws InterruptedException{

		int count = 0;    
		int current = 0 ;
		String first,second,third;
		second="0";
		third=" ";
		List<WebElement> list = null;
		//max number of test cases - 5000
		Object[][] obj=new Object[5000][2];
		Object[][] obj_new = null;

		while(!second.equals(third)) { 
			Thread.sleep(1000);
			if(list != null)
				list.clear();

			//get list of items on the current page	 
			list = driver.findElements(By.className("ef447dde"));

			for(int j=0;j<list.size();j++) {
				String description = list.get(j).findElement(By.xpath(".//div[contains(@class, '_7afabd84')]")).getText() ;  	 

				//extract the location to be tested delimited by ','
				String[] parts = description.split(",");

				obj[count+j][0] = parts[parts.length-2] ;   
				obj[count+j][1] = description;    
				current = j+1;                    

			}

			//total count of items
			count = count + current;  

			actions.moveToElement(list.get(current-1)).perform();
			jse.executeScript("window.scrollBy(0,250)");

			Thread.sleep(500);

			//extract numbers from bottom of page in order to know when to stop moving forward	
			Pattern p = Pattern.compile("\\d+");
			String summary = driver.findElement(By.xpath("//span[contains(@aria-label, 'Summary text') and contains(@class, 'ca3976f7')]")).getText();
			summary = summary.replace(",","");
			Matcher m = p.matcher(summary);
			if(m.find())
				first = m.group();
			if(m.find())
				second = m.group();
			if(m.find())
				third = m.group();

			//keep moving forward if not on the last page
			if(!second.equals(third))
				driver.findElement(By.xpath("//a[contains(@title, 'Next') and contains(@class, 'b7880daf')]")).click();

		}


		obj_new = new Object [count][2];

		for(int k = 0 ; k < count ; k++) {
			obj_new[k][0] = obj[k][0];
			obj_new[k][1] = obj[k][1];
		}

		return obj_new;

	}


	@Test(dataProvider ="test-data")
	public void search_test(String webResult, String ident) throws InterruptedException {

		   
		Assert.assertTrue(webResult.contains("Dubai Marina"));
		

	}

	@AfterTest
	public void tearDown(){

		driver.quit();

	}


}
