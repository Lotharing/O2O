package top.lothar.o2o.cache;

import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.util.SafeEncoder;

/*
 * 工具类
 */
public class JedisUtil {

	// 缓存生存时间
	private final int expire = 60000;
	// 操作Key的方法
	public Keys keys;
	// 对存储对象为String类型的操作
	public Strings STRINGS;
	/** Redis连接池对象 */
	private JedisPool jedisPool;
	/**
	 * 获取redis连接池
	 * @return
	 */
	public JedisPool getJedisPool() {
		return jedisPool;
	}
	/**
	 * 设置redis连接池
	 * @param jedisPoolWriper
	 */
	public void setJedisPool(JedisPoolWriper jedisPoolWriper) {
		this.jedisPool = jedisPoolWriper.getJedisPool();
	}
	/**
	 * 从jedispool中获取redis对象
	 * @return
	 */
	public Jedis getJedis() {
		return jedisPool.getResource();
	}
	/**
	 * 设置过期时间
	 * @param key
	 * @param seconds
	 */
	public void expire(String key, int seconds) {
		if (seconds <= 0) {
			return;
		}
		Jedis jedis = getJedis();
		jedis.expire(key, seconds);
		jedis.close();
	}

	/*
	 * 设置默认过期时间
	 */
	public void expire(String key) {
		expire(key, expire);
	}

	/***************************************************** Keys *************************************************************/
	public class Keys {
		/**
		 * 清空所有key
		 * @return
		 */
		public String flushAll() {
			// 获取redis连接池
			Jedis jedis = getJedis();
			String stata = jedis.flushAll();
			jedis.close();
			return stata;
		}
		/**
		 * 删除keys对应的记录,可以是多个key
		 * @param keys
		 * @return
		 */
		public long del(String... keys) {
			Jedis jedis = getJedis();
			long count = jedis.del(keys);
			jedis.close();
			return count;
		}
		/**
		 * 判断key是否存在
		 * @param key
		 * @return
		 */
		public boolean exists(String key) {
			// ShardedJedis sjedis = getShardedJedis();
			Jedis sjedis = getJedis();
			boolean exis = sjedis.exists(key);
			sjedis.close();
			return exis;
		}
		/**
		 * 查找所有匹配给定的模式的键 key的表达式,*表示多个，？表示一个
		 * @param pattern
		 * @return
		 */
		public Set<String> keys(String pattern) {
			Jedis jedis = getJedis();
			Set<String> set = jedis.keys(pattern);
			jedis.close();
			return set;
		}
	}
	/**
	 * 对String的操作
	 * @author Lothar
	 *
	 */
	public class Strings {
		/*
		 * 根据key获取记录
		 */
		public String get(String key){
			Jedis sjedis=getJedis();
			String value = sjedis.get(key);
			sjedis.close();
			return value;
		}
		/*
		 * 添加记录，如果记录中已经存在，覆盖原来的key的value
		 * 返回状态码
		 */
		public String set(String key,String value){
			return  set(SafeEncoder.encode(key), SafeEncoder.encode(value));
		}
		/*
		 *  添加记录，如果记录中已经存在，覆盖原来的key的value
		 */
		public String set(byte []key,byte []value){
			Jedis sjedis=getJedis();
			String status = sjedis.set(key, value);
			sjedis.close();
		    return status;
		}
	}

}
