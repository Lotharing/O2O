package top.lothar.o2o.dao;
/**
 * 平台账号操作
 * @author Lothar
 *
 */

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import top.lothar.o2o.entity.LocalAuth;

public interface LocalAuthDao {
	/**
	 * 通过用户名密码查询--->用户登录验证
	 * @param usernameString
	 * @param password
	 * @return
	 */
	LocalAuth queryLocalByUserNameAndPwd(@Param("username")String username , @Param("password")String password);
	/**
	 * 通过用户userId进行查询对应的loacalAuth(有或无)
	 * @param userId
	 * @return
	 */
	LocalAuth queryLocalByUserId(@Param("userId")long userId);
	/**
	 * 添加平台账号
	 * @param localAuth
	 * @return
	 */
	int insertLocalAuth(LocalAuth localAuth);
	/**
	 * 修改平台账号密码
	 * @param userId
	 * @param username
	 * @param password
	 * @param newPassword
	 * @param lastEditTime
	 * @return
	 */
	int updateLocalAuth(@Param("userId")long userId,@Param("username")String username,@Param("password")String password,@Param("newPassword")String newPassword,@Param("lastEditTime")Date lastEditTime);
}
