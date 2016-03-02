package com.qingruan.museum.engine.service.business.aircleaner.impl;

import java.sql.Timestamp;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qingruan.museum.dao.entity.Device;
import com.qingruan.museum.dao.entity.record.ActivityCleanerData;
import com.qingruan.museum.domain.models.enums.RecordDataType;
import com.qingruan.museum.engine.exception.ExceptionLog;
import com.qingruan.museum.engine.service.business.aircleaner.HandleAirCleanerService;
import com.qingruan.museum.jpa.ActivityMonitoringDataService;
import com.qingruan.museum.msg.MsgProperty.ApplicationType;
import com.qingruan.museum.msg.aircleaner.AirCleanerDataContent;
import com.qingruan.museum.msg.aircleaner.AirCleanerMsg;
import com.qingruan.museum.session.IpcanContext;

@Slf4j
@Service
public class HandleAirCleanerServiceImpl implements HandleAirCleanerService {
	@Autowired
	private ActivityMonitoringDataService activityMonitoringDataService;

	/**
	 * 保存上报的空气净化器数据
	 */
	@Override
	public void saveAirCleanerDataReport(IpcanContext ipcanContext,
			RecordDataType recordDataType) {
		log.info("{ Start save air cleaner Data }");
		if (ipcanContext.getRecMuseumMsg() == null
				|| ipcanContext.getRecMuseumMsg().getMsgBody() == null)
			;
		else {

			if (ipcanContext.getRunTimeInfo().getApplicationType()
					.equals(ApplicationType.AIR_CLEANER)) {
				// 保存上报空气净化器的数据
				this.saveAirCleanerData(ipcanContext.getRunTimeInfo()
						.getAirCleanerMsg(), ipcanContext);
			}
		}
		log.info("{ End save air cleaner  Data }");
	}

	/**
	 * 保存气象站上报的数据
	 * 
	 * @param msgBody
	 * @param ipcanContext
	 * @return
	 */
	private Boolean saveAirCleanerData(AirCleanerMsg msg,
			IpcanContext ipcanContext) {

		if (msg != null && msg.getAirCleanerDatas() != null) {
			ActivityCleanerData data = new ActivityCleanerData();
			List<AirCleanerDataContent> datas = msg.getAirCleanerDatas();

			Device device = ipcanContext.getRunTimeInfo().getAirCleaner();
			if (device != null) {
				data.setDevice(device);
				if (ipcanContext.getRunTimeInfo().getRepoArea() != null)
					data.setRepoAreaId(ipcanContext.getRunTimeInfo()
							.getRepoArea().getId());

				data.setDateTime(new Timestamp(System.currentTimeMillis()));

				for (AirCleanerDataContent content : datas) {
					switch (content.getMonitorDataType()) {
					case TEMPERATURE:
						data.setTemperature(content.getValue());
						break;
					case HUMIDITY:
						data.setHumidity(content.getValue());
						break;
					case PM2_5:
						data.setPm2_5(content.getValue());
						break;
					case PM1_0:
						data.setPm1_0(content.getValue());
						break;
					default:
						break;
					}
				}
			}

			if (data != null) {
				log.info("air cleaner data is: " + data);
				try {
					activityMonitoringDataService
							.saveOneActivityAirCleanerData(data);
				} catch (Exception e) {
					log.error(ExceptionLog.getErrorStack(e));

				}

				return Boolean.TRUE;
			}
		}

		return Boolean.FALSE;
	}

}
