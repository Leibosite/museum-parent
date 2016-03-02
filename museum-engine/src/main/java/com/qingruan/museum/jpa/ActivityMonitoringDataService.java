/**
 2015年1月20日
 14cells
 
 */
package com.qingruan.museum.jpa;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qingruan.museum.dao.entity.record.ActivityCleanerData;
import com.qingruan.museum.dao.entity.record.ActivityConstantThData;
import com.qingruan.museum.dao.entity.record.ActivityDataRecord;
import com.qingruan.museum.dao.entity.record.ActivityDeviceInfoRecord;
import com.qingruan.museum.dao.entity.record.ActivityWeatherData;
import com.qingruan.museum.dao.entity.record.HourDataRecord;
import com.qingruan.museum.dao.repository.DeviceDao;
import com.qingruan.museum.dao.repository.record.ActivityCleanerDataDao;
import com.qingruan.museum.dao.repository.record.ActivityConstantThDataDao;
import com.qingruan.museum.dao.repository.record.ActivityDataRecordDao;
import com.qingruan.museum.dao.repository.record.ActivityDeviceInfoRecordDao;
import com.qingruan.museum.dao.repository.record.ActivityWeatherDataDao;
import com.qingruan.museum.dao.repository.record.HourDataRecordDao;
import com.qingruan.museum.domain.enums.device.DeviceStatus;

/**
 * @author tommy
 * 
 */
@Service
@Slf4j
@Transactional(readOnly = true)
public class ActivityMonitoringDataService {
	@Autowired
	private ActivityDataRecordDao activityDataRecordDao;
	@Autowired
	private HourDataRecordDao hourDataRecordDao;
	@Autowired
	private DeviceDao deviceDao;
	@Autowired
	private ActivityDeviceInfoRecordDao activityDeviceInfoRecordDao;
	@Autowired
	private ActivityConstantThDataDao activityConstantThDataDao;
	@Autowired
	private ActivityWeatherDataDao activityWeatherDataDao;
	@Autowired
	private ActivityCleanerDataDao activityCleanerDataDao;

	public ActivityMonitoringDataService() {
	}

	public void saveOneActivityDataRecord(ActivityDataRecord record) {
		log.info("{start save ActivityDataRecord}" + record.toString());
		activityDataRecordDao.save(record);
		log.info("{end save ActivityDataRecord}");

	}

	public void saveHourRecordData(HourDataRecord record) {
		hourDataRecordDao.save(record);
	}

	@Transactional(readOnly = false)
	public void updateRunTimeDeviceInfo(Double currentPower, Long updateStamp,
			DeviceStatus deviceStatus, Long id,String currentValue) {
		try {
//	
			deviceDao.updateRunTimeDeviceInfoById(currentPower, updateStamp,
					deviceStatus, id,currentValue);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}

	}

	public void saveActivityDeviceInfoRecord(ActivityDeviceInfoRecord entity) {
		activityDeviceInfoRecordDao.save(entity);
	}

	public void saveActivityConstantThData(ActivityConstantThData entity) {
		activityConstantThDataDao.save(entity);
	}

	public void saveAllConstantThData(List<ActivityConstantThData> entitys) {
		activityConstantThDataDao.save(entitys);
	}
	
	public void saveOneActivityWeatherData(ActivityWeatherData entity) {
		log.info("{start save ActivityWeatherData}");
		activityWeatherDataDao.save(entity);
		log.info("{end save ActivityWeatherData}");

	}
	public void saveOneActivityAirCleanerData(ActivityCleanerData entity) {
		log.info("{start save ActivityCleanerData}");
		activityCleanerDataDao.save(entity);
		log.info("{end save ActivityWeatherData}");

	}
}
