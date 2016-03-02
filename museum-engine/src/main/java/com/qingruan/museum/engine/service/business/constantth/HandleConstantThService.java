package com.qingruan.museum.engine.service.business.constantth;

import com.qingruan.museum.domain.models.enums.RecordDataType;
import com.qingruan.museum.session.IpcanContext;

/**
 * 处理恒温恒湿机上报数据
 * 
 * @author tommy
 * 
 */
public interface HandleConstantThService {
	/**
	 * 
	 * 
	 * 保存恒温恒湿机上报的数据
	 * 
	 * @param ipcanContext
	 * @param recordDataType
	 */
	void saveConstantThDataReport(IpcanContext ipcanContext,
			RecordDataType recordDataType);
}
