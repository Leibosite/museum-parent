package com.qingruan.museum.pma.model;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.qingruan.museum.pma.model.enums.OperatorType;

@Getter
@Setter
public class Constraint implements Serializable {
	private static final long serialVersionUID = 1L;

	private Boolean isService;
	private String interfaceName;
	// constraint name
	private String name;
	// constraint param
	private List<String> param;
	private OperatorType operatorType;
	// constraint value
	private String value;
	private Policy policy;

}
