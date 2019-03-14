package top.lothar.o2o.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.lothar.o2o.cache.JedisUtil;
import top.lothar.o2o.service.CacheService;
@Service
public class CacheServiceImpl implements CacheService {
	
	@Autowired
	private JedisUtil.Keys jedisKeys;

	@Override
	public void removeFromCache(String Prefix) {
		//取出以 参数Prefix开头的所有key
		//然后进行遍历set  进行删除
		
		Set<String> keys = jedisKeys.keys(Prefix+"*");
		for (String keySet : keys) {
			jedisKeys.del(keySet);
		}
	}

}
