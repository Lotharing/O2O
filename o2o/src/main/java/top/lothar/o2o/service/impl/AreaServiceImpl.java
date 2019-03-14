package top.lothar.o2o.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.internal.compiler.ast.ReturnStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import top.lothar.o2o.cache.JedisUtil;
import top.lothar.o2o.dao.AreaDao;
import top.lothar.o2o.entity.Area;
import top.lothar.o2o.exceptions.AreaOperationException;
import top.lothar.o2o.service.AreaService;

@Service
public class AreaServiceImpl implements AreaService {
	
	private static Logger log = LoggerFactory.getLogger(AreaServiceImpl.class);
	
	@Autowired
	private AreaDao areaDao;
	
	@Autowired
    private JedisUtil.Strings jedisStrings;
	
    @Autowired
    private JedisUtil.Keys jedisKeys;
	
	@Override
	@Transactional
	public List<Area> getAreaList() {
		//定义redis的key
		String key = AREALISTKEY;
		//区域接受对象
		List<Area> areaList = null;
		//定义jackson数据转换操作类
		ObjectMapper mapper = new ObjectMapper();
		//判断redis中是否有此key
		if (!jedisKeys.exists(key)) {
			//缓存没有则查询数据库
			areaList = areaDao.queryArea();
			String jsonString;
			try {
				jsonString = mapper.writeValueAsString(areaList);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				throw new AreaOperationException(e.getMessage());
			}
			//存入缓存
			jedisStrings.set(key, jsonString);
		} else {
			//如果缓存中有数据
			String jsonString = jedisStrings.get(key);
			JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, Area.class);
			try {
				areaList = mapper.readValue(jsonString, javaType);
			} catch (JsonParseException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				throw new AreaOperationException(e.getMessage());
			} catch (JsonMappingException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				throw new AreaOperationException(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				throw new AreaOperationException(e.getMessage());
			}
		}
		return areaList;
//		return areaDao.queryArea();
	}

}
