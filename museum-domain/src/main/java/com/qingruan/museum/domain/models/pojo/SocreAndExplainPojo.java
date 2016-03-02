package com.qingruan.museum.domain.models.pojo;

import lombok.Getter;
import lombok.Setter;

import com.qingruan.museum.domain.models.enums.MonitorDataType;

@Setter
@Getter
public class SocreAndExplainPojo {

	private MonitorDataType monitorObjectType;
	//评分
	private Double score;
	//数据解释
	private String explain;
}
