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
import top.lothar.o2o.entity.Product;
import top.lothar.o2o.entity.ProductCategory;
import top.lothar.o2o.entity.ProductImg;
import top.lothar.o2o.entity.Shop;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductDaoTest extends BaseTest{
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private ProductImgDao productImgDao;
	
	@Ignore
	public void testAInsertProduct() throws Exception{
		Shop shop1 = new Shop();
		shop1.setShopId(1L);
		ProductCategory pc1 = new ProductCategory();
		pc1.setProductCategoryId(1L);
		
		Product product1 = new Product();
		product1.setProductName("测试2");
		product1.setProductDesc("测试2DEsc");
		product1.setImgAddr("test1");
		product1.setPriority(2);
		product1.setEnableStatus(1);
		product1.setCreateTime(new Date());
		product1.setLastEditTime(new Date());
		product1.setShop(shop1);
		product1.setProductCategory(pc1);
		
		Product product2 = new Product();
		product2.setProductName("测试3");
		product2.setProductDesc("测试3DEsc");
		product2.setImgAddr("test3");
		product2.setPriority(3);
		product2.setEnableStatus(1);
		product2.setCreateTime(new Date());
		product2.setLastEditTime(new Date());
		product2.setShop(shop1);
		product2.setProductCategory(pc1);
		
		int effectNum = productDao.insertProduct(product1);
		assertEquals(1, effectNum);
		effectNum = productDao.insertProduct(product2);
		assertEquals(1, effectNum);
	}
	
	@Ignore
	public void testBQueryProductList() {
		Product productCondition = new Product();
		List<Product> productList = productDao.queryProductList(productCondition, 0, 3);
		assertEquals(3, productList.size());
		
		int count = productDao.queryProductCount(productCondition);
		assertEquals(5, count);
		
		productCondition.setProductName("第");
		productList = productDao.queryProductList(productCondition, 0, 3);
		assertEquals(2, productList.size());
		
		count = productDao.queryProductCount(productCondition);
		assertEquals(2, count);
		
	}
	
	@Ignore
	public void testCQueryProductByProductId() throws Exception {
		long productId = 4;
		
		ProductImg productImg1 = new ProductImg();
		productImg1.setImgAddr("图片1");
		productImg1.setImgDesc("测试图片1");
		productImg1.setPriority(1);
		productImg1.setCreateTime(new Date());
		productImg1.setProductId(productId);
		
		ProductImg productImg2 = new ProductImg();
		productImg2.setImgAddr("图片2");
		productImg2.setImgDesc("测试图片2");
		productImg2.setPriority(2);
		productImg2.setCreateTime(new Date());
		productImg2.setProductId(productId);
		
		List<ProductImg> productImgs = new ArrayList<ProductImg>();
		productImgs.add(productImg1);
		productImgs.add(productImg2);
		
		int effectNum = productImgDao.bacthInsertProductImg(productImgs);
		assertEquals(2, effectNum);
		//查询product = 4 的下边有几张详情图
		Product product = productDao.queryProductById(productId);
		assertEquals(2, product.getProductImgList().size());
	}
	
	@Ignore
	public void testDUpdateProdut() throws Exception{
		Product product = new Product();
		ProductCategory pc = new ProductCategory();
		Shop shop = new Shop();
		
		shop.setShopId(1L);
		pc.setProductCategoryId(2L);
		
		product.setProductId(1L);
		product.setShop(shop);
		product.setProductName("第一个商品");
		product.setProductCategory(pc);
		
		int effectNum = productDao.updateProduct(product);
		assertEquals(1,effectNum);
	}
	
	@Test
	public void testEUpdateProductCategoryToNull() {
		int effectNum = productDao.updateProductCategoryToNull(2L);
		assertEquals(2, effectNum);
	}
}
