package com.qingruan.museum.admin.service.alarm.history;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.qingruan.museum.admin.service.spec.AlarmSpecifications;
import com.qingruan.museum.admin.utils.BusinessRecordUtil;
import com.qingruan.museum.dao.entity.Alarm;
import com.qingruan.museum.dao.repository.AlarmDao;
import com.qingruan.museum.framework.jackson.JSONUtil;

/**
 * 历史告警
 * 
 * @author tommy
 * 
 */
@Slf4j
@Component
@Transactional(readOnly = true)
public class HistoryAlarmManager {

	@Autowired
	private AlarmDao alarmDao;

	// public Policy getPolicy(Long id) {
	// // 避免报空指针异常
	// if (policyWrapperDao.findOne(id) != null) {
	// Policy policy = JSONUtil.deserialize(policyWrapperDao.findOne(id)
	// .getPolicyContent(), Policy.class);
	// policy.setId(id);
	// return policy;
	// } else
	// return null;
	//
	// }

	/**
	 * 保存
	 * 
	 * @param entity
	 */
	@Transactional(readOnly = false)
	public void saveAlarm(Alarm entity) {

		alarmDao.save(entity);
		log.info("save alarm {}", JSONUtil.serialize(entity));
	}

	@Transactional(readOnly = false)
	public void deleteAlarm(Long id) {
		alarmDao.delete(id);
		log.info("delete alarm {}", id);
	}

	@Transactional(readOnly = false)
	public void disable(Long id) {
		Alarm entity = alarmDao.findOne(id);
		// TODO:
		// entity.setStatus(false);
		this.saveAlarm(entity);
		log.info("disable alarm {}", JSONUtil.serialize(entity));
	}

	@Transactional(readOnly = false)
	public void enable(Long id) {
		Alarm entity = alarmDao.findOne(id);
		// TODO:
		// entity.setStatus(false);
		this.saveAlarm(entity);
		log.info("enable alarm {}", JSONUtil.serialize(entity));
	}

	public Page<Alarm> getPaged(String alarmName, Integer status, String start,
			String end, Pageable pageable) {

		// TODO:
		Long startL = BusinessRecordUtil.convertTimeString2Long(start);
		Long endL = BusinessRecordUtil.convertTimeString2Long(end);

		Page<Alarm> historyAlarms = alarmDao.findAll(
				AlarmSpecifications.alarmSepc(alarmName, status, startL, endL),
				pageable);

		return historyAlarms;
	}

	public List<Alarm> findByNameLike(String name) {
		List<Alarm> alarms = alarmDao.findByNameLike(name);
		return alarms;
	}

}