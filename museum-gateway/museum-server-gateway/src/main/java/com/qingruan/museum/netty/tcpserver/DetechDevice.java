package com.qingruan.museum.netty.tcpserver;

import lombok.extern.slf4j.Slf4j;
import io.netty.channel.Channel;

import com.qingruan.museum.domain.enums.device.DeviceStatus;
import com.qingruan.museum.framework.jackson.JSONUtil;
import com.qingruan.museum.framework.util.EncoderUtil;
import com.qingruan.museum.gateway.MuseumGateway;
import com.qingruan.museum.gateway.common.netty.SendMessage;
import com.qingruan.museum.msg.MsgHeader;
import com.qingruan.museum.msg.MsgProperty;
import com.qingruan.museum.msg.MuseumMsg;
import com.qingruan.museum.msg.MsgHeader.DeliveryMode;
import com.qingruan.museum.msg.MsgProperty.ApplicationType;
import com.qingruan.museum.msg.MsgProperty.CmdType;
import com.qingruan.museum.msg.sensor.SensorMsg;
import com.qingruan.museum.msg.sensor.SensorMsg.SensorAppType;
import com.qingruan.museum.msg.sensor.SensorMsg.SensorMsgType;
import com.qingruan.museum.msg.sensor.SensorMsg.SensorVersion;
import com.qingruan.museum.msg.sensor.SensorStatusInfo;

@Slf4j
public class DetechDevice {

	public void detechDeviceByID(String masterGatewayID, String id) {

		System.out.println("开始探测");
		String deviceType = "A0";
		StringBuilder builder = new StringBuilder();
		builder.append("3A");
		builder.append("80");
		builder.append("000000000000");
		builder.append("000000000000");
		builder.append("0003");
		builder.append(deviceType);
		builder.append(id);
		String checkCode = EncoderUtil.int2HexStr(
				UnPack.xor(builder.toString()), 2);
		builder.append(checkCode);
		builder.append("EE");
		Channel channel = CommonData.deviceChannels.get(masterGatewayID);
		SendMessage.sendMsg(channel, builder.toString());
		System.out.println("发送发送探测结束");

	}

	public void detechDeviceByMAC(String masterGatewayID, String mac) {

		System.out.println("开始探测");
		String deviceType = "A0";
		StringBuilder builder = new StringBuilder();
		builder.append("3A");
		builder.append("80");
		builder.append("000000000000");
		builder.append("000000000000");
		builder.append("0007");
		builder.append(deviceType);
		builder.append(mac);
		String checkCode = EncoderUtil.int2HexStr(
				UnPack.xor(builder.toString()), 2);
		builder.append(checkCode);
		builder.append("EE");
		Channel channel = CommonData.deviceChannels.get(masterGatewayID);
		SendMessage.sendMsg(channel, builder.toString());
		System.out.println("发送发送探测结束");

	}

	public void detechDeviceMasterGateway(String masterGatewayID) {

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

		Channel channel = CommonData.deviceChannels.get(masterGatewayID);
		if (null != channel) {
			if (channel.isActive()) {
				// 向engin端发送信息
				SensorStatusInfo sensorStatusInfo = new SensorStatusInfo();
				sensorStatusInfo.setDeviceStatus(DeviceStatus.CONNECTED);
				sensorStatusInfo.setMasterId(masterGatewayID);
				museumMsg_getMacList.setMsgBody(sensorMsg);
				String sendJson = JSONUtil.serialize(museumMsg_getMacList);
				MuseumGateway.redisMQPushSender.sendNettyToEngine(sendJson);
			} else {
				SensorStatusInfo sensorStatusInfo = new SensorStatusInfo();
				sensorStatusInfo.setDeviceStatus(DeviceStatus.DISCONNECT);
				sensorStatusInfo.setMasterId(masterGatewayID);
				museumMsg_getMacList.setMsgBody(sensorMsg);
				String sendJson = JSONUtil.serialize(museumMsg_getMacList);
				MuseumGateway.redisMQPushSender.sendNettyToEngine(sendJson);

			}
		}else{
			log.error("channel is null");
		}
	}

}
