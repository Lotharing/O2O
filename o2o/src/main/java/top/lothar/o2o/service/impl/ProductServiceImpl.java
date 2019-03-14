package top.lothar.o2o.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import top.lothar.o2o.dao.ProductDao;
import top.lothar.o2o.dao.ProductImgDao;
import top.lothar.o2o.dto.ImageHolder;
import top.lothar.o2o.dto.ProductExecution;
import top.lothar.o2o.entity.Product;
import top.lothar.o2o.entity.ProductImg;
import top.lothar.o2o.enums.ProductStateEnum;
import top.lothar.o2o.exceptions.ProductOperationException;
import top.lothar.o2o.service.ProductService;
import top.lothar.o2o.util.ImageUtil;
import top.lothar.o2o.util.PageCalculator;
import top.lothar.o2o.util.PathUtil;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao productDao;

	@Autowired
	private ProductImgDao productImgDao;

	@Override
	@Transactional
	public ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList)
			throws ProductOperationException {
		// 1.处理缩略图，获取缩略图相对路径并赋值给product
		// 2.往tb_product写入商品信息，获取productId
		// 3.结合productId批量处理商品详情图
		// 4.将商品详情图列表批量插入tb_product_img中
		if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
			// 设置默认属性
			product.setCreateTime(new Date());
			product.setLastEditTime(new Date());
			// 默认上架状态
			product.setEnableStatus(1);
			// 如果商品缩略图不为空则添加
			if (thumbnail != null) {
				addThumbnail(product, thumbnail);
			}
			try {
				int effectedNum = productDao.insertProduct(product);
				if (effectedNum < 0) {
					throw new ProductOperationException("添加商品失败");
				}
			} catch (Exception e) {
				throw new ProductOperationException("添加商品失败" + e.getMessage());
			}
			// 如过商品详情图片不为空则添加
			if (productImgList != null && productImgList.size() > 0) {
				addProductImgList(product, productImgList);
			}
			return new ProductExecution(ProductStateEnum.SUCCESS, product);
		} else {
			return new ProductExecution(ProductStateEnum.INNER_ERROR);
		}
	}

	/**
	 * 添加缩略图
	 * 
	 * @param product
	 * @param thumHolder
	 */
	private void addThumbnail(Product product, ImageHolder thumbnail) {
		// 根据商品所属的shopid得到构建缩略图路径
		String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
		// 生成带有水印的图片并返回缩略图绝对地址
		String thumbnailAddr = ImageUtil.generateThumbnail(thumbnail, dest);
		product.setImgAddr(thumbnailAddr);
	}

	/**
	 * 批量添加商品详情图
	 * 
	 * @param product
	 * @param productImgHolderList
	 */
	private void addProductImgList(Product product, List<ImageHolder> productImgHolderList) {
		// 获取图片存储路径，这里直接存放到相应店铺的文件夹底下
		String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
		List<ProductImg> productImgList = new ArrayList<ProductImg>();
		// 遍历图片一次去处理，并添加进productImg实体类里
		for (ImageHolder productImgHolder : productImgHolderList) {
			//存储图片并返回绝对路径
			String imgAddr = ImageUtil.generateNormalImg(productImgHolder, dest);
			ProductImg productImg = new ProductImg();
			productImg.setImgAddr(imgAddr);
			productImg.setProductId(product.getProductId());
			productImg.setCreateTime(new Date());
			productImgList.add(productImg);
		}
		// 如果确实是有图片需要添加的，就执行批量添加操作
		if (productImgList.size() > 0) {
			try {
				int effectedNum = productImgDao.bacthInsertProductImg(productImgList);
				if (effectedNum <= 0) {
					throw new ProductOperationException("创建商品详情图片失败");
				}
			} catch (Exception e) {
				throw new ProductOperationException("创建商品详情图片失败:" + e.toString());
			}
		}
	}

	
	@Override
	public Product getProductById(Long productId) {
		// TODO Auto-generated method stub
		return productDao.queryProductById(productId);
	}
	

	@Override
	@Transactional
	public ProductExecution modifyProduct(Product product, ImageHolder thumbnail,
			List<ImageHolder> productImgHolderList) throws ProductOperationException {
		/* 如果缩略图有参数则处理缩略图
		如果原先存在缩略图，先删除，在进行添加，之后获取路径赋值给Product
		如果详情图有参数进行同样的操作
		然后将tb_product_img下面的商品原先的商品详情图片删除
		更新tb_product信息
		空值判断 */
		if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
			//给商品设置默认属性
			product.setLastEditTime(new Date());
			// 若商品缩略图不为空且原有缩略图不为空则删除原有缩略图并添加
			if (thumbnail != null) {
				// 先获取一遍原有信息，因为原来的信息里有原图片地址
				Product tempProduct = productDao.queryProductById(product.getProductId());
				if (tempProduct.getImgAddr() != null) {
					ImageUtil.deleteFileOrPath(tempProduct.getImgAddr());
				}
				addThumbnail(product, thumbnail);
			}
			// 如果有新存入的商品详情图，则将原先的删除，并添加新的图片
			if (productImgHolderList != null && productImgHolderList.size() > 0) {
				deleteProductImgList(product.getProductId());
				addProductImgList(product, productImgHolderList);
			}
			try {
				// 更新商品信息
				int effectedNum = productDao.updateProduct(product);
				if (effectedNum <= 0) {
					throw new ProductOperationException("更新商品信息失败");
				}
				return new ProductExecution(ProductStateEnum.SUCCESS, product);
			} catch (Exception e) {
				// TODO: handle exception
				throw new ProductOperationException("更新商品信息失败:" + e.toString());
			}
		}else {
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
	}
	/**
	 * 删除指定商品详情图片
	 * @param productId
	 */
	private void deleteProductImgList(Long productId) {
		// 进行查询先
		List<ProductImg> productImgList = productImgDao.queryProductImgList(productId);
		if (productImgList != null && productImgList.size() > 0) {
			// 删除原来的详情图片
			for (ProductImg productImg : productImgList) {
				ImageUtil.deleteFileOrPath(productImg.getImgAddr());
			}
			// 删除数据库中的图片
			productImgDao.deleteProductImgByProductId(productId);
		}
	}

	@Override
	public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
		// 页码转换成数据库的行码，并调用dao层取回指定页码的商品列表
		int rowIndex = PageCalculator.calculatorRowIndex(pageIndex, pageSize);
		List<Product> productList = productDao.queryProductList(productCondition, rowIndex, pageSize);
		// 基于同样的查询条件返回该查询条件下的商品总数
		int count = productDao.queryProductCount(productCondition);
		ProductExecution pe = new ProductExecution();
		pe.setProductList(productList);
		pe.setCount(count);
		return pe;
	}
	
}
