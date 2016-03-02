package com.qingruan.museum.framework.esb.redispushpop;

import static com.qingruan.museum.framework.esb.redispushpop.RedisMQPushSender.A_2_E;
import static com.qingruan.museum.framework.esb.redispushpop.RedisMQPushSender.ENGINE_TO_AGENT;
import static com.qingruan.museum.framework.esb.redispushpop.RedisMQPushSender.E_2_A;
import static com.qingruan.museum.framework.esb.redispushpop.RedisMQPushSender.E_2_N;
import static com.qingruan.museum.framework.esb.redispushpop.RedisMQPushSender.N_2_E;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import com.qingruan.framework.redis.ShardedJedisSentinelPoolTemplate;
import com.qingruan.framework.redis.ShardedJedisSentinelPoolTemplate.ShardedJedisSentinelPoolCallback;
import com.qingruan.museum.framework.log.ExceptionLog;

/**
 * 
 * @author jcy
 * @modified author tommy
 * 
 */
@Slf4j
public class RedisMQBlpopReceiver implements Runnable {

	private NettyToEngineMessageListener n2eListener;
	private EngineToNettyMessageListener e2nListener;
	private EngineToAdminMessageListener e2aListener;
	private AdminToEngineMessageListener a2eListener;
	private EngineToAgentMessageListener engineToAgentListener;

	private byte[] MQ_RECEIVER_KEY = "RedisMQBlpopReceiver".getBytes();
	private List<String> channelStrings = new ArrayList<String>();

	@Setter
	private ShardedJedisSentinelPoolTemplate jedisSentinelTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		try {

			if (jedisSentinelTemplate == null || channelStrings.size() == 0) {
		
				log.info("{jedisSentinelTemplate is null and have not register listener}");
			} else {
				final String[] channels = channelStrings
						.toArray(new String[channelStrings.size()]);

				while (true) {
					List<String> messageList = new ArrayList<String>();
					
					try {
						messageList = (List<String>)jedisSentinelTemplate.run(MQ_RECEIVER_KEY,
								new ShardedJedisSentinelPoolCallback<Object>() {
		
									@Override
									public Object execute(Jedis paramJedis) {
										List<String> messageList = null;
										
										try {
											messageList = paramJedis.brpop(0, channels);
											System.out.println(messageList);
										} catch (Exception e) {
											log.error(ExceptionLog.getErrorStack(e));
											return new ArrayList<String>();
										}
										
										return messageList;	
									}
									
								});
					} catch (Exception e) {
						Thread.sleep(5000);
						continue;
					}
					
					
					if(messageList.size()==0){
						Thread.sleep(5000);
						continue;
					}
					
					
					Iterator<String> iterator = messageList.iterator();
	
					while (iterator.hasNext()) {
						String listName = iterator.next();
						Object message = iterator.hasNext() ? iterator.next() : "null";
								
						switch (listName) {
						case N_2_E:
							receiveNettyToEngine(message);
							break;
						case E_2_N:
							receiveEngineToNetty(message);
							break;
						case E_2_A:
							receiveEngineToAdmin(message);
							break;
						case A_2_E:
							receiveAdminToEngine(message);
							break;
						case ENGINE_TO_AGENT:
							receiveEngineToNetty(message);
							break;
						default:
							break;
						}
	
					}
				}
			}

		} catch (Exception e) {
			log.error(ExceptionLog.getErrorStack(e));
		}

	}

	public void receiveNettyToEngine(Object message) {
		if (n2eListener != null) {

			n2eListener.receiveMessage(message);
		}
	}

	public void receiveEngineToNetty(Object message) {
		if (e2nListener != null) {
           System.out.println("------[recieved]---------{receiveEngineToNetty}----[massge]--is: " + message.toString());
			e2nListener.receiveMessage(message);
		}
	}

	public void receiveAdminToEngine(Object message) {
		if (a2eListener != null) {

			a2eListener.receiveMessage(message);
		}
	}

	public void receiveEngineToAdmin(Object message) {
		if (e2aListener != null) {

			e2aListener.receiveMessage(message);
		}
	}

	public void receiveEngineToAgent(Object message) {
		if (engineToAgentListener != null) {
			engineToAgentListener.receiveMessage(message);
		}
	}

	/**
	 * 注册不同的回掉函数接收不同的消息
	 * 
	 * @param n2eListener
	 */
	public void setMessageListener(NettyToEngineMessageListener n2eListener) {
		channelStrings.add(N_2_E);
		this.n2eListener = n2eListener;
	}

	public void setMessageListener(EngineToNettyMessageListener e2nListener) {
		channelStrings.add(E_2_N);
		this.e2nListener = e2nListener;
	}

	public void setMessageListener(AdminToEngineMessageListener a2eListener) {
		channelStrings.add(A_2_E);
		this.a2eListener = a2eListener;
	}

	public void setMessageListener(EngineToAdminMessageListener e2aListener) {
		channelStrings.add(E_2_A);
		this.e2aListener = e2aListener;
	}

	public void setEngineToAgentListener(
			EngineToAgentMessageListener engineToAgentListener) {
		channelStrings.add(ENGINE_TO_AGENT);
		this.engineToAgentListener = engineToAgentListener;
	}

	public interface NettyToEngineMessageListener {
		void receiveMessage(Object message);
	}

	public interface EngineToNettyMessageListener {
		void receiveMessage(Object message);
	}

	public interface EngineToAdminMessageListener {
		void receiveMessage(Object message);
	}

	public interface AdminToEngineMessageListener {
		void receiveMessage(Object message);
	}

	public interface EngineToAgentMessageListener {
		void receiveMessage(Object message);
	}

}
