package com.qingruan.museum.agent.service.schedule.archive;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qingruan.museum.dao.entity.RepoArea;
import com.qingruan.museum.dao.entity.record.DayDataRecord;
import com.qingruan.museum.dao.entity.record.HourDataRecord;
import com.qingruan.museum.dao.repository.RepoAreaDao;
import com.qingruan.museum.dao.repository.record.DayDataRecordDao;
import com.qingruan.museum.dao.repository.record.HourDataRecordDao;
import com.qingruan.museum.domain.models.enums.MonitorDataType;
import com.qingruan.museum.domain.models.enums.MonitorDeviceType;

/**
 * @desp 从小时表中获取数据，根据区域和对象每天归一次档，
 * @author leibosite
 * 
 */

@Service
@Slf4j
public class DailyArchiveSchedule {
	@Autowired
	private RepoAreaDao repoAreaDao;
	@Autowired
	private HourDataRecordDao hourDataRecordDao;
	@Autowired
	private DayDataRecordDao dayDataRecordDao;

	public void computeDayArchiveSchedule() {
		log.info("{ 每一天数据开始自动归档! }");
		Long currentTime=System.currentTimeMillis();
		List<RepoArea> areas = repoAreaDao.findAll();
		for (RepoArea repoArea : areas) {
			for(MonitorDeviceType monitorDeviceType:MonitorDeviceType.values()){
			for (MonitorDataType monitorObjectType : MonitorDataType.values()) {
				DayDataRecord dayDataRecord = getDayDataRecordAchive(monitorObjectType, monitorDeviceType,repoArea.getId(),currentTime);
				
				if(dayDataRecord != null){
					dayDataRecord.setMonitorDeviceType(monitorDeviceType);
					dayDataRecord.setObjectType(monitorObjectType);
					dayDataRecord.setUpdateStamp(currentTime);
					log.info("{ start: save dayDataRecord! }");
					dayDataRecordDao.save(dayDataRecord);
					log.info("{ end: save dayDataRecord! }");
				}
			}
		}
		}
	}
	
	private DayDataRecord getDayDataRecordAchive(MonitorDataType monitorObjectType,MonitorDeviceType monitorDeviceType,Long areaId,Long currentTime){
		DayDataRecord dataRecord;

		List<HourDataRecord> hourDataRecords = hourDataRecordDao.findHourDataRecordByAreaIdBetweenStartTimeAndEndTime(areaId,monitorObjectType, HourArchiveSchedule.getBeforTimeMillis(Calendar.DAY_OF_MONTH), currentTime,monitorDeviceType);
		if(hourDataRecords == null || hourDataRecords.size()== 0){
			log.info("{ there is no data of " + monitorObjectType + "! }");
			return null;
		}else{
			log.info("{ create dayDataRecord of " + monitorObjectType + " }");
			dataRecord = new DayDataRecord();
			dataRecord.setRepoArea(repoAreaDao.findOne(areaId));
			dataRecord.setObjectType(monitorObjectType);
			dataRecord.setMonitorDeviceType(monitorDeviceType);
			computeDayDataRecord(hourDataRecords,dataRecord);
		}	
		return dataRecord;
	}
	
	private void computeDayDataRecord(List<HourDataRecord> dataRecords,DayDataRecord dayDataRecord){
		double max = 0, min = 0, avg = 0, score = 0, sub = 0, maxTmp = 0, minTmp = 0;
		int count = 0;
		
		for (HourDataRecord dataRecord : dataRecords) {
			maxTmp = dataRecord.getMax();
			minTmp = dataRecord.getMin();
			
			max = (max > maxTmp) ? max : maxTmp;
			min = (min < minTmp) ? min : minTmp;
			
			sub += dataRecord.getAvg();
			count++;
		}
		avg = sub / count;
		
		dayDataRecord.setMin(min);
		dayDataRecord.setMax(max);
		dayDataRecord.setAvg(avg);
		dayDataRecord.setDateTime(new Timestamp(System.currentTimeMillis()));	
	}
}
