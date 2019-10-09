package com.java;

import static com.java.ElementUtil.findElement;

import java.awt.Point;
import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.robot.Keyboard;
import com.robot.Mouse;

public class DressUploader {
	
	private static WebDriver driver;
	private static JavascriptExecutor jsExecutor;
	private static String folderLoc = "C:\\Chanu\\MyStyle4U";
	private static Keyboard keyboard = null;
	
	public static void main(String[] args) throws IOException {
		launchBrowser();
		keyboard = new Keyboard();
		login();
		editPage(Pages.KIDS);
		/*Map<String, String> detailsMap = readDetails();
		Iterator<String> keysI = detailsMap.keySet().iterator();
		while(keysI.hasNext()) {
			String key = keysI.next();
			postDress(key);
			updatePermLink(key);
		}*/
		for(int i=1; i<50; i++) {
			String prefix = "31";
			String itemId = i < 10 ? "0" + i : i + "";
			String id = prefix + itemId;
			postDress(id, i);
			updatePermLink(id);
		}
	}
	
	private static void updatePermLink(String id) {
		switchToEditor();
		WaitUtil.sleep(3);
		WaitUtil.waitForElementClickable(driver, By.xpath("//h2[.//text()='"+ id +"']"));
		WebElement titleElmt = findElement(driver, By.xpath("//h2[.//text()='"+ id +"']"));
		jsExecutor.executeScript("arguments[0].click();", titleElmt);
		switchToMain();
		findElement(driver, By.id("blog-post-options")).click(); WaitUtil.sleep(2);
		findElement(driver, By.cssSelector("span[class='post-option-title']")).click(); WaitUtil.sleep(2);
		findElement(driver, By.id("post-options-permalink")).clear(); WaitUtil.sleep(1);
		findElement(driver, By.id("post-options-permalink")).sendKeys(id); WaitUtil.sleep(1);
		findElement(driver, By.xpath("//div[contains(text(), 'Update')]")).click();
		closeDialog();
	}
	
	private static void closeDialog() {
		WaitUtil.waitForElementClickable(driver, By.xpath("(//div[@class='weebly-dialog-close'])[last()]"));
		WebElement closeIcon = findElement(driver, By.xpath("(//div[@class='weebly-dialog-close'])[last()]"));
		jsExecutor.executeScript("arguments[0].click();", closeIcon);
	}

	private static void loadScripts() {
		jsExecutor.executeScript(ElementUtil.ABSOLUTE_OFFSET_LEFT);
		jsExecutor.executeScript(ElementUtil.ABSOLUTE_OFFSET_TOP);
	}
	
	private static void postDress(String id, int index) {
		switchToMain();
		findElement(driver, By.cssSelector("button[class$='new-post']")).click();
		switchToEditor();
		findElement(driver, By.id("blog-post-title")).sendKeys(id);
		dragImage(); dragButton(id);
		uploadImg(index + "");
		findElement(driver, By.xpath("//div[contains(text(), 'Post')]")).click();
		switchToMain();
		closeDialog();
	}
	
	private static void dragImage() {
		ElementUtil.dragAndDrop(new Point(54, 315), new Point(577, 525));
		WaitUtil.sleep(2);
	}

	private static void dragButton(String id) {
		ElementUtil.dragAndDrop(new Point(150, 480), new Point(577, 570));
		findElement(driver, By.xpath("//span[text()='Button Text']")).click();
		findElement(driver, By.xpath("//span[text()='textaligncenter']")).click();
		WaitUtil.sleep(2);
		findElement(driver, By.xpath("//div[contains(text(), 'Button Text')]")).click();
		WaitUtil.sleep(2);
		findElement(driver, By.name("adv")).clear();
		WaitUtil.sleep(1);
		findElement(driver, By.name("adv")).sendKeys("FREE QUOTE");
		WaitUtil.sleep(2);
		findElement(driver, By.xpath("//span[text()='leftarrowlarge']")).click();
		WaitUtil.sleep(2);
		findElement(driver, By.xpath("//span[text()='Link']")).click();
		WaitUtil.sleep(2);
		findElement(driver, By.xpath("//span[text()='Website URL']")).click();
		WaitUtil.sleep(2);
		findElement(driver, By.cssSelector("input[class='js-url-input']")).sendKeys("https://api.whatsapp.com/send?"
				+ "phone=919000793116&text=hi My Style 4 U, I would like to get a free quote for the product id " + id + 
				". Can you help me?");
		WaitUtil.sleep(3);
		Mouse mouse = new Mouse();
		mouse.moveRelative(0, 150);
		mouse.click();
	}

	private static void uploadImg(String id) {
		findElement(driver, By.xpath("//p[text()='UPLOAD IMAGE']")).click();
		switchToMain();
		WebElement uploadBtn = findElement(driver, By.cssSelector("label[class*='w-upload-button']"));
		jsExecutor.executeScript("arguments[0].click()", uploadBtn);
		WaitUtil.sleep(3);
		keyboard.type(folderLoc + "\\" + id + ".jpg");
		keyboard.enter();
		WaitUtil.sleep(3);
	}

	private static void switchToMain() {
		driver.switchTo().defaultContent();
	}
	
	private static void switchToEditor() {
		WaitUtil.waitForElementPresence(driver, By.id("editor-frame"));
		driver.switchTo().frame("editor-frame");
	}
	
	/*private static Map<String, String> readDetails() throws IOException {
		Map<String, String> detailsMap = new LinkedHashMap<String, String>();
		Scanner sc = new Scanner(new File(folderLoc + "/Details.csv"));
		sc.nextLine();
		while(sc.hasNextLine()) {
			String line = sc.nextLine();
			String[] parts = line.split(",");
			detailsMap.put(parts[0], parts[1]);
		}
		sc.close();
		return detailsMap;
	}*/
	
	public enum Pages {
		KIDS("kids"), MOM_BABY_DUO("Mom Baby Duo"), WOMEN("Women");
		
		private String link;
		
		Pages(String link) {
			this.link = link;
		}
		
		public String getLink() {
			return link;
		}
	}
	
	private static void editPage(Pages page) {
		findElement(driver, By.id("edit-site")).click();
		switchToEditor();
		findElement(driver, By.xpath("//a[contains(text(), '"+page.getLink()+"')]")).click();
	}
	
	private static void login() {
		driver.get("https://www.weebly.com/login");
		findElement(driver, By.id("weebly-username")).sendKeys("dnadh961@gmail.com");
		findElement(driver, By.id("weebly-password")).sendKeys("dnadh961");
		findElement(driver, By.xpath("//input[@value='Log In']")).click();
		loadScripts();
	}

	private static void launchBrowser() {
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		ChromeOptions options = new ChromeOptions();
		options.addArguments("start-maximized");
		//options.addArguments("--kiosk");
		options.addArguments("--test-type");
		options.addArguments("disable-infobars");
		options.addArguments("chrome.switches", "--disable-extensions");
		driver = new ChromeDriver(options);
		jsExecutor = (JavascriptExecutor) driver;
	}
}
