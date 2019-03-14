package top.lothar.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import top.lothar.o2o.BaseTest;
import top.lothar.o2o.entity.PersonInfo;
import top.lothar.o2o.entity.WechatAuth;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WechatAuthDaoTest extends BaseTest{
	
	@Autowired
	private WechatAuthDao wechatAuthDao;
	
	@Test
	public void testAqueryWechatInfoByOpenId() {
		String openId = "zzz";
		WechatAuth wechatAuth = wechatAuthDao.queryWechatInfoByOpenId(openId);
		System.out.println(wechatAuth.getPersonInfo().getName());
	}
	
	@Ignore
	public void testBinserWechatAuth() {
		PersonInfo personInfo=new PersonInfo();
		WechatAuth wechatAuth=new WechatAuth();
		//设置的是userId关联tb_person_info的哪一行
		personInfo.setUserId(1L);
		wechatAuth.setPersonInfo(personInfo);
		wechatAuth.setOpenId("lonel");
		wechatAuth.setCreateTime(new Date());
		int insertWechatAuth = wechatAuthDao.insertWechatAuth(wechatAuth);
		assertEquals(1, insertWechatAuth);
	}
	
}
