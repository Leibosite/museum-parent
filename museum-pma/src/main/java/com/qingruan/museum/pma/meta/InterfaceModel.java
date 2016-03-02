package com.qingruan.museum.pma.meta;

import java.util.Map;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class InterfaceModel {
	private String displayName;
	private String description;
	private String className;
	private String packageInfo;
	private Map<String, MethodModel> methodModels;
	private String callingExpress;
	@JsonIgnore
	private MethodAccess methodAccess;// for reflection method invoke
	@JsonIgnore
	private Object serviceBean;// for reflection method invoke instance
	@JsonIgnore
	private Object conflictSolver;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getPackageInfo() {
		return packageInfo;
	}

	public void setPackageInfo(String packageInfo) {
		this.packageInfo = packageInfo;
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

	public String getCallingExpress() {
		return callingExpress;
	}

	public void setCallingExpress(String callingExpress) {
		this.callingExpress = callingExpress;
	}

	public Map<String, MethodModel> getMethodModels() {
		return methodModels;
	}

	public void setMethodModels(Map<String, MethodModel> methodModels) {
		this.methodModels = methodModels;
	}

	public MethodAccess getMethodAccess() {
		return methodAccess;
	}

	public void setMethodAccess(MethodAccess methodAccess) {
		this.methodAccess = methodAccess;
	}

	public Object getServiceBean() {
		return serviceBean;
	}

	public void setServiceBean(Object serviceBean) {
		this.serviceBean = serviceBean;
	}

	public Object getConflictSolver() {
		return conflictSolver;
	}

	public void setConflictSolver(Object conflictSolver) {
		this.conflictSolver = conflictSolver;
	}

}
