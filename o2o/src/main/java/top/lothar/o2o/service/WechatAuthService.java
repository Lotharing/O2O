package top.lothar.o2o.service;

import top.lothar.o2o.dto.WechatAuthExecution;
import top.lothar.o2o.entity.WechatAuth;
import top.lothar.o2o.exceptions.WechatAuthOperationException;

public interface WechatAuthService {
	/**
	 * 根据openId查找平台对应的微信账号
	 * @param openId
	 * @return
	 */
	WechatAuth getWechatAuthByOpenId(String openId);
	/**
	 * 注册本平台的微信账号
	 * @param wechatAuth
	 * @return
	 * @throws WechatAuthOperationException
	 */
	WechatAuthExecution register(WechatAuth wechatAuth) throws WechatAuthOperationException;
}
