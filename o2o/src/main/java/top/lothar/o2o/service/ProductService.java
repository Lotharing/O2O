package top.lothar.o2o.service;

import java.util.List;

import top.lothar.o2o.dto.ImageHolder;
import top.lothar.o2o.dto.ProductExecution;
import top.lothar.o2o.entity.Product;
import top.lothar.o2o.exceptions.ProductOperationException;

public interface ProductService {
	/**
	  * 添加商品信息以及图片处理
	 * @param product	商品信息
	 * @param thumbnail	商品缩略图
	 * @param productImgList	商品详情图集合
	 * @return
	 * @throws ProductOperationException
	 */
	ProductExecution addProduct(Product product,ImageHolder thumbnail,List<ImageHolder> productImgList) throws ProductOperationException;
	/**
	 *  根据productId查询唯一确定的商品信息
	 * @param productId
	 * @return
	 */
	Product getProductById(Long productId);
	/**
	  * 根据查询出来的信息，通过前端用户修改之后,进行此处商品更新
	 * @param product
	 * @param thumbnail
	 * @param productImgHolderList
	 * @return
	 * @throws ProductOperationException
	 */
	ProductExecution modifyProduct(Product product,ImageHolder thumbnail,List<ImageHolder> productImgHolderList) throws ProductOperationException;
	/**
	  * 查询商品列表并分页，可输入的条件有: 商品名称（模糊） 商品状态，店铺id，商品类别
	 * @param productCondition
	 * @param rowSize
	 * @param pageSize
	 * @return
	 */
	ProductExecution getProductList(Product productCondition,int pageIndex,int pageSize);
}
