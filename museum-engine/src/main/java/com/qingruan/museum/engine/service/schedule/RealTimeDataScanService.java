package com.qingruan.museum.engine.service.schedule;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import com.qingruan.framework.redis.ShardedJedisSentinelPoolTemplate;
import com.qingruan.framework.redis.ShardedJedisSentinelPoolTemplate.ShardedJedisSentinelPoolCallback;
import com.qingruan.museum.dao.entity.Device;
import com.qingruan.museum.dao.entity.RepoArea;
import com.qingruan.museum.dao.entity.record.ActivityConstantThData;
import com.qingruan.museum.dao.entity.record.ActivityDataRecord;
import com.qingruan.museum.dao.repository.AlarmDao;
import com.qingruan.museum.dao.repository.DeviceDao;
import com.qingruan.museum.dao.repository.RepoAreaDao;
import com.qingruan.museum.dao.repository.record.ActivityConstantThDataDao;
import com.qingruan.museum.dao.repository.record.ActivityDataRecordDao;
import com.qingruan.museum.domain.models.enums.AreaType;
import com.qingruan.museum.domain.models.enums.MonitorDataType;
import com.qingruan.museum.engine.utils.CommonUtils;
import com.qingruan.museum.framework.jackson.JSONUtil;
import com.qingruan.museum.msg.DataContent;
import com.qingruan.museum.msg.notification.AreaDataMsg;
import com.qingruan.museum.msg.notification.DeviceDataMsg;

/**
 * 扫描实时数据归档
 * 
 * @author tommy
 * 
 */
@Service
@Slf4j
public class RealTimeDataScanService {
	@Autowired
	private DeviceDao deviceDao;
	@Autowired
	private RepoAreaDao repoAreaDao;
	@Autowired
	private ActivityDataRecordDao activityDataRecordDao;
	@Autowired
	private AlarmDao alarmDao;
	@Autowired
	private ActivityConstantThDataDao activityConstantThDataDao;
	@Autowired
	private ShardedJedisSentinelPoolTemplate jedisSentinelTemplate;
	// 5分钟的毫秒数
	final static Long interval = 36000000000L;

	// final static Long interval = 15552000000L;

	public void scanTask() {
		log.info("{开始扫面装载实时数据}--每5分钟扫描一次");
		scan();
		log.info("{结束扫面装载实时数据}--每5分钟扫描一次");
	}

	private void scan() {

		log.info("{RealTimeDataScanService.scan start}");

		List<RepoArea> repoAreas = repoAreaDao.findAll();
		if (repoAreas == null || repoAreas.size() == 0)
			return;
		log.info("{开始遍历所有区域,打包实时区域数据概览}");
		for (RepoArea repoArea : repoAreas) {
			AreaDataMsg areaDataMsg = new AreaDataMsg();
			Long areaId = repoArea.getId();
			Long currentTime = System.currentTimeMillis();
			Long alarmCount = alarmDao.findAlarmCountByAreaAndTimeScope(areaId,
					currentTime - interval, currentTime);
			areaDataMsg.setAreaId(areaId);
			areaDataMsg.setAlarmNumber(alarmCount);
			areaDataMsg.setTimeStamp(currentTime);
			Integer score=CommonUtils.generatRandom();
			log.info("{-----{score for this area}------[score]-is: }"+score);
			areaDataMsg.setScore(score);
			String flag;
			if (alarmCount == null || alarmCount == 0L)
				flag = "NORMAL";
			else
				flag = "ALARM";
			areaDataMsg.setStatus(flag);
			// 区域为恒温恒湿展柜
			if (repoArea.getAreaType().equals(AreaType.CONSTANT_TH_DISPLAY)) {
				List<ActivityConstantThData> thData = activityConstantThDataDao
						.findActivityConstantThDataByAreaIdBetweenAndTimeScope(
								areaId, currentTime - interval, currentTime);
				if (thData == null || thData.size() == 0)
					continue;
				areaDataMsg.setDataOverviews(getTAreaOverView(areaId, thData));
				String areaJson = JSONUtil.serialize(areaDataMsg);
				this.saveRedis(areaJson, areaId);
				continue;

			}
			// 非恒温恒湿展柜
			List<Device> devices = deviceDao.findByRepoArea(repoArea);
			if (devices == null || devices.size() == 0)
				continue;
			else {

				areaDataMsg.setDataOverviews(getCommonAreaOverView(areaId));
				for (Device device : devices) {
					String json = device.getCurrentMonitorValue();
					if (StringUtils.isBlank(json))
						;
					else {
						DeviceDataMsg deviceDataMsg = JSONUtil.deserialize(
								json, DeviceDataMsg.class);
						if (deviceDataMsg == null)
							continue;
						else {
							areaDataMsg.getDataDetails().add(deviceDataMsg);

							String areaJson = JSONUtil.serialize(areaDataMsg);
							this.saveRedis(areaJson, areaId);
							continue;
						}

					}

				}

			}

		}

		log.info("{RealTimeDataScanService.scan end}");
	}

	private void saveRedis(final String json, final Long areaId) {
		jedisSentinelTemplate.run("http_simple_queue_service",
				new ShardedJedisSentinelPoolCallback<String>() {
					@Override
					public String execute(Jedis jedis) {

						jedis.set(AreaDataMsg.class.getName() + ":" + areaId,
								json);
						return "";
					}
				});
	}

	private List<DataContent> getCommonAreaOverView(Long areaId) {
		log.info("{开始查询区域数据概况}");
		List<DataContent> dataOverviews = new ArrayList<DataContent>();
		Long currentTime = System.currentTimeMillis();
		for (MonitorDataType monitorObjectType : MonitorDataType.values()) {
			List<ActivityDataRecord> records = activityDataRecordDao
					.findActivityDataRecordByAreaIdBetweenStartTimeAndEndTime(
							areaId, monitorObjectType, currentTime - interval,
							currentTime);
			if (records == null || records.size() == 0)
				;
			else {
				DataContent dataContent = new DataContent();
				dataContent.setMonitorDataType(monitorObjectType);
				dataContent.setValue(records.get(0).getValue());
				dataOverviews.add(dataContent);

			}

		}
		log.info("{结束查询区域数据概况}");
		return dataOverviews;
	}

	/**
	 * 获取恒温恒湿展柜
	 * 
	 * @param areaId
	 * @return
	 */
	private List<DataContent> getTAreaOverView(Long areaId,
			List<ActivityConstantThData> thDatas) {
		log.info("{开始查询恒温恒湿展柜区域数据概况}");
		List<DataContent> dataOverviews = new ArrayList<DataContent>();
		Boolean temper = Boolean.TRUE;
		Boolean hum = Boolean.TRUE;
		for (ActivityConstantThData th : thDatas) {
			if (temper.equals(Boolean.FALSE) && hum.equals(Boolean.FALSE))
				break;

			if (th.getObjectType().equals(MonitorDataType.TEMPERATURE)
					&& temper.equals(Boolean.TRUE)) {
				DataContent dataContent = new DataContent();
				dataContent.setMonitorDataType(th.getObjectType());
				dataContent.setValue(th.getValue());
				dataOverviews.add(dataContent);
				temper = Boolean.FALSE;
				continue;
			}
			if (th.getObjectType().equals(MonitorDataType.HUMIDITY)
					&& hum.equals(Boolean.TRUE)) {
				DataContent dataContent = new DataContent();
				dataContent.setMonitorDataType(th.getObjectType());
				dataContent.setValue(th.getValue());
				dataOverviews.add(dataContent);
				hum = Boolean.FALSE;
				continue;
			}

		}

		log.info("{结束查询恒温恒湿展柜区域数据概况}");
		return dataOverviews;
	}

}
