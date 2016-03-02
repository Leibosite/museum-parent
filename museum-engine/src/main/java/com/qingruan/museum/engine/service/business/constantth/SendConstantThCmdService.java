package com.qingruan.museum.engine.service.business.constantth;

import com.qingruan.museum.msg.MuseumMsg;

/**
 * 下发恒温恒湿控制命令
 * 
 * @author tommy
 * 
 */
public interface SendConstantThCmdService {
	/**
	 * 
	 * 下发温度和湿度
	 * 
	 * @param ipcanContext
	 * @return
	 */
	MuseumMsg sendConstantCmd(Long areaId);
	
	
	MuseumMsg setConstantCmd(Long id, Long presentValue,
			Long topLimit, Long lowerLimit, Long fluctuationValue,String monitorDataType);
}
