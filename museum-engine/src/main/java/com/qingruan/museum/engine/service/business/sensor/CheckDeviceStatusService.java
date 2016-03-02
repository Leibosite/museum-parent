package com.qingruan.museum.engine.service.business.sensor;

import com.qingruan.museum.dao.entity.Device;

/**
 * 
 * author zhanghui
 * 
 */
public interface CheckDeviceStatusService {
	/**
	 * 
	 * Engine 检查设备状态
	 * 
	 * @param device
	 * 
	 */
	void sendDeviceStatusCheck(Device device);
}
