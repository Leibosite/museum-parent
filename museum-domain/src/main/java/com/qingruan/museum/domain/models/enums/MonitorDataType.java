/**
 2015年1月15日
 tommy
 
 */
package com.qingruan.museum.domain.models.enums;

/**
 * 定义监测数据的类型
 * 
 * @author tommy
 * 
 */
public enum MonitorDataType {

	SOIL_TEMPERATURE("土壤温度"), SOIL_HUMIDITY("土壤湿度"), SOIL_SALINITY("土壤电导率"), WIND_SPEED(
			"风速"), SO2("二氧化硫"), CO2("二氧化碳"),O2("氧气"), TEMPERATURE("温度"), HUMIDITY("湿度"), LIGHTING(
			"光照"), UV("紫外线"), AIRPRESSURE("大气压"), TVOC("VOC有害气体"), PM2_5(
			"PM2.5"), PM10("PM10"), PM1_0("PM1.0"), UM_01_03(
			"0.1升空气中直径在0.3um以上的颗粒"), UM_01_05("0.1升空气中直径在0.5um以上的颗粒"), UM_01_10(
			"0.1升空气中直径在1.0um以上的颗粒"), UM_01_25("0.1升空气中直径在2.5um以上的颗粒"), UM_01_50(
			"0.1升空气中直径在5.0um以上的颗粒"), UM_01_100("0.1升空气中直径在10um以上的颗粒"), CH2O(
			"甲醛"), WIND_DIRECTION("风向"), LONGITUDE("经度"), LATITUDE("纬度"), HEIGHT(
			"海拔高度"),MAIN_CONTROL_PANEL_VOLTAGE("主控板电压"),SOLAR_ENERGY_VOLTAGE("太阳能电池板电压"),
			GPS_SPEED("GPS速度"),GPS_DIRECTION("GPS方向"),X1("保留字段1"),X2("保留字段2");

	private String value;

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public String value() {
		return value;
	}

	private MonitorDataType(String value) {
		this.value = value;
	}

	public static MonitorDataType from(String value) {
		for (MonitorDataType item : MonitorDataType.values()) {
			if (item.value.equals(value))
				return item;
		}

		return null;
	}

}
