package com.qingruan.museum.domain.models.enums;

/**
 * 区域类型
 * 
 * @author tommy
 * 
 */
public enum AreaType {

	EXHIBITION_HALL("展厅"), WAREHOUSE("库房"), CONSTANT_TH_DISPLAY("恒温恒湿展柜"), COMMON_DISPLAY(
			"普通展柜"), MUSEUM("整个博物馆");

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

	private AreaType(String value) {
		this.value = value;
	}

	public static AreaType from(String value) {
		for (AreaType item : AreaType.values()) {
			if (item.value.equals(value))
				return item;
		}

		return null;
	}

}
