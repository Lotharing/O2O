package top.lothar.o2o.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/frontend")
public class FrontendController {
	/**
	 * o2o主页
	 * @return
	 */
	@RequestMapping(value="/index",method = RequestMethod.GET)
	private String index() {
		return "frontend/index";
	}
	/**
	 * 商品类别页
	 * @return
	 */
	@RequestMapping(value="/shoplist",method = RequestMethod.GET)
	private String shopList() {
		return "frontend/shoplist";
	}
	/**
	 * 店铺详情页
	 * @return
	 */
	@RequestMapping(value="shopdetail",method = RequestMethod.GET)
	private String showShopDetail() {
		return "/frontend/shopdetail";
	}
	/**
	 * 商品详情页
	 * @return
	 */
	@RequestMapping(value="productdetail",method = RequestMethod.GET)
	private String showProductDetail() {
		return "/frontend/productdetail";
	}
}
