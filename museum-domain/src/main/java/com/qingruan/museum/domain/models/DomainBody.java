/**
 2014年11月23日
 tommy
 
 */
package com.qingruan.museum.domain.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Domain消息体
 * 
 * @author tommy
 * 
 */
@Getter
@Setter
@ToString
public class DomainBody extends BaseDomain {
	private static final long serialVersionUID = -100005L;
	// 监测点ID
	public String monitoringPointId;
	// 监测点站ID
	public String monitoringStationId;
	// 温度
	public Double temperature;
	// 湿度
	public Double humidity;
	// 电量
	public Double power;
	// 光照
	public Double lighting;
	// 紫外线强度
	public Double uvIntensity;
	// 二氧化硫浓度
	public Double sulfurDioxide;
	// 二氧化碳浓度
	public Double carbonDioxide;
	// 甲醛、甲苯、苯等VOC气体
	public Double voc;
	// PM10，PM2.5，mg/m3
	// 甲醛
	public Double formalclehyde;
	//大气压
	public Double atmosphere;
	//风速
	public Double windSpeed;
	//风向
	public Double windDirection;
	//海拔
	public Double height;
	//经度 
	public Double longitude;
	//纬度
	public Double latitude;
	// 火警

	public Boolean fireAlarm;

	// 水警

	public Boolean waterAlarm;

	//   烟雾

	public Boolean smoke;

	// 描述信息

	public String desc;

	// 扩展信息

	public String extend;
}
