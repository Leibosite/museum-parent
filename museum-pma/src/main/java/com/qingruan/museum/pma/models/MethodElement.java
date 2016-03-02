package com.qingruan.museum.pma.models;

import java.util.List;

import com.qingruan.museum.pma.enums.DomainType;
import com.qingruan.museum.pma.enums.ExcuteType;
import com.qingruan.museum.pma.enums.ServiceType;

public class MethodElement extends ThenElement {
	private ServiceType serviceType;
	private List<Object> parameters;
	private DomainType returnValue;

	public MethodElement() {
		this.excuteType = ExcuteType.method;
	}

	public ServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}

	public List<Object> getParameters() {
		return parameters;
	}

	public void setParameters(List<Object> parameters) {
		this.parameters = parameters;
	}

	public DomainType getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(DomainType returnValue) {
		this.returnValue = returnValue;
	}
}
