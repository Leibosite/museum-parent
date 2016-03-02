package com.qingruan.museum.msg.sensor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SensorNetInfo {
	private String sensorMacAddr;
	private Long timeStamp;// 用来检查设备的存活状态
	// 信号强度
	private int rssi;
	private String uuid;
	private String major;
	private String minor;

	public SensorNetInfo() {

	}

	public SensorNetInfo(String sensorMacAddr, Long timeStamp, int rssi,
			String uuid, String major, String minor) {

	}
}