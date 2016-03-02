package com.qingruan.museum.msg.weather;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

import com.qingruan.museum.msg.MsgBody;
import com.qingruan.museum.msg.Result;

@Data
public class WeatherMsg implements MsgBody{
	
	private String msgId;
	private Result result;
	private int qos;
	private String stationMacAddr;
	private List<WeatherDataContent> weatherDatas = new ArrayList<WeatherDataContent>();
	private Long timestamp; 

	@Override
	public String toJson() {
		// TODO Auto-generated method stub
		return null;
	}

}
