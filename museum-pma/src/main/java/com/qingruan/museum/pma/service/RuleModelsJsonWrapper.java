package com.qingruan.museum.pma.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qingruan.museum.pma.meta.DomainModel;
import com.qingruan.museum.pma.meta.InterfaceModel;


public class RuleModelsJsonWrapper {

	private List<DomainModel> constraintDomain = new ArrayList<DomainModel>();
	private List<InterfaceModel> constraintInterface = new ArrayList<InterfaceModel>();
	private List<InterfaceModel> executionInterface = new ArrayList<InterfaceModel>();

	private Map<String, DomainModel> allDomainModels = new HashMap<String, DomainModel>();
	private Map<String, InterfaceModel> allInterfaceModels = new HashMap<String, InterfaceModel>();

	public List<DomainModel> getConstraintDomain() {
		return constraintDomain;
	}

	public void setConstraintDomain(List<DomainModel> constraintDomain) {
		this.constraintDomain = constraintDomain;
	}

	public List<InterfaceModel> getConstraintInterface() {
		return constraintInterface;
	}

	public void setConstraintInterface(List<InterfaceModel> constraintInterface) {
		this.constraintInterface = constraintInterface;
	}

	public List<InterfaceModel> getExecutionInterface() {
		return executionInterface;
	}

	public void setExecutionInterface(List<InterfaceModel> executionInterface) {
		this.executionInterface = executionInterface;
	}

	public Map<String, DomainModel> getAllDomainModels() {
		return allDomainModels;
	}

	public void setAllDomainModels(Map<String, DomainModel> allDomainModels) {
		this.allDomainModels = allDomainModels;
	}

	public Map<String, InterfaceModel> getAllInterfaceModels() {
		return allInterfaceModels;
	}

	public void setAllInterfaceModels(Map<String, InterfaceModel> allInterfaceModels) {
		this.allInterfaceModels = allInterfaceModels;
	}

}
