package com.qingruan.museum.engine.service.rest.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class EnvironmentMessage {

	private Integer resultCode;
	private String resultMsg;
	private List<EnvironmentBean> data ;

}
