package com.qingruan.museum.pma.model.enums;

public enum OperatorType {
	GREATER(">"), 
	LESS("<"), 
	EQUALS("=="), 
	NOT_EQUALS("!="), 
	GREATER_EQUAL(">="), 
	LESS_EQAUL("<=");
	
	private String value;

	OperatorType(String s) {
		this.value = s;
	}

	public String getValue() {
		return this.value;
	}

}
