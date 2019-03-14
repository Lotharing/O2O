package top.lothar.o2o.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import top.lothar.o2o.cache.JedisUtil;
import top.lothar.o2o.dao.ShopCategoryDao;
import top.lothar.o2o.entity.ShopCategory;
import top.lothar.o2o.exceptions.ShopCategoeyOperationException;
import top.lothar.o2o.service.ShopCategoryService;

@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {
	
	private static Logger log = LoggerFactory.getLogger(ShopCategoryServiceImpl.class);

	@Autowired
	private JedisUtil.Keys jedisKeys;
	@Autowired
	private JedisUtil.Strings jedisStrings;
	
	
	@Autowired
	private ShopCategoryDao shopCategoryDao;

	@Override
	public List<ShopCategory> queryShopCategory(ShopCategory shopCategoryCondition) {
		// TODO Auto-generated method stub
		/*
		 * 1.创建key前缀
		 * 
		 * 2.拼接出完整的key
		 * 
		 * 3.判断key是否存在
		 * 
		 * 4.如果存在，则直接从redis缓存中取出
		 * 
		 * 5.如果不存在，则从db中查找，并存放在redis缓存中
		 * 
		 * 6.返回List对象
		 */
		// 定义key前缀
		String key = SCLISTKEY;
		// 定义接受对象
		List<ShopCategory> shopCategoryList = null;
		// 定义jackson数据转化操作类
		ObjectMapper mapper = new ObjectMapper();

		// 拼接key
		if (shopCategoryCondition == null) {
			// key
			// 当传入shopCategoryCondition为空时，
			key = key + "_" + "null";
		} else if (shopCategoryCondition != null && shopCategoryCondition.getParent() != null
				&& shopCategoryCondition.getParent().getShopCategoryId() != null) {
			// key
			key = key + "_" + shopCategoryCondition.getParent().getShopCategoryId();

		} else if (shopCategoryCondition != null) {
			key = key + "notnull";
		}

		if (!jedisKeys.exists(key)) {
			// 不存在
			shopCategoryList = shopCategoryDao.queryShopCategory(shopCategoryCondition);
			try {
				String jsonString = mapper.writeValueAsString(shopCategoryList);
				jedisStrings.set(key, jsonString);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		} else {
			// 存在
			String jsonString = jedisStrings.get(key);
			JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, ShopCategory.class);
			try {
				shopCategoryList = mapper.readValue(jsonString, javaType);
			} catch (JsonParseException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				throw new ShopCategoeyOperationException(e.getMessage());
			} catch (JsonMappingException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				throw new ShopCategoeyOperationException(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				throw new ShopCategoeyOperationException(e.getMessage());
			}
		}
		return shopCategoryList;
//		return shopCategoryDao.queryShopCategory(shopCategoryCondition);
	}

}
