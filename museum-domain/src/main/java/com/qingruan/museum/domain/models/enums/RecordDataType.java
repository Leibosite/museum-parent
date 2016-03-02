/**
 2015年1月27日
 14cells
 
 */
package com.qingruan.museum.domain.models.enums;

/**
 * @author Tommy
 * 
 */
public enum RecordDataType {

	ALL(1), EQUIPMENT(2),ENVIRONMENT(3),;

	private final Integer value;

	private RecordDataType(Integer value) {
		this.value = value;
	}

	public Integer value() {
		return value;
	}

	public static RecordDataType from(Integer value) {
		for (RecordDataType item : RecordDataType.values()) {
			if (item.value.equals(value))
				return item;
		}

		return null;
	}

}
