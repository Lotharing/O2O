package top.lothar.o2o.web.local;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import top.lothar.o2o.dto.LocalAuthExecution;
import top.lothar.o2o.entity.LocalAuth;
import top.lothar.o2o.entity.PersonInfo;
import top.lothar.o2o.enums.LocalAuthStateEnum;
import top.lothar.o2o.exceptions.LocalAuthOperationException;
import top.lothar.o2o.service.LocalAuthService;
import top.lothar.o2o.util.CodeUtil;
import top.lothar.o2o.util.HttpServletRequestUtil;
import top.lothar.o2o.util.MD5;

@Controller
@RequestMapping(value="local",method = {RequestMethod.GET,RequestMethod.POST})
public class LocalAuthController {
	
	@Autowired
	private LocalAuthService localAuthService;
	/**
	 * 微信绑定本地账号操作
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/bindlocalauth",method = RequestMethod.POST)
	private Map<String, Object> bindLocalAuth(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//验证码校验
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "验证码输入错误");
		}
		//获取前台传过来的账号和密码以及session中的user信息
		String username = HttpServletRequestUtil.getString(request, "userName");
		String password = HttpServletRequestUtil.getString(request, "password");
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		//非空判断
		if (username != null && password != null && user != null && user.getUserId() != null) {
			//创建LocalAuth对象并赋值
			LocalAuth localAuth = new LocalAuth();
			localAuth.setUsername(username);
			localAuth.setPassword(password);
			localAuth.setPersonInfo(user);
			//绑定账号
			LocalAuthExecution localAuthExecution = localAuthService.bindLocalAuth(localAuth);
			if (localAuthExecution.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
				modelMap.put("success", true);
			}else {
				modelMap.put("success", false);
				modelMap.put("errMshg", localAuthExecution.getStateInfo());
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "用户名密码不能为空");
		}
		return modelMap;
	}
	/**
	 * 修改密码
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/changelocalpwd",method = RequestMethod.POST)
	private Map<String, Object> changeLocalPwd(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String,Object>();
		//验证码校验
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "验证码输入错误");
		}
		//从session中获取用户信息,用户一旦经过微信扫码登录就能取到用户信息，之前进行无缝连接（实现自动注册）
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user"); 
		//获取用户名
		String username = HttpServletRequestUtil.getString(request, "userName");
		//获取用户密码
		String password = HttpServletRequestUtil.getString(request, "password");
		//获取新密码
		String newpassword = HttpServletRequestUtil.getString(request, "newPassword");
		//非空判断
		if (username != null && password != null && newpassword != null && user != null && user.getUserId() != null
				&& !password.equals(newpassword)) {
			try {
				// 查看原先帐号，看看与输入的帐号是否一致，不一致则认为是非法操作
				LocalAuth localAuth = localAuthService.getLocalAuthByUserId(user.getUserId());
				//账号是否与数据库中一致
				if (localAuth == null || !localAuth.getUsername().equals(username)) {
					modelMap.put("success", false);
					modelMap.put("errMsg", "输入的账号非本次登录的账号");
				}
				//修改平台账号密码
				LocalAuthExecution localAuthExecution = localAuthService.modifyLocalAuth(user.getUserId(), username, password, newpassword);
				if (localAuthExecution.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", localAuthExecution.getStateInfo());
				}
			} catch (LocalAuthOperationException e) {
				// TODO: handle exception
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入密码");
		}
		return modelMap;
	}
	/**
	 * 用户登录
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/logincheck", method = RequestMethod.POST)
	private Map<String, Object> logincheck(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 获取是否需要进行验证码校验的标识符（用于三次输入密码之后开始填写验证码）
		boolean needVerify = HttpServletRequestUtil.getBoolean(request, "needVerify");
		if (needVerify && !CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		// 获取输入的帐号
		String userName = HttpServletRequestUtil.getString(request, "userName");
		// 获取输入的密码
		String password = HttpServletRequestUtil.getString(request, "password");
		// 非空校验
		if (userName != null && password != null) {
			// 传入帐号和密码去获取平台帐号信息
			LocalAuth localAuth = localAuthService.getLocalAuthByUsernameAndPwd(userName, password);
			if (localAuth != null) {
				// 若能取到帐号信息则登录成功
				modelMap.put("success", true);
				// 同时在session里设置用户信息
				request.getSession().setAttribute("user", localAuth.getPersonInfo());
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "用户名或密码错误");
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "用户名和密码均不能为空");
		}
		return modelMap;
	}
	/**
	 * 退出登录，注销session
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	private Map<String, Object> logout(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 将用户session置为空
		request.getSession().setAttribute("user", null);
		modelMap.put("success", true);
		return modelMap;
	}
	
}
