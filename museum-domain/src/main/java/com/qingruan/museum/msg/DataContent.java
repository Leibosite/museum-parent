package com.qingruan.museum.msg;

import lombok.Data;

import com.qingruan.museum.domain.models.enums.MonitorDataType;

/**
 * 传感数据内容
 * 
 * @author tommy
 * 
 */
@Data
public class DataContent {
	// 监测数据类型
	private MonitorDataType monitorDataType;
	private Double value;
	private Integer checkCode;
}
