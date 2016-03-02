package com.qingruan.museum.engine.service.business.sensor;

import com.qingruan.museum.domain.models.enums.RecordDataType;
import com.qingruan.museum.msg.MsgHeader;
import com.qingruan.museum.msg.MsgProperty;
import com.qingruan.museum.msg.MuseumMsg;
import com.qingruan.museum.msg.sensor.SensorMsg;
import com.qingruan.museum.msg.sensor.SensorNetStruct;
import com.qingruan.museum.session.IpcanContext;

/**
 * 
 * @author tommy
 * @author zhanghui
 * 
 */
public interface HandleSensorReqService {
	/**
	 * 中继获取传感器的Mac地址列表
	 * 
	 * @return
	 */

	MuseumMsg getSensorMacList(SensorNetStruct sensorNetStructs, MsgHeader msgHeader,
			MsgProperty msgProperty, SensorMsg sensorMsg);
	
	/**
	 * 主网关获取网络拓扑结构
	 * 
	 * @return
	 */
	MuseumMsg getNetTopologyList(SensorNetStruct sensorNetStruct, MsgHeader msgHeader,
			MsgProperty msgProperty, SensorMsg sensorMsg);

	/**
	 * 保存上报数据
	 * 
	 * @param ipcanContext
	 * @param recordDataType
	 */
	void saveSensorDataReport(IpcanContext ipcanContext,
			RecordDataType recordDataType);

	/**
	 * 传感器上报状态，Engine处理记录归档，或生成告警信息
	 * 
	 * @param ipcanContext
	 * @return
	 */
	MuseumMsg reportSensorStatus(IpcanContext ipcanContext);

	/**
	 * 传感器上报升级状态
	 * 
	 * @param ipcanContext
	 * @return
	 */
	MuseumMsg reportSensorUpgradeStatus(IpcanContext ipcanContext);
	
	/**
	 * 传感器处理设备告警
	 * 
	 * @param ipcanContext
	 * @return
	 */
	MuseumMsg reportSensorAlarmStatus(MsgHeader msgHeader, MsgProperty msgProperty, SensorMsg sensorMsg);

}
