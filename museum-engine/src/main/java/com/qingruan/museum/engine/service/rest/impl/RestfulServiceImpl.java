package com.qingruan.museum.engine.service.rest.impl;

import static io.netty.buffer.Unpooled.copiedBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import com.google.gson.Gson;
import com.qingruan.framework.redis.ShardedJedisSentinelPoolTemplate;
import com.qingruan.framework.redis.ShardedJedisSentinelPoolTemplate.ShardedJedisSentinelPoolCallback;
import com.qingruan.museum.dao.entity.Device;
import com.qingruan.museum.dao.entity.record.ActivityCleanerData;
import com.qingruan.museum.dao.entity.record.ActivityWeatherData;
import com.qingruan.museum.dao.entity.record.DayDataRecord;
import com.qingruan.museum.dao.entity.record.HourDataRecord;
import com.qingruan.museum.dao.entity.record.MonthDataRecord;
import com.qingruan.museum.dao.entity.record.YearDataRecord;
import com.qingruan.museum.dao.repository.DeviceDao;
import com.qingruan.museum.dao.repository.record.ActivityCleanerDataDao;
import com.qingruan.museum.dao.repository.record.ActivityWeatherDataDao;
import com.qingruan.museum.dao.repository.record.DayDataRecordDao;
import com.qingruan.museum.dao.repository.record.HourDataRecordDao;
import com.qingruan.museum.dao.repository.record.MonthDataRecordDao;
import com.qingruan.museum.dao.repository.record.YearDataRecordDao;
import com.qingruan.museum.domain.model.ResultCode;
import com.qingruan.museum.domain.model.weather.HistoryData;
import com.qingruan.museum.domain.model.weather.WeatherData;
import com.qingruan.museum.domain.model.weather.WeatherDataHistory;
import com.qingruan.museum.domain.model.weather.WeatherDataOverview;
import com.qingruan.museum.domain.models.enums.MonitorDataType;
import com.qingruan.museum.domain.models.enums.MonitorDeviceType;
import com.qingruan.museum.engine.framework.ApplicationContextGuardian;
import com.qingruan.museum.engine.service.business.constantth.SendConstantThCmdService;
import com.qingruan.museum.engine.service.rest.RestfulService;
import com.qingruan.museum.exception.WebApiException;
import com.qingruan.museum.exception.enums.WebApiExceptionType;
import com.qingruan.museum.framework.esb.redispushpop.RedisMQPushSender;
import com.qingruan.museum.framework.jackson.JSONUtil;
import com.qingruan.museum.framework.util.GlobalParameter.TimeFormat;
import com.qingruan.museum.framework.util.TimeUtil;
import com.qingruan.museum.msg.MuseumMsg;
import com.qingruan.museum.msg.notification.AreaDataMsg;

@Service
@Slf4j
public class RestfulServiceImpl implements RestfulService {
	@Autowired
	private RedisMQPushSender redisMQPushSender;
	@Autowired
	private SendConstantThCmdService sendConstantThCmdService;
	@Autowired
	private ActivityWeatherDataDao activityWeatherDataDao;
	@Autowired
	private HourDataRecordDao hourDataRecordDao;
	@Autowired
	private DayDataRecordDao dayDataRecordDao;
	@Autowired
	private MonthDataRecordDao monthDataRecordDao;
	@Autowired
	private YearDataRecordDao yearDataRecordDao;
	@Autowired
	private DeviceDao deviceDao;
	@Autowired
	private ActivityCleanerDataDao activityCleanerDataDao;
	final static Long interval = 86400000L;
	final static Long monthInterval = 2592000000L;
	final static Long yearInterval = 31449600000L;
	final static Long tenYearInterval = 314496000000L;

	private static ShardedJedisSentinelPoolTemplate jedisSentinelTemplate;
	private static Gson gson = new Gson();
	private final static String HTTPSQS = "http_simple_queue_service";

	static {
		final ApplicationContextGuardian applicationContextGuardian = ApplicationContextGuardian
				.getInstance();
		jedisSentinelTemplate = (ShardedJedisSentinelPoolTemplate) applicationContextGuardian
				.GetAppContext()
				.getBean(ShardedJedisSentinelPoolTemplate.class);
	}

	@Override
	public ByteBuf getAreaDataMsg(final String arg) {

		if ("list".equals(arg)) {
			String jsonString = jedisSentinelTemplate.run(HTTPSQS,
					new ShardedJedisSentinelPoolCallback<String>() {

						@Override
						public String execute(Jedis paramJedis) {
							Set<String> areaDataMsg = paramJedis
									.keys(AreaDataMsg.class.getName() + ":*");
							String[] keys = new String[areaDataMsg.size()];
							areaDataMsg.toArray(keys);
							List<String> areaDataList = paramJedis.mget(keys);
							List<AreaDataMsg> msgsList = new ArrayList<AreaDataMsg>();
							for (String string : areaDataList) {
								AreaDataMsg areaDataMsgObject = gson.fromJson(
										string, AreaDataMsg.class);
								msgsList.add(areaDataMsgObject);
							}

							String areaDataJson = gson.toJson(msgsList);
							return areaDataJson;
						}
					});

			ByteBuf content = copiedBuffer(jsonString, CharsetUtil.UTF_8);
			return content;

		} else {

			String jsonString = jedisSentinelTemplate.run(HTTPSQS,
					new ShardedJedisSentinelPoolCallback<String>() {

						@Override
						public String execute(Jedis paramJedis) {
							String areaDataMsg = paramJedis
									.get(AreaDataMsg.class.getName() + ":"
											+ arg);
							if (areaDataMsg == null) {
								return "NO_DATA";
							}

							return areaDataMsg;
						}
					});
			ByteBuf content = copiedBuffer(jsonString, CharsetUtil.UTF_8);
			return content;
		}

	}

	@Override
	public ByteBuf getErrorMsg() {
		ByteBuf content = copiedBuffer("NO_DATA", CharsetUtil.UTF_8);
		return content;
	}

	@Override
	public ByteBuf getConstantThMsg(final String arg) {

		Boolean flag = this.sendConstantThCmd(Long.parseLong(arg));
		String result = "";
		if (flag.equals(Boolean.TRUE))
			result = "SUCCESS";
		else
			result = "FAILED";
		ByteBuf content = copiedBuffer(result, CharsetUtil.UTF_8);
		return content;
	}

	@Override
	public ByteBuf getSensorUpgradeMsg(final String arg) {

		return null;
	}

	/**
	 * 下发恒温恒湿调节指令
	 * 
	 * @param id
	 */
	private Boolean sendConstantThCmd(Long id) {
		if (id == null)
			return Boolean.FALSE;
		MuseumMsg museumMsg = sendConstantThCmdService.sendConstantCmd(id);
		if (museumMsg == null)
			return Boolean.FALSE;
		try {
			String json = JSONUtil.serialize(museumMsg);
			redisMQPushSender.sendEngineToNetty(json);
			return Boolean.TRUE;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Boolean.FALSE;

	}

	@Override
	public ByteBuf getWeatherMsg(String arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ByteBuf getOverViewWeatherData(Long id) {
		ByteBuf content = null;
		try {
			if (id == null)

				throw new WebApiException(
						WebApiExceptionType.MISS_WEATHER_DEVICE_ID);
			else {

				List<ActivityWeatherData> activityWeatherDatas = activityWeatherDataDao
						.findActivityWeatherDataByDeviceId(id);
				if (activityWeatherDatas == null
						|| activityWeatherDatas.size() == 0)
					return null;
				ActivityWeatherData aciActivityWeatherData = activityWeatherDatas
						.get(0);
				WeatherDataOverview weatherDataOverview = new WeatherDataOverview();
				if (aciActivityWeatherData.getAirPressure() != null) {
					WeatherData w = new WeatherData();
					w.setValue(aciActivityWeatherData.getAirPressure());
					w.setMonitorDataType(MonitorDataType.AIRPRESSURE);
					weatherDataOverview.getDataOverviews().add(w);
				}
				if (aciActivityWeatherData.getHeight() != null) {
					WeatherData w = new WeatherData();
					w.setValue(aciActivityWeatherData.getHeight());
					w.setMonitorDataType(MonitorDataType.HEIGHT);
					weatherDataOverview.getDataOverviews().add(w);

				}
				if (aciActivityWeatherData.getHumidity() != null) {
					WeatherData w = new WeatherData();
					w.setValue(aciActivityWeatherData.getHumidity());
					w.setMonitorDataType(MonitorDataType.HUMIDITY);
					weatherDataOverview.getDataOverviews().add(w);

				}
				if (aciActivityWeatherData.getLatitude() != null) {
					WeatherData w = new WeatherData();
					w.setValue(aciActivityWeatherData.getLatitude());
					w.setMonitorDataType(MonitorDataType.LATITUDE);
					weatherDataOverview.getDataOverviews().add(w);

				}
				if (aciActivityWeatherData.getLighting() != null) {
					WeatherData w = new WeatherData();
					w.setValue(aciActivityWeatherData.getLighting());
					w.setMonitorDataType(MonitorDataType.LIGHTING);
					weatherDataOverview.getDataOverviews().add(w);

				}
				if (aciActivityWeatherData.getLongitude() != null) {
					WeatherData w = new WeatherData();
					w.setValue(aciActivityWeatherData.getLongitude());
					w.setMonitorDataType(MonitorDataType.LONGITUDE);
					weatherDataOverview.getDataOverviews().add(w);

				}
				if (aciActivityWeatherData.getMainCtrlPanelWoltage() != null) {
					WeatherData w = new WeatherData();
					w.setValue(aciActivityWeatherData.getMainCtrlPanelWoltage());
					w.setMonitorDataType(MonitorDataType.MAIN_CONTROL_PANEL_VOLTAGE);
					weatherDataOverview.getDataOverviews().add(w);

				}
				if (aciActivityWeatherData.getPm1_0() != null) {
					WeatherData w = new WeatherData();
					w.setValue(aciActivityWeatherData.getPm1_0());
					w.setMonitorDataType(MonitorDataType.PM1_0);
					weatherDataOverview.getDataOverviews().add(w);

				}
				if (aciActivityWeatherData.getPm2_5() != null) {
					WeatherData w = new WeatherData();
					w.setValue(aciActivityWeatherData.getPm2_5());
					w.setMonitorDataType(MonitorDataType.PM2_5);
					weatherDataOverview.getDataOverviews().add(w);

				}
				if (aciActivityWeatherData.getSolarEnergyVoltage() != null) {
					WeatherData w = new WeatherData();
					w.setValue(aciActivityWeatherData.getSolarEnergyVoltage());
					w.setMonitorDataType(MonitorDataType.SOLAR_ENERGY_VOLTAGE);
					weatherDataOverview.getDataOverviews().add(w);

				}
				if (aciActivityWeatherData.getTemperature() != null) {
					WeatherData w = new WeatherData();
					w.setValue(aciActivityWeatherData.getTemperature());
					w.setMonitorDataType(MonitorDataType.TEMPERATURE);
					weatherDataOverview.getDataOverviews().add(w);

				}
				if (aciActivityWeatherData.getUv() != null) {
					WeatherData w = new WeatherData();
					w.setValue(aciActivityWeatherData.getUv());
					w.setMonitorDataType(MonitorDataType.UV);
					weatherDataOverview.getDataOverviews().add(w);

				}
				if (aciActivityWeatherData.getWindSpeed() != null) {
					WeatherData w = new WeatherData();
					w.setValue(aciActivityWeatherData.getWindSpeed());
					w.setMonitorDataType(MonitorDataType.WIND_SPEED);
					weatherDataOverview.getDataOverviews().add(w);

				}
				if (StringUtils.isNoneBlank(aciActivityWeatherData
						.getWindDirection())) {
					weatherDataOverview
							.setSpeedDirection(aciActivityWeatherData
									.getWindDirection());

				}
				weatherDataOverview.setResultCode(ResultCode.SUCCESS.value());
				weatherDataOverview.setMsg(ResultCode.SUCCESS.toString());
				content = copiedBuffer(JSONUtil.serialize(weatherDataOverview),
						CharsetUtil.UTF_8);

			}

		} catch (WebApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
	}

	@Override
	public ByteBuf getRealtimeWeatherData(Long id) {
		return this.getOverViewWeatherData(id);
	}

	@Override
	public ByteBuf getHistoryWeatherData(Long id, String granulariy,
			MonitorDataType monitorDataType, Long timeStamp) {
		// 通过try catch来捕获业务处理中的异常
		ByteBuf content = null;
		WeatherDataHistory weatherDataHistory = null;
		try {
			if (id == null || StringUtils.isBlank(granulariy)
					|| monitorDataType == null || timeStamp == null)
				throw new WebApiException(
						WebApiExceptionType.MISS_WEATHER_HISTORY_PARAMS);
			else {
				MonitorDeviceType monitorDeviceType = MonitorDeviceType.WEATHER_STATION;

				// 根据不同的时间粒度进行业务分流处理
				switch (granulariy) {
				case "HOUR":
					String hourDay = TimeUtil.getStringTime(timeStamp,
							TimeFormat.YEAR_MONTH_DAY);
					log.info("----------{case HOUR}----{hourDay}-----is: "
							+ hourDay);
					Long startTime = TimeUtil.getLongTimeByFormat(hourDay,
							TimeUtil.DEFAULT_DATE_FORMAT);
					timeStamp = timeStamp + interval;
					log.info("----------{case HOUR}----{startTime}-----is: "
							+ startTime);
					log.info("----------{case HOUR}----{endTime}-----is: "
							+ timeStamp);
					Device device = deviceDao.findOne(id);
					if (device == null || device.getRepoArea() == null) {
						log.info("----------{case HOUR}----{该设备没有绑定区域}-----is: ");
						return null;
					}

					List<HourDataRecord> records = hourDataRecordDao
							.findAllHourDataRecordByDeviceAndTimeScopeAndType(
									device.getRepoArea().getId(), startTime,
									timeStamp, monitorDataType,
									monitorDeviceType);
					if (records == null || records.size() == 0)
						throw new WebApiException(
								WebApiExceptionType.RECORDS_NOT_EXIST_IN_DB);
					weatherDataHistory = new WeatherDataHistory();
					log.info("----------{case HOUR}----{records.size}-----is: "
							+ records.size());
					for (HourDataRecord record : records) {
						HistoryData historyData = new HistoryData();
						historyData.setMonitorDataType(monitorDataType);
						historyData.setAvg(record.getAvg());
						historyData.setMax(record.getMax());
						historyData.setMin(record.getMin());
						historyData.setTimeStamp(record.getUpdateStamp());
						weatherDataHistory.getHistoryDatas().add(historyData);

					}
					weatherDataHistory
							.setResultCode(ResultCode.SUCCESS.value());
					weatherDataHistory.setMsg(ResultCode.SUCCESS.toString());
					content = copiedBuffer(
							JSONUtil.serialize(weatherDataHistory),
							CharsetUtil.UTF_8);

					break;
				case "DAY":
					String day = TimeUtil.getStringTime(timeStamp,
							TimeFormat.YEAR_MONTH);
					startTime = TimeUtil.getLongTimeByFormat(day,
							TimeUtil.DEFAULT_SHORT_YEAR_MONTH_FORMAT);
					Long endTime = monthInterval + startTime;
					List<DayDataRecord> dayRecords = dayDataRecordDao
							.findAllDayDataRecordByDeviceAndTimeScopeAndType(
									id, startTime, endTime, monitorDataType,
									monitorDeviceType);
					if (dayRecords == null || dayRecords.size() == 0)
						throw new WebApiException(
								WebApiExceptionType.RECORDS_NOT_EXIST_IN_DB);
					weatherDataHistory = new WeatherDataHistory();

					for (DayDataRecord record : dayRecords) {
						HistoryData historyData = new HistoryData();
						historyData.setMonitorDataType(monitorDataType);
						historyData.setAvg(record.getAvg());
						historyData.setMax(record.getMax());
						historyData.setMin(record.getMin());
						historyData.setTimeStamp(record.getUpdateStamp());
						weatherDataHistory.getHistoryDatas().add(historyData);

					}
					weatherDataHistory
							.setResultCode(ResultCode.SUCCESS.value());
					weatherDataHistory.setMsg(ResultCode.SUCCESS.toString());
					content = copiedBuffer(
							JSONUtil.serialize(weatherDataHistory),
							CharsetUtil.UTF_8);
					break;
				case "MONTH":
					String month = TimeUtil.getStringTime(timeStamp,
							TimeFormat.YEAR);
					startTime = TimeUtil.getLongTimeByFormat(month,
							TimeUtil.DEFAULT_SHORT_YEAR_FORMAT);
					endTime = startTime + yearInterval;
					List<MonthDataRecord> monthRecords = monthDataRecordDao
							.findAllMonthDataRecordByDeviceAndTimeScopeAndType(
									id, startTime, endTime, monitorDataType,
									monitorDeviceType);
					if (monthRecords == null || monthRecords.size() == 0)
						throw new WebApiException(
								WebApiExceptionType.RECORDS_NOT_EXIST_IN_DB);
					weatherDataHistory = new WeatherDataHistory();

					for (MonthDataRecord record : monthRecords) {
						HistoryData historyData = new HistoryData();
						historyData.setMonitorDataType(monitorDataType);
						historyData.setAvg(record.getAvg());
						historyData.setMax(record.getMax());
						historyData.setMin(record.getMin());
						historyData.setTimeStamp(record.getUpdateStamp());
						weatherDataHistory.getHistoryDatas().add(historyData);

					}
					weatherDataHistory
							.setResultCode(ResultCode.SUCCESS.value());
					weatherDataHistory.setMsg(ResultCode.SUCCESS.toString());
					content = copiedBuffer(
							JSONUtil.serialize(weatherDataHistory),
							CharsetUtil.UTF_8);

					break;
				case "YEAR":
					String year = TimeUtil.getStringTime(timeStamp,
							TimeFormat.YEAR);
					startTime = TimeUtil.getLongTimeByFormat(year,
							TimeUtil.DEFAULT_SHORT_YEAR_FORMAT);
					endTime = startTime + tenYearInterval;
					List<YearDataRecord> yearRecords = yearDataRecordDao
							.findAllYearDataRecordByDeviceAndTimeScopeAndType(
									id, startTime, endTime, monitorDataType,
									monitorDeviceType);
					if (yearRecords == null || yearRecords.size() == 0)
						throw new WebApiException(
								WebApiExceptionType.RECORDS_NOT_EXIST_IN_DB);
					weatherDataHistory = new WeatherDataHistory();

					for (YearDataRecord record : yearRecords) {
						HistoryData historyData = new HistoryData();
						historyData.setMonitorDataType(monitorDataType);
						historyData.setAvg(record.getAvg());
						historyData.setMax(record.getMax());
						historyData.setMin(record.getMin());
						historyData.setTimeStamp(record.getUpdateStamp());
						weatherDataHistory.getHistoryDatas().add(historyData);

					}
					weatherDataHistory
							.setResultCode(ResultCode.SUCCESS.value());
					weatherDataHistory.setMsg(ResultCode.SUCCESS.toString());
					content = copiedBuffer(
							JSONUtil.serialize(weatherDataHistory),
							CharsetUtil.UTF_8);
					break;

				default:
					break;

				}
				return content;
			}
		} catch (WebApiException e) {
			weatherDataHistory = new WeatherDataHistory();
			weatherDataHistory.setResultCode(e.getExceptionType().value());
			weatherDataHistory.setMsg(e.getExceptionType().toString());
			content = copiedBuffer(JSONUtil.serialize(weatherDataHistory),
					CharsetUtil.UTF_8);
			return content;
		}

	}

	@Override
	public ByteBuf getOverViewAirCleanerData(Long id) {
		// TODO Auto-generated method stub
		return getRealtimeAirCleanerData(id);
	}

	@Override
	public ByteBuf getRealtimeAirCleanerData(Long id) {
		ByteBuf content = null;
		try {
			if (id == null)

				throw new WebApiException(
						WebApiExceptionType.MISS_AIR_CLEANER_DEVICE_ID);
			Device airCleaner=deviceDao.findOne(id);
			if(airCleaner==null)
				throw new WebApiException(
						WebApiExceptionType.DEVICE_NOT_EXIST_IN_DB);
			
                Long currentTime=System.currentTimeMillis();
                // 30分钟之前
                Long startTime=currentTime-1800000L;
				List<ActivityCleanerData> activityCleanerDatas = activityCleanerDataDao.findActivityCleanerDataByAreaIdBetweenStartTimeAndEndTime(id, startTime, currentTime);
						
					
				if (activityCleanerDatas == null
						|| activityCleanerDatas.size() == 0)
					throw new WebApiException(
							WebApiExceptionType.RECORDS_NOT_EXIST_IN_DB);
				ActivityCleanerData activityCleanerData = activityCleanerDatas
						.get(0);
				WeatherDataOverview weatherDataOverview = new WeatherDataOverview();
				if (activityCleanerData.getTemperature()!= null) {
					WeatherData w = new WeatherData();
					w.setValue(activityCleanerData.getTemperature());
					w.setMonitorDataType(MonitorDataType.TEMPERATURE);
					weatherDataOverview.getDataOverviews().add(w);
				}
				if (activityCleanerData.getHumidity() != null) {
					WeatherData w = new WeatherData();
					w.setValue(activityCleanerData.getHumidity());
					w.setMonitorDataType(MonitorDataType.HUMIDITY);
					weatherDataOverview.getDataOverviews().add(w);

				}
	
	
				if (activityCleanerData.getPm1_0() != null) {
					WeatherData w = new WeatherData();
					w.setValue(activityCleanerData.getPm1_0());
					w.setMonitorDataType(MonitorDataType.PM1_0);
					weatherDataOverview.getDataOverviews().add(w);

				}
				if (activityCleanerData.getPm2_5() != null) {
					WeatherData w = new WeatherData();
					w.setValue(activityCleanerData.getPm2_5());
					w.setMonitorDataType(MonitorDataType.PM2_5);
					weatherDataOverview.getDataOverviews().add(w);

				}
				weatherDataOverview.setResultCode(ResultCode.SUCCESS.value());
				weatherDataOverview.setMsg(ResultCode.SUCCESS.toString());
				content = copiedBuffer(JSONUtil.serialize(weatherDataOverview),
						CharsetUtil.UTF_8);

		

		} catch (WebApiException e) {
			WeatherDataOverview	weatherDataOverview = new WeatherDataOverview();
			weatherDataOverview.setResultCode(e.getExceptionType().value());
			weatherDataOverview.setMsg(e.getExceptionType().toString());
			content = copiedBuffer(JSONUtil.serialize(weatherDataOverview),
					CharsetUtil.UTF_8);
			return content;
		}
		return content;
	}

	@Override
	public ByteBuf getHistoryAirCleanerData(Long id, String granulariy,
			MonitorDataType monitorDataType, Long timeStamp) {
		// TODO Auto-generated method stub
		return null;
	}

}
