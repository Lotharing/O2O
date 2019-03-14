package top.lothar.o2o.service;

public interface CacheService {
	/**
	 * 依据key的前缀删除匹配该模式下所有的key-value
	 * 如传入shopcategpryList，以shopCategoryList打头的所有key以及对应的value都会被清除
	 * @param keyPrefix
	 */
	void removeFromCache(String Prefix);
}
