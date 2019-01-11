package top.lothar.o2o.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import top.lothar.o2o.dao.ShopDao;
import top.lothar.o2o.dto.ImageHolder;
import top.lothar.o2o.dto.ShopExecution;
import top.lothar.o2o.entity.Shop;
import top.lothar.o2o.enums.ShopStateEnum;
import top.lothar.o2o.exceptions.ShopOperationException;
import top.lothar.o2o.service.ShopService;
import top.lothar.o2o.util.ImageUtil;
import top.lothar.o2o.util.PageCalculator;
import top.lothar.o2o.util.PathUtil;

/**
 * ShopService的实现类
 * 
 * @author Lothar
 *
 */
@Service
public class ShopServiceImpl implements ShopService {

	@Autowired
	private ShopDao shopDao;

	@Override
	@Transactional
	public ShopExecution addShop(Shop shop, ImageHolder thumbnail) {
		// TODO Auto-generated method stub
		if (shop == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		}
		try {
			// 给店铺信息赋值
			shop.setEnableStatus(0);
			shop.setCreateTime(new Date());
			shop.setLastEditTime(new Date());
			// 添加店铺信息
			int effectedNum = shopDao.insertShop(shop);
			if (effectedNum <= 0) {
				// 回滚必须用这个异常终止
				throw new ShopOperationException("店铺创建失败");
			} else {
				if (thumbnail != null) {
					try {
						// 存储图片(需要用shop中的id，和要传对应id的图片)
						addShopImg(shop, thumbnail);
					} catch (Exception e) {
						// TODO: handle exception
						throw new RuntimeException("addShopImg error" + e.getMessage());
					}
					// 主要更新店铺的图片地址
					effectedNum = shopDao.updateShop(shop);
					if (effectedNum <= 0) {
						throw new ShopOperationException("更新图片地址失败");
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw new ShopOperationException("addShop error"+e.getMessage());
		}
		//添加成功就是放回枚举类型-审核中-CHECK
		return new ShopExecution(ShopStateEnum.CHECK, shop);
	}
	/**
	 * 存储图片
	 * @param shop    店铺实体（因为要知道图片是哪个店铺的）
	 * @param thumbnail 店铺图片实体（名称，文件流）让ImageUtil加水印并存放
	 */
	private void addShopImg(Shop shop, ImageHolder thumbnail) {
		// 获取shop图片目录的相对值路径
		String dest = PathUtil.getShopImagePath(shop.getShopId());
		//构建图片和路径-返回要存储数据库生成的图片地址
		String shopImgAddr = ImageUtil.generateThumbnail(thumbnail, dest);
		shop.setShopImg(shopImgAddr);
	}
	/**
	 * 通过店铺ID得到店铺信息
	 */
	@Override
	public Shop getByShopId(long shopId) {
		// TODO Auto-generated method stub
		return shopDao.queryByShopId(shopId);
	}
	/**
	 * 修改之后更新店铺信息操作
	 */
	@Override
	public ShopExecution modifyShop(Shop shop, ImageHolder thumbnail) throws ShopOperationException {
		if (shop == null || shop.getShopId() ==null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		}else {
			//1.判断是否需要处理图片
			try {
				if (thumbnail.getImage()!= null && thumbnail.getImageName()!=null && !"".equals(thumbnail.getImageName())) {
					//根据传入的ShopId得到将要修改的数据库信息
					Shop tempShop = shopDao.queryByShopId(shop.getShopId());
					//判断已有的店铺信息中是否存在图片
					if (tempShop.getShopImg() != null) {
						ImageUtil.deleteFileOrPath(tempShop.getShopImg());
					}
					addShopImg(shop, thumbnail);
				}
				//2.更新店铺信息
				shop.setLastEditTime(new Date());
				int effectedNum = shopDao.updateShop(shop);
				if (effectedNum <= 0) {
					return new ShopExecution(ShopStateEnum.INNER_ERROR);
				}else {
					//更新成功则根据更新shop的ID查询到更新的这个shop实体类信息
					shop = shopDao.queryByShopId(shop.getShopId());
					return new ShopExecution(ShopStateEnum.SUCCESS,shop);
				}
			} catch (Exception e) {
				// TODO: handle exception
				throw new ShopOperationException("modifyShop error:"+e.getMessage());
			}
		}
	}
	/**
	 * 根据shopCondition分页返回相应的列表数据
	 */
	@Override
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
		// TODO Auto-generated method stub
		int rowIndex = PageCalculator.calculatorRowIndex(pageIndex, pageSize);
		List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
		int count = shopDao.queryShopCount(shopCondition);
		ShopExecution se = new ShopExecution();
		if (shopList != null) {
			se.setShopList(shopList);
			se.setCount(count);
		}else {
			se.setState(ShopStateEnum.INNER_ERROR.getState());
		}
		return se;
	}

}
