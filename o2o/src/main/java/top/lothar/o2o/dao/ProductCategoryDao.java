package top.lothar.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import top.lothar.o2o.entity.ProductCategory;

public interface ProductCategoryDao {
	/**
	 * 通过shopid查询店铺商品类别,一个店铺对应多个商品List接收
	 * @param shopId
	 * @return
	 */
	List<ProductCategory> queryProductCategoryList(Long shopId);
	/**
	 * 批量新增商品类别
	 * @param productCategorieList
	 * @return
	 */
	int batchInsertProductCategory(List<ProductCategory> productCategorieList);
	/**
	 * 删除指定商品类别
	 * Mybatis识别不出必须用@param注解
	 * @param productCategoryId
	 * @param shopId
	 * @return
	 */
	int deleteProductCategory(@Param("productCategoryId")long productCategoryId,@Param("shopId")long shopId);
	
}
