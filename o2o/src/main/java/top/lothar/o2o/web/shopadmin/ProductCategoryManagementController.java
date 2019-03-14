package top.lothar.o2o.web.shopadmin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import top.lothar.o2o.dto.ProductCategoryExecution;
import top.lothar.o2o.dto.Result;
import top.lothar.o2o.entity.ProductCategory;
import top.lothar.o2o.entity.Shop;
import top.lothar.o2o.enums.ProductCategoryStateEnum;
import top.lothar.o2o.exceptions.ProductCategoryOperationException;
import top.lothar.o2o.service.ProductCategoryService;

@Controller
@RequestMapping("/shopadmin")
public class ProductCategoryManagementController {

	@Autowired
	private ProductCategoryService productCategoryService;

	/**
	  * 根据店铺id得到，店铺下所有商品信息
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getproductcategorylist", method = RequestMethod.GET)
	public Result<List<ProductCategory>> getProductCategoryList(HttpServletRequest request) {
		// 从登陆进来时候存储的currentShop对象-->ShopManagementcontroller中的getShopManagementInfo方法
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		List<ProductCategory> list = null;
		if (currentShop != null && currentShop.getShopId() > 0) {
			list = productCategoryService.getProductCategoryList(currentShop.getShopId());
			return new Result<List<ProductCategory>>(true, list);
		} else {
			ProductCategoryStateEnum ps = ProductCategoryStateEnum.INNER_ERROR;
			return new Result<List<ProductCategory>>(false, ps.getState(), ps.getStateInfo());
		}
	}

	/**
	  * 批量添加商品类别
	 * 
	 * @param productCategoryList
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addproductcategorys", method = { RequestMethod.POST, RequestMethod.GET })
	private Map<String, Object> addProductCategorys(@RequestBody List<ProductCategory> productCategoryList,
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		for (ProductCategory pc : productCategoryList) {
			pc.setShopId(currentShop.getShopId());
		}
		if (productCategoryList != null && productCategoryList.size() > 0) {
			try {
				ProductCategoryExecution pe = productCategoryService.batchAddProductCategory(productCategoryList);
				if (pe.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			} catch (ProductCategoryOperationException e) {
				// TODO: handle exception
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入至少一个商品类别");
		}
		return modelMap;
	}
	/**
	  *  删除指定的商品类别
	 * @param productCategoryId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/removeproductcategory", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> removeProductCategory(Long productCategoryId, HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (productCategoryId != null && productCategoryId > 0) {
			try {
				Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
				ProductCategoryExecution pe = productCategoryService.deleteProductCategory(productCategoryId,
						currentShop.getShopId());
				if (pe.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			} catch (ProductCategoryOperationException e) {
				// TODO: handle exception
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请至少选择一个商品类别");
		}
		return modelMap;
	}
	
}
