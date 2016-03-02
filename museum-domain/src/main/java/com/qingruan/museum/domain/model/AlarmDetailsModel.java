/**
 2015年2月10日
 14cells
 
 */
package com.qingruan.museum.domain.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 告警详情Model，序列时Json格式
 * 
 * @author tommy
 * 
 */
@Setter
@Getter
public class AlarmDetailsModel implements Serializable {
	private static final long serialVersionUID = -5114857129571489352L;
	// 告警发生时间
	private String alarmTime;
	// 告警处理时间
	private String alarmProcessTime;
	// 告警所在区域名称
	private String repoAreaName;

	private Long repoAreaId;
	// 设备名称
	private String deviceName;
	// 设备Id
	private Long deviceId;
	// 设备编号
	private String deviceNumber;
	// 当前值
	private Double currentValue;
	// 最大阀值
	private Double maxThreshold;
	// 最小阀值
	private Double minThreshold;

}
