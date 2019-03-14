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

public class PersonInfoDaoTest extends BaseTest{
	
	@Autowired
	private PersonInfoDao personInfoDao;
	
	@Test
	public void testAQueryPersonInfoById() {
		long userId = 1;
		PersonInfo resultInfo = personInfoDao.queryPersonInfoById(userId);
		System.out.println(resultInfo.getName());
		assertEquals("test", resultInfo.getEmail());
	}
	
	@Ignore
	public void testBInsertPersonInfo() {
		PersonInfo personInfo =  new PersonInfo();
		personInfo.setName("赵路通");
		personInfo.setCreateTime(new Date());
		personInfo.setLastEditTime(new Date());
		personInfo.setEmail("nullpoint@163.com");
		personInfo.setEnableStatus(1);
		int effectNum = personInfoDao.insertPersonInfo(personInfo);
		assertEquals(1, effectNum);
	}
	
}
