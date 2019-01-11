package top.lothar.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import top.lothar.o2o.entity.Shop;

public interface ShopDao {
	/**
	  * 多个参数要用param注解
	 * 分页查询店铺 可输入条件（模糊）店铺状态，类别，区域ID，owner
	 * @param shopCondition	传入的shop实体类携带擦查询条件
	 * @param rowIndex	从第几行取数据
	 * @param pageSize	一共取到第几数据，就是Limit语句中的两个参数，直白的说就是往后查几个
	 * @return
	 */
	List<Shop> queryShopList(@Param("shopCondition")Shop shopCondition , @Param("rowIndex")int rowIndex , @Param("pageSize")int pageSize);
	/**
	 * 返回queryShopList的总数
	 * @param shopCondition
	 * @return
	 */
	int queryShopCount(@Param("shopCondition")Shop shopCondition);
	/**
	 * 根据shopID查询店铺信息
	 * @param shopId
	 * @return
	 */
	Shop queryByShopId(long shopId);
	/**
	  * 新增店铺
	 * @param shop
	 * @return -1 不成功  1成功
	 */
	int insertShop(Shop shop);
	/**
	  * 更新店铺信息
	 * @param shop
	 * @return
	 */
	int updateShop(Shop shop);
	
}
