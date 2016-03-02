package com.qingruan.museum.framework.esb.redispushpop;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import com.qingruan.framework.redis.ShardedJedisSentinelPoolTemplate;
import com.qingruan.framework.redis.ShardedJedisSentinelPoolTemplate.ShardedJedisSentinelPoolCallback;
import com.qingruan.museum.framework.jackson.JSONUtil;

@Slf4j
public class RedisMQPushSender {

	public static final String N_2_E = "nettyToEngine";
	public static final String E_2_N = "engineToNetty";
	public static final String A_2_E = "adminToEngine";
	public static final String E_2_A = "engineToAdmin";
	public static final String ENGINE_TO_AGENT = "ENGINE_TO_AGENT";
	public static final String ERROR_LOG = "netty-error-log";
	public static final byte[] MQ_SENDER_KEY = "RedisMQPushSender".getBytes();

	@Setter
	private ShardedJedisSentinelPoolTemplate jedisSentinelTemplate;

	public void sendNettyToEngine(final Object message) {
		log.info((String) message);
		jedisSentinelTemplate.run(MQ_SENDER_KEY,
				new ShardedJedisSentinelPoolCallback<Object>() {

					@Override
					public Object execute(Jedis paramJedis) {
						String msgData = message.toString();
						log.info("--@-------------------------------{sendNettyToEngine}------------------------@---");
						log.info("--@--------------------------------------------------------------------------@---");
						log.info("--@--------------------------------------------------------------------------@---");
						log.info("--@--------------------------------------------------------------------------@---");
						log.info("--@--------------------------------------------------------------------------@---");
						log.info("--@--------------------------------------------------------------------------@---");
						log.info("--@--------------------------------------------------------------------------@---");
						log.info("--@--------------------------------------------------------------------------@---");
						log.info("------[sending]---------{sendNettyToEngine}----[massge]--is: " + JSONUtil.serialize(msgData));
						paramJedis.lpush(N_2_E, msgData);
						return null;
					}
				});
	}

	public void sendEngineToNetty(final Object message) {
		log.info("------[start]---------{sendEngineToNetty}----[massge]--is: " + message);

		jedisSentinelTemplate.run(MQ_SENDER_KEY,
				new ShardedJedisSentinelPoolCallback<Object>() {

					@Override
					public Object execute(Jedis paramJedis) {
						log.info("--@-------------------------------{sendEngineToNetty}------------------------@---");
						log.info("--@--------------------------------------------------------------------------@---");
						log.info("--@--------------------------------------------------------------------------@---");
						log.info("--@--------------------------------------------------------------------------@---");
						log.info("--@--------------------------------------------------------------------------@---");
						log.info("--@--------------------------------------------------------------------------@---");
						log.info("--@--------------------------------------------------------------------------@---");
						log.info("--@--------------------------------------------------------------------------@---");
					
						log.info("------[sending]---------{sendEngineToNetty}----[massge]--is: " + message.toString());
						paramJedis.lpush(E_2_N, message.toString());
						return null;
					}
				});
	}

	public void sendAdminToEngine(final Object message) {

		jedisSentinelTemplate.run(MQ_SENDER_KEY,
				new ShardedJedisSentinelPoolCallback<Object>() {

					@Override
					public Object execute(Jedis paramJedis) {

						paramJedis.lpush(A_2_E, message.toString());
						return null;
					}
				});
	}

	public void sendEngineToAdming(final Object message) {

		jedisSentinelTemplate.run(MQ_SENDER_KEY,
				new ShardedJedisSentinelPoolCallback<Object>() {

					@Override
					public Object execute(Jedis paramJedis) {

						paramJedis.lpush(E_2_A, message.toString());
						return null;
					}
				});
	}

	/**
	 * Engine 发送归档信息给Agent
	 * 
	 * @param message
	 */
	public void sendEngineToAgent(final Object message) {

		jedisSentinelTemplate.run(MQ_SENDER_KEY,
				new ShardedJedisSentinelPoolCallback<Object>() {

					@Override
					public Object execute(Jedis paramJedis) {

						paramJedis.lpush(ENGINE_TO_AGENT, message.toString());
						return null;
					}
				});
	}

	public void sendErrorLogToRMQ(final Object message) {
		jedisSentinelTemplate.run(MQ_SENDER_KEY,
				new ShardedJedisSentinelPoolCallback<Object>() {

					@Override
					public Object execute(Jedis paramJedis) {

						paramJedis.lpush(ERROR_LOG, message.toString());
						return null;
					}
				});
	}
}
