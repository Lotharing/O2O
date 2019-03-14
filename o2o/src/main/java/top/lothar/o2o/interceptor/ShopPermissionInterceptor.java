package top.lothar.o2o.interceptor;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import top.lothar.o2o.entity.Shop;
/**
 * 店家管理系统操作权限验证
 * @author Lothar
 *
 */
public class ShopPermissionInterceptor extends HandlerInterceptorAdapter{
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//从session中获取当前操作店铺currentShop
		Shop currentShop= (Shop)request.getSession().getAttribute("currentShop");
		//从session中当前操作的店铺列表shopList
		@SuppressWarnings("unchecked")
		List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
		//进行空值判断
		if (currentShop != null && shopList != null) {
			// 遍历可操作的店铺列表
			for (Shop shop : shopList) {
				// 如果当前店铺在可操作的列表里则返回true，进行接下来的用户操作
				if (shop.getShopId() == currentShop.getShopId()) {
					return true;
				}
			}
		}
		// 若不满足拦截器的验证则返回false,终止用户操作的执行
		return false;
	}

}
