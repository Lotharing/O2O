package top.lothar.o2o.dao;

import top.lothar.o2o.entity.WechatAuth;

public interface WechatAuthDao {
	/**
	 * 通过openId查询本平台对应微信账号
	 * @param openId
	 * @return
	 */
	WechatAuth queryWechatInfoByOpenId(String openId);
	/**
	 * 添加对应本平台微信账号
	 * @param wechatAuth
	 * @return
	 */
	int insertWechatAuth(WechatAuth wechatAuth);
}
