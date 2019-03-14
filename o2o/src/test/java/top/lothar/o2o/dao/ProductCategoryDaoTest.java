package top.lothar.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import top.lothar.o2o.BaseTest;
import top.lothar.o2o.entity.ProductCategory;
//该注解按照名称排列test执行顺序ABC...
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductCategoryDaoTest extends BaseTest{
	
	@Autowired
	private ProductCategoryDao productCategoryDao;
	
	@Ignore
	public void testBQueryByShopId() throws Exception{
		long shopId = 1;
		List<ProductCategory> ProductCategoryList = productCategoryDao.queryProductCategoryList(shopId);
		System.out.println("该店铺自定义类别数量:"+ProductCategoryList.size());
	}
	
	@Ignore
	public void testABatchInsertProductCategory() {
		
		ProductCategory productCategoryOne = new ProductCategory();
		productCategoryOne.setProductCategoryName("店铺2商品类别3");
		productCategoryOne.setPriority(2);
		productCategoryOne.setCreateTime(new Date());
		productCategoryOne.setShopId(2L);
		
		ProductCategory productCategoryTow = new ProductCategory();
		productCategoryTow.setProductCategoryName("店铺2商品类别4");
		productCategoryTow.setPriority(1);
		productCategoryTow.setCreateTime(new Date());
		productCategoryTow.setShopId(2L);
		
		List<ProductCategory> productCategoryList = new ArrayList<ProductCategory>();
		productCategoryList.add(productCategoryOne);
		productCategoryList.add(productCategoryTow);
		
		int effectNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
		assertEquals(2, effectNum);
	}
	
	@Test
	public void testCDeleteProductCategory() throws Exception{
		long shopId = 1;
		
		List<ProductCategory> productCategoryList = productCategoryDao.queryProductCategoryList(shopId);
		
		for (ProductCategory pc : productCategoryList) {
			if ("店铺1商品类别4".equals(pc.getProductCategoryName())||"店铺1商品类别5".equals(pc.getProductCategoryName())) {
				int effectNum = productCategoryDao.deleteProductCategory(pc.getProductCategoryId(), shopId);
				assertEquals(1, effectNum);
			}
		}
	}
}
