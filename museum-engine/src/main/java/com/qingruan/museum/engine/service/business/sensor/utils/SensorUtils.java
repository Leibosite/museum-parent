package com.qingruan.museum.engine.service.business.sensor.utils;

import com.qingruan.museum.msg.MsgHeader;
import com.qingruan.museum.msg.MsgProperty;
import com.qingruan.museum.msg.MuseumMsg;
import com.qingruan.museum.msg.MsgHeader.DeliveryMode;
import com.qingruan.museum.msg.MsgProperty.ApplicationType;
import com.qingruan.museum.msg.MsgProperty.CmdType;
import com.qingruan.museum.msg.sensor.MtmNetInfo;
import com.qingruan.museum.msg.sensor.SensorData;
import com.qingruan.museum.msg.sensor.SensorMsg;
import com.qingruan.museum.msg.sensor.SensorMsg.SensorAppType;
import com.qingruan.museum.msg.sensor.SensorMsg.SensorMsgType;

public class SensorUtils {

	public static MuseumMsg assembSensorReq(String macAddr,
			SensorAppType sensorAppType) {
		MuseumMsg museumMsg = new MuseumMsg();
		MsgHeader msgHeader = new MsgHeader("1", "NETTY", "ENGINE", "ENGINE",
				System.currentTimeMillis(), 0, DeliveryMode.PTP, 1);
		museumMsg.setMsgHeader(msgHeader);
		MsgProperty msgProperty = new MsgProperty(CmdType.REQUEST,
				ApplicationType.SENSOR);
		museumMsg.setMsgProperty(msgProperty);
		SensorData data = new SensorData();
		MtmNetInfo mtmNetInfos = new MtmNetInfo();
		mtmNetInfos.setStationMacAddr(macAddr);
		data.setMtmNetInfo(mtmNetInfos);
		SensorMsg sensorMsg = new SensorMsg("1", SensorMsgType.TEXT,
				sensorAppType, 1, data, null);
		museumMsg.setMsgBody(sensorMsg);
		return museumMsg;
	}
}
