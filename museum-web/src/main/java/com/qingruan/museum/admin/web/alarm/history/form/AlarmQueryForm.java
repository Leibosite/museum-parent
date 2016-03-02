package com.qingruan.museum.admin.web.alarm.history.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlarmQueryForm {
	
	private String name;

	private Integer status;
	
	private String startTime;
	
	private String endTime;
}
