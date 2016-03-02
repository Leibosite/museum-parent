package com.qingruan.museum.engine.service.schedule;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qingruan.museum.dao.entity.Device;
import com.qingruan.museum.dao.repository.DeviceDao;
import com.qingruan.museum.domain.models.enums.DeviceType;
import com.qingruan.museum.engine.service.business.sensor.SendSensorCmdService;

@Service
@Slf4j
public class MonitorStationUpdateService {
	@Autowired
	private DeviceDao deviceDao;
	@Autowired
	private SendSensorCmdService sendSensorCmdService;

	@Autowired
	public void scanTask() {
		log.info("@Start--{MonitorStationUpdateService}--{开始更新监测站维护的监测点信息}--每5分钟扫描一次");
		List<Device> devices = deviceDao
				.findByDeviceType(DeviceType.MONITORING_STATION);
		if (devices == null || devices.size() == 0) {
			log.info("@Handling--{MonitorStationUpdateService}--{数据库中无监测站，跳过}------");
			return;
		}
		for (Device device : devices) {
			String macAddr = device.getMacAddr();
			// 检查中继管辖传感器状态
//			sendSensorCmdService.sendSensorStatusCheck(macAddr);
			// 更新中继管辖信息
//			sendSensorCmdService.sendStationInfoUpdate(macAddr);
			
			
			
		}
		sendSensorCmdService.sendSimulatorData("C3CEAC21B9D8", "E84E062A1B0F");
		log.info("@End--{MonitorStationUpdateService}--{开始更新监测站维护的监测点信息}");
	}

}
