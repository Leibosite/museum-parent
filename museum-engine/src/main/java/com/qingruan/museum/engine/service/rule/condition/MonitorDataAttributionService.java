/**
 2015年3月18日
 14cells
 
 */
package com.qingruan.museum.engine.service.rule.condition;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.qingruan.museum.pma.annotation.ConstraintSelection;
import com.qingruan.museum.pma.annotation.DomainElement;
import com.qingruan.museum.pma.annotation.MethodElement;
import com.qingruan.museum.session.IpcanContext;

/**
 * @author tommy
 * 
 */
@Slf4j
@Service
@ConstraintSelection(isService = true)
@DomainElement(name = "condition.MonitorDataAttributionCheck")
public class MonitorDataAttributionService {

	/**
	 * @desp 上报的监测数据所属区域
	 * @param ipcanContext
	 * @param saveFlagType
	 * @return
	 */
	@MethodElement(name = "method.area")
	@DomainElement(name = "domain.specifiedArea", chosenClassName = "Area")
	public Long area(
			@DomainElement(name = "ipcan context", staticValue = "ipcanContext") IpcanContext ipcanContext) {
		log.debug("MonitorDataAttributionService.area()-----------------------start.");
		Long areaId = ipcanContext.getRunTimeInfo().getMonitoringPoint()
				.getRepoArea().getId();
		if (areaId == null) {
			return -1L;
		}
		log.debug("MonitorDataAttributionService.area()-----------------------end.");
		return areaId;
	}

	@MethodElement(name = "method.monitorPoint")
	@DomainElement(name = "domain.specifiedMonitorPoint", chosenClassName = "MonitorPoint")
	public Long monitorPoint(
			@DomainElement(name = "ipcan context", staticValue = "ipcanContext") IpcanContext ipcanContext) {
		log.debug("MonitorDataAttributionService.monitorPoint()-----------------------start.");
		Long monitorPointId = ipcanContext.getRunTimeInfo()
				.getMonitoringPoint().getId();
		if (monitorPointId == null) {
			return -1L;
		}
		log.debug("MonitorDataAttributionService.monitorPoint()-----------------------end.");
		return monitorPointId;
	}

	@MethodElement(name = "method.monitorStation")
	@DomainElement(name = "domain.specifiedMonitorStation", chosenClassName = "MonitorStation")
	public Long monitorStation(
			@DomainElement(name = "ipcan context", staticValue = "ipcanContext") IpcanContext ipcanContext) {
		log.debug("MonitorDataAttributionService.monitorStation()-----------------------start.");
		Long monitorStationId = ipcanContext.getRunTimeInfo()
				.getMonitoringStation().getId();
		if (monitorStationId == null) {
			return -1L;
		}
		log.debug("MonitorDataAttributionService.monitorStation()-----------------------end.");
		return monitorStationId;
	}
}
