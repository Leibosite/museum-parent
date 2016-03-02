package com.qingruan.museum.engine.service.rule.action;

import java.util.Iterator;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.qingruan.museum.engine.exception.ExceptionLog;
import com.qingruan.museum.engine.service.rule.DelayTask;
import com.qingruan.museum.engine.service.rule.DelayTaskAgency;
import com.qingruan.museum.pma.annotation.DomainElement;
import com.qingruan.museum.pma.annotation.ExecutionSelection;
import com.qingruan.museum.pma.annotation.MethodElement;
import com.qingruan.museum.session.IpcanContext;

/**
 * *按照短信模板发送短信 *需要实现和短信猫的接口
 * 
 * @author tommy
 * 
 */
@Slf4j
@Service
@ExecutionSelection
@DomainElement(name = "execution.sendMessageByTemplate")
public class BsSendMsgByTemplate implements BusinessService {
	public static final char MSGS_SEPARATOR = ',';

	@Override
	public boolean isAsynExecute() {
		return true;
	}

	@Override
	@Async
	public void execute(DelayTask delayTask, DelayTaskAgency delayTaskAgency) {
		log.debug("execute ------------------------ start");

		try {
			List<Object> params = delayTask.getParams();

			Iterator<Object> iterator = params.iterator();

			IpcanContext ipcanContext = (IpcanContext) iterator.next();
			String templateId = (String) iterator.next();
			String msgs = (String) iterator.next();
			Integer sendFreqCtlCycle = (Integer) iterator.next();
			Integer sendTimeLimit = (Integer) iterator.next();

			sendMsgByTemplate(ipcanContext, templateId, msgs, sendFreqCtlCycle,
					sendTimeLimit, delayTask);
		} catch (Exception e) {
			log.error(ExceptionLog.getErrorStack(e));
		}

		log.debug("execute ----------------------- end");
	}

	/**
	 * 发送短信模板
	 * 
	 * @param ipcanContext
	 * @param templateId
	 * @param msgs
	 * @param sendFreqCtlCycle
	 * @param sendTimeLimit
	 */
	@MethodElement(name = "method.sendMsgByTemplate")
	public void sendMsgByTemplate(
			@DomainElement(name = "ipcan context", staticValue = "ipcanContext") IpcanContext ipcanContext,
			@DomainElement(name = "domain.specifiedSMSTemplate", chosenClassName = "SmsTemplate") String templateId,
			@DomainElement(name = "domain.messages") String msgs,
			@DomainElement(name = "domain.smsSendFreqCtlCycle") Integer sendFreqCtlCycle,
			@DomainElement(name = "domain.smsSendTimeLimit") Integer sendTimeLimit) {

	}

	/**
	 * 发送邮件模板
	 * 
	 * @param ipcanContext
	 * @param templateId
	 * @param msgs
	 * @param sendFreqCtlCycle
	 * @param sendTimeLimit
	 */
	@MethodElement(name = "method.sendEmailByTemplate")
	public void sendEmailByTemplate(
			@DomainElement(name = "ipcan context", staticValue = "ipcanContext") IpcanContext ipcanContext,
			@DomainElement(name = "domain.specifiedEmailTemplate", chosenClassName = "EmailTemplate") String templateId,
			@DomainElement(name = "domain.messages") String msgs,
			@DomainElement(name = "domain.smsSendFreqCtlCycle") Integer sendFreqCtlCycle,
			@DomainElement(name = "domain.smsSendTimeLimit") Integer sendTimeLimit) {

	}

	public void sendMsgByTemplate(IpcanContext ipcanContext, String templateId,
			String msgs, Integer sendFreqCtlCycle, Integer sendTimeLimit,
			DelayTask delayTask) {
		log.info("短信已经发送");
	}

	private String[] spliteMessages(String msgs) {
		if (msgs != null && !"".equals(msgs))
			return msgs.split(",");
		else
			return null;

	}

	@Override
	public void executeBatchTasks(List<DelayTask> tasks) {
		// TODO Auto-generated method stub

	}

}
