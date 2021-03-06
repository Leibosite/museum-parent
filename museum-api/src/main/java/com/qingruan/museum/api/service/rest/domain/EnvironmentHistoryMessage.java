package com.qingruan.museum.api.service.rest.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EnvironmentHistoryMessage {

	private Integer resultCode;
	private String resultMsg;
	private List<EnvironmentHistoryBean> data;

}
