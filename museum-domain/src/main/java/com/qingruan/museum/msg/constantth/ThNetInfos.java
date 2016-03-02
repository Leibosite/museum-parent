package com.qingruan.museum.msg.constantth;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.qingruan.museum.msg.sensor.MtmNetInfo.IpAddrType;

@Getter
@Setter
public class ThNetInfos {
	// 中控的MAC地址
	private String plcMacAddr;
	// 中控的IPV4地址
	private String plcIpv4Addr;
	// 中控的IPV6地址
	private String plcIpv6Addr;
	// 中控的IP地址类型
	private IpAddrType plcIpAddrType;
	// 中控所管辖恒温恒湿机组网信息列表
	private List<String> thNetInfos = new ArrayList<>();
}
