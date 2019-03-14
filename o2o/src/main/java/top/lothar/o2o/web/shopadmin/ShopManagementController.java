package top.lothar.o2o.web.shopadmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.fabric.xmlrpc.base.Array;

import top.lothar.o2o.dto.ImageHolder;
import top.lothar.o2o.dto.ShopExecution;
import top.lothar.o2o.entity.Area;
import top.lothar.o2o.entity.PersonInfo;
import top.lothar.o2o.entity.Shop;
import top.lothar.o2o.entity.ShopCategory;
import top.lothar.o2o.enums.ShopStateEnum;
import top.lothar.o2o.exceptions.ShopOperationException;
import top.lothar.o2o.service.AreaService;
import top.lothar.o2o.service.ShopCategoryService;
import top.lothar.o2o.service.ShopService;
import top.lothar.o2o.util.CodeUtil;
import top.lothar.o2o.util.HttpServletRequestUtil;
import top.lothar.o2o.web.wechat.WechatLoginController;
/**
 * 处理店铺提交的信息
 * @author Lothar
 *
 */
@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {
	
	private static Logger log = LoggerFactory.getLogger(ShopManagementController.class);
	@Autowired
	private ShopService shopService;
	
	@Autowired
	private ShopCategoryService shopCategoryService;
	
	@Autowired
	private AreaService areaService;
	
	/**
	 * 根据ShopId获取店铺信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getshopbyid",method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopById(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String,Object>();
		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		
		if (shopId > -1) {
			try {
				Shop shop = shopService.getByShopId(shopId);
				List<Area> areaList = areaService.getAreaList();
				modelMap.put("shop", shop);
				modelMap.put("areaList", areaList);
				modelMap.put("success", true);
			} catch (Exception e) {
				// TODO: handle exception
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty ShopId");
		}
		return modelMap;
	}
	/**
	 * 查找店铺类别列表信息 | 区域列表信息
	 * 返回给前台
	 * @return
	 */
	@RequestMapping(value="/getshopinitinfo",method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopInitInfo(){
		Map<String, Object> modelMap = new HashMap<String,Object>();
		List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
		List<Area> areaList = new ArrayList<Area>();
		
		try {
			//获取数据库中ShopCategory的二级店铺列表,不携带parentid的信息，也就是完全二级店铺信息
			shopCategoryList = shopCategoryService.queryShopCategory(new ShopCategory());
			areaList = areaService.getAreaList();
			modelMap.put("shopCategoryList", shopCategoryList);
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);
		} catch (Exception e) {
			// TODO: handle exception
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		return modelMap;
	}
	/**
	 * 注册店铺信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/registershop",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> registerShop(HttpServletRequest request){
		
		Map<String, Object> modelMap = new HashMap<String, Object>();
		
		//判断验证码 1.错误返回给前端信息 Ajax中data.errMsg得到展示  2.正确 到下一步后台获取注册信息 
		if(!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "验证码输入错误");
			return modelMap;
		}
		//1.接受并转化相应的参数，包括店铺信息和图片信息（在验证码正确的前提下才验证通过可以数据注册）
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		
		try {
			//将前台表单中的值存放在shop对象中
			shop = mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		/*
		 * 处理图片
		 */
		CommonsMultipartFile shopImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		//是否有上传的文件流
		if (commonsMultipartResolver.isMultipart(request)) {
			//强制转换提取文件流
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "上传图片不能为空");
			return modelMap;
		}
		// 2.注册店铺
		if (shop != null && shopImg != null) {
			//以下硬编码  ，后边从seessiob中获取
//			PersonInfo user=new PersonInfo();
//			user.setUserId(1L);
//			user.setName("测试");
//			request.getSession().setAttribute("user", user);
			
			//session去获取当前登录用户
			PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");
			shop.setOwner(owner);
			ShopExecution se;
			try {
				ImageHolder imageHolder=new ImageHolder(shopImg.getOriginalFilename(), shopImg.getInputStream());
				se = shopService.addShop(shop, imageHolder);
				if (se.getState() == ShopStateEnum.CHECK.getState()) {
					modelMap.put("success", true);
					// 从session中取出--该用户能操作的店铺列表
					@SuppressWarnings("unchecked")
					List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
					if (shopList == null || shopList.size() == 0) {
						shopList = new ArrayList<Shop>();
					}
					//se.getShop是从第一次注册时候成功，存入se对象中一个shop
					shopList.add(se.getShop());
					//存放session用于前台展示
					request.getSession().setAttribute("shopList", shopList);

				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (ShopOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺信息");
			return modelMap;
		}
	}
	/**
	 * 修改店铺信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/modifyshop", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入验证码错误");
			return modelMap;
		}
		// 1.接受并转化相应的参数，包裹商店信息和图片信息
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr"); // 接受前端传过来的数据
		// 转换为实体类
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {
			shop = mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		/*
		 * 处理图片
		 */
		CommonsMultipartFile shopImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		}
		// 2.修改店铺
		if (shop != null && shop.getShopId() != null) {
			ShopExecution se;
			try {
				if (shopImg == null) {
					se = shopService.modifyShop(shop,null);
				} else {
					ImageHolder imageHolder=new ImageHolder(shopImg.getOriginalFilename(), shopImg.getInputStream());
					se = shopService.modifyShop(shop,imageHolder);
				}
				if (se.getState() == ShopStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (ShopOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺ID");
			return modelMap;
		}
	}
	/**
	 * 从shopList中通过点击获得shopId,从而就可以走这里
	 * 如果不是就是违规操作
	 * 判断是否是违规操作，ture直接跳转到shoplist展示页
	 * @param request
	 * @return
	 */
	@RequestMapping(value="getshopmanagementinfo",method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopManagementInfo(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String,Object>();
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if(shopId < 0) {
			//没有传过来时候先看看session中是否已经存在
			Object currentShopObj = request.getSession().getAttribute("currentShop");
			if (currentShopObj == null) {
				modelMap.put("redirect", true);
				modelMap.put("url", "/o2o/shopadmin/shoplist");
			}else {
				Shop currentShop = (Shop)currentShopObj;
				modelMap.put("redirect", false);
				modelMap.put("shopId", currentShop.getShopId());
			}
		}else {
			Shop currentShop = new Shop();
			currentShop.setShopId(shopId);
			request.getSession().setAttribute("currentShop", currentShop);
			modelMap.put("redirect", false);
		}
		return modelMap;
	}
	/**
	 * 1.根据用户信息取寻找所拥有的店铺
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getshoplist",method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopList(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String,Object>();
		
		
//		user.setUserId(6L);
//		user.setName("test");
//		request.getSession().setAttribute("user", user);
		
		//以上硬编码，在之前local中login中session中已经存入user
		PersonInfo user = (PersonInfo)request.getSession().getAttribute("user");
		log.debug("在getShopList中session获取user"+"UserId为==================================="+user.getUserId());
		try {
			Shop shopCondition = new Shop();
			shopCondition.setOwner(user);
			ShopExecution se = shopService.getShopList(shopCondition, 0, 100);
			modelMap.put("shopList", se.getShopList());
			modelMap.put("user", user);
			modelMap.put("success", true);
		} catch (Exception e) {
			// TODO: handle exception
			modelMap.put("success", false);
			modelMap.put("errMSg", e.getMessage());
		}
		return modelMap;
	}
	
}
