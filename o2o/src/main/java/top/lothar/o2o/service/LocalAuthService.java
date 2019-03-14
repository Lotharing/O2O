package top.lothar.o2o.service;

import top.lothar.o2o.dto.LocalAuthExecution;
import top.lothar.o2o.entity.LocalAuth;
import top.lothar.o2o.exceptions.LocalAuthOperationException;

public interface LocalAuthService {
	/**
	 * 通过用户名密码查询--->用户登录验证
	 * @param username
	 * @param password
	 * @return
	 */
	LocalAuth getLocalAuthByUsernameAndPwd(String username, String password);
	/**
	 * 根据userId查询本地账号信息
	 * @param userId
	 * @return
	 */
	LocalAuth getLocalAuthByUserId(long userId);
	/**
	 * 绑定微信，生成平台专属账号
	 * @param localAuth
	 * @return
	 * @throws LocalAuthOperationException
	 */
	LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException;
	/**
	 * 修改平台账号的登录密码
	 * @param userId
	 * @param username
	 * @param password
	 * @param newPassword
	 * @return
	 * @throws LocalAuthOperationException
	 */
	LocalAuthExecution modifyLocalAuth(Long userId, String username, String password, String newPassword) throws LocalAuthOperationException;
}
