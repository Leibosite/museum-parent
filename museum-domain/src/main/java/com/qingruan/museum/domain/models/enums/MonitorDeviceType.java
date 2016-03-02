package com.qingruan.museum.domain.models.enums;

/**
 * 监测数据类型
 * 
 * @author tommy
 * 
 */
public enum MonitorDeviceType {
	SENSOR(0), CLEANER(1), WEATHER_STATION(2), SHOW_CASE(3);

	private final Integer value;

	private MonitorDeviceType(Integer value) {
		this.value = value;
	}

	public Integer value() {
		return value;
	}

	public static MonitorDeviceType from(Integer value) {
		for (MonitorDeviceType item : MonitorDeviceType.values()) {
			if (item.value.equals(value))
				return item;
		}

		return null;
	}
}
