package com.qingruan.museum.domain.model.weather;

import lombok.Getter;
import lombok.Setter;

import com.qingruan.museum.domain.models.enums.MonitorDataType;

@Getter
@Setter
public class HistoryData {
	private MonitorDataType monitorDataType;
	private Double avg;
	private Double max;
	private Double min;
	private Double reference;
	private Long timeStamp;
}
