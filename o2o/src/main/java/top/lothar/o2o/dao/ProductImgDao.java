package top.lothar.o2o.dao;

import java.util.List;

import top.lothar.o2o.entity.ProductImg;

public interface ProductImgDao {
	/**
	 * 批量添加图片
	 * 一个商品对应多个详情图
	 * @param productImgList
	 * @return
	 */
	int bacthInsertProductImg(List<ProductImg> productImgList);
	/**
	 * 删除指定商品下的所有详情图片
	 * @param productId
	 * @return
	 */
	int deleteProductImgByProductId(long productId);
	/**
	 * 查询指定唯一商品详情图
	 * @param productId
	 * @return
	 */
	List<ProductImg> queryProductImgList(Long productId);
}
