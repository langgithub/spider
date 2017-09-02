package com.champion.spider.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtils {

	private static JedisPool jedisPool = null;

	//Redis服务器IP
	private  String ADDR = "192.168.25.65";
	
	//Redis的端口号
	private  int PORT = 6379;
	
	//访问密码
	private  String AUTH = null;

	//可用连接实例的最大数目，默认值为8；
	//如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
	private  int MAX_ACTIVE = 1024;
	  
	//控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
	private  int MAX_IDLE = 200;
	  
	//等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
	private  int MAX_WAIT = 10000;
	
	private  int TIMEOUT = 10000;
	
	//在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
	private  boolean TEST_ON_BORROW = true;
	
	private RedisUtils(){
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxActive(MAX_ACTIVE);
		config.setMaxIdle(MAX_IDLE);
		config.setMaxWait(MAX_WAIT);
		config.setTestOnBorrow(TEST_ON_BORROW);
		jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT, AUTH);
	}

	public static Jedis getJedis() {
		if(jedisPool == null){
			synchronized (RedisUtils.class){
				if(jedisPool == null){
					new RedisUtils();
				}
			}
		}
		return jedisPool.getResource();
	}

	public static void returnResource(Jedis jedis) {
		if (jedis != null) {    
			jedisPool.returnResource(jedis);       
		}
	}
}