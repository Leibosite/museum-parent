/**
 2015年1月15日
 tommy
 
 */
package com.qingruan.museum.engine.service.rule.condition;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.qingruan.museum.domain.models.enums.MonitorDataType;
import com.qingruan.museum.domain.models.enums.SaveFlagType;
import com.qingruan.museum.msg.MsgProperty.ApplicationType;
import com.qingruan.museum.msg.sensor.SensorDataContent;
import com.qingruan.museum.msg.sensor.SensorMsg;
import com.qingruan.museum.msg.sensor.SensorMsg.SensorAppType;
import com.qingruan.museum.pma.annotation.ConstraintSelection;
import com.qingruan.museum.pma.annotation.DomainElement;
import com.qingruan.museum.pma.annotation.MethodElement;
import com.qingruan.museum.session.IpcanContext;

/**
 * 监测数据上报
 * 
 * @author tommy
 * 
 */
@Slf4j
@Service
@ConstraintSelection(isService = true)
@DomainElement(name = "condition.MonitorDataReportCheck")
public class MonitorDataReportService {

	/**
	 * 上报的监测数据
	 * 
	 * @param ipcanContext
	 * @param monitorDataType
	 * @return
	 */
	@MethodElement(name = "method.monitorDataReport")
	@DomainElement(name = "domain.monitorDataReport")
	public Double monitorDataReport(
			@DomainElement(name = "ipcan context", staticValue = "ipcanContext") IpcanContext ipcanContext,
			@DomainElement(name = "domain.monitorDataType") MonitorDataType monitorDataType) {
		log.debug("monitorDataReport-----------------------start.");

		if (ipcanContext.getRecMuseumMsg() != null
				&& ipcanContext.getRecMuseumMsg().getMsgProperty()
						.getApplicationType().equals(ApplicationType.SENSOR)) {
			try {
				SensorMsg msg = (SensorMsg) ipcanContext.getRecMuseumMsg()
						.getMsgBody();
				if (msg != null
						&& msg.sensorAppType != null
						&& msg.sensorAppType
								.equals(SensorAppType.GATEWAY_POST_SENSOR_DATA)) {

					if (msg.getData() != null
							&& msg.getData().getDatas() != null
							&& msg.getData().getDatas().size() != 0) {
						List<SensorDataContent> datas = msg.getData()
								.getDatas();

						for (SensorDataContent content : datas) {
							if (content != null
									&& content.getMonitorDataType() != null
									&& content.getMonitorDataType().equals(
											monitorDataType))
								return content.getValue();

						}

					}

				}

			} catch (Exception e) {

			}

		}
		log.debug("monitorDataReport-----------------------end.");
		return -1D;
	}

	/**
	 * 保存上报数据开关
	 * 
	 * @param ipcanContext
	 * @param monitorDataType
	 * @return
	 */
	@MethodElement(name = "method.monitorDataSaveFlag")
	@DomainElement(name = "domain.monitorDataSaveFlag")
	public Boolean monitorDataSaveFlag(
			@DomainElement(name = "ipcan context", staticValue = "ipcanContext") IpcanContext ipcanContext,
			@DomainElement(name = "domain.saveFlagType") SaveFlagType saveFlagType) {
		log.debug("saveFlagType-----------------------start.");

		return Boolean.TRUE;
	}
}
