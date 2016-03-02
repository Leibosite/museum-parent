package com.qingruan.museum.netty.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.qingruan.museum.msg.sensor.SensorNetInfo;

public class MacListCacheBean {

	private String masterGetwayID;
	private int successNum;
	private List<Map<String,List<SensorNetInfo>>> list = new ArrayList<>();

	public MacListCacheBean(String masterGetwayID) {
		this.masterGetwayID = masterGetwayID;
		this.successNum = 0;
	}

	public String getMasterGetwayID() {
		return masterGetwayID;
	}

	public void setMasterGetwayID(String masterGetwayID) {
		this.masterGetwayID = masterGetwayID;
	}

	
	public int getSuccessNum() {
		return successNum;
	}

	public void setSuccessNum(int successNum) {
		this.successNum = successNum;
	}

	public List<Map<String, List<SensorNetInfo>>> getList() {
		return list;
	}

	public void setList(List<Map<String, List<SensorNetInfo>>> list) {
		this.list = list;
	}

	
	
}
