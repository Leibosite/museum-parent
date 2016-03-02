package com.qingruan.museum.engine.service.schedule;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qingruan.museum.dao.entity.Device;
import com.qingruan.museum.dao.repository.DeviceDao;
import com.qingruan.museum.domain.enums.device.DeviceStatus;
import com.qingruan.museum.domain.models.enums.DeviceType;

/**
 * 每五分钟调度执行一次，查看设备是否失灵
 * 
 * @author 雷得宝
 * 
 */
@Service
@Slf4j
public class DeviceStatusScanService {
	@Autowired
	private DeviceDao deviceDao;

	public void scanTask() {
		log.info("{开始扫面设备的状态}--每5分钟扫描一次");
		scanningTask();
		log.info("{结束扫面设备的状态}");
	}

	private void scanningTask() {
		List<Device> devices = (List<Device>) deviceDao.findAll();
		List<Device> todoLists = new ArrayList<Device>();
		if (devices == null || devices.size() == 0)
			return;
		else {
			for (Device device : devices) {
				DeviceType type = device.getDeviceType();
				if (type == null)
					return;
				else {

					if (type.equals(DeviceType.MONITORING_POINT_ONE)
							|| type.equals(DeviceType.MONITORING_POINT_TWO)
							|| type.equals(DeviceType.MONITORING_STATION)) {

						Long updateDate = device.getUpdateStamp();
						Long currentTime = System.currentTimeMillis();
						if (updateDate == null) {
							log.error("{ there is no update_stamp of device }");
							throw new RuntimeException();
						} else {
							Long interval = (currentTime - updateDate) / 1000 / 60 / 60;
							log.info("每一小时扫描数据库，更新设备状态");
							// 判断实体的最后修改时间截至当前时间是时间间隔>5min,则认为设备失活
							if (interval > 1) {
								device.setDeviceStatus(DeviceStatus.DISCONNECT);
								todoLists.add(device);
							}
						}
					}
				}
			}

			if (todoLists.size() != 0)
				try {
					for (Device device : todoLists) {
						deviceDao.save(device);
					}

				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException();
				}

		}
	}
}
