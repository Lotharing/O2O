package top.lothar.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import top.lothar.o2o.BaseTest;
import top.lothar.o2o.entity.ShopCategory;

public class ShopCategoryDaoTest extends BaseTest{
	
	@Autowired
	private ShopCategoryDao shopCategoryDao;
	
	@Test
	public void testQueryShopCategory() {
		//查找数据库中有几个这样的对象
		List<ShopCategory> shopCategoryList = shopCategoryDao.queryShopCategory(null);
		System.out.println(shopCategoryList.size());
//		assertEquals(2, shopCategoryList.size());
//		
//		ShopCategory testCategory = new ShopCategory();
//		ShopCategory parentCategory = new ShopCategory();
//		
//		//查找数据库中有Parent为1的对象
//		parentCategory.setShopCategoryId(1L);
//		testCategory.setParent(parentCategory);
//		shopCategoryList = shopCategoryDao.queryShopCategory(testCategory);
//		assertEquals(1, shopCategoryList.size());
//		System.out.println(shopCategoryList.get(0).getShopCategoryName());
		
	}
}
