/**
 2015年1月16日
 tommy
 */
package com.qingruan.museum.session;

import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.qingruan.museum.dao.entity.Device;
import com.qingruan.museum.dao.entity.RepoArea;
import com.qingruan.museum.msg.MsgHeader;
import com.qingruan.museum.msg.MsgProperty;
import com.qingruan.museum.msg.MsgProperty.ApplicationType;
import com.qingruan.museum.msg.aircleaner.AirCleanerMsg;
import com.qingruan.museum.msg.constantth.ConstantThMsg;
import com.qingruan.museum.msg.sensor.SensorMsg;
import com.qingruan.museum.msg.weather.WeatherMsg;

/**
 * 运行时需要的信息，进入规则引擎之前将运行态数据从内存数据库中取出
 * 
 * @author tommy
 * 
 */
@ToString(includeFieldNames = true)
@Getter
@Setter
public class RunTimeInfo implements Cloneable {
	// 监测点
	private Device MonitoringPoint;
	// 监测站
	private Device MonitoringStation;
	// 区域
	private RepoArea repoArea;
	// 恒温恒湿机上联PLC
	private Device plc;
	// 一台PLC下恒温恒湿机列表
	// 恒温恒湿机
	private Device constantTh;
	// 气象站
	private Device weatherStation;
	private HashMap<Integer, Device> contantThMachines = new HashMap<Integer, Device>();

	private MsgHeader msgHeader;

	private MsgProperty msgProperty;

	private SensorMsg sensorMsg;

	private ConstantThMsg constantThMsg;

	private WeatherMsg weatherMsg;
	
	private AirCleanerMsg airCleanerMsg;

	// 消息的应用类型
	private ApplicationType applicationType;
	
	private Device airCleaner;
}
