package com.qingruan.museum.netty.tcpserver;

import com.qingruan.museum.domain.enums.device.DeviceStatus;
import com.qingruan.museum.framework.jackson.JSONUtil;
import com.qingruan.museum.gateway.MuseumGateway;
import com.qingruan.museum.msg.MsgHeader;
import com.qingruan.museum.msg.MsgProperty;
import com.qingruan.museum.msg.MuseumMsg;
import com.qingruan.museum.msg.MsgHeader.DeliveryMode;
import com.qingruan.museum.msg.MsgProperty.ApplicationType;
import com.qingruan.museum.msg.MsgProperty.CmdType;
import com.qingruan.museum.msg.sensor.SensorMsg;
import com.qingruan.museum.msg.sensor.SensorStatusInfo;
import com.qingruan.museum.msg.sensor.SensorMsg.SensorAppType;
import com.qingruan.museum.msg.sensor.SensorMsg.SensorMsgType;
import com.qingruan.museum.msg.sensor.SensorMsg.SensorVersion;

public class SendToEngineDecethMessage {

	public void send2EngineDecethMac(String mac, DeviceStatus deviceStatus) {

		MuseumMsg museumMsg_getMacList = new MuseumMsg();
		MsgHeader msgHeader = new MsgHeader();
		msgHeader.setDeliveryMode(DeliveryMode.PTP);
		msgHeader.setSendFrom("NETTY");
		msgHeader.setSendFrom("ENGINE");
		msgHeader.setTimeStamp(System.currentTimeMillis());
		museumMsg_getMacList.setMsgHeader(msgHeader);
		MsgProperty msgProperty = new MsgProperty();
		msgProperty.setCmdType(CmdType.REQUEST);
		msgProperty.setApplicationType(ApplicationType.SENSOR);
		museumMsg_getMacList.setMsgProperty(msgProperty);

		SensorMsg sensorMsg = new SensorMsg();
		sensorMsg.setSensorAppType(SensorAppType.GATEWAY_POST_SENSOR_STATUS);
		sensorMsg.setSensorMsgType(SensorMsgType.TEXT);
		sensorMsg.setSensorVersion(SensorVersion.V2);
		SensorStatusInfo sensorStatusInfo = new SensorStatusInfo();
		sensorStatusInfo.setDeviceStatus(deviceStatus);
		sensorStatusInfo.setMacAddr(mac);
		museumMsg_getMacList.setMsgBody(sensorMsg);
		String sendJson = JSONUtil.serialize(museumMsg_getMacList);
		MuseumGateway.redisMQPushSender.sendNettyToEngine(sendJson);

	}

	public void send2EngineDecethID(String ID, DeviceStatus deviceStatus) {
		MuseumMsg museumMsg_getMacList = new MuseumMsg();
		MsgHeader msgHeader = new MsgHeader();
		msgHeader.setDeliveryMode(DeliveryMode.PTP);
		msgHeader.setSendFrom("NETTY");
		msgHeader.setSendFrom("ENGINE");
		msgHeader.setTimeStamp(System.currentTimeMillis());
		museumMsg_getMacList.setMsgHeader(msgHeader);
		MsgProperty msgProperty = new MsgProperty();
		msgProperty.setCmdType(CmdType.REQUEST);
		msgProperty.setApplicationType(ApplicationType.SENSOR);
		museumMsg_getMacList.setMsgProperty(msgProperty);

		SensorMsg sensorMsg = new SensorMsg();
		sensorMsg.setSensorAppType(SensorAppType.GATEWAY_POST_SENSOR_STATUS);
		sensorMsg.setSensorMsgType(SensorMsgType.TEXT);
		sensorMsg.setSensorVersion(SensorVersion.V2);
		SensorStatusInfo sensorStatusInfo = new SensorStatusInfo();
		sensorStatusInfo.setDeviceStatus(deviceStatus);
		sensorStatusInfo.setId(ID);
		museumMsg_getMacList.setMsgBody(sensorMsg);
		String sendJson = JSONUtil.serialize(museumMsg_getMacList);
		MuseumGateway.redisMQPushSender.sendNettyToEngine(sendJson);
	}
}
