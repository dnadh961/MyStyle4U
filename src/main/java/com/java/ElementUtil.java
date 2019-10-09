package com.java;

import java.awt.AWTException;
import java.awt.Point;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.robot.Mouse;

public class ElementUtil {
	
	public static final String ABSOLUTE_OFFSET_LEFT= "window.getAbsoluteElementOffsetLeft=function getAbsoluteElementOffsetLeft(elmt)"
			+ "{"
				+ "var clientRect = elmt.getBoundingClientRect();"
				+ "var absoluteLeft = clientRect.left;"
				+ "var parent = elmt.ownerDocument.defaultView.frameElement;"
				+ "while(parent!=null)"
				+ "{"
					+ "absoluteLeft = absoluteLeft+parent.getBoundingClientRect().left;"
					+ "parent = parent.ownerDocument.defaultView.frameElement;"
				+ "}"
				+ "return Math.round(absoluteLeft);"
			+ "}";
	
	public static final String ABSOLUTE_OFFSET_TOP= "window.getAbsoluteElementOffsetTop=function getAbsoluteElementOffsetTop(elmt)"
			+ "{"
				+ "var clientRect = elmt.getBoundingClientRect();"
				+ "var absoluteTop = clientRect.top;"
				+ "var parent = elmt.ownerDocument.defaultView.frameElement;"
				+ "while(parent!=null)"
				+ "{"
					+ "absoluteTop = absoluteTop+parent.getBoundingClientRect().top;"
					+ "parent = parent.ownerDocument.defaultView.frameElement;"
				+ "}"
				+ "return Math.round(absoluteTop);"
			+ "}";
	
	
	private static int getElementLeft(WebDriver driver, JavascriptExecutor jse, String dom) {
		driver.switchTo().defaultContent();
		String script = "var elmt=" + dom + ";return getAbsoluteElementOffsetLeft(elmt);";
		long left = (Long) jse.executeScript(script);
		return (int) left;
	}

	
	private static int getElementTop(WebDriver driver, JavascriptExecutor jse, String dom) {
		driver.switchTo().defaultContent();
		String script = "var elmt=" + dom + ";return getAbsoluteElementOffsetTop(elmt);";
		long top = (Long) jse.executeScript(script);
		return (int) top;
	}

	
	private static int getElementWidth(WebDriver driver, JavascriptExecutor jse, String dom) {
		driver.switchTo().defaultContent();
		String script = "var elmt=" + dom + ";return Math.round(elmt.getBoundingClientRect().width);";
		long top = (Long) jse.executeScript(script);
		return (int) top;
	}

	
	private static int getElementHeight(WebDriver driver, JavascriptExecutor jse, String dom) {
		driver.switchTo().defaultContent();
		String script = "var elmt=" + dom + ";return Math.round(elmt.getBoundingClientRect().height);";
		long top = (Long) jse.executeScript(script);
		return (int) top;
	}
	
	public static void dragAndDrop(WebDriver driver, String sourceDOM, String targetDOM) throws AWTException {
		Mouse mouse = new Mouse();
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		moveMouseToThis(driver, jse, sourceDOM, mouse);
		mouse.pressLeftButton();
		mouse.moveRelative(45, 0);
		WaitUtil.sleep(2);
		moveMouseToThis(driver, jse, targetDOM, mouse);
		WaitUtil.sleep(2);
		mouse.releaseLeftButton();
		WaitUtil.sleep(2);
	}
	
	public static void dragAndDrop(Point source, Point target) {
		Mouse mouse = new Mouse();
		mouse.moveTo(source.x, source.y);
		WaitUtil.sleep(2);
		mouse.pressLeftButton();
		WaitUtil.sleep(2);
		mouse.moveRelative(45, 0);
		WaitUtil.sleep(2);
		mouse.moveTo(target.x, target.y);
		WaitUtil.sleep(2);
		mouse.releaseLeftButton();
		WaitUtil.sleep(2);
	}
	
	private static void moveMouseToThis(WebDriver driver, JavascriptExecutor jse, String dom, Mouse mouse) throws AWTException {
		int height = getElementHeight(driver, jse, dom);
		int width = getElementWidth(driver, jse, dom);
		int top = getElementTop(driver, jse, dom);
		int left = getElementLeft(driver, jse, dom);

		mouse.moveTo(left + width / 2 , top + height / 2);
	}

	public static WebElement findElement(WebDriver driver, By by){
		WaitUtil.waitForElementPresence(driver, by);
		return driver.findElement(by);
	}
	
	public static boolean verifyElement(WebDriver driver, By by){
		boolean isPresent = true;
		try{
			driver.findElement(by);
		}catch(NoSuchElementException e){
			isPresent = false;
		}
		return isPresent;
	}
}
