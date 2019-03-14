package top.lothar.o2o.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import top.lothar.o2o.dao.LocalAuthDao;
import top.lothar.o2o.dto.LocalAuthExecution;
import top.lothar.o2o.entity.LocalAuth;
import top.lothar.o2o.enums.LocalAuthStateEnum;
import top.lothar.o2o.exceptions.LocalAuthOperationException;
import top.lothar.o2o.service.LocalAuthService;
import top.lothar.o2o.util.MD5;

@Service
public class LocalAuthServiceImpl implements LocalAuthService {
	
	@Autowired
	private LocalAuthDao localAuthDao;

	@Override
	public LocalAuth getLocalAuthByUsernameAndPwd(String username, String password) {
		// TODO Auto-generated method stub
		return localAuthDao.queryLocalByUserNameAndPwd(username, MD5.getMd5(password));
	}

	@Override
	public LocalAuth getLocalAuthByUserId(long userId) {
		// TODO Auto-generated method stub
		return localAuthDao.queryLocalByUserId(userId);
	}

	@Override
	@Transactional
	public LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException {
		// TODO Auto-generated method stub
		//空值判断localAuth的账号密码，用户信息特别是userId不能为空
		if (localAuth == null || localAuth.getPassword() == null || localAuth.getUsername() == null || localAuth.getPersonInfo() == null
				|| localAuth.getPersonInfo().getUserId() == null) {
			return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
		}
		//查询此用户是否已经绑定过微信账号
		LocalAuth tempLocalAuth = localAuthDao.queryLocalByUserId(localAuth.getPersonInfo().getUserId());
		if (tempLocalAuth != null) {
			//已经存在则退出保证平台账号的一致性
			return new LocalAuthExecution(LocalAuthStateEnum.ONLY_ONE_ACCOUNT);
		}
		//不存在时候进行微信与本地用户进行绑定
		try {
			localAuth.setCreateTime(new Date());
			localAuth.setLastEditTime(new Date());
			//对密码进行MD5加密
			localAuth.setPassword(MD5.getMd5(localAuth.getPassword()));
			int effectNum = localAuthDao.insertLocalAuth(localAuth);
			if (effectNum < 0) {
				throw new LocalAuthOperationException("绑定本地用户失败");
			}else {
				return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS,localAuth);
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw new LocalAuthOperationException("insert localAuth error"+e.getMessage());
		}
	}

	@Override
	@Transactional
	public LocalAuthExecution modifyLocalAuth(Long userId, String username, String password, String newPassword)
			throws LocalAuthOperationException {
		// TODO Auto-generated method stub
		// 非空判断，判断传入的用户Id,帐号,新旧密码是否为空，新旧密码是否相同，若不满足条件则返回错误信息
		if (userId != null && username != null && password != null && newPassword != null && !password.equals(newPassword)) {
			try {
				// 更新密码，并对新密码进行MD5加密
				int effectNum = localAuthDao.updateLocalAuth(userId, username, MD5.getMd5(password), MD5.getMd5(newPassword), new Date());
				//判断是否成功
				if (effectNum <= 0) {
					throw new LocalAuthOperationException("更新密码失败");
				}
				return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
			} catch (Exception e) {
				// TODO: handle exception
				throw new LocalAuthOperationException("更新密码失败"+e.getMessage());
			}
		}else {
			return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
		}
	}

}
