package top.lothar.o2o.service;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import top.lothar.o2o.BaseTest;
import top.lothar.o2o.dto.WechatAuthExecution;
import top.lothar.o2o.entity.PersonInfo;
import top.lothar.o2o.entity.WechatAuth;
import top.lothar.o2o.enums.WechatAuthStateEnum;

public class WechatAuthServiceTest extends BaseTest{
	
	@Autowired
	private WechatAuthService wechatAuthService;
	
	@Test
	public void testRegister() {
		WechatAuth wechatAuth = new WechatAuth();
		PersonInfo personInfo = new PersonInfo();
		String openId = "openIDzzz";
		
		personInfo.setCreateTime(new Date());
		personInfo.setName("zzz测试一下");
		personInfo.setUserType(1);
		
		wechatAuth.setPersonInfo(personInfo);
		wechatAuth.setOpenId(openId);
		wechatAuth.setCreateTime(new Date());
		
		WechatAuthExecution wae = wechatAuthService.register(wechatAuth);
		assertEquals(WechatAuthStateEnum.SUCCESS.getState(), wae.getState());
		
		wechatAuth  = wechatAuthService.getWechatAuthByOpenId(openId);
		System.out.println(wechatAuth.getPersonInfo().getName());
		
	}
}
