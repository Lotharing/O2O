package top.lothar.o2o.dto;

import java.util.List;

import top.lothar.o2o.enums.LocalAuthStateEnum;
import top.lothar.o2o.entity.LocalAuth;

public class LocalAuthExecution {

	private int state;
	
	
	private String stateInfo;
	
	private int count;
	
	private List<LocalAuth> localAuthList;
	
	private LocalAuth localAuth;
	
	//失败时使用的构造器
	public LocalAuthExecution(LocalAuthStateEnum localAuthStateEnum){
		this.state=localAuthStateEnum.getState();
		this.stateInfo=localAuthStateEnum.getStateInfo();
	}
	
	//成功时使用的构造器
	public LocalAuthExecution(LocalAuthStateEnum localAuthStateEnum,LocalAuth localAuth){
		this.state=localAuthStateEnum.getState();
		this.stateInfo=localAuthStateEnum.getStateInfo();
		this.localAuth=localAuth;
	}
	
	//成功时使用的构造器
	public LocalAuthExecution(LocalAuthStateEnum localAuthStateEnum,List<LocalAuth> localAuthList){
		this.state=localAuthStateEnum.getState();
		this.stateInfo=localAuthStateEnum.getStateInfo();
		this.localAuthList=localAuthList;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<LocalAuth> getLocalAuthList() {
		return localAuthList;
	}

	public void setLocalAuthList(List<LocalAuth> localAuthList) {
		this.localAuthList = localAuthList;
	}

	public LocalAuth getLocalAuth() {
		return localAuth;
	}

	public void setLocalAuth(LocalAuth localAuth) {
		this.localAuth = localAuth;
	}
	
	
}
