package com.qingruan.museum.framework.esb.redispubsub;


import lombok.Setter;

import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

import com.qingruan.framework.redis.ShardedJedisSentinelPoolTemplate;
import com.qingruan.framework.redis.ShardedJedisSentinelPoolTemplate.ShardedJedisSentinelPoolCallback;

@Component("redisPubSender")
public class RedisPubSender {
	
	public static final byte[] PUB_KEY = "RedisPublisher".getBytes(); 
	@Setter
	private ShardedJedisSentinelPoolTemplate jedisSentinelTemplate;
	
    public void publishMessageByChannel(final String channel,final String message){
    	
    	jedisSentinelTemplate.run(PUB_KEY, new ShardedJedisSentinelPoolCallback<Boolean>() {

			@Override
			public Boolean execute(Jedis jedis) {
			
				jedis.publish(channel, message);				
				return true;
			}
		
    	});
    	
    }
}
