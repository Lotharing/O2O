package top.lothar.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import top.lothar.o2o.BaseTest;
import top.lothar.o2o.entity.LocalAuth;
import top.lothar.o2o.entity.PersonInfo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LocalAuthDaoTest extends BaseTest{
	
	@Autowired
	private LocalAuthDao localAuthDao;
	
	private static final String USERNAME = "testusername";
	
	private static final String PASSWORD = "testpassword";
	
	@Test
	public void testAqueryLocalByUserNameAndPwd() {
		LocalAuth localAuth = localAuthDao.queryLocalByUserNameAndPwd(USERNAME, PASSWORD);
		System.out.println(localAuth.getUsername()+"---"+localAuth.getPersonInfo().getName());
	}
	
	@Test
	public void testBqueryLocalByUserId() {
		LocalAuth localAuth = localAuthDao.queryLocalByUserId(3);
		System.out.println(localAuth.getUsername());
	}
	
	@Test
	public void testCinsertLocalAuth() {
		LocalAuth localAuthConditionAuth = new LocalAuth();
		PersonInfo personInfo = new PersonInfo();
		personInfo.setUserId(2L);
		localAuthConditionAuth.setPersonInfo(personInfo);
		localAuthConditionAuth.setUsername("username");
		localAuthConditionAuth.setPassword("password");
		localAuthConditionAuth.setCreateTime(new Date());
		localAuthConditionAuth.setLastEditTime(new Date());
		int effectNum = localAuthDao.insertLocalAuth(localAuthConditionAuth);
		assertEquals(1, effectNum);
	}
	
	@Test
	public void testDupdateLocalAuth() {
		long userId = 2;
		String username = "username";
		String password = "password";
		String newPassword = "testpassword";
		Date lastEditTime = new Date();
		int effectNum = localAuthDao.updateLocalAuth(userId, username, password, newPassword, lastEditTime);
		assertEquals(1, effectNum);
	}
}
