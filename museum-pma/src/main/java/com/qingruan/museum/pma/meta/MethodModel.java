package com.qingruan.museum.pma.meta;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class MethodModel {
	private String displayName;
	private String description;
	private String serviceName;
	private DomainModel returnModel;
	private List<DomainModel> params;
	private String callingExpress;
	@JsonIgnore
	private Object conflictTarget;	//冲突解决类的对象
	@JsonIgnore
	private String dependParam;

	public String getDependParam() {
		return dependParam;
	}

	public void setDependParam(String dependParam) {
		this.dependParam = dependParam;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public List<DomainModel> getParams() {
		return params;
	}

	public void setParams(List<DomainModel> params) {
		this.params = params;
	}

	public DomainModel getReturnModel() {
		return returnModel;
	}

	public void setReturnModel(DomainModel returnModel) {
		this.returnModel = returnModel;
	}

	public String getCallingExpress() {
		return callingExpress;
	}

	public void setCallingExpress(String callingExpress) {
		this.callingExpress = callingExpress;
	}

	public Object getConflictTarget() {
		return conflictTarget;
	}

	public void setConflictTarget(Object conflictTarget) {
		this.conflictTarget = conflictTarget;
	}

}
