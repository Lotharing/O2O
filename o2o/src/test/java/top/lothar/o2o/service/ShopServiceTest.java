package top.lothar.o2o.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import top.lothar.o2o.BaseTest;
import top.lothar.o2o.dto.ImageHolder;
import top.lothar.o2o.dto.ShopExecution;
import top.lothar.o2o.entity.Area;
import top.lothar.o2o.entity.PersonInfo;
import top.lothar.o2o.entity.Shop;
import top.lothar.o2o.entity.ShopCategory;
import top.lothar.o2o.enums.ShopStateEnum;
import top.lothar.o2o.exceptions.ShopOperationException;

public class ShopServiceTest extends BaseTest{
	
	@Autowired
	private ShopService shopService;
	
	@Ignore
	public void testModifyShop() throws ShopOperationException,FileNotFoundException{
		Shop shop = new Shop();
		shop.setShopId(2L);
		shop.setShopName("修改后的店铺名称");
		shop.setLastEditTime(new Date()); 
		
		File shopImg = new File("D:\\MyImage\\head.jpg");
		InputStream inputStream=new FileInputStream(shopImg);
		ImageHolder imageHolder=new ImageHolder("lulu.jpg", inputStream);
		
		ShopExecution shopExecution = shopService.modifyShop(shop, imageHolder);
		//这个实在更新操作完成后返回值传入的枚举值还有shop
		System.out.println("新的图片地址"+shopExecution.getShop().getShopImg());
	}
	
	@Ignore
	public void testAddShop() throws FileNotFoundException {
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
		shop.setShopName("测试的店铺1");
		shop.setShopDesc("test1");
		shop.setShopAddr("LuoYang1");
		shop.setPhone("18438655111");
		shop.setShopImg("test");
		shop.setPriority(1);
		shop.setCreateTime(new Date());
		shop.setEnableStatus(ShopStateEnum.CHECK.getState());
		shop.setAdvice("审核中");
		File shopImg = new File("D:\\MyImage\\head.jpg");
		InputStream inputStream=new FileInputStream(shopImg);
		ImageHolder imageHolder=new ImageHolder("lulu.jpg", inputStream);
		ShopExecution se = shopService.addShop(shop, imageHolder);
		assertEquals(ShopStateEnum.CHECK.getState(), se.getState());
	}
	
	@Test
	public void testGetShopList() {
		Shop shopCondition = new Shop();
		ShopCategory sc = new ShopCategory();
		sc.setShopCategoryId(2L);
		shopCondition.setShopCategory(sc);
		//一页，每页两个
		ShopExecution se = shopService.getShopList(shopCondition, 2, 2);
		System.out.println("店铺列表数为"+se.getShopList().size());
		System.out.println("2L的店铺总数"+se.getCount());
	}
}
