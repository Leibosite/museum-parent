package com.qingruan.museum.engine.service.rule;

import java.util.ArrayList;

import com.qingruan.museum.engine.service.rule.action.BusinessService;

public class DelayTask {
	private String businessServiceName;
	
	private BusinessService businessService;
	
	private ArrayList<Object> params;
	
	private boolean batched;
	
	private int salience;
	
	private Object sprEntity;
	
	private Long policyId;
	
	//指明在params中第几个参数是用于冲突检测的,从0开始算
	private Integer paramIndex;
	
	public DelayTask() {
		params = new ArrayList<Object>();
		
		batched = false;
		
		salience = 0;
	}
	
	public DelayTask(int paramSize) {
		params = new ArrayList<Object>();
		
		params.ensureCapacity(paramSize);
	}

	public String getBusinessServiceName() {
		return businessServiceName;
	}

	public void setBusinessServiceName(String businessServiceName) {
		this.businessServiceName = businessServiceName;
	}

	public ArrayList<Object> getParams() {
		return params;
	}

	public void setParams(ArrayList<Object> params) {
		this.params = params;
	}
	
	public void addParam(Object param) {
		params.add(param);
	}

	public BusinessService getBusinessService() {
		return businessService;
	}

	public void setBusinessService(BusinessService businessService) {
		this.businessService = businessService;
	}

	public boolean isBatched() {
		return batched;
	}

	public void setBatched(boolean batched) {
		this.batched = batched;
	}

	public int getSalience() {
		return salience;
	}

	public void setSalience(Integer salience) {
		if (salience == null) {
			return;
		}
		
		this.salience = salience;
	}

	public Object getSprEntity() {
		return sprEntity;
	}

	public void setSprEntity(Object sprEntity) {
		this.sprEntity = sprEntity;
	}

	public Long getPolicyId() {
		return policyId;
	}

	public void setPolicyId(Long policyId) {
		this.policyId = policyId;
	}

	public Integer getParamIndex() {
		return paramIndex;
	}

	public void setParamIndex(Integer paramIndex) {
		this.paramIndex = paramIndex;
	}
}
