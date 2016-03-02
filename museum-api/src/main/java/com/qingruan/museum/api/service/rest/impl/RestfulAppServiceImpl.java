package com.qingruan.museum.api.service.rest.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.qingruan.museum.api.service.rest.RestfulAppService;
import com.qingruan.museum.dao.entity.Device;
import com.qingruan.museum.dao.entity.RepoArea;
import com.qingruan.museum.dao.entity.record.ActivityDataRecord;
import com.qingruan.museum.dao.entity.record.HourDataRecord;
import com.qingruan.museum.dao.repository.DeviceDao;
import com.qingruan.museum.dao.repository.RepoAreaDao;
import com.qingruan.museum.dao.repository.record.ActivityDataRecordDao;
import com.qingruan.museum.dao.repository.record.HourDataRecordDao;
import com.qingruan.museum.domain.model.app.HelpAdviceModel;
import com.qingruan.museum.domain.model.app.MonitorDataByObjectTypeModel;
import com.qingruan.museum.domain.model.app.MonitorDataModel;
import com.qingruan.museum.domain.model.app.MonitorMqiDetailDataModel;
import com.qingruan.museum.domain.model.app.MuseumAllData;
import com.qingruan.museum.domain.model.app.MuseumEnvironmentDataModel;
import com.qingruan.museum.domain.model.app.ShowRoomInformationModel;
import com.qingruan.museum.domain.model.app.ShowroomDetailModel;
import com.qingruan.museum.domain.model.app.ShowroomModel;
import com.qingruan.museum.domain.models.enums.MonitorDataType;
import com.qingruan.museum.domain.models.enums.MonitorDeviceType;

@Service
@Slf4j
public class RestfulAppServiceImpl implements RestfulAppService {

	@Autowired
	private HourDataRecordDao hourDataRecordDao;
	@Autowired
	private RepoAreaDao repoAreaDao;
	@Autowired
	private DeviceDao deviceDao;
	@Autowired
	private ActivityDataRecordDao activityDataRecordDao;
	
	final static Long dayTtimeStamp = 86400000L; // 24*60*60*1000 一天的毫秒数
	final static Long hourTimeStamp = 3600000L;   // 60*60*1000; 一小时的毫秒数

	@Override
	public String fetchMuseumOverviewData() {

		/*
		 * Long startTime = TimeUtil.getLongTimeByFormat("2015-03-18 14:00",
		 * TimeUtil.DEFAULT_DATETIME_FORMAT_EX); Long endTime =
		 * System.currentTimeMillis(); Long areaId = 18L; List<HourDataRecord>
		 * list = hourDataRecordDao.findOneHourDataRecordByTimeScope(areaId,
		 * startTime, endTime);
		 */
		MuseumAllData museumAllData = new MuseumAllData();

		museumAllData.setResult_code("20001");
		museumAllData.setResult_message("sucess");

		MuseumEnvironmentDataModel environmentDataModel = new MuseumEnvironmentDataModel();
		environmentDataModel.setPm_2_5(24);
		environmentDataModel.setCo2(600);
		environmentDataModel.setTemperature(23);
		environmentDataModel.setHumidity(55);
		environmentDataModel.setLighting(21);
		environmentDataModel.setUv(22);
		environmentDataModel.setTotal_score(86.7);
		museumAllData.setEnvironment_data(environmentDataModel);

		List<ShowroomModel> showroomList = new ArrayList<ShowroomModel>();

		List<RepoArea> areaList = repoAreaDao.findAll();

		String advice[] = { "开启空调", "开启空气净化器", "调整光照强度" };

		for (RepoArea repoArea : areaList) {

			ShowroomModel showroomModel = new ShowroomModel();

			showroomModel.setId(repoArea.getId());
			showroomModel.setName(repoArea.getName());

			Long currentTime = System.currentTimeMillis();
			Long lastHour = currentTime - hourTimeStamp;
			List<HourDataRecord> hourDataRecordList = hourDataRecordDao
					.findHourDataRecordByAreaIdBetweenStartTimeAndEndTime(repoArea.getId(), MonitorDataType.CO2,
							lastHour, currentTime, MonitorDeviceType.SENSOR);
			double co2 = 0.0;
			if (hourDataRecordList != null && !hourDataRecordList.isEmpty()) {
				co2 = hourDataRecordList.get(0).getAvg();
			} else {
				co2 = getCo2Random();
			}

			hourDataRecordList = null;
			double temperature = 0.0;
			hourDataRecordList = hourDataRecordDao.findHourDataRecordByAreaIdBetweenStartTimeAndEndTime(
					repoArea.getId(), MonitorDataType.TEMPERATURE, lastHour, currentTime, MonitorDeviceType.SENSOR);
			if (hourDataRecordList != null && !hourDataRecordList.isEmpty()) {
				temperature = hourDataRecordList.get(0).getAvg();
			} else {
				temperature = getTemperatureRandom();
			}

			hourDataRecordList = null;
			double humidity = 0.0;
			hourDataRecordList = hourDataRecordDao.findHourDataRecordByAreaIdBetweenStartTimeAndEndTime(
					repoArea.getId(), MonitorDataType.HUMIDITY, lastHour, currentTime, MonitorDeviceType.SENSOR);
			if (hourDataRecordList != null && !hourDataRecordList.isEmpty()) {
				humidity = hourDataRecordList.get(0).getAvg();
			} else {
				humidity = getHumidityRandom();
			}

			showroomModel.setValue1("二氧化碳：" + co2);
			showroomModel.setValue2("温度：" + temperature);
			showroomModel.setValue3("湿度：" + humidity);
			showroomModel.setScore(getScoreRandom());
			int adviceIcon = getAdviceIconRandom();
			showroomModel.setAdvice_icon(adviceIcon);
			showroomModel.setAdvice(advice[adviceIcon - 1]);
			showroomList.add(showroomModel);
		}

		museumAllData.setShowroom(showroomList);

		Gson gson = new Gson();
		String jsonData = gson.toJson(museumAllData);
		//System.out.println(jsonData);
		log.info(jsonData);
		return jsonData;
	}

	private int getCo2Random() {
		int max = 800;
		int min = 300;
		Random random = new Random();

		int s = random.nextInt(max) % (max - min + 1) + min;
		return s;
	}

	private int getTemperatureRandom() {
		int max = 25;
		int min = 20;
		Random random = new Random();

		int s = random.nextInt(max) % (max - min + 1) + min;
		return s;
	}

	private int getHumidityRandom() {
		int max = 80;
		int min = 30;
		Random random = new Random();

		int s = random.nextInt(max) % (max - min + 1) + min;
		return s;
	}

	private int getScoreRandom() {
		int max = 99;
		int min = 80;
		Random random = new Random();

		int s = random.nextInt(max) % (max - min + 1) + min;
		return s;
	}

	private int getAdviceIconRandom() {
		int max = 3;
		int min = 1;
		Random random = new Random();

		int s = random.nextInt(max) % (max - min + 1) + min;
		return s;
	}

	private int getLightingRandom() {
		int max = 800;
		int min = 400;
		Random random = new Random();

		int s = random.nextInt(max) % (max - min + 1) + min;
		return s;
	}

	public static void main(String[] args) {

		System.out.println(new RestfulAppServiceImpl().fetchMuseumOverviewData());
	}

	@Override
	public String fetchShowroomDetailData(int showroomID) {

		/**
		 * 合理区间 湿度 35~55 湿度 20~22 CO2 <700 光照 <300
		 */

		ShowroomDetailModel showroomDetail = new ShowroomDetailModel();

		showroomDetail.setResult_code("20001");
		showroomDetail.setResult_message("success");

		ShowRoomInformationModel showroomInformationModel = showroomDetail.getShowroom_information();

		Long currentTime = System.currentTimeMillis();
		Long lastHour = currentTime - hourTimeStamp;

		RepoArea repoArea = repoAreaDao.findOne((long) showroomID);

		List<HourDataRecord> hourDataRecordList = hourDataRecordDao
				.findHourDataRecordByAreaIdBetweenStartTimeAndEndTime((long) showroomID, MonitorDataType.CO2, lastHour,
						currentTime, MonitorDeviceType.SENSOR);
		double co2 = 0.0;
		if (hourDataRecordList != null && !hourDataRecordList.isEmpty()) {
			co2 = hourDataRecordList.get(0).getAvg();
		} else {
			co2 = getCo2Random();
		}

		hourDataRecordList = null;
		double temperature = 0.0;
		hourDataRecordList = hourDataRecordDao.findHourDataRecordByAreaIdBetweenStartTimeAndEndTime((long) showroomID,
				MonitorDataType.TEMPERATURE, lastHour, currentTime, MonitorDeviceType.SENSOR);
		if (hourDataRecordList != null && !hourDataRecordList.isEmpty()) {
			temperature = hourDataRecordList.get(0).getAvg();
		} else {
			temperature = getTemperatureRandom();
		}

		hourDataRecordList = null;
		double humidity = 0.0;
		hourDataRecordList = hourDataRecordDao.findHourDataRecordByAreaIdBetweenStartTimeAndEndTime((long) showroomID,
				MonitorDataType.HUMIDITY, lastHour, currentTime, MonitorDeviceType.SENSOR);
		if (hourDataRecordList != null && !hourDataRecordList.isEmpty()) {
			humidity = hourDataRecordList.get(0).getAvg();
		} else {
			humidity = getHumidityRandom();
		}

		double lighting = 0.0;
		hourDataRecordList = hourDataRecordDao.findHourDataRecordByAreaIdBetweenStartTimeAndEndTime((long) showroomID,
				MonitorDataType.HUMIDITY, lastHour, currentTime, MonitorDeviceType.SENSOR);
		if (hourDataRecordList != null && !hourDataRecordList.isEmpty()) {
			lighting = hourDataRecordList.get(0).getAvg();
		} else {
			lighting = getLightingRandom();
		}

		showroomInformationModel.setName(repoArea.getName());
		showroomInformationModel.setTemperature(temperature);
		showroomInformationModel.setHumidity(humidity);

		MonitorDataModel monitorDataModel = new MonitorDataModel();
		monitorDataModel.setCo2(co2);
		monitorDataModel.setHumidity(humidity);
		monitorDataModel.setLighting(lighting);
		monitorDataModel.setSoil_humidity(20);
		monitorDataModel.setSoil_salinity(20);
		monitorDataModel.setSoil_temperature(20);
		monitorDataModel.setTemperature(temperature);

		showroomDetail.setMonitor_data(monitorDataModel);

		List<HelpAdviceModel> adviceList = showroomDetail.getShowroom_advice();

		HelpAdviceModel temperatureAdvice = new HelpAdviceModel();
		temperatureAdvice.setName("空调");

		if (temperature > 20 && temperature < 22) {
			temperatureAdvice.setAdvice("温度正常");
		} else if (temperature > 22) {
			temperatureAdvice.setAdvice("温度过高,需要降低温度");
		} else {
			temperatureAdvice.setAdvice("温度过低,需要升高温度");
		}
		adviceList.add(temperatureAdvice);

		HelpAdviceModel humidityAdvise = new HelpAdviceModel();
		humidityAdvise.setName("空气净化器");
		if (humidity > 35 && humidity < 55) {
			humidityAdvise.setAdvice("空气湿度正常");
		} else if (humidity >= 55) {
			humidityAdvise.setAdvice("空气湿度过高,需要开启空气净化器的除湿功能");
		} else {
			humidityAdvise.setAdvice("空气湿度过低,需要开启空气净化器的加湿功能");
		}
		adviceList.add(humidityAdvise);

		HelpAdviceModel co2Advise = new HelpAdviceModel();
		co2Advise.setName("通风设备");
		if (co2 < 700) {
			co2Advise.setAdvice("二氧化碳浓度正常");
		} else {
			co2Advise.setAdvice("二氧化碳浓度过高,需要开启通风设备");
		}
		adviceList.add(co2Advise);

		HelpAdviceModel lightAdvise = new HelpAdviceModel();
		lightAdvise.setName("照明设备");
		if (lighting < 300) {
			lightAdvise.setAdvice("光照强度正常");
		} else {
			lightAdvise.setAdvice("光照强度过高,需要调整照明设备的光照强度");
		}
		adviceList.add(lightAdvise);

		List<MonitorMqiDetailDataModel> monitorMqiDataModels = showroomDetail.getMonitor_mqi_data();

		List<Device> devices = deviceDao.findByRepoAreaAndDeviceType(repoArea);

		Long lastSecond = currentTime - dayTtimeStamp;
		
		List<ActivityDataRecord> activityDataRecords = activityDataRecordDao
				.findActivityDataRecordByObjectTypeBetweenAndTimeScope(MonitorDataType.TEMPERATURE, lastSecond, currentTime);

		if (activityDataRecords != null && !activityDataRecords.isEmpty() && activityDataRecords.size() != 0 && devices.size()!=0) {

			for (Device device : devices) {
				MonitorMqiDetailDataModel monitorMqiDetailDataModel = new MonitorMqiDetailDataModel();
				monitorMqiDetailDataModel.setName(device.getName());
				double value = 0.0;
				for (ActivityDataRecord activityDataRecord : activityDataRecords) {
					if ((device.getId() - activityDataRecord.getDevice().getId()) == 0) {
						value = activityDataRecord.getValue();
						monitorMqiDetailDataModel.setValue(value);
						monitorMqiDataModels.add(monitorMqiDetailDataModel);
						break;
					}
				}
			}
		}

		Gson gson = new Gson();
		String jsonData = gson.toJson(showroomDetail);
		//System.out.println(jsonData);
		log.info(jsonData);
		return jsonData;
	}

	@Override
	public String fetchMonitorDataByObjectType(int objectType, int showroomId) {

		MonitorDataByObjectTypeModel monitorDataByObjectTypeModel = new MonitorDataByObjectTypeModel();

		monitorDataByObjectTypeModel.setResult_code("20001");
		monitorDataByObjectTypeModel.setResult_message("success");

		List<MonitorMqiDetailDataModel> monitorMqiDataModels = monitorDataByObjectTypeModel.getMonitor_mqi_data();

		List<Device> devices = deviceDao.findByRepoAreaAndDeviceType(repoAreaDao.findOne((long) showroomId));
		
		Long currentTime = System.currentTimeMillis();
		Long lastSecond = currentTime - dayTtimeStamp;

		MonitorDataType monitorDataType = MonitorDataType.TEMPERATURE;

		switch (objectType) {
		case 1:
			monitorDataType = MonitorDataType.TEMPERATURE;
			break;
		case 2:
			monitorDataType = MonitorDataType.HUMIDITY;
			break;
		case 3:
			monitorDataType = MonitorDataType.CO2;
			break;
		case 4:
			monitorDataType = MonitorDataType.WIND_SPEED;
			break;
		case 5:
			monitorDataType = MonitorDataType.SOIL_TEMPERATURE;
			break;
		case 6:
			monitorDataType = MonitorDataType.SOIL_HUMIDITY;
			break;
		case 7:
			monitorDataType = MonitorDataType.SOIL_SALINITY;
			break;
		}

		List<ActivityDataRecord> activityDataRecords = activityDataRecordDao
				.findActivityDataRecordByObjectTypeBetweenAndTimeScope(monitorDataType, lastSecond, currentTime);

		if (activityDataRecords != null && activityDataRecords.size() != 0 && devices.size()!=0) {

			for (Device device : devices) {
				MonitorMqiDetailDataModel monitorMqiDetailDataModel = new MonitorMqiDetailDataModel();
				monitorMqiDetailDataModel.setName(device.getName());
				double value = 0.0;
				for (ActivityDataRecord activityDataRecord : activityDataRecords) {
					
					if ((device.getId() - activityDataRecord.getDevice().getId()) == 0) {
						value = activityDataRecord.getValue();
						monitorMqiDetailDataModel.setValue(value);
						//System.out.println(monitorMqiDetailDataModel);
						//log.info(arg0);
						monitorMqiDataModels.add(monitorMqiDetailDataModel);
						break;
					}
				}
			}
		}

		Gson gson = new Gson();
		String jsonData = gson.toJson(monitorDataByObjectTypeModel);
		//System.out.println(jsonData);
		log.info(jsonData);
		return jsonData;
	}

}
