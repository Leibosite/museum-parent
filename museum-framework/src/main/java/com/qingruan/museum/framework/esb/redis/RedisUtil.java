package com.qingruan.museum.framework.esb.redis;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Slf4j
public class RedisUtil {
	
	public RedisUtil(){
		
	}
	
	//Redis可以最大连接实例的个数
	public final static int Maxactive = 10000;
	//Redis最大空闲连接数超过这个数目的空闲连接将会被回收。
	public final static int Maxidle = 500;
	//Redis最小空闲连接数
	public final static int Minidls = 100;
	//Redis连接超时时间
	public final static int Maxwait = 2000;
	public final static int RetryNum = 3;
	//Redis连接IP
    public final static String ipA ="10.0.1.194";
    public final static String ipB ="10.0.1.194";
    //Redis连接端口
    public final static int port = 6381;
    public int expire;
    public static JedisPool pool = null;
    
    /** 
     * 获取连接池. 
     * @return 连接池实例 
     */  
    private static JedisPool getPool() {  	        
        
          
        if(pool==null){
        	JedisPoolConfig config = new JedisPoolConfig();  
            config.setMaxTotal(Maxactive);;  
            config.setMaxIdle(Maxidle);
            config.setMinIdle(Minidls);
            config.setMaxWaitMillis(Maxwait);  
            config.setTestOnBorrow(true);  
            config.setTestOnReturn(true);  
            
            /**
             * 配置Redis双节点
             * ipA，ipB
             */
//            List<JedisShardInfo> redisNodes = new ArrayList<JedisShardInfo>();
//            JedisShardInfo nodeA = new JedisShardInfo(ipA, port);
//            JedisShardInfo nodeB = new JedisShardInfo(ipB, port);
//            redisNodes.add(nodeA);
//            redisNodes.add(nodeB);
//            
//            sharedPool = new ShardedJedisPool(config, redisNodes, Hashing.MURMUR_HASH,Sharded.DEFAULT_KEY_TAG_PATTERN);
            
            
               
            /** 
             *如果你遇到 java.net.SocketTimeoutException: Read timed out exception的异常信息 
             *请尝试在构造JedisPool的时候设置自己的超时值. JedisPool默认的超时时间是2秒(单位毫秒) 
             */  
             pool = new JedisPool(config, ipB, port,3000); 
        }
        return pool;  
    }  
  
    /** 
     *类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例 
     *没有绑定关系，而且只有被调用到时才会装载，从而实现了延迟加载。 
     */  
    private static class RedisUtilHolder{  
        /** 
         * 静态初始化器，由JVM来保证线程安全 
         */  
        private static RedisUtil instance = new RedisUtil();  
    }  
  
    /** 
     *当getInstance方法第一次被调用的时候，它第一次读取 
     *RedisUtilHolder.instance，导致RedisUtilHolder类得到初始化；而这个类在装载并被初始化的时候，会初始化它的静 
     *态域，从而创建RedisUtil的实例，由于是静态的域，因此只会在虚拟机装载类的时候初始化一次，并由虚拟机来保证它的线程安全性。 
     *这个模式的优势在于，getInstance方法并没有被同步，并且只是执行一个域的访问，因此延迟初始化并没有增加任何访问成本。 
     */  
    public static RedisUtil getInstance() {  
        return RedisUtilHolder.instance;  
    }  
      
    /** 
     * 获取Redis实例. 
     * @return Redis工具类实例 
     */  
    public Jedis getJedis() {  
        Jedis jedis  = null;  
        int count =0;  
        do{  
            try{   
                jedis = getPool().getResource();  	            
            } catch (Exception e) {
            	e.printStackTrace();
            	log.error("获取jedis实例错误："+e.toString());
                 // 销毁对象    
                getPool().returnBrokenResource(jedis);
                jedis = null;
            }
            count++;  
        }while(jedis==null &&count<RetryNum);  
        	
        return jedis;  
    }  
  
    /** 
     * 释放redis实例到连接池. 
     * @param jedis redis实例 
     */  
    public static void closeJedis(Jedis jedis) {  
        if(jedis != null) {  
            getPool().returnResource(jedis);  
        }  
    }
    
    public static abstract interface RedisClientCallback {
		public abstract void execute(Jedis client);
	}
    
    public static  void run(RedisClientCallback callback){
    	Jedis redisClient = getInstance().getJedis();//从Redis连接池得到Redis客户端实例
    	callback.execute(redisClient);
    	closeJedis(redisClient);//把这个实例的资源返回给连接池
    }
    
    
    public static void main(String[] args) {
    	RedisUtil.run(new RedisClientCallback() {

			@Override
			public void execute(Jedis client) {
				
				client.set("test", "123456");
				String test = client.get("test");
				System.out.println(test);
			}
		});
	}
    
    
}
