package com.qingruan.museum.api.service.rest.domain;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PolicyGroupListMsg extends ResponseBaseMsg {

	private List<PolicyGroupMsg> policyGroupList = new ArrayList<PolicyGroupMsg>();
}
