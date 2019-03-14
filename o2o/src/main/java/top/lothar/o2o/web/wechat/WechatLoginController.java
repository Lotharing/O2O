package top.lothar.o2o.web.wechat;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import top.lothar.o2o.dto.UserAccessToken;
import top.lothar.o2o.dto.WechatAuthExecution;
import top.lothar.o2o.dto.WechatUser;
import top.lothar.o2o.entity.PersonInfo;
import top.lothar.o2o.entity.WechatAuth;
import top.lothar.o2o.enums.WechatAuthStateEnum;
import top.lothar.o2o.service.PersonInfoService;
import top.lothar.o2o.service.WechatAuthService;
import top.lothar.o2o.util.wechat.WechatUtil;
/*
 *  获取关注公众号之后的微信用户信息的接口，如果在微信浏览器里访问
 * redirect_url是访问微信指定的url后回调的一个地址
 *  回调内容带信息，即回调时会传过来一个code---->access_token---->获取openId---->获取用户信息----->最终去操作DB
 * https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxd0689996c13262bc&redirect_uri=http://39.96.21.162/o2o/wechatlogin/logincheck&role_type=1&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect
 * https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxd0689996c13262bc&redirect_uri=http://www.lothar.top/o2o/wechatlogin/logincheck&role_type=1&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect
 *  则这里将会获取到code,之后再可以通过code获取到access_token 进而获取到用户信息
 * 
 */
@Controller
@RequestMapping("/wechatlogin")
public class WechatLoginController {
	
	private static Logger log = LoggerFactory.getLogger(WechatLoginController.class);
	//定义常量  用于判断用户点击后的动向
	private static final String FRONTEND = "1";//前台管理系统
	private static final String SHOPEND = "2";//店家管理系统
	@Autowired
	private PersonInfoService personInfoService;
	@Autowired
	private WechatAuthService wechatAuthService;
	
	@RequestMapping(value="/logincheck",method = {RequestMethod.GET})
	public String doGet(HttpServletRequest request , HttpServletResponse response) {
		log.debug("weixin login get ...");
		// 获取微信公众号传输过来的code,通过code可获取access_token,进而获取用户信息
		String code = request.getParameter("code");
		// 这个state可以用来传我们自定义的信息，方便程序调用，这里也可以不用
		// 1.前端展示页面1  2.店家管理系统2	公众号中的两个按钮入口
		String roleType = request.getParameter("state");
		log.debug("weixin login code:" + code);
		//微信用户的实际信息
		WechatUser user = null;
		String openId = null;
		WechatAuth auth = null;
		if (code != null) {
			UserAccessToken token = null;
			try {
				//1.通过code获取access_token
				token = WechatUtil.getUserAccessToken(code);
				log.debug("weixin login token ..."+token.toString());
				//2.通过token获取access_token
				String accessToken = token.getAccessToken();
				//3.通过token获取openId
				openId = token.getOpenId();
				//4.通过accessToken和openId获取用户信息
				user = WechatUtil.getUserInfo(accessToken, openId);
				log.debug("weixin login user ..."+user.toString());
				//将openId放入session对象中去
				request.getSession().setAttribute("openId", openId);
				//通过openId获取微信用户信息
				auth = wechatAuthService.getWechatAuthByOpenId(openId);
			} catch (IOException e) {
				// TODO: handle exception
				log.debug("error in getUserAccessToken or getUserInfo findByopenId"+e.toString());
				e.printStackTrace();
			}
		}
		//对上边获取的微信账号信息，进行判断，如果DB中没有查到信息，则注册微信账号信息，并且同时注册用户信息
		if (auth == null) {
			//把WechatUser的信息转换到PersonInfo中,并存储数据库wechat_auth信息
			PersonInfo personInfo = WechatUtil.getPersonInfoFromRequest(user);
			//微信号信息
			auth = new WechatAuth();
			auth.setOpenId(openId);
			//判断是展示系统还是管理系统
			if (FRONTEND.equals(roleType)) {
				personInfo.setUserType(1);
			}else {
				personInfo.setUserType(2);
			}
			//就是微信号信息userId
			auth.setPersonInfo(personInfo);
			//根据auth的信息，如果没有userId表示第一次微信登录,同时把personInfo的信息也插入数据库（本地用户信息）
			WechatAuthExecution we = wechatAuthService.register(auth);
			//wechat_authe 和 person_info 信息插入成功则将person_info信息存入session中
			if (we.getState()!=WechatAuthStateEnum.SUCCESS.getState()) {
				return null;
			}else {
				personInfo = personInfoService.getPersonInfoById(auth.getPersonInfo().getUserId());
				request.getSession().setAttribute("user", personInfo);
				PersonInfo test = (PersonInfo) request.getSession().getAttribute("user");
				log.debug("微信登录成功之后，personinfo和wechatauth都存储以后"+"UserID为=============="+test.getUserId());
			}
		}
		//展示端，管理端的控制（就是当auth不为null的时候，就表示有该微信账号直接跳转）
		if (FRONTEND.equals(roleType)) {
			return "frontend/index";
		}else {
			return "shop/shoplist";
		}
		/**
		 * ---------start
		 * 实现：获取到openId之后，可以通过它与数据库相互判断，微信账号在此网站是否已经有对应账号
		 * 没有直接创建，实现微信与网站的无缝连接
		 * ---------end
		 */
	}

}
