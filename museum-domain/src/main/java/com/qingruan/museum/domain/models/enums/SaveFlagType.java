/**
 2015年1月27日
 14cells
 
 */
package com.qingruan.museum.domain.models.enums;

/**
 * @author tommy
 *
 */
public enum SaveFlagType {

	SAVE(0),NON_SAVE(1);
	
	private final Integer value;

	private SaveFlagType(Integer value) {
		this.value = value;
	}
	
	public Integer value() {
		return value;
	}

	public static SaveFlagType from(Integer value) {
		for (SaveFlagType item : SaveFlagType.values()) {
			if (item.value.equals(value))
				return item;
		}
		
		return null;
	}

}



