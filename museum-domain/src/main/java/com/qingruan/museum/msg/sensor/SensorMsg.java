package com.qingruan.museum.msg.sensor;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.qingruan.museum.msg.MsgBody;
import com.qingruan.museum.msg.Result;

@Getter
@Setter
@ToString
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT, property = "@class")
public class SensorMsg implements MsgBody {
	private String msgId;
	public SensorMsgType sensorMsgType;
	public SensorAppType sensorAppType;
	// 传感器的版本：树莓派为V1,433M的为V2版本
	private SensorVersion sensorVersion;
	private int qos;
	public SensorData data;
	// 设备状态
	private SensorStatusInfo sensorStatusInfo;
	private Result result;
	// 运行状态
	private SensorOperatingMode sensorOperatingMode;
	// 历史数据上报状态
	private SensorHistoryRecord sensorHistoryRecord;

	// 传感器消息类型
	public static enum SensorMsgType {
		TEXT
	}

	// 传感器应用类型
	/**
	 * 
	 * <p>
	 * 1.GATEWAY_GET_SENSOR_MAC_LIST： 网关向服务器请求中继所管辖的Sensor MAC地址列表
	 * 2.ENGINE_POST_SENSOR_MAC_LIST：服务器向中继下发所管辖的Sensor MAC地址列表
	 * 3.ENGINE_POST_SENSOR_UPGRADE:业务端下发传感器升级
	 * 4.GATEWAY_REPORT_SENSOR_UPGRADE:上报传感器升级状态
	 * 5.ENGINE_GET_SENSOR_STATUS:引擎端下发检查中继下面的Beacon的存活指令
	 * 6.GATEWAY_POST_SENSOR_STATUS:中继上报传感器状态
	 * 7.GATEWAY_POST_SENSOR_DATA:上报传感器数据
	 * 8.ENGINE_GET_HISTORY_SENSOR_DATA:获取传感器历史数据
	 * 9.GATEWAY_GET_NET_TOPOLOCY_INFO :网关获取网络拓扑信息
	 * 10.ENGINE_POST_NET_TOPOLOCY_INFO :引擎向网关推送网络拓扑信息
	 * 11.ENGINE_NOTIFY_GW_UPDATE_NET_INFO :引擎通知网关更新网络信息
	 * 12.GATEWAY_REPORT_ALARM:网关上报设备告警信息
	 * 13.ENGINE_START_DEBUG_MODE:引擎开始调试模式
	 * 14.ENGINE_NOTIFY_ALARM:引擎通知网关告警处理结果
	 * 15.ENGINE_GET_HISTORY_RECORD:引擎通知网关上报历史数据
	 * 16.ENGINE_GET_DEVICE_STATUS:引擎端检查设备存活指令
	 * 17.ENGINE_UPDATE_SENSOR_MAC_LIST :引擎更新MAC LIST列表
	 * 18.ENGINE_UPDATE_NET_TOPOLOCY_INFO:引擎更新网络拓扑
	 * </p>
	 * 
	 * 
	 */
	public static enum SensorAppType {
		GATEWAY_GET_SENSOR_MAC_LIST,
		ENGINE_POST_SENSOR_MAC_LIST,
		ENGINE_GET_SENSOR_UPGRADE,
		GATEWAY_POST_SENSOR_UPGRADE,
		ENGINE_GET_SENSOR_STATUS,
		GATEWAY_POST_SENSOR_STATUS,
		GATEWAY_POST_SENSOR_DATA,
		ENGINE_GET_HISTORY_SENSOR_DATA,
		GATEWAY_GET_NET_TOPOLOCY_INFO,
		ENGINE_POST_NET_TOPOLOCY_INFO,
		ENGINE_NOTIFY_GW_UPDATE_NET_INFO,
		GATEWAY_REPORT_ALARM,
		ENGINE_START_DEBUG_MODE,
		ENGINE_NOTIFY_ALARM,
		ENGINE_GET_HISTORY_RECORD,
		ENGINE_GET_DEVICE_STATUS,
		ENGINE_GET_SENSOR_DATA,
		ENGINE_UPDATE_SENSOR_MAC_LIST,
		ENGINE_UPDATE_NET_TOPOLOCY_INFO;
	}

	public static enum SensorVersion {
		V1, V2, V3, V4
	}

	@Override
	public String toJson() {
		// TODO Auto-generated method stub
		return null;
	}

	// 构造方法
	public SensorMsg(String msgId, SensorMsgType sensorMsgType,
			SensorAppType sensorAppType, int qos, SensorData data, Result result) {
		this.msgId = msgId;
		this.sensorMsgType = sensorMsgType;
		this.sensorAppType = sensorAppType;
		this.qos = qos;
		this.data = data;
		this.result = result;
		this.sensorVersion = null;

	}

	public SensorMsg() {

	}
}
