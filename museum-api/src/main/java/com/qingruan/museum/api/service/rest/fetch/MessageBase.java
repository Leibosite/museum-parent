package com.qingruan.museum.api.service.rest.fetch;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MessageBase {

	private Integer resultCode;
	private String resultMsg;
	private List<Message> listDatas;

}
