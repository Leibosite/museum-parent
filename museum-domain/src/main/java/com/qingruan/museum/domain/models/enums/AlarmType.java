package com.qingruan.museum.domain.models.enums;

public enum AlarmType {
	ALARM(0),WARNING(1);
	
	private final Integer value;

	private AlarmType(Integer value) {
		this.value = value;
	}
	
	public Integer value() {
		return value;
	}

	public static AlarmType from(Integer value) {
		for (AlarmType item : AlarmType.values()) {
			if (item.value.equals(value))
				return item;
		}
		
		return null;
	}
}
