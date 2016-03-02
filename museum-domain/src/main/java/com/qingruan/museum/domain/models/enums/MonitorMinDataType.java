package com.qingruan.museum.domain.models.enums;

/**
 * @desp 实体DailyRecordStatistics 中“最小”属性枚举 
 * @author leibosite
 */
//TODO: 命名需要重构
public enum MonitorMinDataType {		
	temperatureMin(0), humidityMin(1), illuminanceMin(2), uvIntensityMin(3), tvocMin(
			4), carbonDioxideMin(5), sulfurDioxideMin(6), pm02_5Min(7), pm10Min(
			8), formaldehydeMin(9);

	private final Integer value;

	private MonitorMinDataType(Integer value) {
		this.value = value;
	}

	public Integer value() {
		return value;
	}

	public static MonitorMinDataType from(Integer value) {
		for (MonitorMinDataType item : MonitorMinDataType.values()) {
			if (item.value.equals(value))
				return item;
		}
		return null;
	}
}
