/**
 2014年11月28日
 14cells
 
 */
package com.qingruan.museum.init;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import com.qingruan.museum.api.exception.ExceptionLog;
import com.qingruan.museum.framework.esb.redispubsub.RedisPubSender;
import com.qingruan.museum.framework.esb.redispubsub.RedisSubReceiver;
import com.qingruan.museum.framework.esb.redispushpop.RedisMQBroker;
import com.qingruan.museum.framework.esb.redispushpop.RedisMQPushSender;
import com.qingruan.museum.netty.httpsqs.HttpSQSServer;

/**
 * @author tommy
 * 
 */
@Getter
@Setter
@Slf4j
public class InitialEngineSystem implements Serializable {
	private static final long serialVersionUID = -1200100000000001L;
	@Autowired
	private RedisMQBroker redisMQBroker;

	@Autowired
	private HttpSQSServer httpSQSServer;

	@Autowired
	private RedisSubReceiver redisSubReceiver;

	@Autowired
	private RedisPubSender redisPubSender;
	
	@Autowired
	private RedisMQPushSender redisMQPushSender;

	public InitialEngineSystem() {
	}

	private String message;

	/**
	 * 初始化方法
	 */
	public void init() {
		log.info("----------------------------Museum-API-Server is starting-------------------------------");
		if (httpSQSServer == null)
			System.out.println("httpSQSServer为空");
		else {

			try {
				httpSQSServer.runServer();
			} catch (Exception e) {
				log.error(ExceptionLog.getErrorStack(e));
				throw new RuntimeException();
			}

		}

		log.info("API Server已经启动");
		log.info("----------------------------Museum-API-Server is started-------------------------------");
	}

	/**
	 * 关闭方法
	 */
	public void destory() {
		log.info("----------------------------httpSQSServer 被关闭-------------------------------");
		if (httpSQSServer == null)
			System.out.println("协议栈-Netty为空");
		else
			httpSQSServer.shutDownHttpSQSServer();
		;
	}

}
