package com.qingruan.museum.msg.sensor;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * 
 * @author tommy
 * 
 */
@Data
public class SensorData {
	// 时间戳
	private Long timeStamp;
	// 物联网组网信息
	private MtmNetInfo mtmNetInfo;
	// 升级包信息
	private PatchInfo patchInfo;
	// 传感数据
	private List<SensorDataContent> datas = new ArrayList<SensorDataContent>();
	// 电量
	private Double power;

	// 传感器的组网信息
	private SensorNetInfo sensorNetInfo;
	// 告警设备的状态
	private SensorStatusInfo sensorStatusInfo;

}
