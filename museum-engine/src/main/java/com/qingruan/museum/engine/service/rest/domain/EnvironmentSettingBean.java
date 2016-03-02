package com.qingruan.museum.engine.service.rest.domain;

import lombok.Getter;
import lombok.Setter;

import com.qingruan.museum.domain.models.enums.MonitorDataType;

@Setter
@Getter
public class EnvironmentSettingBean {

	private long id;
	private MonitorDataType monitorDataType;
	private Double value;

}
