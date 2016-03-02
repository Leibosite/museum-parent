package com.qingruan.museum.engine.service.rule.condition;


import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qingruan.museum.engine.service.provision.CommonProvision;
import com.qingruan.museum.pma.annotation.ConstraintSelection;
import com.qingruan.museum.pma.annotation.DomainElement;
import com.qingruan.museum.pma.annotation.MethodElement;

@Slf4j
@Service
@ConstraintSelection(isService = true)
@DomainElement(name = "condition.CommonCondition")
public class CommonConditionService {

	@Autowired
	private CommonProvision commonProvision;

	@MethodElement(name = "method.intervalCheckOfCurrentTime")
	@DomainElement(name = "current time belong specified interval")
	public Boolean intervalCheckOfCurrentTime(@DomainElement(name = "domain.cron") String cronExpress){
		log.debug("intervalCheckOfCurrentTime------------Cron Expression:{}", cronExpress);
		    
		return commonProvision.IntervalCheckOfCurTime(cronExpress);
	}

}
