package top.lothar.o2o.service;


import top.lothar.o2o.dto.ImageHolder;
import top.lothar.o2o.dto.ShopExecution;
import top.lothar.o2o.entity.Shop;
import top.lothar.o2o.exceptions.ShopOperationException;

public interface ShopService {
	/**
	 * 根据shopCondition分页返回相应列表数据
	 * @param shopCondition
	 * @param pageIndex	页数，需要工具类对dao层统计的行数进行转换
	 * @param pageSize
	 * @return	返回值ShopExecution为了把List和Count联合在一起，因为是其中的两个属性，通过构造方法和set方法设置参数
	 */
	public ShopExecution getShopList(Shop shopCondition,int pageIndex,int pageSize);
	/**
	 * 通过店铺ID获取店铺信息
	 * @param shopId
	 * @return
	 */
	Shop getByShopId(long shopId);
	/**
	  * 更新店铺信息
	 * @param shop	（shop实体类）
	 * @param thumbnail	（含有图片 流和名字 的实体）
	 * @return
	 * @throws ShopOperationException
	 */
	ShopExecution modifyShop(Shop shop,ImageHolder thumbnail)throws ShopOperationException;
	/**
	 *  添加店铺操作，处理业务逻辑
 	 * 店铺操作的状态，以及state标识等信息
	 * @param shop
	 * @return
	 */
	ShopExecution addShop(Shop shop,ImageHolder thumbnail);
	
}
