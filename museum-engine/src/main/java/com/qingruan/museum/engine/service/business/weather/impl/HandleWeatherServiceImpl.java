package com.qingruan.museum.engine.service.business.weather.impl;

import java.sql.Timestamp;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qingruan.museum.dao.entity.Device;
import com.qingruan.museum.dao.entity.record.ActivityWeatherData;
import com.qingruan.museum.domain.models.enums.RecordDataType;
import com.qingruan.museum.engine.service.business.weather.HandleWeatherService;
import com.qingruan.museum.jpa.ActivityMonitoringDataService;
import com.qingruan.museum.msg.MsgProperty.ApplicationType;
import com.qingruan.museum.msg.weather.WeatherDataContent;
import com.qingruan.museum.msg.weather.WeatherMsg;
import com.qingruan.museum.session.IpcanContext;

@Service
@Slf4j
public class HandleWeatherServiceImpl implements HandleWeatherService {
	@Autowired
	private ActivityMonitoringDataService activityMonitoringDataService;

	@Override
	public void saveWeatherDataReport(IpcanContext ipcanContext,
			RecordDataType recordDataType) {
		log.info("{ Start save weather station Data }");
		if (ipcanContext.getRecMuseumMsg() == null
				|| ipcanContext.getRecMuseumMsg().getMsgBody() == null)
			;
		else {

			if (ipcanContext.getRunTimeInfo().getApplicationType()
					.equals(ApplicationType.METEOROLOGICAL_STATION)) {
				// 保存气象站上报的数据
				this.saveWeatherData(ipcanContext.getRunTimeInfo()
						.getWeatherMsg(), ipcanContext);
			}
		}
		log.info("{ End save weather station Data }");
	}

	/**
	 * 保存气象站上报的数据
	 * 
	 * @param msgBody
	 * @param ipcanContext
	 * @return
	 */
	private Boolean saveWeatherData(WeatherMsg msg, IpcanContext ipcanContext) {
		ActivityWeatherData data = new ActivityWeatherData();
		if (msg != null && msg.getWeatherDatas() != null) {
			List<WeatherDataContent> datas = msg.getWeatherDatas();

			Device device = ipcanContext.getRunTimeInfo().getWeatherStation();
			if (device != null) {
				data.setDevice(device);
				data.setRepoAreaId(ipcanContext.getRunTimeInfo().getRepoArea()
						.getId());
				data.setDateTime(new Timestamp(System.currentTimeMillis()));

				for (WeatherDataContent content : datas) {
					switch (content.getMonitorDataType()) {
					case WIND_SPEED:
						data.setWindSpeed(content.getValue());
						break;
					case WIND_DIRECTION:
						data.setWindDirection(content.getWindDirection());
						break;
					case LONGITUDE:
						data.setLongitude(content.getValue());
						break;
					case LATITUDE:
						data.setLatitude(content.getValue());
						break;
					case HEIGHT:
						data.setHeight(content.getValue());
						break;
					case TEMPERATURE:
						Double value = content.getValue();
						if (value != null && value > 100L) {

							value = value / 100;
							
						}
						data.setTemperature(value);
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
					case LIGHTING:
						data.setLighting(content.getValue());
						break;
					case UV:
						data.setUv(content.getValue());
						break;
					case MAIN_CONTROL_PANEL_VOLTAGE:
						data.setMainCtrlPanelWoltage(content.getValue());

						break;
					case SOLAR_ENERGY_VOLTAGE:
						data.setSolarEnergyVoltage(content.getValue());

						break;
					case GPS_SPEED:
						data.setGpsSpeed(content.getValue());

						break;
					case GPS_DIRECTION:
						data.setGpsDirection(content.getValue());
						break;
					default:
						break;
					}
				}
				if (data != null) {
					log.info("weather station data is: " + data);
					activityMonitoringDataService.saveOneActivityWeatherData(data);
					return Boolean.TRUE;
				}
			}


		}

		return Boolean.FALSE;
	}
}
