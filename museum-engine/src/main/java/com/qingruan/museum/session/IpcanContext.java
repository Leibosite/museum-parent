/**
 2015年1月15日
 tommy
 
 */
package com.qingruan.museum.session;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.qingruan.museum.msg.MuseumMsg;

/**
 * 规则引擎运行时模型
 * 
 * @author tommy
 * 
 */
@ToString(includeFieldNames = true)
@Getter
@Setter
public class IpcanContext implements Cloneable {
	private MuseumMsg recMuseumMsg;
	private List<MuseumMsg> sendingMuseumMsgs = new ArrayList<>();

	// 运行时数据
	private RunTimeInfo runTimeInfo;
	// 数据流实时评分
	private RealTimeScoreAndExplain realTimeScore;
	private Boolean isRunEngine = Boolean.TRUE;
}
