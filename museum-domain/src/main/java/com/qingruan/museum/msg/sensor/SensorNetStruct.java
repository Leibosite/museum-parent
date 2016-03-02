package com.qingruan.museum.msg.sensor;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 组网信息结构体
 * 
 * @author zhanghui
 * 
 */
@Getter
@Setter
public class SensorNetStruct {
	// 主网关的编号
	private String masterGatewayId;
	// 主网关的mac地址
	private String masterGatewayMacAddr;
	// 主网关下的Mac地址列表和组网设备路径
	private List<SensorMacAddrInfo> sensorMacAddrInfos = new ArrayList<SensorMacAddrInfo>();
}
