package com.qingruan.museum.engine.service.business.weather;

import com.qingruan.museum.domain.models.enums.RecordDataType;
import com.qingruan.museum.msg.MsgHeader;
import com.qingruan.museum.msg.MsgProperty;
import com.qingruan.museum.msg.MuseumMsg;
import com.qingruan.museum.msg.sensor.SensorMsg;
import com.qingruan.museum.session.IpcanContext;

/**
 * 
 * @author john
 * 
 */
public interface HandleWeatherService {
	/**
	 * 
	 * 
	 * 保存气象站上报的数据
	 * 
	 * @param ipcanContext
	 * @param recordDataType
	 */
	void saveWeatherDataReport(IpcanContext ipcanContext,
			RecordDataType recordDataType);

}
