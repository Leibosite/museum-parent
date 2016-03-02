package com.qingruan.museum.msg.constantth;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * 
 * @author tommy
 * 
 */
@Data
public class ThData {
	// 时间戳
	private Long timeStamp;
	// 恒温恒湿组网信息
	private ThNetInfos thNetInfos;
	// 传感数据
	private List<ThDataContent> datas = new ArrayList<>();

	// 主网关id
	private String masterGatewayID;
	// 定位的路径
	private String path;
	// 恒湿机采集设备的MAC地址
	private String presentMAC;
	// 设定值
	private Long presentValue;
	// 报警上限
	private Long topLimit;
	// 报警下线
	private Long lowerLimit;
	// 波动值
	private Long fluctuationValue;
	// 上报的值
	private Long reportValue;
	// 时间戳
	private String timeStamps;
	
	// 恒温恒湿机MAC地址
	private String macAddr;
}
