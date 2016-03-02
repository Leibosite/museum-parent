package com.qingruan.museum.agent.init;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import com.qingruan.museum.agent.listener.EngineToAgentMsgListener;
import com.qingruan.museum.framework.esb.redispushpop.RedisMQBlpopReceiver;
import com.qingruan.museum.framework.esb.redispushpop.RedisMQBroker;
import com.qingruan.museum.framework.log.ExceptionLog;

/**
 * @author tommy
 * 
 */
@Getter
@Setter
@Slf4j
public class InitialAgentSystem {

	@Autowired
	private RedisMQBroker redisMQBroker;

	public InitialAgentSystem() {
	}

	private String message;

	/**
	 * 初始化方法
	 */
	public void init() {
		log.info("----------------------------Museum-Agent-System is starting-------------------------------");
		System.out
				.println("----------------------------Museum-Agent-System is starting-------------------------------");
		if (redisMQBroker == null)
			System.out.println("Redis消息队列代理为空");
		else {

			try {
				RedisMQBlpopReceiver redisMQBlpopReceiver = new RedisMQBlpopReceiver();
				registeredAgentMsgListener(redisMQBlpopReceiver);
				redisMQBroker.startListenRedisMQ(redisMQBlpopReceiver);
			} catch (Exception e) {
				log.error(ExceptionLog.getErrorStack(e));
				throw new RuntimeException();
			}

		}
		System.out
				.println("----------------------------Museum-Agent-System is started-------------------------------");
		log.info("----------------------------Museum-Agent-System is started-------------------------------");
	}

	/**
	 * 关闭方法
	 */
	public void destory() {
	}

	protected void registeredAgentMsgListener(
			RedisMQBlpopReceiver redisMQBlpopReceiver) {
		try {
			System.out.println("开始注册消息队列registeredAgentMsgListener");

			redisMQBlpopReceiver
					.setEngineToAgentListener(new EngineToAgentMsgListener());
			System.out.println("结束注册消息队列registeredAgentMsgListener");
		} catch (Exception e) {

			e.printStackTrace();
			throw new RuntimeException(
					"{registeredListener runtime exception!}");
		}

	}

}
