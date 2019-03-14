package top.lothar.o2o.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import top.lothar.o2o.dao.ProductCategoryDao;
import top.lothar.o2o.dao.ProductDao;
import top.lothar.o2o.dto.ProductCategoryExecution;
import top.lothar.o2o.entity.ProductCategory;
import top.lothar.o2o.enums.ProductCategoryStateEnum;
import top.lothar.o2o.exceptions.ProductCategoryOperationException;
import top.lothar.o2o.service.ProductCategoryService;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private ProductCategoryDao productCategoryDao;

	@Override
	public List<ProductCategory> getProductCategoryList(Long shopId) {
		// TODO Auto-generated method stub
		return productCategoryDao.queryProductCategoryList(shopId);
	}

	@Override
	@Transactional
	public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList)
			throws ProductCategoryOperationException {
		// TODO Auto-generated method stub
		if (productCategoryList!=null&&productCategoryList.size()>0) {
			try {
				int effectNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
				if (effectNum<0) {
					throw new ProductCategoryOperationException("店铺类别创建失败");
				}else {
					return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
				}
			} catch (Exception e) {
				// TODO: handle exception
				throw new ProductCategoryOperationException("batchAddProductCategory error:"+e.getMessage());
			}
		}else {
			return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
		}
	}
	

	@Override
	@Transactional
	public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId)
			throws ProductCategoryOperationException {
		//删除商品类别的同时将此商品下的类别ID置为空
		try {
			int effectNum = productDao.updateProductCategoryToNull(productCategoryId);
			if (effectNum < 0) {
				throw new ProductCategoryOperationException("商品类别更新失败");
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw new ProductCategoryOperationException("deleteProductCategory error:"+e.getMessage());
		}
		//删除productCategory
		try {
			int effectNum = productCategoryDao.deleteProductCategory(productCategoryId, shopId);
			if (effectNum<=0) {
				throw new ProductCategoryOperationException("店铺类别删除失败");
			}else {
				//Controller层验证是否成功用
				return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw new ProductCategoryOperationException("deleteProductCategory error:"+e.getMessage());
		}
	}

}
