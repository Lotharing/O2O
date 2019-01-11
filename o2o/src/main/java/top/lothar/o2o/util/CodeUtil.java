package top.lothar.o2o.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 判断填入的验证码是否符合kapcha自动生成的验证码
 * @author Lothar
 *
 */
public class CodeUtil {
	/**
	 * 验证码判断
	 * @param request
	 * @return
	 */
	@SuppressWarnings("null")
	public static boolean checkVerifyCode(HttpServletRequest request) {
		//生成的验证码，开源工具中封装的生成在当前Session中存放
		String verityCodeExpected = (String)request.getSession().getAttribute(
				com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		//输入的验证码
		String verityCodeActual = HttpServletRequestUtil.getString(request, "verifyCodeActual");
		//比对
		if(verityCodeActual == null && !verityCodeActual.equals(verityCodeExpected)) {
			return false;
		}
		return true;
	}
}
