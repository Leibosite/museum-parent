package com.qingruan.museum.engine.service.business.aircleaner;

import com.qingruan.museum.domain.models.enums.RecordDataType;
import com.qingruan.museum.session.IpcanContext;

/**
 * 处理空气净化器
 * 
 * @author tommy
 * 
 */
public interface HandleAirCleanerService {
	/**
	 * 
	 * 
	 * 保存气象站上报的数据
	 * 
	 * @param ipcanContext
	 * @param recordDataType
	 */
	void saveAirCleanerDataReport(IpcanContext ipcanContext,
			RecordDataType recordDataType);
}
