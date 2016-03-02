package com.qingruan.museum.agent.service.schedule.archive;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qingruan.museum.dao.entity.RepoArea;
import com.qingruan.museum.dao.entity.record.DayDataRecord;
import com.qingruan.museum.dao.entity.record.MonthDataRecord;
import com.qingruan.museum.dao.repository.RepoAreaDao;
import com.qingruan.museum.dao.repository.record.DayDataRecordDao;
import com.qingruan.museum.dao.repository.record.MonthDataRecordDao;
import com.qingruan.museum.domain.models.enums.MonitorDataType;
import com.qingruan.museum.domain.models.enums.MonitorDeviceType;

/**
 * @desp 从小时表中获取数据，根据区域和对象每天归一次档，
 * @author leibosite
 * 
 */

@Service
@Slf4j
public class MonthArchiveSchedule {
	@Autowired
	private RepoAreaDao repoAreaDao;
	@Autowired
	private DayDataRecordDao dayDataRecordDao;
	@Autowired
	private MonthDataRecordDao monthDataRecordDao;

	public void computeMonthArchiveSchedule() {
		log.info("{ 每个月数据开始自动归档! }");
		
		List<RepoArea> areas = repoAreaDao.findAll();
		for (RepoArea repoArea : areas) {
			for(MonitorDeviceType monitorDeviceType:MonitorDeviceType.values()){
			for (MonitorDataType monitorObjectType : MonitorDataType.values()) {
				MonthDataRecord monthDataRecord = getMonthDataRecordAchive(monitorObjectType, monitorDeviceType,repoArea.getId());
				
				if(monthDataRecord != null){
					monthDataRecord.setMonitorDeviceType(monitorDeviceType);
					monthDataRecord.setObjectType(monitorObjectType);
					log.info("{ start: save monthDataRecord! }");
					monthDataRecordDao.save(monthDataRecord);
					log.info("{ end: save monthDataRecord! }");
				}
			}
			}
		}
	}
	
	private MonthDataRecord getMonthDataRecordAchive(MonitorDataType monitorObjectType,MonitorDeviceType monitorDeviceType,Long areaId){
		MonthDataRecord monthDataRecords;

		List<DayDataRecord> DataRecords = dayDataRecordDao.findDayDataRecordByAreaIdBetweenStartTimeAndEndTime(areaId,monitorObjectType, HourArchiveSchedule.getBeforTimeMillis(Calendar.MONTH), System.currentTimeMillis(),monitorDeviceType);
		if(DataRecords == null || DataRecords.size()== 0){
			log.info("{ there is no data of " + monitorObjectType + "! }");
			return null;
		}else{
			log.info("{ create monthDataRecord of " + monitorObjectType + " }");
			monthDataRecords = new MonthDataRecord();
			monthDataRecords.setRepoArea(repoAreaDao.findOne(areaId));
			monthDataRecords.setObjectType(monitorObjectType);
			monthDataRecords.setMonitorDeviceType(monitorDeviceType);
			computeMonthDataRecord(DataRecords,monthDataRecords);
		}	
		return monthDataRecords;
	}
	
	private void computeMonthDataRecord(List<DayDataRecord> dataRecords,MonthDataRecord monthDataRecord){
		double max = 0, min = 0, avg = 0, score = 0, sub = 0, maxTmp = 0, minTmp = 0;
		int count = 0;
		
		for (DayDataRecord dataRecord : dataRecords) {
			maxTmp = dataRecord.getMax();
			minTmp = dataRecord.getMin();
			
			max = (max > maxTmp) ? max : maxTmp;
			min = (min < minTmp) ? min : minTmp;
			
			sub += dataRecord.getAvg();
			count++;
		}
		avg = sub / count;
		
		monthDataRecord.setMin(min);		
		monthDataRecord.setMax(max);
		monthDataRecord.setAvg(avg);
		monthDataRecord.setDateTime(new Timestamp(System.currentTimeMillis()));	
	}
}
