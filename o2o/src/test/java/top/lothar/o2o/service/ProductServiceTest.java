package top.lothar.o2o.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import top.lothar.o2o.BaseTest;
import top.lothar.o2o.dao.ProductDao;
import top.lothar.o2o.dto.ImageHolder;
import top.lothar.o2o.dto.ProductExecution;
import top.lothar.o2o.entity.Product;
import top.lothar.o2o.entity.ProductCategory;
import top.lothar.o2o.entity.Shop;
import top.lothar.o2o.enums.ProductStateEnum;

public class ProductServiceTest extends BaseTest{
	
	@Autowired
	private ProductService productService;
	
	@Ignore
	public void testAddProduct() throws FileNotFoundException{
		
		Product product=new Product();
		
		Shop shop=new Shop();
		ProductCategory productCategory=new ProductCategory();
		shop.setShopId(1L);
		productCategory.setProductCategoryId(1L);
		
		product.setShop(shop);
		product.setProductCategory(productCategory);
		product.setCreateTime(new Date());
		product.setLastEditTime(new Date());
		product.setPriority(20);
		product.setEnableStatus(ProductStateEnum.SUCCESS.getState());
		product.setProductDesc("测试店铺");
		product.setProductName("添加缩略图详情图");
		
		File file=new File("D:\\MyImage\\test.jpg");
		InputStream in=new FileInputStream(file);
		ImageHolder imageHolder=new ImageHolder(file.getName(), in);
		
		List<ImageHolder> imageHolderList=new ArrayList<ImageHolder>();
		
		File file1 = new File("D:\\MyImage\\testone.jpg");
		InputStream in1=new FileInputStream(file1);
		
		File file2=new File("D:\\MyImage\\testtwo.jpg");
		InputStream in2=new FileInputStream(file2);
		
		ImageHolder imageHolder1=new ImageHolder(file1.getName(), in1);
		ImageHolder imageHolder2=new ImageHolder(file2.getName(), in2);
		
		imageHolderList.add(imageHolder1);
		imageHolderList.add(imageHolder2);
		//参数分别为，product信息，product缩略图，product详情图组
		ProductExecution addProduct = productService.addProduct(product, imageHolder, imageHolderList);
		if(addProduct.getState()==ProductStateEnum.SUCCESS.getState()){
			System.out.println("junit成功");
		}
	}
	
	@Test
	public void testModifyProduct() throws Exception{
		Product product = new Product();
		product.setProductId(2L);
		product.setProductName("第二个商品");
		Shop shop = new Shop();
		shop.setShopId(1L);
		product.setShop(shop);
		product.setProductDesc("更新商品信息");
		File file1 = new File("D:\\MyImage\\test.jpg");
		InputStream is1 = new FileInputStream(file1);
		ImageHolder imageHolder1 = new ImageHolder(file1.getName(), is1);
		File file2 = new File("D:\\MyImage\\testone.jpg");
		File file3 = new File("D:\\MyImage\\testtwo.jpg");
		InputStream is2 = new FileInputStream(file2);
		InputStream is3 = new FileInputStream(file3);
		ImageHolder imageHolder2 = new ImageHolder(file2.getName(), is2);
		ImageHolder imageHolder3 = new ImageHolder(file3.getName(), is3);
		List<ImageHolder> productImgHolderList = new ArrayList<ImageHolder>();
		productImgHolderList.add(imageHolder2);
		productImgHolderList.add(imageHolder3);
		ProductExecution modifyProduct = productService.modifyProduct(product, imageHolder1, productImgHolderList);
		if (modifyProduct.getState()==ProductStateEnum.SUCCESS.getState()) {
			System.out.println("Junit测试成功");
		}
	}
	
}
