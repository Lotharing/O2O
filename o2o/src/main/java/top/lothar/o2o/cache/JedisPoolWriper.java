package top.lothar.o2o.cache;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/*
 * 强指定redis的JedisPool接口构造函数，这样才能在centos成功创建jedispool
 * 
 * 根据配置，端口 ，ip来创建连接池
 * 
 */
public class JedisPoolWriper {
    //redis连接池对象
	private JedisPool jedisPool;
    
	public JedisPoolWriper(JedisPoolConfig poolConfig,final String host,final int port){
		
		try{
			jedisPool=new JedisPool(poolConfig,host,port);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	
	
	/*
	 * 获取redis连接池对象
	 * 
	 */
	public JedisPool getJedisPool() {
		return jedisPool;
	}



	/*
	 * 注入redis连接对象
	 */
	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}
	
}
