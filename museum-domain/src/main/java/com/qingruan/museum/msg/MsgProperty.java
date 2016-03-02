package com.qingruan.museum.msg;

import java.io.Serializable;

import lombok.Data;

/**
 * 消息属性
 * 
 * @author tommy
 * 
 */
@Data
public class MsgProperty implements Serializable {
	private static final long serialVersionUID = 9178007484943999994L;
	// 标记是请求消息还是回复消息
	private CmdType cmdType;
	// 标记消息的应用类型
	private ApplicationType applicationType;

	public static enum CmdType {
		REQUEST, ANSWER
	}

	public static enum ApplicationType {
		SENSOR, RULE_UPDATE, ALARM, BEACON, CAMERA, CONSTANT_TH, LICENSE, METEOROLOGICAL_STATION,AIR_CLEANER,NEW_CONSTANT_TH
	}

	// 构造方法
	public MsgProperty(CmdType cmdType, ApplicationType applicationType) {
		this.cmdType = cmdType;
		this.applicationType = applicationType;

	}

	public MsgProperty() {

	}
}
