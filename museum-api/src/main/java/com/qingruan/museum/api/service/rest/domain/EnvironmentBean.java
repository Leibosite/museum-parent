package com.qingruan.museum.api.service.rest.domain;

import lombok.Getter;
import lombok.Setter;

import com.qingruan.museum.domain.models.enums.MonitorDataType;

@Setter
@Getter
public class EnvironmentBean {
	private MonitorDataType monitorDataType;
	private Double value;
}
