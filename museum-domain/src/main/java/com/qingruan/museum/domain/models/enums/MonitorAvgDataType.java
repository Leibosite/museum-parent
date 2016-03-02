package com.qingruan.museum.domain.models.enums;

/**
 * @desp 实体DailyRecordStatistics 中“平均”属性枚举 
 * @author leibosite
 *
 */
public enum MonitorAvgDataType {
	
	temperatureAvg(0), humidityAvg(1), illuminanceAvg(2), uvIntensityAvg(3), tvocAvg(
			4), carbonDioxideAvg(5), sulfurDioxideAvg(6), pm02_5Avg(7), pm10Avg(
			8), formaldehydeAvg(9);

	private final Integer value;

	private MonitorAvgDataType(Integer value) {
		this.value = value;
	}

	public Integer value() {
		return value;
	}

	public static MonitorAvgDataType from(Integer value) {
		for (MonitorAvgDataType item : MonitorAvgDataType.values()) {
			if (item.value.equals(value))
				return item;
		}

		return null;
	}
}
