package top.lothar.o2o.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
/**
  * 跳转到店铺操作
  * 视图解析器解析
 * @author Lothar
 *
 */
@Controller
@RequestMapping(value="shopadmin",method=RequestMethod.GET)
public class ShopAdminController {
	/**
	 * 商店注册页
	 * @return
	 */
	@RequestMapping(value="shopoperation")
	public String shopOperation() {
		return "shop/shopoperation";
	}
	/**
	 * 商店列表页
	 * @return
	 */
	@RequestMapping(value="shoplist")
	public String shoplist(){
		//转发至商铺列表页面
		return "shop/shoplist";
	}
	/**
	 * 店铺管理页
	 * @return
	 */
	@RequestMapping(value = "/shopmanagement")
	public String shopManagement() {
		// 转发至店铺管理页面
		return "shop/shopmanagement";
	}
	/**
	 * 跳转至类别管理页面	
	 * @return
	 */
	@RequestMapping(value="/productcategorymanagement",method=RequestMethod.GET)
	public String productcategorymanagement(){
		return "shop/productcategorymanagement";
	}
	/**
	 * 转发至商品添加编辑页面
	 * @return
	 */
	@RequestMapping(value="/productoperation")
	public String productOperation() {
		return "shop/productoperation";
	}
	
	@RequestMapping(value="/productmanagement")
	public String productManagement() {
		return "shop/productmanagement";
	}
}
