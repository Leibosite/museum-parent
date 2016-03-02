package com.qingruan.museum.msg.sensor;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 传感器网络的拓扑信息,后续实现使用树描述拓扑信息
 * 
 * @author tommy
 * 
 */
@Getter
@Setter
public class SensorNetTopology {
	// 主网关的mac地址
	private String masterGatewayMacAddr;
	// 主网关的编号
	private String masterGatewayId;
	// 主网关下路由网关网络拓扑信息
	private List<String> routerNetTopology = new ArrayList<String>();
}
