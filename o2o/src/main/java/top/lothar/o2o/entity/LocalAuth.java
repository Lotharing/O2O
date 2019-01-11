package top.lothar.o2o.entity;

import java.util.Date;
/**
 * 本地账号实体类
 * @author Lothar
 *
 */
public class LocalAuth {
	//本地账号Id
	private Long localAuthId;
	//用户名
	private String username;
	//密码
	private String password;
	//创建时间
	private Date createTime;
	//更新时间
	private Date lastEditTime;
	//与Person用户ID关联主外键（用户）
	private PersonInfo personInfo;

	public Long getLocalAuthId() {
		return localAuthId;
	}

	public void setLocalAuthId(Long localAuthId) {
		this.localAuthId = localAuthId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastEditTime() {
		return lastEditTime;
	}

	public void setLastEditTime(Date lastEditTime) {
		this.lastEditTime = lastEditTime;
	}

	public PersonInfo getPersonInfo() {
		return personInfo;
	}

	public void setPersonInfo(PersonInfo personInfo) {
		this.personInfo = personInfo;
	}
	
}