/**
 2015年1月27日
 tommy
 
 */
package com.qingruan.museum.engine.service.rule.action;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qingruan.museum.domain.models.enums.RecordDataType;
import com.qingruan.museum.engine.service.business.aircleaner.HandleAirCleanerService;
import com.qingruan.museum.engine.service.business.constantth.HandleConstantThService;
import com.qingruan.museum.engine.service.business.sensor.HandleSensorReqService;
import com.qingruan.museum.engine.service.business.weather.HandleWeatherService;
import com.qingruan.museum.msg.MsgProperty.ApplicationType;
import com.qingruan.museum.pma.annotation.DomainElement;
import com.qingruan.museum.pma.annotation.ExecutionSelection;
import com.qingruan.museum.pma.annotation.MethodElement;
import com.qingruan.museum.session.IpcanContext;

/**
 * @author tommy
 * 
 */
@Slf4j
@Service
@ExecutionSelection
@DomainElement(name = "execution.saveMonitoringReportData")
public class BsSaveMonitoringReportDataService {
	@Autowired
	private HandleSensorReqService sensorHandleService;
	@Autowired
	private HandleConstantThService handleConstantThService;
	@Autowired
	private HandleWeatherService handleWeatherService;
	@Autowired
	private HandleAirCleanerService handleAirCleanerService;

	@MethodElement(name = "method.saveMonitoringRecord")
	public void saveMonitoringRecord(
			@DomainElement(name = "Ipcan Context", staticValue = "ipcanContext") IpcanContext ipcanContext,
			@DomainElement(name = "domain.specifiedRecordDataType") RecordDataType recordDataType) {
		try {
			log.info("--------saveMonitoringRecord()----{开始保存上报的监测数据记录}");
			log.info("{开始保存上报的监测数据记录}");
			log.info("{ipcanContext}---is:" + ipcanContext);
			switch (ipcanContext.getRunTimeInfo().getApplicationType()) {
			case SENSOR:
				log.info("{开始保存上报的{传感器}监测数据记录}");
				sensorHandleService.saveSensorDataReport(ipcanContext,
						recordDataType);
				break;
			case CONSTANT_TH:
				log.info("{开始保存上报的{恒温恒湿机}监测数据记录}");
				handleConstantThService.saveConstantThDataReport(ipcanContext,
						recordDataType);
				log.info("{结束保存上报的{恒温恒湿机}监测数据记录}");
				break;
			case METEOROLOGICAL_STATION:
				log.info("{开始保存上报的{气象站}监测数据记录}");
				handleWeatherService.saveWeatherDataReport(ipcanContext,
						recordDataType);
				break;
			case AIR_CLEANER:
				log.info("{开始保存上报的--{air-cleaner}-[空气净化器]--监测数据记录}");
				handleAirCleanerService.saveAirCleanerDataReport(ipcanContext, recordDataType);
				log.info("{结束保存上报的--{air-cleaner}-[空气净化器]--监测数据记录}");
				break;
			default:
				break;
			}
			log.info("{结束保存上报的监测数据记录}");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
