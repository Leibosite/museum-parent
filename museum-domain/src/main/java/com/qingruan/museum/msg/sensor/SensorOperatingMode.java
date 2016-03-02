package com.qingruan.museum.msg.sensor;

import lombok.Getter;
import lombok.Setter;

/**
 * 传感器运行状态
 * 
 * @author zhanghui
 * 
 */
@Getter
@Setter
public class SensorOperatingMode {

	// 主网关设备的id
	private String id;
	// 运行模式
	private OperatingMode operatingMode;

	// 运行模式，DEBUG 或 PRODUCTION
	public static enum OperatingMode {
		DEBUG, PRODUCTION;
	}
}
