package com.qingruan.museum.pma.meta;

import java.util.List;

import com.qingruan.museum.pma.model.enums.ValueType;

public class DomainModel {
	private String displayName;
	private String description;
	private String className;
	private String packageInfo;
	private List<DomainModel> subObjects;
	private ValueType valueType;
	private String callingExpress;
	private String staticValue;
	private String chosenClassName;
	private boolean isMultiple;

	private String extraProp;

	public String getExtraProp() {
		return extraProp;
	}

	public void setExtraProp(String extraProp) {
		this.extraProp = extraProp;
	}

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

	public List<DomainModel> getSubObjects() {
		return subObjects;
	}

	public void setSubObjects(List<DomainModel> subObjects) {
		this.subObjects = subObjects;
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

	public ValueType getValueType() {
		return valueType;
	}

	public void setValueType(ValueType valueType) {
		this.valueType = valueType;
	}

	public String getCallingExpress() {
		return callingExpress;
	}

	public void setCallingExpress(String callingExpress) {
		this.callingExpress = callingExpress;
	}

	public String getStaticValue() {
		return staticValue;
	}

	public void setStaticValue(String staticValue) {
		this.staticValue = staticValue;
	}

	public String getChosenClassName() {
		return chosenClassName;
	}

	public void setChosenClassName(String chosenClassName) {
		this.chosenClassName = chosenClassName;
	}

	public boolean isMultiple() {
		return isMultiple;
	}

	public void setMultiple(boolean isMultiple) {
		this.isMultiple = isMultiple;
	}

}
