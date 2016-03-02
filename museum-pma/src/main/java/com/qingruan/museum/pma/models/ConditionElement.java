package com.qingruan.museum.pma.models;

import com.qingruan.museum.pma.enums.DomainType;
import com.qingruan.museum.pma.enums.LogicConnectorType;

public class ConditionElement {
	private DomainType domainType;
	private Object domainInstance;
	private LogicConnectorType logicConnectorType;
	private Object value;

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public DomainType getDomainType() {
		return domainType;
	}

	public void setDomainType(DomainType domainType) {
		this.domainType = domainType;
	}

	public LogicConnectorType getLogicConnectorType() {
		return logicConnectorType;
	}

	public void setLogicConnectorType(LogicConnectorType logicConnectorType) {
		this.logicConnectorType = logicConnectorType;
	}

	public Object getDomainInstance() {
		return domainInstance;
	}

	public void setDomainInstance(Object domainInstance) {
		this.domainInstance = domainInstance;
	}
}
