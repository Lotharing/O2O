package top.lothar.o2o.service;

import static org.junit.Assert.assertEquals;

import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import top.lothar.o2o.BaseTest;
import top.lothar.o2o.entity.ShopCategory;
/**
 * 主要测试缓存
 * @author Lothar
 *
 */
public class ShopCategoryServiceTest extends BaseTest{
	
	
	@Autowired
	private ShopCategoryService shopCategoryService;
	
	@Test
	public void testQueryShopCategory(){
		List<ShopCategory> shopCategoryList = shopCategoryService.queryShopCategory(null);
		System.out.println(shopCategoryList.size());
		
		ShopCategory testCategory = new ShopCategory();
		ShopCategory parentCategory = new ShopCategory();
		//查找数据库中有Parent为1的对象
		parentCategory.setShopCategoryId(1L);
		testCategory.setParent(parentCategory);
		shopCategoryList = shopCategoryService.queryShopCategory(testCategory);
		assertEquals(2, shopCategoryList.size());
		System.out.println(shopCategoryList.get(0).getShopCategoryName());
	}
	
}
