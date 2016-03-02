package com.qingruan.museum.engine.service.rule.action;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.qingruan.museum.pma.annotation.DomainElement;
import com.qingruan.museum.pma.annotation.ExecutionSelection;
import com.qingruan.museum.pma.annotation.MethodElement;
import com.qingruan.museum.session.IpcanContext;

/**
 * 恒温恒湿机策略执行动作
 * 
 * @author tommy
 * 
 */
@Slf4j
@Service
@ExecutionSelection
@DomainElement(name = "execution.microEnvAction")
public class BsMicroEnvActionService {
	/**
	 * 设置温度
	 * 
	 * @param ipcanContext
	 * @param temperature
	 */
	@MethodElement(name = "method.setTemperature")
	public void setTemperature(
			@DomainElement(name = "Ipcan Context", staticValue = "ipcanContext") IpcanContext ipcanContext,
			@DomainElement(name = "domain.specifiedTemperature") Long temperature) {
		try {
			log.info("--------setTemperature()----{开始设置恒温恒湿机温度}");
			log.info("{}");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 设置温度
	 * 
	 * @param ipcanContext
	 * @param temperature
	 */
	@MethodElement(name = "method.setHumidity")
	public void setHumidity(
			@DomainElement(name = "Ipcan Context", staticValue = "ipcanContext") IpcanContext ipcanContext,
			@DomainElement(name = "domain.specifiedHumidity") Long humidity) {
		try {
			log.info("--------setHumidity()----{开始设置恒温恒湿机湿度}");
			log.info("{}");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
