package com.qingruan.museum.pma.model;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Execution implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private String interfaceName;
	private List<String> param;
	private Policy policy;
}
