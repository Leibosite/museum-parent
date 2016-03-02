package com.qingruan.museum.engine.service.rest.domain;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PolicyGroupMsg{
	private Long id;
	private String groupName;
	private String desp;
	private List<PolicyMsg> policies=new ArrayList<PolicyMsg>();

}
