/**
 2015年3月19日
 14cells
 
 */
package com.qingruan.museum.engine.listener;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qingruan.museum.engine.MuseumEngine;
import com.qingruan.museum.engine.exception.ExceptionLog;
import com.qingruan.museum.engine.service.workflow.AbstractWorkflow;
import com.qingruan.museum.framework.esb.redispushpop.RedisMQBlpopReceiver.NettyToEngineMessageListener;
import com.qingruan.museum.msg.InterfaceTypeAdapter;
import com.qingruan.museum.msg.MuseumMsg;

/**
 * @author tommy
 * 
 */
@Service
@Slf4j
public class NettyToEngineMsgListener implements NettyToEngineMessageListener {
	private Gson gson = new GsonBuilder().registerTypeAdapter(MuseumMsg.class,
			new InterfaceTypeAdapter()).create();

	@Override
	public void receiveMessage(Object message) {

		log.info("NettyToEngineMsgListener -------Thread id is:"
				+ Thread.currentThread().getName());
		if (message == null) {
			log.info("{NettyToEngineMsgListener.receiveMessage() message is null,skip}");
			return;
		}

		try {

			// MuseumMsg museumMsg = JSONUtil.deserialize((String) message,
			// MuseumMsg.class);
			log.info("{recieved museum domain from redis MQ,start call engine to handle it,message is:}"
					+ message);
			MuseumMsg museumMsg = gson.fromJson(message.toString(),
					MuseumMsg.class);
			log.info("{recieved museum domain from redis MQ,start call engine to handle it}");
			AbstractWorkflow abstractWorkflow = MuseumEngine.applicationContextGuardian
					.GetAppContext().getBean(AbstractWorkflow.class);
			if (abstractWorkflow == null)
				;
			abstractWorkflow.onReceive(museumMsg);

		} catch (Exception e) {
			log.error(ExceptionLog.getErrorStack(e));
			//TODO :
			throw new RuntimeException();

		}

	}

}
