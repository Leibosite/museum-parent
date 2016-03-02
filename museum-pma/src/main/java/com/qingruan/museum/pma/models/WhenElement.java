package com.qingruan.museum.pma.models;

import java.util.List;

import com.qingruan.museum.pma.enums.DomainType;
import com.qingruan.museum.pma.enums.LogicConnectorType;

public class WhenElement {
	// web page use, display for selecting when element type.
	private DomainType domainType;
	// object signature in real import path.
	private Object domainInstance;
	// type of the object, i.e. Object, primitive object, or others.
	private LogicConnectorType logicConnectorType;
	// the value input by user on web page.
	private Object value;
	// sub element (the field of current element). For the purpose of choosing
	// when element aspect.
	private List<String> subElement;

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

	public List<String> getSubElement() {
		return subElement;
	}

	public void setSubElement(List<String> subElement) {
		this.subElement = subElement;
	}
}
