package com.qingruan.museum.api.service.rest.domain;

import lombok.Getter;
import lombok.Setter;

import com.qingruan.museum.pma.model.Policy;

@Getter
@Setter
public class PolicyMsg {
	private Long id;
	private String name;
	private String desp;
	private Policy content;
	private String salience;
	private Integer status;

}
