package com.qingruan.museum.framework.esb.redispubsub;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import com.qingruan.framework.redis.ShardedJedisSentinelPoolTemplate;
import com.qingruan.framework.redis.ShardedJedisSentinelPoolTemplate.ShardedJedisSentinelPoolCallback;


@Slf4j
public class RedisSubReceiver implements Runnable{
	private SubcribeMessageListener listener;
	private String channel;
	@Setter
	private ShardedJedisSentinelPoolTemplate jedisSentinelTemplate;
	
	public void setSubcribeListener(SubcribeMessageListener listener,String channel){
		this.listener = listener;
		this.channel = channel;
	}
	
	public interface SubcribeMessageListener {
		void receiveMessage(Object message);
	}

	@Override
	public void run(){
		
		jedisSentinelTemplate.run(RedisPubSender.PUB_KEY, new ShardedJedisSentinelPoolCallback<Boolean>() {

			@Override
			public Boolean execute(Jedis jedis) {
				BroadcastListener broadcastListener;
				try {
					broadcastListener = new BroadcastListener(listener);
					
					jedis.subscribe(broadcastListener, channel);
				} catch (Exception e) {
					log.error(e.getLocalizedMessage());
					e.printStackTrace();
					
				}
				return true;
			}
		});
	}

}
