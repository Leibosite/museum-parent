package com.qingruan.museum.domain.model;

public enum ResultCode {

	SUCCESS(2001);
	private final Integer value;

	private ResultCode(Integer value) {
		this.value = value;
	}

	public Integer value() {
		return value;
	}

	public static ResultCode from(Integer value) {
		for (ResultCode item : ResultCode.values()) {
			if (item.value.equals(value))
				return item;
		}

		return null;
	}

}
