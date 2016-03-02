package com.qingruan.museum.msg.sensor;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Mac地址列表以及组网设备路径
 * 
 * @author zhanghui
 * 
 */
@Getter
@Setter
public class SensorMacAddrInfo {
	// 设备路径
	private String routerNetTopology;
	// Mac 地址列表
	private List<SensorNetInfo> sensorNetInfos = new ArrayList<SensorNetInfo>();
}
