package com.hjs.pages;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

import com.hjs.config.CommonAppiumPage;
import com.hjs.db.SmsVerifyCode;
import com.hjs.mybatis.inter.SmsVerifyCodeOperation;

public class MyBankCardPageObject extends CommonAppiumPage{
	
	@AndroidFindBy(id="tv_bank_name")
	private AndroidElement bankName;		//银行名称
	@AndroidFindBy(id="image_security_card_mark")
	private AndroidElement safeBankCard;	//安全卡
	@AndroidFindBy(id="dlg_sms_input_singlebtn")
	private AndroidElement smsVerifyCodeConfirmBtn;		//验证码确认按钮
	@AndroidFindBy(id="btn_unbind")
	private AndroidElement unBindBankCardBtn;		//解除绑定按钮
	@AndroidFindBy(id="refresh_animationView")
	private AndroidElement refreshIcon;		//刷新图标
	@AndroidFindBy(xpath="//*[contains(@text,'添加银行卡')]")
	private AndroidElement addBankCardBtn;		//添加银行卡按钮
	
	private By bankNameLocator=By.id("tv_bank_name");	//银行名称Locator
	private By addBankCardBtnLocator=By.xpath("//*[contains(@text,'添加银行卡')]");		//添加银行卡按钮Locator
	public MyBankCardPageObject(AndroidDriver<AndroidElement> driver) {
		super(driver);
	}
	public SetBankCardPageObject addNewBankCard(){
		clickEle(addBankCardBtn,"添加银行卡按钮");
		return new SetBankCardPageObject(driver);
	}
	public MyBankCardPageObject unbundSafeBankCard(String pwd,String phoneNum) throws Exception{
		clickEle(safeBankCard,"安全卡");
		clickEle(unBindBankCardBtn,"解除绑定按钮");
		SafeKeyBoard safeKeyBoard=new SafeKeyBoard(driver);
		safeKeyBoard.sendNum(pwd);
//		threadsleep(2000);
//		List<SmsVerifyCode> smsVerifyCodeList=new ArrayList<SmsVerifyCode>();
//		smsVerifyCodeList=getMsgVerifyCode(phoneNum);
//		if(smsVerifyCodeList.isEmpty()){
//			throw new Exception("数据库未找到"+phoneNum+"的验证码");
//		}
//		String msgVerifyCode=smsVerifyCodeList.get(smsVerifyCodeList.size()-1).getVerify_code(); //最近的一个验证码
//		safeKeyBoard.sendNum(msgVerifyCode);
//		safeKeyBoard.pressFinishBtn();
//		clickEle(smsVerifyCodeConfirmBtn,"验证码确认按钮");
		return new MyBankCardPageObject(driver);
	}
	public MyBankCardPageObject unbundNormalBankCard(String pwd,String phoneNum,String last4BankCardId) throws Exception{
		
		AndroidElement toUnbundCard=driver.findElement(By.xpath("//android.widget.TextView[@resource-id='com.evergrande.eif.android.hengjiaosuo:id/tv_bank_name' and contains(@text,'"+last4BankCardId+"')]"));
		clickEle(toUnbundCard,"待解绑卡"+last4BankCardId);
		clickEle(unBindBankCardBtn,"解除绑定按钮");
		SafeKeyBoard safeKeyBoard=new SafeKeyBoard(driver);
		safeKeyBoard.sendNum(pwd);
		return new MyBankCardPageObject(driver);
	}
	public List<String> getMyBankCardNameList(){
		List<String> bankCardList=new ArrayList<String>();
		try{
			super.waitEleUnVisible(refreshIcon,15);	//等待刷新
			super.waitAuto(2);
			List<AndroidElement> bankCardEles=driver.findElements(By.id("tv_bank_name"));
			for(int i=0;i<bankCardEles.size();i++){
				bankCardList.add(bankCardEles.get(i).getText());
			}
			super.waitAuto(super.getWaitTime());
			return bankCardList;
		}
		catch(Exception e){
		}
		super.waitAuto(super.getWaitTime());
		return null;
	}
	public List<SmsVerifyCode> getMsgVerifyCode(String phoneNum) throws IOException{
        Reader reader = Resources.getResourceAsReader("eifGoutongMybatis.xml");  
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);  
        reader.close();  
        SqlSession session = sqlSessionFactory.openSession();
        try {
        	SmsVerifyCodeOperation smsVerifyCodeOperation=session.getMapper(SmsVerifyCodeOperation.class);
        	List<SmsVerifyCode> smsVerifyCodes = smsVerifyCodeOperation.getSmsVerifyCode(phoneNum);
        	return smsVerifyCodes;
        } finally {
            session.close();
        }
	}
	public boolean verifyInthisPage(){
		return isElementExsit(addBankCardBtnLocator);
	}
	
}
