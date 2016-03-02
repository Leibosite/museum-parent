package com.qingruan.museum.msg.aircleaner;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

import com.qingruan.museum.msg.MsgBody;
import com.qingruan.museum.msg.Result;

@Data
public class AirCleanerMsg implements MsgBody{

	private String msgId;
	private Result result;
	private int qos;
	private String stationMacAddr;
	private List<AirCleanerDataContent> airCleanerDatas = new ArrayList<AirCleanerDataContent>();
	private Long timestamp; 

	@Override
	public String toJson() {
		return null;
	}

}
