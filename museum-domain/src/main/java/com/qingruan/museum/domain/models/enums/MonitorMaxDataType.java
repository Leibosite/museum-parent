package com.qingruan.museum.domain.models.enums;

/**
 * @desp 实体DailyRecordStatistics 中“最大”属性枚举 
 * @author leibosite
 *
 */
public enum MonitorMaxDataType {

	temperatureMax(0), humidityMax(1), illuminanceMax(2), uvIntensityMax(3), tvocMax(
			4), carbonDioxideMax(5), sulfurDioxideMax(6), pm02_5Max(7), pm10Max(
			8), formaldehydeMax(9);

	private final Integer value;

	private MonitorMaxDataType(Integer value) {
		this.value = value;
	}

	public Integer value() {
		return value;
	}

	public static MonitorMaxDataType from(Integer value) {
		for (MonitorMaxDataType item : MonitorMaxDataType.values()) {
			if (item.value.equals(value))
				return item;
		}

		return null;
	}
}
