package top.lothar.o2o.service;

import java.util.List;

import top.lothar.o2o.dto.ProductCategoryExecution;
import top.lothar.o2o.entity.ProductCategory;
import top.lothar.o2o.exceptions.ProductCategoryOperationException;

public interface ProductCategoryService {
	/**
	 * 查询指定shopid店铺下的所有商品类别信息
	 * 
	 * @param shopId
	 * @return
	 */
	List<ProductCategory> getProductCategoryList(Long shopId);

	/**
	 * 批量店家商品
	 * 
	 * @param productCategoriyList
	 * @return
	 * @throws ProductCategoryOperationException
	 */
	ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoriyList)
			throws ProductCategoryOperationException;
	/**
	 *  删除指定商品类别（将此类别下的商品类别id置为空，在进行删除，防止商品已经挂载）
	 * @param productCategoryId
	 * @param shopId
	 * @return
	 * @throws ProductCategoryOperationException
	 */
	ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId)
			throws ProductCategoryOperationException;
}
