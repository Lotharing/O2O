package top.lothar.o2o.web.frontend;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import top.lothar.o2o.entity.Product;
import top.lothar.o2o.service.ProductService;
import top.lothar.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/frontend")
public class ProductDetailController {

	@Autowired
	private ProductService productService;
	/**
	 * 获取商品详情页面信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listproductdetailpageinfo")
	@ResponseBody
	public Map<String, Object> listProductDetailPageInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 获取前端页面传递过来的productId
		long productId = HttpServletRequestUtil.getLong(request, "productId");
		if (productId != -1) {
			Product product = productService.getProductById(productId);
			modelMap.put("success", true);
			modelMap.put("product", product);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty productId");
		}
		return modelMap;
	}
}
