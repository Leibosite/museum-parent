package com.qingruan.museum.engine.service.rule.action;

import java.util.Date;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qingruan.museum.dao.entity.Alarm;
import com.qingruan.museum.dao.entity.Device;
import com.qingruan.museum.domain.enums.WarnCatego;
import com.qingruan.museum.domain.enums.WarnFlag;
import com.qingruan.museum.domain.model.AlarmDetailsModel;
import com.qingruan.museum.engine.exception.ExceptionLog;
import com.qingruan.museum.engine.utils.TimeUtils;
import com.qingruan.museum.framework.jackson.JSONUtil;
import com.qingruan.museum.jpa.JpaAlarmService;
import com.qingruan.museum.pma.annotation.DomainElement;
import com.qingruan.museum.pma.annotation.ExecutionSelection;
import com.qingruan.museum.pma.annotation.MethodElement;
import com.qingruan.museum.session.IpcanContext;

@Slf4j
@Service
@ExecutionSelection
@DomainElement(name = "execution.reportAlarm")
public class BsReportAlarmService {
	@Autowired
	private JpaAlarmService jpaAlarmService;

	/**
	 * 根据告警或预警进行不同的处理操作
	 * 
	 * @param ipcanContext
	 * @param bearerUsage
	 */
	@MethodElement(name = "method.reportAlarm")
	public void reportAlarm(
			@DomainElement(name = "Ipcan Context", staticValue = "ipcanContext") IpcanContext ipcanContext,
			@DomainElement(name = "domain.specifiedAlarmType") WarnCatego warnCatego) {
		try {
			log.info("{开始保存上报告警信息到数据库中}");
			Alarm alarm = new Alarm();
			alarm.setAlarmDetails(warnCatego.value().toString());
			alarm.setDesp("告警信息");
			alarm.setStatus("1");
			alarm.setPriority("1");
			alarm.setWarnCategory(jpaAlarmService.findBywarnCatego(warnCatego));
			String date = TimeUtils.currentTime();
			alarm.setDateStamp(date);
			String name = "告警" + date;
			alarm.setName(name);
			Device device = ipcanContext.getRunTimeInfo().getMonitoringPoint();
			if (device != null && device.getRepoArea() != null) {
				// 设置告警详情
				AlarmDetailsModel model = new AlarmDetailsModel();
				String dat = new Date().toString();
				model.setDeviceId(device.getId());
				model.setDeviceName(device.getName());
				model.setRepoAreaId(device.getRepoArea().getId());
				model.setRepoAreaName(device.getRepoArea().getName());
				model.setAlarmTime(dat);
				model.setCurrentValue(0D);

				alarm.setAlarmDetails(JSONUtil.serialize(model));
				alarm.setDateStamp(dat);
				device.setWarnFlag(WarnFlag.WARN);
				alarm.setDevice(device);
				alarm.setRepoArea(device.getRepoArea());
				jpaAlarmService.saveOneAlarm(alarm);
				// log.info("{结束保存上报告警信息到数据库中}" + JSONUtil.serialize(alarm));
			}
		} catch (Exception e) {
			log.error(ExceptionLog.getErrorStack(e));
		}

	}

}
