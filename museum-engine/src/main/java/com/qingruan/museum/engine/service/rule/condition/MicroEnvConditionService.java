package com.qingruan.museum.engine.service.rule.condition;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.qingruan.museum.pma.annotation.ConstraintSelection;
import com.qingruan.museum.pma.annotation.DomainElement;
import com.qingruan.museum.pma.annotation.MethodElement;
import com.qingruan.museum.session.IpcanContext;

/**
 * 微环境调控条件检查实现类
 * 
 * @author tommy
 * 
 */
@Slf4j
@Service
@ConstraintSelection(isService = true)
@DomainElement(name = "condition.MicroEnvConditionService")
public class MicroEnvConditionService {
	/**
	 * 判断恒温恒湿机
	 * 
	 * @param ipcanContext
	 * @return
	 */
	@MethodElement(name = "method.isConstantTh")
	@DomainElement(name = "domain.isConstantTh", chosenClassName = "ConstantTh")
	public Long isConstantTh(
			@DomainElement(name = "ipcan context", staticValue = "ipcanContext") IpcanContext ipcanContext) {
		log.info("--{start}---[rule]---{isConstantTh}----------");
		Long id = -1L;
		log.info("--{end}---[rule]---{isConstantTh}----------");
		return id;
	}

	/**
	 * 判断归属展柜
	 * 
	 * @param ipcanContext
	 * @return
	 */
	@MethodElement(name = "method.isShowCase")
	@DomainElement(name = "domain.isShowCase", chosenClassName = "ShowCase")
	public Long isShowCase(
			@DomainElement(name = "ipcan context", staticValue = "ipcanContext") IpcanContext ipcanContext) {
		log.info("--{start}---[rule]---{isShowCase}----------");
		Long id = -1L;
		log.info("--{end}---[rule]---{isShowCase}----------");
		return id;
	}
}
