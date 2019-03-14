package top.lothar.o2o.service;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import top.lothar.o2o.BaseTest;
import top.lothar.o2o.dto.LocalAuthExecution;
import top.lothar.o2o.entity.LocalAuth;
import top.lothar.o2o.entity.PersonInfo;

public class LocalAuthServiceTest extends BaseTest{
	
	@Autowired
	private LocalAuthService localAuthService;
	
	@Ignore
	@Test
	public void testbindLocalAuth() {
		LocalAuth localAuthCondition = new LocalAuth();
		PersonInfo personInfo = new PersonInfo();
		personInfo.setUserId(1L);
		localAuthCondition.setUsername("wenbo");
		localAuthCondition.setPassword("wenbo3629");
		localAuthCondition.setPersonInfo(personInfo);
		LocalAuthExecution localAuthExecution = localAuthService.bindLocalAuth(localAuthCondition);
		System.out.println(localAuthExecution.getLocalAuth().getUsername());
		System.out.println(localAuthExecution.getLocalAuth().getPassword());
	}
	
	@Test
	public void testmodifyLocalAuth() {
		LocalAuthExecution localAuthExecution = localAuthService.modifyLocalAuth(1L, "wenbo", "wenbo1995", "wenbo3629");
		System.out.println(localAuthExecution.getState());
		System.out.println(localAuthExecution.getStateInfo());
	}
	
}
