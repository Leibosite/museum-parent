package com.qingruan.museum.framework.esb.redispushpop;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qingruan.framework.redis.ShardedJedisSentinelPoolTemplate;
import com.qingruan.museum.framework.esb.redispubsub.RedisSubReceiver;
import com.qingruan.museum.framework.exception.ErrHandler;

@Service
@Slf4j
@Getter
@Setter
public class RedisMQBroker {
	@Autowired
	private ShardedJedisSentinelPoolTemplate jedisSentinelTemplate;

	public void startListenRedisMQ(RedisMQBlpopReceiver redisMQBlpopReceiver) {
		if (jedisSentinelTemplate == null) {
		
			log.info("{RedisMQBroker.startListenRedisMQ(),jedisSentinelTemplate is null}");
			System.exit(0);
		}

		log.info("start----{RedisMQBroker.startListenRedisMQ}");
		redisMQBlpopReceiver.setJedisSentinelTemplate(jedisSentinelTemplate);
		log.debug("start register lister for redis mq{}");
		log.debug("end register lister for redis mq{}");
		Thread thread = new Thread(redisMQBlpopReceiver);
		thread.setUncaughtExceptionHandler(new ErrHandler());
		
		thread.start();
		log.info("end----{RedisMQBroker.startListenRedisMQ}");
	}
	
	public void startListenRedisSub(RedisSubReceiver redisSubReceiver) {
		if (jedisSentinelTemplate == null) {
			log.info("{RedisMQBroker.startListenRedisMQ(),jedisSentinelTemplate is null}");
			System.exit(0);
		}

		log.info("start----{RedisMQBroker.startListenRedisMQ}");
		log.debug("start register lister for redis mq{}");
		log.debug("end register lister for redis mq{}");
		Thread thread = new Thread(redisSubReceiver);
		thread.setUncaughtExceptionHandler(new ErrHandler());
		thread.start();
		log.info("end----{RedisMQBroker.startListenRedisMQ}");
	}


}
