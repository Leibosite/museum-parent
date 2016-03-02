/**
 2015年2月9日
 14cells
 
 */
package com.qingruan.museum.domain.enums;

/**
 * 告警Flag
 * @author tommy
 * 
 */
public enum WarnFlag {

	WARN("发生告警"), NORMAL("正常");

	private final String value;

	private WarnFlag(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public static WarnFlag from(String value) {
		for (WarnFlag item : WarnFlag.values()) {
			if (item.value.equals(value))
				return item;
		}

		return null;
	}

}
