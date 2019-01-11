package top.lothar.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import top.lothar.o2o.BaseTest;
import top.lothar.o2o.entity.Area;
import top.lothar.o2o.entity.PersonInfo;
import top.lothar.o2o.entity.Shop;
import top.lothar.o2o.entity.ShopCategory;

public class ShopDaoTest extends BaseTest{
	
	@Autowired
	private ShopDao shopDao;
	
	@Ignore
	public void testQueryShopList() {
		Shop shopCondition = new Shop();
		PersonInfo owner = new PersonInfo();
		owner.setUserId(1L);
		shopCondition.setOwner(owner);
		List<Shop> shopList = shopDao.queryShopList(shopCondition, 0, 5);
		int count = shopDao.queryShopCount(shopCondition);
		System.out.println("owner为1的店铺使用Limit取出的数量"+shopList.size());
		System.out.println("一共有"+count+"条");
	}
	
	@Ignore
	public void testQueryArea() {
		Shop shopCondition = new Shop();
		Area area = new Area();
		area.setAreaId(1);
		shopCondition.setArea(area);
		List<Shop> shopList = shopDao.queryShopList(shopCondition, 0,5);
		int count = shopDao.queryShopCount(shopCondition);
		System.out.println("查询几条limit限制区域店铺"+shopList.size());
		System.out.println("查询areaId为1的店铺数量共有"+count);
	}
	
	
	@Ignore
	public void testQueryCount() {
		Shop shopCondition = new Shop();
		ShopCategory sc = new ShopCategory();
		sc.setShopCategoryId(1L);
		shopCondition.setShopCategory(sc);
		List<Shop> shopList = shopDao.queryShopList(shopCondition, 0,2);
		int count = shopDao.queryShopCount(shopCondition);
		System.out.println("查询几条limit限制店铺"+shopList.size());
		System.out.println("查询shopcategoryId为1的店铺数量共有"+count);
	}
	
	@Ignore
	public void testSelectShop() {
		long shopId = 1;
		Shop shop = shopDao.queryByShopId(shopId);
		System.out.println("areaID"+shop.getArea().getAreaId());
		System.out.println("areaName"+shop.getArea().getAreaName());

		System.out.println("shopCategoryId"+shop.getShopCategory().getShopCategoryId());
		System.out.println("shopCategoryId"+shop.getShopCategory().getShopCategoryName());
	}
	
	@Ignore
	public void testInsertShop() {
		Shop shop = new Shop();
		PersonInfo owner = new PersonInfo();
		Area area = new Area();
		ShopCategory shopCategory = new ShopCategory();
		owner.setUserId(1L);
		area.setAreaId(1);
		shopCategory.setShopCategoryId(1L);
		shop.setOwner(owner);
		shop.setArea(area);
		shop.setShopCategory(shopCategory);
		shop.setShopName("测试的店铺");
		shop.setShopDesc("test");
		shop.setShopAddr("LuoYang");
		shop.setPhone("18438655091");
		shop.setShopImg("test");
		shop.setPriority(1);
		shop.setCreateTime(new Date());
		shop.setEnableStatus(1);
		shop.setAdvice("审核中");
		int effectedNum = shopDao.insertShop(shop);
		assertEquals(1, effectedNum);
	}
	
	@Test
	public void testUpdateShop() {
		Shop shop = new Shop();
		//更新第一条信息
		shop.setShopId(1L);
		shop.setShopName("店铺1");
		shop.setShopDesc("测试更新");
		shop.setLastEditTime(new Date());
		int effectedNum = shopDao.updateShop(shop);
		assertEquals(1, effectedNum);
	}
	
}
