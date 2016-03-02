package com.qingruan.museum.msg.constantth;

import lombok.Data;

import com.qingruan.museum.msg.MsgBody;
import com.qingruan.museum.msg.Result;

@Data
public class ConstantThMsg implements MsgBody{
	
	private String msgId;
	public ThMsgType thMsgType;
	public ThAppType thAppType;
	private int qos;
	private ThData data;
	private Result result;

	// 恒温恒湿机消息类型
	public static enum ThMsgType {
		TEXT
	}

	// 恒温恒湿机应用类型
	/**
	 * <p>
	 * 
	 * 1.ENGINE_GET_TH_PRESET_DATA 业务引擎下发读取设定值
	 * 2.ENGINE_WRITE_TH_PRESET_DATA:设置恒温恒湿机的温湿度（向下设定）
	 * 3.ENGINE_NOTIFY 业务引擎下发取消息通知
	 * 4.GATEWAY_POST_TH_MONITOR_DATA：上报恒温恒湿机监测的温湿度 
	 * 5.GATEWAY_POST_TH_PRESET_DATA：上报恒温恒湿机预设置的温湿度（上报）
	 * 6.GATEWAY_POST_WRITE_STATE：   上报设定状态，是成功或者是失败
	 * 
	 * </p>
	 * 
	 */
	// Read
	public static enum ThAppType {
		
		ENGINE_GET_TH_PRESET_DATA, ENGINE_WRITE_TH_PRESET_DATA,ENGINE_NOTIFY,
		GATEWAY_POST_TH_MONITOR_DATA,GATEWAY_POST_TH_PRESET_DATA,GATEWAY_POST_WRITE_STATE
	}

	@Override
	public String toJson() {
		return "";
	}
}
