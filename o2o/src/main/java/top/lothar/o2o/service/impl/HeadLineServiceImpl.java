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
import top.lothar.o2o.dao.HeadLineDao;
import top.lothar.o2o.entity.HeadLine;
import top.lothar.o2o.exceptions.HeadLineOperationException;
import top.lothar.o2o.service.HeadLineService;
@Service
public class HeadLineServiceImpl implements HeadLineService {
	
	@Autowired
	private JedisUtil.Keys jedisKeys;
	@Autowired
	private JedisUtil.Strings jedisStrings;

	private static Logger log = LoggerFactory.getLogger(HeadLineServiceImpl.class);
	
	@Autowired
	private HeadLineDao headLineDao;

	@Override
	public List<HeadLine> getHeadLineList(HeadLine headLineCondition){
		// TODO Auto-generated method stub
		// 定义redis的key前缀
		String key = HLLISTKEY;
		// 定义接受对象
		List<HeadLine> headLineList = null;
		// 定义jackson数据转化操作类
		ObjectMapper mapper = new ObjectMapper();

		// 对key进行拼接
		if (headLineCondition != null && headLineCondition.getEnableStatus() != null) {
			key = key + "_" + headLineCondition.getEnableStatus();
		}
		// 判断key是否存在
		if (!jedisKeys.exists(key)) {
			// 不存在
			headLineList = headLineDao.queryHeadLine(headLineCondition);
            //将数据存在redis缓存中
			try {
				String jsonString = mapper.writeValueAsString(headLineList);
				jedisStrings.set(key, jsonString);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				throw new HeadLineOperationException(e.getMessage());
			}
		} else {
			// 存在则直接取出来
			String jsonString = jedisStrings.get(key);
			// 将指定的String类型转换为List集合类型
			JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, HeadLine.class);
			try {
				headLineList = mapper.readValue(jsonString, javaType);
			} catch (JsonParseException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				throw new HeadLineOperationException(e.getMessage());
			} catch (JsonMappingException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				throw new HeadLineOperationException(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				throw new HeadLineOperationException(e.getMessage());
			}
		}
		return headLineList;
//		return headLineDao.queryHeadLine(headLineCondition);
	}

}
