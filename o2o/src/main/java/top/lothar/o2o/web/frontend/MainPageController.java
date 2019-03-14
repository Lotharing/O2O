package top.lothar.o2o.web.frontend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import top.lothar.o2o.entity.HeadLine;
import top.lothar.o2o.entity.ShopCategory;
import top.lothar.o2o.service.HeadLineService;
import top.lothar.o2o.service.ShopCategoryService;

@Controller
@RequestMapping("/frontend")
public class MainPageController {
	
	@Autowired
	private ShopCategoryService shopCategoryService;
	
	@Autowired
	private HeadLineService headLineService;
	/**
	 * 初始化前端系统的主页信息，包括获取一级店铺类别列表以及头条列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/listmainpageinfo",method = RequestMethod.GET)
	private Map<String, Object> listMainPageInfo(){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
		try {
			//获取一级店铺列表（即parentId为空的shopCategory）
			shopCategoryList = shopCategoryService.queryShopCategory(null);
			modelMap.put("shopCategoryList", shopCategoryList);
		} catch (Exception e) {
			// TODO: handle exception
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		List<HeadLine> headLineList = new ArrayList<HeadLine>();
		try {
			//获取状态为可用1的头条列表
			HeadLine headLineCondition = new HeadLine();
			headLineCondition.setEnableStatus(1);
			headLineList = headLineService.getHeadLineList(headLineCondition);
			modelMap.put("headLineList", headLineList);
		} catch (Exception e) {
			// TODO: handle exception
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		modelMap.put("success", true);
		return modelMap;
	}
	
}
