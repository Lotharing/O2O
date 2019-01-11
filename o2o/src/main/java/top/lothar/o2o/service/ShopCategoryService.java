package top.lothar.o2o.service;

import java.util.List;

import top.lothar.o2o.entity.ShopCategory;

public interface ShopCategoryService {
	/**
	 * 查找店铺分类列表
	 * @param shopCategoryCondition
	 * @return
	 */
	List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);
}
