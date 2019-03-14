package top.lothar.o2o.interceptor;

import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import top.lothar.o2o.entity.PersonInfo;
/**
 * 店家管理系统登录权限拦截
 * @author Lothar
 * 
 * 采用spring自带的interceptors验证
 * 
 * 继承HandlerInterceptorAdapter
 * 
 * 重写preHandle方法
 */
public class ShopLoginInterceptor extends HandlerInterceptorAdapter {
	/*
	 * 主要做事前拦截，即用户操作发生前，改写preHandle里的逻辑，进行拦截 个人理解 ：在调用指定的handler前进行身份拦截
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 先从session中取出用户信息
		Object objectUser = request.getSession().getAttribute("user");
		// 对用户信息进行判断
		if (objectUser != null) {
			// 用户信息不为空，则将其转换为PersonInfo
			PersonInfo user = (PersonInfo) objectUser;
			// 对user进行判断userId和enableStatus
			if (user != null && user.getUserId() > 0 && user.getEnableStatus() == 1) {
				// 如果通过上述的验证步骤，则返回true，可以进行接下来操作
				return true;
			}
		}
		// 不满足验证，则直接跳转到登录页面
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<script>");
		out.println("window.open ('" + request.getContextPath() + "/local/login?usertype=2','_self')");
		out.println("</script>");
		out.println("</html>");
		return false;
	}

}
