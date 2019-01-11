package top.lothar.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import top.lothar.o2o.entity.ShopCategory;

public interface ShopCategoryDao {
	/**
	  * 返回店铺类别List
	 * @param shopCategoryCondition
	 * @return
	 */
	List<ShopCategory> queryShopCategory(@Param("shopCategoryCondition") ShopCategory shopCategoryCondition);

}
