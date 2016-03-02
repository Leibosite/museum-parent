package com.qingruan.museum.jpa;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qingruan.museum.dao.entity.Alarm;
import com.qingruan.museum.dao.entity.Device;
import com.qingruan.museum.dao.entity.WarnCategory;
import com.qingruan.museum.dao.repository.AlarmDao;
import com.qingruan.museum.dao.repository.DeviceDao;
import com.qingruan.museum.dao.repository.WarnCategoryDao;
import com.qingruan.museum.domain.enums.WarnCatego;

/**
 * @author tommy
 * 
 */
@Service
@Slf4j
public class JpaAlarmService {
	@Autowired
	private AlarmDao alarmDao;
	@Autowired
	private DeviceDao deviceDao;
	@Autowired
	private WarnCategoryDao warnCategoryDao;

	/**
	 * 保存告警信息
	 * 
	 * @param alarm
	 */
	public void saveOneAlarm(Alarm alarm) {
		log.info("{save on alar}" + alarm.toString());

		alarmDao.saveAndFlush(alarm);
	}

	/**
	 * 保存告警列表
	 * 
	 * @param alarms
	 */
	public void saveAllAlarms(List<Alarm> alarms) {

		alarmDao.save(alarms);
	}

	public Device findByDeviceNo(String number) {
		return deviceDao.findByDeviceNo(number);
	}

	public WarnCategory findBywarnCatego(WarnCatego category) {
		return warnCategoryDao.findByCategory(category);

	}

	public void saveDevice(Device device) {
		deviceDao.save(device);

	}
}
