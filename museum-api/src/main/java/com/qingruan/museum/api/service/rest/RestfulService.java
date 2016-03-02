package com.qingruan.museum.api.service.rest;

import com.qingruan.museum.domain.models.enums.MonitorDataType;

import io.netty.buffer.ByteBuf;

/**
 * 提供RESTFUL接口
 * 
 * @author tommy
 * 
 */

public interface RestfulService {
	/**
	 * 获取区域内所有实时监测数据
	 * 
	 * @param arg
	 * @return
	 */
	ByteBuf getAreaDataMsg(final String arg);

	ByteBuf getErrorMsg();

	/**
	 * 获取恒温恒湿数据
	 * 
	 * @param arg
	 * @return
	 */
	ByteBuf getConstantThMsg(final String arg);

	/**
	 * 获取传感器升级消息
	 * 
	 * @param arg
	 * @return
	 */
	ByteBuf getSensorUpgradeMsg(final String arg);

	/**
	 * 获取气象站数据
	 * 
	 * @param arg
	 * @return
	 */
	ByteBuf getWeatherMsg(final String arg);

	/**
	 * 获取气象站数据概况
	 * 
	 * @param id
	 * @return
	 */

	ByteBuf getOverViewWeatherData(final Long id);

	/**
	 * 获取实时气象站数据
	 * 
	 * @param id
	 * @return
	 */

	ByteBuf getRealtimeWeatherData(final Long id);

	/**
	 * 获取历史气象站数据
	 * 
	 * @param id
	 * @return
	 */
	ByteBuf getHistoryWeatherData(final Long id,final String granulariy,final MonitorDataType monitorDataType,final Long timeStamp);
	/**
	 * 获取气象站数据概况
	 * 
	 * @param id
	 * @return
	 */

	ByteBuf getOverViewAirCleanerData(final Long id);

	/**
	 * 获取实时气象站数据
	 * 
	 * @param id
	 * @return
	 */

	ByteBuf getRealtimeAirCleanerData(final Long id);

	/**
	 * 获取历史气象站数据
	 * 
	 * @param id
	 * @return
	 */
	ByteBuf getHistoryAirCleanerData(final Long id,final String granulariy,final MonitorDataType monitorDataType,final Long timeStamp);




}
