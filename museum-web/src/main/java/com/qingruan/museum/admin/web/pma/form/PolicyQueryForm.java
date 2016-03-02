package com.qingruan.museum.admin.web.pma.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PolicyQueryForm {
	
	private String policyName;

	private Integer status;
	
	private String startTime;
	
	private String endTime;
}
