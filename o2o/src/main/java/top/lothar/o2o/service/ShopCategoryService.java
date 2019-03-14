package top.lothar.o2o.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import top.lothar.o2o.entity.ShopCategory;

public interface ShopCategoryService {
	public static final String SCLISTKEY = "shopcategorylist";
	/**
	 * 获取shopcategory列表，可根据条件查询
	 * @param shopCategoryCondition
	 * @return
	 */
	List<ShopCategory> queryShopCategory(@Param("shopCategoryCondition") ShopCategory shopCategoryCondition);
}
