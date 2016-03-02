package com.qingruan.museum.pma.models;

import com.qingruan.museum.pma.enums.ExcuteType;

public class IfStatement extends ThenElement {
	private ConditionElement conditionElement;
	private ThenElement thenElement;

	public IfStatement() {
		this.excuteType = ExcuteType.if_statement;
	}

	public ConditionElement getConditionElement() {
		return conditionElement;
	}

	public void setConditionElement(ConditionElement conditionElement) {
		this.conditionElement = conditionElement;
	}

	public ThenElement getThenElement() {
		return thenElement;
	}

	public void setThenElement(ThenElement thenElement) {
		this.thenElement = thenElement;
	}
}
