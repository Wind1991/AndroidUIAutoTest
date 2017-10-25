package com.hjs.pages;

import org.openqa.selenium.By;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

import com.hjs.config.CommonAppiumPage;
import com.hjs.config.DisConfConfig;

public class LoginPageObject extends CommonAppiumPage{
	
	@AndroidFindBy(id="phone_view")
	private AndroidElement loginPhoneInput;		//登录手机号输入框
	@AndroidFindBy(id="editText_captcha")
	private AndroidElement captchaInput;		//验证码输入框
	
	@AndroidFindBy(id="button_nextStep")
	private AndroidElement assertPhoneNumBtn;		//验证手机号
	@AndroidFindBy(id="editText_pwd")
	private AndroidElement pwdInput;		//密码输入框
	@AndroidFindBy(id="button_login")
	private AndroidElement loginBtn;		//登录按钮
	@AndroidFindBy(id="button_switch_account")
	private AndroidElement switchAccountBtn;		//更换账号或注册
	
	
	private By loginedAccountLocator=By.id("textView_account");	//已登录账号Locator
	public LoginPageObject(AndroidDriver<AndroidElement> driver) {
		super(driver);
	}
	
	public GesturePwd login(String password){
		sendKeys(pwdInput, password);
		clickEle(loginBtn,"登录按钮");
		return new GesturePwd(driver);
		
	}
	public CommonAppiumPage verifyPhoneNum(String phoneNumber){
		clickEle(loginPhoneInput,"登录手机号输入框");
		SafeKeyBoard safeKeyBoard=new SafeKeyBoard(driver);
		safeKeyBoard.sendNum(phoneNumber);
		safeKeyBoard.pressFinishBtn();
		if(super.isElementExsit(5,captchaInput)){
			DisConfConfig disConfConfig=new DisConfConfig();
	    	disConfConfig.closeVerifyCode();	//关闭验证码验证
			clickEle(captchaInput,"验证码输入框");
			super.sendKeys(captchaInput, "1234");
		}
		clickEle(assertPhoneNumBtn,"验证手机号按钮");
		if(this.verifyInthisPage()){
			DisConfConfig disConfConfig=new DisConfConfig();
	    	disConfConfig.openVerifyCode();	//打开验证码验证
			return new LoginPageObject(driver);
		}
		else {
			DisConfConfig disConfConfig=new DisConfConfig();
	    	disConfConfig.openVerifyCode();	//打开验证码验证
			return new SignUpPageObject(driver);
		}
	}
	public CommonAppiumPage switchAccount(String phoneNumber){
		clickEle(switchAccountBtn,"切换账号或注册按钮");
		clickEle(loginPhoneInput,"登录手机号输入框");
		SafeKeyBoard safeKeyBoard=new SafeKeyBoard(driver);
		safeKeyBoard.sendNum(phoneNumber);
		safeKeyBoard.pressFinishBtn();
		if(super.isElementExsit(5,captchaInput)){
			DisConfConfig disConfConfig=new DisConfConfig();
	    	disConfConfig.closeVerifyCode();	//关闭验证码验证
			clickEle(captchaInput,"验证码输入框");
			super.sendKeys(captchaInput, "1234");
		}
		clickEle(assertPhoneNumBtn,"验证手机号按钮");
		//driver.findElement(By.id("phone_view")).click();
		if(this.verifyInthisPage()){
			DisConfConfig disConfConfig=new DisConfConfig();
	    	disConfConfig.openVerifyCode();	//打开验证码验证
			return new LoginPageObject(driver);
		}
		else {
			DisConfConfig disConfConfig=new DisConfConfig();
	    	disConfConfig.openVerifyCode();	//打开验证码验证
			return new SignUpPageObject(driver);
		}
	}
	public boolean verifyInthisPage(){
		return isElementExsit(loginedAccountLocator);
	}
	
}
