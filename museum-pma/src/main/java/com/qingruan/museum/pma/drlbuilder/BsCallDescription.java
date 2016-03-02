package com.qingruan.museum.pma.drlbuilder;

import java.util.ArrayList;
import java.util.List;

public class BsCallDescription {
	private String bsName;
	
	private List<String> params = new ArrayList<String>();

	public String getBsName() {
		return bsName;
	}

	public void setBsName(String bsName) {
		this.bsName = bsName;
	}

	public List<String> getParams() {
		return params;
	}

	public void setParams(List<String> params) {
		this.params = params;
	}
	
	public void addParam(String param) {
		params.add(param);
	}
	
	public String getParam(int index) {
		return params.get(index);
	}
}
