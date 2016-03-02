package com.qingruan.museum.msg.sensor;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MtmNetInfo {
	// 中继的MAC地址
	private String stationMacAddr;
	// 中继的id
	private String topologyID;
	// 中继的IPV4地址
	private String stationIpv4Addr;
	// 中继的IPV6地址
	private String stationIpv6Addr;
	// 中继的IP地址类型
	private IpAddrType stationIpAddrType;
	// 中继所管辖传感器组网信息列表
	private List<SensorNetInfo> sensorNetInfos = new ArrayList<SensorNetInfo>();
	// 主网关下的网络拓扑信息
	private SensorNetTopology sensorNetTopology;
	// 主网关下的组网信息
	private SensorNetStruct sensorNetStruct;

	// IP地址类型类型，标记单栈还是双栈
	public static enum IpAddrType {
		IPV4, IPV6;
	}
}
