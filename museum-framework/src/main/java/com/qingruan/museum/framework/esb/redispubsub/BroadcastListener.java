package com.qingruan.museum.framework.esb.redispubsub;


import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPubSub;

import com.qingruan.museum.framework.esb.redispubsub.RedisSubReceiver.SubcribeMessageListener;

/**
 * 
 * @author jcy
 * 用法例如要接收netty-to-engine的消息，获取RedisMQReceiver实例，填充参数MessageListener,在此之前使listener注册回调函数
 * 用相应的回调函数收取信息。
 */
@Slf4j
public class BroadcastListener extends JedisPubSub {
	
	private SubcribeMessageListener listener;
	
	public BroadcastListener(SubcribeMessageListener listener) throws Exception{
		if(listener!=null){
			this.listener = listener;
		}else{
			throw new Exception("如果你想要收到信息，你要注册SubcribeMessageListener，不能为空");
		}
		
	}
	
	@Override
	public void onMessage(String channel, String message) {	
		log.info(message);
		listener.receiveMessage(message);
	}
	
	@Override
	public void onPMessage(String pattern, String channel, String message) {}

	@Override
	public void onSubscribe(String channel, int subscribedChannels) {}

	@Override
	public void onUnsubscribe(String channel, int subscribedChannels) {}

	@Override
	public void onPUnsubscribe(String pattern, int subscribedChannels) {}

	@Override
	public void onPSubscribe(String pattern, int subscribedChannels) {}
}
