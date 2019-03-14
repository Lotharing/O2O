package top.lothar.o2o.web.frontend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import top.lothar.o2o.dto.ProductExecution;
import top.lothar.o2o.entity.Product;
import top.lothar.o2o.entity.ProductCategory;
import top.lothar.o2o.entity.Shop;
import top.lothar.o2o.service.ProductCategoryService;
import top.lothar.o2o.service.ProductService;
import top.lothar.o2o.service.ShopService;
import top.lothar.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/frontend")
public class ShopDetailController {
	
	@Autowired
	private ShopService shopService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductCategoryService  productCategoryService;
	/**
	 * 获取店铺信息和商品类别信息
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/listshopdetailpageinfo",method = RequestMethod.GET)
	public Map<String, Object> listShopDetailPageInfo(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//从请求中获取ShopId
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		List<ProductCategory> productCategoryList;
		Shop shop = null;
		if (shopId != 1) {
			productCategoryList = productCategoryService.getProductCategoryList(shopId);
			shop = shopService.getByShopId(shopId);
			modelMap.put("success", true);
			modelMap.put("productCategoryList", productCategoryList);
			modelMap.put("shop", shop);
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "Empty ShopId");
		}
		return modelMap;
	}
	/**
	 * 依据分页信息查询商品列表
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/listproductsbyshop",method = RequestMethod.GET)
	public Map<String, Object> listProductsByShop(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String,Object>();
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if ((shopId > -1) && (pageSize > -1) && (pageIndex > -1)) {
			//获取商品类别ID
			long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
			//获取模糊查询商品名称
			String productName = HttpServletRequestUtil.getString(request, "productName");
			//组合查询条件
			Product productCondition = compactProductCondition4Search(shopId, productCategoryId, productName);
			ProductExecution pe = productService.getProductList(productCondition, pageIndex, pageSize);
			modelMap.put("productList", pe.getProductList());
			modelMap.put("count", pe.getCount());
			modelMap.put("success", true);
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "Empty pageSize or pageIndex or shopId");
		}
		return modelMap;
	}
	/**
	 * 依据前台传的信息进行条件组合
	 * @param shopId
	 * @param productId
	 * @param productName
	 * @return
	 */
	public Product compactProductCondition4Search(long shopId, long productCategoryId, String productName) {
		Product productConditionProduct = new Product();
		Shop shop = new Shop();
		shop.setShopId(shopId);
		productConditionProduct.setShop(shop);
		if (productCategoryId!=-1L) {
			//查询某个商品类别下面的商品列表
			ProductCategory productCategory = new ProductCategory();
			productCategory.setProductCategoryId(productCategoryId);
			productConditionProduct.setProductCategory(productCategory);
		}
		if (productName != null) {
			//依据模糊名查询
			productConditionProduct.setProductName(productName);
		}
		//只展示通过审核的商品
		productConditionProduct.setEnableStatus(1);
		return productConditionProduct;
	}
}
