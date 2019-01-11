package top.lothar.o2o.entity;

import java.util.Date;
/**
 * 微信账号实体类
 * @author Lothar
 *
 */
public class WechatAuth {
	//微信Id
	private Long wechatAuthId;
	//微信号与公众号绑定关联的标识
	private String openId;
	//创建时间
	private Date createTime;
	//与person用户ID关联主外键（用户）
	private PersonInfo personInfo;
	
	public long getWechatAuthId() {
		return wechatAuthId;
	}
	public void setWechatAuthId(long wechatAuthId) {
		this.wechatAuthId = wechatAuthId;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public PersonInfo getPersonInfo() {
		return personInfo;
	}
	public void setPersonInfo(PersonInfo personInfo) {
		this.personInfo = personInfo;
	}
	
}
