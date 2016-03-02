package com.qingruan.museum.engine.listener;

import lombok.extern.slf4j.Slf4j;

import com.qingruan.museum.engine.MuseumEngine;
import com.qingruan.museum.engine.service.handlemsg.RuleMsgHandler;
import com.qingruan.museum.framework.esb.redispubsub.RedisSubReceiver.SubcribeMessageListener;
import com.qingruan.museum.framework.jackson.JSONUtil;
import com.qingruan.museum.msg.ruleupdate.RuleUpdateMsg;
@Slf4j
public class AdminToEngineMsgListener implements SubcribeMessageListener {


	@Override
	public void receiveMessage(Object message) {
		// TODO Auto-generated method stub

		try {
			log.info("BroadcastListener receive ================="
					+ message);
			RuleMsgHandler ruleMsgHandler=MuseumEngine.ruleMsgHandler;
			extracted(message,ruleMsgHandler);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

	private void extracted(Object message,RuleMsgHandler ruleMsgHandler)
			throws Exception {
		ruleMsgHandler.onReceive(JSONUtil.deserialize(message.toString(), RuleUpdateMsg.class));
	}

}
