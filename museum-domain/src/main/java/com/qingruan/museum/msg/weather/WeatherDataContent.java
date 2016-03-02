package com.qingruan.museum.msg.weather;

import lombok.Getter;
import lombok.Setter;

import com.qingruan.museum.msg.DataContent;

@Getter
@Setter
public class WeatherDataContent extends DataContent{
	/*
	 * 旧版气象站协议风向
	 */
	private String windDirection;
	
}
