package com.qingruan.museum.msg.sensor;

import lombok.Getter;
import lombok.Setter;

/**
 * 历史数据上报
 * 
 * @author zhanghui
 * 
 */
@Getter
@Setter
public class SensorHistoryRecord {
	// 设备的id
	private String id;
	// 设备的MAC地址
	private String macAddr;
	// 路径
	private String topologyPath;
	// 上报模式
	private HistoryReportMode historyReportMode;

	// 上报模式，START 或 STOP
	public static enum HistoryReportMode {
		START, STOP;
	}
}
