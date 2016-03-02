package com.qingruan.museum.agent.service.schedule.archive;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qingruan.museum.dao.entity.RepoArea;
import com.qingruan.museum.dao.entity.record.MonthDataRecord;
import com.qingruan.museum.dao.entity.record.YearDataRecord;
import com.qingruan.museum.dao.repository.RepoAreaDao;
import com.qingruan.museum.dao.repository.record.MonthDataRecordDao;
import com.qingruan.museum.dao.repository.record.YearDataRecordDao;
import com.qingruan.museum.domain.models.enums.MonitorDataType;
import com.qingruan.museum.domain.models.enums.MonitorDeviceType;

/**
 * @desp 从月表中获取数据，根据区域和对象每年进行一次归档
 * @author leibosite
 * 
 */

@Service
@Slf4j
public class YearArchiveSchedule {
	@Autowired
	private RepoAreaDao repoAreaDao;
	@Autowired
	private MonthDataRecordDao monthDataRecordDao;
	@Autowired
	private YearDataRecordDao yearDataRecordDao;

	public void computeMonthArchiveSchedule() {
		log.info("{ 每一年数据开始自动归档! }");
		
		List<RepoArea> areas = repoAreaDao.findAll();
		for (RepoArea repoArea : areas) {
			for(MonitorDeviceType monitorDeviceType:MonitorDeviceType.values()){
			for (MonitorDataType monitorObjectType : MonitorDataType.values()) {
				YearDataRecord yearDataRecord = getYearDataRecordAchive(monitorObjectType,monitorDeviceType,repoArea.getId());
				
				if(yearDataRecord != null){
					yearDataRecord.setMonitorDeviceType(monitorDeviceType);
					yearDataRecord.setObjectType(monitorObjectType);
					log.info("{ start: save yearDataRecord! }");
					yearDataRecordDao.save(yearDataRecord);
					log.info("{ end: save yearDataRecord! }");
				}
			}
			}
		}
	}
	
	private YearDataRecord getYearDataRecordAchive(MonitorDataType monitorObjectType,MonitorDeviceType monitorDeviceType,Long areaId){
		YearDataRecord yearDataRecords;

		List<MonthDataRecord> DataRecords = monthDataRecordDao.findMonthDataRecordByAreaIdBetweenStartTimeAndEndTime(areaId,monitorObjectType, HourArchiveSchedule.getBeforTimeMillis(Calendar.YEAR), System.currentTimeMillis(),monitorDeviceType);
		if(DataRecords == null || DataRecords.size()== 0){
			log.info("{ there is no data of " + monitorObjectType + "! }");
			return null;
		}else{
			log.info("{ create yearDataRecord of " + monitorObjectType + " }");
			yearDataRecords = new YearDataRecord();
			yearDataRecords.setRepoArea(repoAreaDao.findOne(areaId));
			yearDataRecords.setObjectType(monitorObjectType);
			yearDataRecords.setMonitorDeviceType(monitorDeviceType);
			computeYearDataRecord(DataRecords,yearDataRecords);
		}	
		return yearDataRecords;
	}
	
	private void computeYearDataRecord(List<MonthDataRecord> dataRecords,YearDataRecord yearDataRecords){
		double max = 0, min = 0, avg = 0, score = 0, sub = 0, maxTmp = 0, minTmp = 0;
		int count = 0;
		
		for (MonthDataRecord dataRecord : dataRecords) {
			maxTmp = dataRecord.getMax();
			minTmp = dataRecord.getMin();
			
			max = (max > maxTmp) ? max : maxTmp;
			min = (min < minTmp) ? min : minTmp;
			
			sub += dataRecord.getAvg();
			count++;
		}
		avg = sub / count;
		
		yearDataRecords.setMin(min);		
		yearDataRecords.setMax(max);
		yearDataRecords.setAvg(avg);
		yearDataRecords.setDateTime(new Timestamp(System.currentTimeMillis()));	
	}
}
