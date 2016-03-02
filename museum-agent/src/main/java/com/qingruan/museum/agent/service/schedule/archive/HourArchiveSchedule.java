package com.qingruan.museum.agent.service.schedule.archive;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qingruan.museum.dao.entity.RepoArea;
import com.qingruan.museum.dao.entity.record.ActivityDataRecord;
import com.qingruan.museum.dao.entity.record.ActivityWeatherData;
import com.qingruan.museum.dao.entity.record.HourDataRecord;
import com.qingruan.museum.dao.repository.RepoAreaDao;
import com.qingruan.museum.dao.repository.record.ActivityDataRecordDao;
import com.qingruan.museum.dao.repository.record.ActivityWeatherDataDao;
import com.qingruan.museum.dao.repository.record.HourDataRecordDao;
import com.qingruan.museum.domain.models.enums.MonitorDataType;
import com.qingruan.museum.domain.models.enums.MonitorDeviceType;
import com.qingruan.museum.framework.util.TimeUtil;
import com.qingruan.museum.framework.util.GlobalParameter.TimeFormat;

/**
 * @desp 从活动表中获取数据，根据区域和对象每小时归一次档，
 * @author leibosite
 * @author tommy
 * @modified by tommy,desp:
 * 
 */
@Service
@Slf4j
public class HourArchiveSchedule {
	@Autowired
	private RepoAreaDao repoAreaDao;
	@Autowired
	private ActivityDataRecordDao activityDataRecordDao;
	@Autowired
	private ActivityWeatherDataDao activityWeatherDataDao;
	@Autowired
	private HourDataRecordDao hourDataRecordDao;

	public void computeHourArchiveSchedule() {
		log.info("{ 每一小时数据开始自动归档! }");
		Long currentTime = System.currentTimeMillis();
		log.info("{ current time is:  }" + currentTime);

		List<RepoArea> areas = repoAreaDao.findAll();
		for (RepoArea repoArea : areas) {

			for (MonitorDataType monitorObjectType : MonitorDataType.values()) {
				HourDataRecord hourDataRecord = getHourDataRecordAchive(
						monitorObjectType, repoArea.getId(),currentTime);
				if(hourDataRecord!=null){
					
					hourDataRecord.setMonitorDeviceType(MonitorDeviceType.SENSOR);
					if (hourDataRecord != null) {
						log.info("{ start: save sensor hourDataRecord! } " +hourDataRecord);
						hourDataRecord.setUpdateStamp(currentTime);
						hourDataRecordDao.save(hourDataRecord);
						log.info("{ end: save sensor hourDataRecord! }");
					}
				}
		
			}

			// 归档气象站数据
			log.info("{ ---------{start}: -------computeHourWeatherData! }");
			computeHourWeatherData(repoArea.getId(), currentTime);
			log.info("{ ---------{End}: -------computeHourWeatherData! }");
		}
	}

	/**
	 * 归档气象站数据函数
	 * 
	 * @param areaId
	 * @param monitorDataType
	 */
	private void computeHourWeatherData(final Long areaId,
			final Long currentTime) {
		Long startTime = currentTime - 3600000L;
		log.info("{ ---------{start}: -------computeHourWeatherData! }");
		log.info("{ ---------{param}: -------areaId! } " + areaId);
		if (areaId == null)
			return;
		RepoArea repoArea = repoAreaDao.findOne(areaId);
		log.info("{ ---------{areaId}: -------areaId! } " +areaId);
		log.info("{ ---------{param}: -------current time! } " + currentTime +" 时间 "+TimeUtil.getStringTime(currentTime,TimeFormat.YEAR_MONTH_DAY_HOUR_MIN_SEC));
			
		log.info("{ ---------{param}: -------start time! } " + startTime+" 时间 "+TimeUtil.getStringTime(currentTime,TimeFormat.YEAR_MONTH_DAY_HOUR_MIN_SEC));
		List<ActivityWeatherData> activityWeatherDatas = activityWeatherDataDao
				.findActivityWeatherDataByAreaIdBetweenStartTimeAndEndTime(
						areaId, startTime, currentTime);
		log.info("{ ---------{param}: -------activityWeatherDatas! } "
				+ activityWeatherDatas.toString());
		if (activityWeatherDatas == null || activityWeatherDatas.size() == 0)
			return;
		HashMap<String, ArrayList<Double>> maps = new HashMap<String, ArrayList<Double>>();

		ArrayList<Double> windSpeeds = new ArrayList<Double>();
		ArrayList<Double> longitudes = new ArrayList<Double>();
		ArrayList<Double> latitudes = new ArrayList<Double>();
		ArrayList<Double> heights = new ArrayList<Double>();
		ArrayList<Double> temperatures = new ArrayList<Double>();
		ArrayList<Double> humiditys = new ArrayList<Double>();
		ArrayList<Double> pm2_5s = new ArrayList<Double>();
		ArrayList<Double> pm1_0s = new ArrayList<Double>();
		ArrayList<Double> airPressures = new ArrayList<Double>();
		ArrayList<Double> uvs = new ArrayList<Double>();
		ArrayList<Double> gpsSpeeds = new ArrayList<Double>();
		ArrayList<Double> gpsDirections = new ArrayList<Double>();

		for (ActivityWeatherData data : activityWeatherDatas) {
			if (data.getWindSpeed() != null)
				windSpeeds.add(data.getWindSpeed());
			if (data.getLongitude() != null)
				longitudes.add(data.getLongitude());
			if (data.getLatitude() != null)
				latitudes.add(data.getLatitude());
			if (data.getHeight() != null)
				heights.add(data.getHeight());
			if (data.getTemperature() != null)
				temperatures.add(data.getTemperature());
			if (data.getHumidity() != null)
				humiditys.add(data.getHumidity());
			if (data.getPm2_5() != null)
				pm2_5s.add(data.getPm2_5());
			if (data.getPm1_0() != null)
				pm1_0s.add(data.getPm1_0());
			if (data.getAirPressure() != null)
				airPressures.add(data.getAirPressure());
			if (data.getUv() != null)
				uvs.add(data.getUv());
			if (data.getGpsSpeed() != null)
				gpsSpeeds.add(data.getGpsSpeed());
			if (data.getGpsDirection() != null)
				gpsDirections.add(data.getGpsDirection());

		}
		maps.put(MonitorDataType.WIND_SPEED.toString(), windSpeeds);
		maps.put(MonitorDataType.LONGITUDE.toString(), longitudes);
		maps.put(MonitorDataType.LATITUDE.toString(), latitudes);
		maps.put(MonitorDataType.HEIGHT.toString(), heights);
		maps.put(MonitorDataType.TEMPERATURE.toString(), temperatures);
		maps.put(MonitorDataType.HUMIDITY.toString(), humiditys);
		maps.put(MonitorDataType.PM2_5.toString(), pm2_5s);
		maps.put(MonitorDataType.PM1_0.toString(), pm1_0s);
		maps.put(MonitorDataType.AIRPRESSURE.toString(), airPressures);
		maps.put(MonitorDataType.UV.toString(), uvs);
		maps.put(MonitorDataType.GPS_SPEED.toString(), gpsSpeeds);
		maps.put(MonitorDataType.GPS_DIRECTION.toString(), gpsDirections);

		Iterator it = maps.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			HourDataRecord hourDataRecord = new HourDataRecord();
			hourDataRecord
					.setMonitorDeviceType(MonitorDeviceType.WEATHER_STATION);
			
			
			hourDataRecord.setRepoArea(repoArea);
			hourDataRecord.setObjectType(MonitorDataType.valueOf(key));
			this.computeWeatherDataRecord(maps.get(key), hourDataRecord);
			log.info("{ ---------{start}: -------hourDataRecordDao save! } "
					+ hourDataRecord.toString());
			try {
				hourDataRecord.setUpdateStamp(currentTime);
				hourDataRecordDao.save(hourDataRecord);
			} catch (Exception e) {
				e.printStackTrace();
			}
		
			log.info("{ ---------{end}: -------hourDataRecordDao save! } ");
		}

	}

	private HourDataRecord getHourDataRecordAchive(
			MonitorDataType monitorObjectType, Long areaId,Long currentTime) {
		HourDataRecord hourDataRecord;
       Long startTime=currentTime-3600000L;
		List<ActivityDataRecord> activityDataRecords = activityDataRecordDao
				.findActivityDataRecordByAreaIdBetweenStartTimeAndEndTime(
						areaId, monitorObjectType,startTime,
								currentTime);
		if (activityDataRecords == null || activityDataRecords.size() == 0) {
			log.info("{ there is no sensor data of " + monitorObjectType + "of area id is: }"+ areaId);
			return null;
		} else {
			log.info("{ create sensor hourDataRecord of " + monitorObjectType + " }");
			hourDataRecord = new HourDataRecord();
			hourDataRecord.setRepoArea(repoAreaDao.findOne(areaId));
			hourDataRecord.setObjectType(monitorObjectType);
			computeActivityDataRecord(activityDataRecords, hourDataRecord);
			return hourDataRecord;
		}
		
	}

	private void computeActivityDataRecord(
			List<ActivityDataRecord> activityDataRecords,
			HourDataRecord hourDataRecord) {
		double max = 0, min = 0, avg = 0, score = 0, sub = 0, temp = 0;
		int count = 0;

		for (ActivityDataRecord activityDataRecord : activityDataRecords) {
			temp = activityDataRecord.getValue();

			max = (max > temp) ? max : temp;
			min = (min < temp) ? min : temp;
			sub += temp;
			count++;
		}
		avg = sub / count;

		hourDataRecord.setMin(min);
		hourDataRecord.setMax(max);
		hourDataRecord.setAvg(avg);
		hourDataRecord.setDateTime(new Timestamp(System.currentTimeMillis()));
	}

	private void computeWeatherDataRecord(List<Double> records,
			HourDataRecord hourDataRecord) {
		double max = 0, min = 0, avg = 0, score = 0, sub = 0, temp = 0;
		int count = 0;

		for (Double record : records) {
			temp = record;

			max = (max > temp) ? max : temp;
			min = (min < temp) ? min : temp;
			sub += temp;
			count++;
		}
		if(count!=0)
		avg = sub / count;

		hourDataRecord.setMin(min);
		hourDataRecord.setMax(max);
		hourDataRecord.setAvg(avg);
		hourDataRecord.setDateTime(new Timestamp(System.currentTimeMillis()));
	}

	/**
	 * 返回前一时间粒度的毫秒数
	 * 
	 * @param dateTime
	 *            时间粒度
	 * @return 毫秒数
	 */
	public static Long getBeforTimeMillis(int dateTime) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(dateTime, calendar.get(dateTime) - 1);
		return calendar.getTimeInMillis();
	}

	/**
	 * 测试反射机制 TODO:delete
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		System.out.println(System.currentTimeMillis());
	System.out.println(MonitorDataType.valueOf("TEMPERATURE"));
	System.out.println(MonitorDataType.from("TEMPERATURE"));
		ActivityWeatherData activityWeatherData = new ActivityWeatherData();
		Field[] field = activityWeatherData.getClass().getDeclaredFields();
		try {
			for (int i = 0; i < field.length; i++) {

				String name = field[i].getName();
				String type = field[i].getGenericType().toString();
				System.out.println(name);
				System.out.println(type);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
