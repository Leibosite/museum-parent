package com.qingruan.museum.engine.service.rest.domain.fetch;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.qingruan.museum.engine.service.rest.domain.ResponseBaseMsg;
@Getter
@Setter
public class JsonData extends ResponseBaseMsg {
	private List<Msg> datas;

}
