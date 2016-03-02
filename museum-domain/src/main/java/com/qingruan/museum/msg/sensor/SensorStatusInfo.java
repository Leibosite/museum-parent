package com.qingruan.museum.msg.sensor;

import com.qingruan.museum.domain.enums.device.DeviceStatus;
import com.qingruan.museum.domain.models.enums.DeviceType;

import lombok.Getter;
import lombok.Setter;

/**
 * 传感器状态信息
 * 
 * @author tommy
 * 
 */
@Getter
@Setter
public class SensorStatusInfo {
	// 设备的MAC地址
	private String macAddr;
	// 设备的id
	private String id;
	// 设备类型
	private DeviceType deviceType;
	// 设备状态
	private DeviceStatus deviceStatus;
	// 主网关的id
	private String masterId;
}
