package top.lothar.o2o.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import top.lothar.o2o.dao.PersonInfoDao;
import top.lothar.o2o.dao.WechatAuthDao;
import top.lothar.o2o.dto.WechatAuthExecution;
import top.lothar.o2o.entity.PersonInfo;
import top.lothar.o2o.entity.WechatAuth;
import top.lothar.o2o.enums.WechatAuthStateEnum;
import top.lothar.o2o.exceptions.WechatAuthOperationException;
import top.lothar.o2o.service.WechatAuthService;

@Service
public class WechatAuthServiceImpl implements WechatAuthService {
	
	private static Logger log = LoggerFactory.getLogger(WechatAuthServiceImpl.class);
	
	@Autowired
	private WechatAuthDao wechatAuthDao;
	
	@Autowired
	private PersonInfoDao personInfoDao;

	@Override
	public WechatAuth getWechatAuthByOpenId(String openId) {
		// TODO Auto-generated method stub
		return wechatAuthDao.queryWechatInfoByOpenId(openId);
	}

	@Override
	@Transactional
	public WechatAuthExecution register(WechatAuth wechatAuth) throws WechatAuthOperationException {
		// TODO Auto-generated method stub
		//空值判断
		if (wechatAuth == null || wechatAuth.getOpenId() == null) {
			return new WechatAuthExecution(WechatAuthStateEnum.NULL_AUTH_INFO);
		}
		try {
			//设置微信账号创建时间
			wechatAuth.setCreateTime(new Date());
			//如果微信帐号里夹带着用户信息并且用户Id为空，则认为该用户第一次使用平台(且通过微信登录)
			//则自动创建用户信息
			if (wechatAuth.getPersonInfo() != null && wechatAuth.getPersonInfo().getUserId() == null) {
				try {
					wechatAuth.getPersonInfo().setCreateTime(new Date());
					wechatAuth.getPersonInfo().setEnableStatus(1);
					PersonInfo personInfo = wechatAuth.getPersonInfo();
					int effectNum = personInfoDao.insertPersonInfo(personInfo);
					wechatAuth.setPersonInfo(personInfo);
					if (effectNum < 0) {
						throw new WechatAuthOperationException("添加用户信息失败");
					}
				} catch (Exception e) {
					// TODO: handle exception
					log.error("insertPersonInfo error:" + e.toString());
					throw new WechatAuthOperationException("insertPersonInfo error: " + e.getMessage());
				}
			}
			//if条件执行后（有personInfo信息则直接添加），创建专属于本平台的微信帐号
			int effectedNum = wechatAuthDao.insertWechatAuth(wechatAuth);
			if (effectedNum <= 0) {
				throw new WechatAuthOperationException("微信帐号创建失败");
			} else {
				return new WechatAuthExecution(WechatAuthStateEnum.SUCCESS, wechatAuth);
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("insertWechatAuth error:" + e.toString());
			throw new WechatAuthOperationException("insertWechatAuth error: " + e.getMessage());
		}
	}

}
