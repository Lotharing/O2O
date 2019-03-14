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
import top.lothar.o2o.entity.ProductImg;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductImgDaoTest extends BaseTest{
	
	@Autowired
	private ProductImgDao productImgDao;
	
	@Ignore
	public void testABatchInsertProductImg() throws Exception{
		
		ProductImg productImgOne = new ProductImg();
		productImgOne.setImgAddr("图片1");
		productImgOne.setImgDesc("测试图片1");
		productImgOne.setPriority(1);
		productImgOne.setCreateTime(new Date());
		productImgOne.setProductId(2L);
		
		ProductImg productImgTwo = new ProductImg();
		productImgTwo.setImgAddr("图片2");
		productImgTwo.setImgDesc("测试图片2");
		productImgTwo.setPriority(2);
		productImgTwo.setCreateTime(new Date());
		productImgTwo.setProductId(2L);
		
		List<ProductImg> productImgList = new ArrayList<ProductImg>();
		productImgList.add(productImgOne);
		productImgList.add(productImgTwo);
		
		int effectNum = productImgDao.bacthInsertProductImg(productImgList);
		assertEquals(2, effectNum);
	}
	@Ignore
	public void testBQueryProductImgList() {
		long productId = 5;
		List<ProductImg> productImgs = productImgDao.queryProductImgList(productId);
		System.out.println(productImgs.size());
		System.out.println(productImgs.get(0).getProductId());
	}
	@Test
	public void testCDeleteProductImgByProductId() throws Exception{
		long productId = 6;
		int effectNum = productImgDao.deleteProductImgByProductId(productId);
		assertEquals(2, effectNum);
		
	}
}
