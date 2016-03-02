package com.qingruan.museum.netty.protocol.up;

import io.netty.channel.Channel;

import java.util.HashSet;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import com.qingruan.museum.domain.enums.device.DeviceStatus;
import com.qingruan.museum.domain.models.enums.MonitorDataType;
import com.qingruan.museum.framework.jackson.JSONUtil;
import com.qingruan.museum.framework.util.EncoderUtil;
import com.qingruan.museum.framework.util.TimeUtil;
import com.qingruan.museum.gateway.MuseumGateway;
import com.qingruan.museum.gateway.common.netty.SendMessage;
import com.qingruan.museum.msg.MsgHeader;
import com.qingruan.museum.msg.MsgHeader.DeliveryMode;
import com.qingruan.museum.msg.MsgProperty;
import com.qingruan.museum.msg.MsgProperty.ApplicationType;
import com.qingruan.museum.msg.MsgProperty.CmdType;
import com.qingruan.museum.msg.MuseumMsg;
import com.qingruan.museum.msg.constantth.ConstantThMsg;
import com.qingruan.museum.msg.constantth.ConstantThMsg.ThAppType;
import com.qingruan.museum.msg.constantth.ConstantThMsg.ThMsgType;
import com.qingruan.museum.msg.constantth.ThData;
import com.qingruan.museum.msg.sensor.MtmNetInfo;
import com.qingruan.museum.msg.sensor.SensorData;
import com.qingruan.museum.msg.sensor.SensorMsg;
import com.qingruan.museum.msg.sensor.SensorMsg.SensorAppType;
import com.qingruan.museum.msg.sensor.SensorMsg.SensorMsgType;
import com.qingruan.museum.msg.sensor.SensorMsg.SensorVersion;
import com.qingruan.museum.msg.sensor.SensorNetStruct;
import com.qingruan.museum.msg.sensor.SensorStatusInfo;
import com.qingruan.museum.msg.weather.WeatherDataContent;
import com.qingruan.museum.msg.weather.WeatherMsg;
import com.qingruan.museum.netty.model.UpDataPackageV3;
import com.qingruan.museum.netty.tcpserver.CommonData;
import com.qingruan.museum.netty.tcpserver.SendToEngineDecethMessage;
import com.qingruan.museum.netty.tcpserver.UnPack;

/**
 * 
 * @author jcy
 * @description: 解析新版本的上传数据
 */
@Slf4j
public class ResolveDataV2 {
	/**
	 * 消息体的元数据图
	 */
	private static int[] dataLengthArray = new int[] { 4, 4, 4, 2, 2, 2, 2, 2,
			2, 2, 2, 4, 4, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 4 };
	
	private final String   CONSTAN_TH_DEVICE = "0310";

	private static int getIndex(int i) {
		int j = 0;
		for (int k = 0; k < i; k++) {
			j = j + dataLengthArray[k];
		}
		return j * 2;
	}

	public void judgeCommand(UpDataPackageV3 msg) {
		switch (msg.getCommand().toUpperCase()) {

		case "79":
			heartBeat(msg);// 心跳
			break;
		case "80":
			heartBeat(msg);// 握手
			break;
		case "81":
			requestMacList(msg);// 准备好获取maclist
			break;
		case "82":
			confirmMacList(msg);// 确认收到
			break;
		case "84":
			send2Engine(msg); // 返回给engine端的探测结果消息
			break;
		case "86":
			getNetInfo(msg); // 索要网络拓扑 @@@@@@@@@@@@@@@@@@@@@@
			break;
		case "87":
			pushNetInfo(msg);
			break;
		case "88":
			send2EngineAlarm(msg); // 告警消息 向engine @@@@@@@@@@@@@@@@@@@@@@@@@@@
			break;
		case "90":
			responseData(msg);// 上报气象站数据
			break;
		case "92":
			responseHistoryData(msg);// 上报历史数据
			break;
		case "93":
			dispatchDataByDeviceType(msg);//上报传感器数据
			break;
		default:
			break;
		}
	}
	
	private void dispatchDataByDeviceType(UpDataPackageV3 msg){
		String dataBody = msg.getDataBody();
		String deviceType = dataBody.substring(4, 8);
		if(deviceType.equals(CONSTAN_TH_DEVICE)){
			reportEngineData(msg);// 上报的恒温恒湿数据
		}else{
			responseSensorData(msg);//上报传感器数据
		}
	}

	private void reportEngineData(UpDataPackageV3 msg) {
		System.out.println("向engine端报告恒温数据");
		String dataBody = msg.getDataBody();
//		String dataType = dataBody.substring(0, 4);
//		String constanFlag = dataBody.substring(4, 8);
		String computeValueSting = dataBody.substring(8, 12);
		Long computeValue = Long.parseLong(computeValueSting) * 100;
//		String settingValue = dataBody.substring(12, 16);
		String topSting = dataBody.substring(16, 20) ;
		Long top = Long.parseLong(topSting) * 100;
		String bottomString = dataBody.substring(20, 24);
		Long bottom = Long.parseLong(bottomString) * 100;
		String fluctuationValueString = dataBody.substring(24, 26);
		Long fluctuationValue = Long.parseLong(fluctuationValueString) * 100;
		String timeStamp = dataBody.substring(26, 30);
//		String flag = dataBody.substring(30, 32);

		MuseumMsg museumMsg = new MuseumMsg();
		MsgHeader msgHeader = new MsgHeader();
		msgHeader.setDeliveryMode(DeliveryMode.PTP);
		msgHeader.setSendFrom("NETTY");
		msgHeader.setSendFrom("ENGINE");
		msgHeader.setTimeStamp(System.currentTimeMillis());
		museumMsg.setMsgHeader(msgHeader);
		MsgProperty msgProperty = new MsgProperty();
		msgProperty.setCmdType(CmdType.REQUEST);
		msgProperty.setApplicationType(ApplicationType.CONSTANT_TH);
		museumMsg.setMsgProperty(msgProperty);

		ConstantThMsg constantMsg = new ConstantThMsg();
		ThData thData = new ThData();
		thData.setMasterGatewayID(msg.getMasterGatewayID());
		thData.setPath(msg.getPath());
		thData.setTimeStamps(timeStamp);
		thData.setTopLimit(top);
		thData.setLowerLimit(bottom);
		thData.setReportValue(computeValue);
		thData.setFluctuationValue(fluctuationValue);
		constantMsg.setData(thData);
		constantMsg.setThAppType(ThAppType.GATEWAY_POST_TH_PRESET_DATA);
		constantMsg.setThMsgType(ThMsgType.TEXT);
		museumMsg.setMsgBody(constantMsg);
		String sendJson = JSONUtil.serialize(museumMsg);

		MuseumGateway.redisMQPushSender.sendNettyToEngine(sendJson);
		System.out.println("向engine端报告恒温数据结束");

	}

	private void pushNetInfo(UpDataPackageV3 msg) {

	}

	private void getNetInfo(UpDataPackageV3 msg) {
		String masterGatewayID = msg.getMasterGatewayID();
		MuseumMsg museumMsg = new MuseumMsg();
		MsgHeader msgHeader = new MsgHeader();
		msgHeader.setDeliveryMode(DeliveryMode.PTP);
		msgHeader.setSendFrom("NETTY");
		msgHeader.setSendFrom("ENGINE");
		msgHeader.setTimeStamp(System.currentTimeMillis());
		museumMsg.setMsgHeader(msgHeader);
		MsgProperty msgProperty = new MsgProperty();
		msgProperty.setCmdType(CmdType.REQUEST);
		msgProperty.setApplicationType(ApplicationType.SENSOR);
		museumMsg.setMsgProperty(msgProperty);

		SensorMsg sensorMsg = new SensorMsg();
		SensorData sensorData = new SensorData();
		MtmNetInfo mtmNetInfo = new MtmNetInfo();
		SensorNetStruct sensorNetStruct = new SensorNetStruct();
		sensorNetStruct.setMasterGatewayId(masterGatewayID);
		mtmNetInfo.setSensorNetStruct(sensorNetStruct);
		// 设置新的
		sensorData.setMtmNetInfo(mtmNetInfo);
		sensorMsg.setData(sensorData);
		// 更改成请求完整数据的标记
		sensorMsg.setSensorAppType(SensorAppType.GATEWAY_GET_NET_TOPOLOCY_INFO);
		sensorMsg.setSensorMsgType(SensorMsgType.TEXT);
		sensorMsg.setSensorVersion(SensorVersion.V2);
		museumMsg.setMsgBody(sensorMsg);
		String sendJson = JSONUtil.serialize(museumMsg);
		System.out.println("向engine发送开始发送81请求完整数据的响应结束");
		MuseumGateway.redisMQPushSender.sendNettyToEngine(sendJson);
		System.out.println("向engine发送开始发送81请求完整数据的响应结束。。等待engine响应");
	}

	/**
	 * 返回的给engine端的返回消息
	 * 
	 * @param msg
	 */
	private void send2Engine(UpDataPackageV3 msg) {
		String dataBody = msg.getDataBody();
		Integer dataLength = msg.getDataLength();

		if (dataBody.length() < 6) {
			log.error("data formate error");
			System.out.println("data formate error");
			return;
		}
		String flag = null;
		String id = null;
		String mac = null;
		if (dataLength == 3) {
			try {
				flag = dataBody.substring(0, 2);

			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			id = dataBody.substring(2);
			if ("00".equalsIgnoreCase(flag)) {
				new SendToEngineDecethMessage().send2EngineDecethID(id,
						DeviceStatus.CONNECTED);
			} else {
				new SendToEngineDecethMessage().send2EngineDecethID(id,
						DeviceStatus.DISCONNECT);
			}

		} else if (dataLength == 7) {
			try {
				flag = dataBody.substring(0, 2);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			mac = dataBody.substring(2);
			if ("00".equalsIgnoreCase(flag)) {
				new SendToEngineDecethMessage().send2EngineDecethMac(mac,
						DeviceStatus.CONNECTED);
			} else {
				new SendToEngineDecethMessage().send2EngineDecethMac(mac,
						DeviceStatus.DISCONNECT);
			}
		} else {
			log.error("data formate error");
		}

	}

	private void send2EngineAlarm(UpDataPackageV3 msg) {
		String dataBody = msg.getDataBody();

		String presentDeviceType = dataBody.substring(0, 2).toUpperCase();
		String id = null;
		String errorType = null;
		String mac = null;
		if ("D0".equals(presentDeviceType)) {
			id = dataBody.substring(2, 6);
			errorType = dataBody.substring(6, 8);
		}else if("C0".equals(presentDeviceType)){
			mac = dataBody.substring(2,14);
			errorType = dataBody.substring(14, 16);
		}else{
			log.error("DeviceType is error");
		}

		MuseumMsg museumMsg = new MuseumMsg();
		MsgHeader msgHeader = new MsgHeader();
		msgHeader.setDeliveryMode(DeliveryMode.PTP);
		msgHeader.setSendFrom("NETTY");
		msgHeader.setSendFrom("ENGINE");
		msgHeader.setTimeStamp(System.currentTimeMillis());
		museumMsg.setMsgHeader(msgHeader);
		MsgProperty msgProperty = new MsgProperty();
		msgProperty.setCmdType(CmdType.REQUEST);
		msgProperty.setApplicationType(ApplicationType.ALARM);
		museumMsg.setMsgProperty(msgProperty);
		SensorMsg sensorMsg = new SensorMsg();
		
		SensorData sensorData = new SensorData();
		SensorStatusInfo sensorStatusInfo = new SensorStatusInfo();
		sensorStatusInfo.setId(id);
		sensorStatusInfo.setMacAddr(mac);
		sensorStatusInfo.setDeviceStatus(DeviceStatus.DISCONNECT);
		sensorData.setSensorStatusInfo(sensorStatusInfo);
		
		sensorMsg.setData(sensorData);
		sensorMsg.setSensorMsgType(SensorMsgType.TEXT);
		sensorMsg.setSensorVersion(SensorVersion.V2);
		museumMsg.setMsgBody(sensorMsg);
		String sendJson = JSONUtil.serialize(museumMsg);
		System.out.println("向engine发送发送告警信息");
		MuseumGateway.redisMQPushSender.sendNettyToEngine(sendJson);
	}
	
	/**
	 * 握手协议
	 * 
	 * @param msg
	 */
	public void shakeHand(UpDataPackageV3 msg) {
		System.out.println("开始心跳");
		String deviceType = "A0";
		StringBuilder builder = new StringBuilder();
		builder.append("3A");
		builder.append("79");
		builder.append(msg.getPath());
		builder.append(msg.getDeviceID());
		builder.append("0002");
		builder.append(deviceType);
		builder.append("00");
		String checkCode = EncoderUtil.int2HexStr(UnPack.xor(builder.toString()), 2);
		builder.append(checkCode);
		builder.append("EE");
		Channel channel = CommonData.deviceChannels.get(msg
				.getMasterGatewayID());
		SendMessage.sendMsg(channel, builder.toString());
		System.out.println("发送心跳响应结束");
	}

	/**
	 * 心跳协议
	 * 
	 * @param msg
	 */
	public void heartBeat(UpDataPackageV3 msg) {
		System.out.println("开始心跳");
		String deviceType = "A0";
		StringBuilder builder = new StringBuilder();
		builder.append("3A");
		builder.append("79");
		builder.append(msg.getPath());
		builder.append(msg.getDeviceID());
		builder.append("0002");
		builder.append(deviceType);
		builder.append("00");
		String checkCode = EncoderUtil.int2HexStr(UnPack.xor(builder.toString()), 2);
		builder.append(checkCode);
		builder.append("EE");
		Channel channel = CommonData.deviceChannels.get(msg
				.getMasterGatewayID());
		SendMessage.sendMsg(channel, builder.toString());
		System.out.println("发送心跳响应结束");
	}

	/**
	 * 握手协议
	 * 
	 * @param msg
	 */
	public void handShake(UpDataPackageV3 msg) {
		System.out.println("接收到握手消息 不响应");
		
	}

	private void requestMacList(UpDataPackageV3 msg) {
		System.out.println("开始发送81请求完整数据的响应");
		String requestMacListResult = "00";
		String deviceType = "A0";
		StringBuilder builder = new StringBuilder();
		builder.append("3A");
		builder.append("81");
		builder.append(msg.getPath());
		builder.append(msg.getDeviceID());
		builder.append("0002");
		builder.append(deviceType);
		builder.append(requestMacListResult);
		String checkCode = EncoderUtil.int2HexStr(
				UnPack.xor(builder.toString()), 2);
		builder.append(checkCode);
		builder.append("EE");
		String masterGatewayID = msg.getMasterGatewayID();
		Channel channel = CommonData.deviceChannels.get(masterGatewayID);
		SendMessage.sendMsg(channel, builder.toString());
		System.out.println("开始发送81请求完整数据的响应结束");
		// ********************************************
//		if (null != CommonData.macList.get(masterGatewayID)) {
//			System.out.println(masterGatewayID + "的数据已经准备完备，无需再向engine端请求");
//		} else {

			// 向engine端请求拓扑数据，给定主网关的ID
			System.out.println("向engine发送开始发送81请求完整数据的响应");
			MuseumMsg museumMsg = new MuseumMsg();
			MsgHeader msgHeader = new MsgHeader();
			msgHeader.setDeliveryMode(DeliveryMode.PTP);
			msgHeader.setSendFrom("NETTY");
			msgHeader.setSendFrom("ENGINE");
			msgHeader.setTimeStamp(System.currentTimeMillis());
			museumMsg.setMsgHeader(msgHeader);
			MsgProperty msgProperty = new MsgProperty();
			msgProperty.setCmdType(CmdType.REQUEST);
			msgProperty.setApplicationType(ApplicationType.SENSOR);
			museumMsg.setMsgProperty(msgProperty);

			SensorMsg sensorMsg = new SensorMsg();
			SensorData sensorData = new SensorData();
			MtmNetInfo mtmNetInfo = new MtmNetInfo();
			SensorNetStruct sensorNetStruct = new SensorNetStruct();
			sensorNetStruct.setMasterGatewayId(masterGatewayID);
			mtmNetInfo.setSensorNetStruct(sensorNetStruct);
			// 设置新的
			sensorData.setMtmNetInfo(mtmNetInfo);
			sensorMsg.setData(sensorData);
			// 更改成请求完整数据的标记
			sensorMsg
					.setSensorAppType(SensorAppType.GATEWAY_GET_SENSOR_MAC_LIST);
			sensorMsg.setSensorMsgType(SensorMsgType.TEXT);
			sensorMsg.setSensorVersion(SensorVersion.V2);
			museumMsg.setMsgBody(sensorMsg);
			String sendJson = JSONUtil.serialize(museumMsg);
			System.out.println("向engine发送开始发送81请求完整数据的响应结束");
			MuseumGateway.redisMQPushSender.sendNettyToEngine(sendJson);
			System.out.println("向engine发送开始发送81请求完整数据的响应结束。。等待engine响应");
//		}
	}

	private void confirmMacList(UpDataPackageV3 msg) {
		
		String path = msg.getPath();
		System.out.println("确认的82");
		// 增加收到的82数 在发送第一包是就初始化了recev82PathNum为0
		// 判断是否发完
		String masterGatewayID = msg.getMasterGatewayID();
		HashSet<String> hashSet = CommonData.recev82PathNum
				.get(masterGatewayID);
		if (null == hashSet) {
			hashSet = new HashSet<>();
			hashSet.add(path);
			CommonData.recev82PathNum.put(masterGatewayID, hashSet);
		} else {
			hashSet.add(path);
			CommonData.recev82PathNum.put(masterGatewayID, hashSet);
		}
		// 在 sendPathPackage(masterGatewayID);先检查是否发完
		if (hashSet.size() == CommonData.sequenceListMap.get(masterGatewayID)
				.size()) {
			System.out.println("82发送完成 清除缓存" + masterGatewayID);
			return;
			//
		} else {
			System.out.println("继续82发送");
			// new MacListSender().sendPathPackage(masterGatewayID);
		}

		// String masterGatewayID = msg.getMasterGatewayID();
		// new MacListSender().sendPackage(masterGatewayID);
		// if (null == CommonData.timerTask.get(masterGatewayID)) {
		// FixTimerTask fixTimerTask = new FixTimerTask(masterGatewayID);
		// Timer timer = new Timer();
		// long delay = 1 * 1000;
		// long intevalPeriod = 1 * 1000;
		// timer.scheduleAtFixedRate(fixTimerTask, delay, intevalPeriod);
		// System.out.println("给" + masterGatewayID + "开启定时任务");
		// CommonData.timerTask.put(masterGatewayID, timer);
		// } else {
		// Timer oldTimer = CommonData.timerTask.get(masterGatewayID);
		// oldTimer.cancel();
		// FixTimerTask fixTimerTask = new FixTimerTask(masterGatewayID);
		// Timer timer = new Timer();
		// long delay = 60 * 1000;
		// long intevalPeriod = 60 * 1000;
		// timer.scheduleAtFixedRate(fixTimerTask, delay, intevalPeriod);
		// System.out.println("给" + masterGatewayID + "开启定时任务");
		// CommonData.timerTask.put(masterGatewayID, timer);
		// }

	}

	/**
	 * 确认macList处理成功
	 * 
	 * @param msg
	 */
	// private void confirmMacListSucces(UpDataPackageV3 msg) {
	//
	// String masterGatewayID = msg.getMasterGatewayID();
	// String path = msg.getPath();
	// ArrayList<String> arrayList = CommonData.sequenceSuccessListMap
	// .get(masterGatewayID);
	// if (null == arrayList) {
	// arrayList = new ArrayList<>();
	// }
	// arrayList.add(path);
	// CommonData.sequenceSuccessListMap.put(masterGatewayID, arrayList);
	// }

	/*
	 * 出现比较重要的任务时，硬件会默认的关闭数据上报来接受来个任务
	 */
	// private void sendOpenData(String masterGatewayID) {
	// System.out.println("开始打开数据上报");
	// String deviceType = "A0";
	// StringBuilder builder = new StringBuilder();
	// builder.append("3A");
	// builder.append("91");
	// builder.append("000000000000");
	// builder.append("000000000000");
	// builder.append("0001");
	// builder.append(deviceType);
	// String checkCode = EncoderUtil.int2HexStr(
	// UnPack.xor(builder.toString()), 2);
	// builder.append(checkCode);
	// builder.append("EE");
	// Channel channel = CommonData.deviceChannels.get(masterGatewayID);
	// SendMessage.sendMsg(channel, builder.toString());
	// System.out.println("打开数据上报结束");
	// }

	/**
	 * flash标记位
	 * 
	 * @param masterGatewayID
	 *            写给那个主网关
	 */

	public void writeFlash(String masterGatewayID) {
		System.out.println("开始写flash");
		String deviceType = "A0";
		String hexTime = TimeUtil.hexTimeString();
		StringBuilder builder = new StringBuilder();
		builder.append("3A");
		builder.append("85");
		builder.append("000000000000");
		builder.append("000000000000");
		builder.append("0005");
		builder.append(deviceType);
		builder.append(hexTime);
		String checkCode = EncoderUtil.int2HexStr(
				UnPack.xor(builder.toString()), 2);
		builder.append(checkCode);
		builder.append("EE");
		Channel channel = CommonData.deviceChannels.get(masterGatewayID);
		SendMessage.sendMsg(channel, builder.toString());
		System.out.println("发送flash结束");

	}

	/**
	 * 解析气象站上报数据
	 * 
	 * @param msg
	 */
	public void responseData(UpDataPackageV3 msg) {
		String handShakeResult = "00";
		try {

			String body = msg.getDataBody();
			resolveWeatherData(body, msg.getDeviceID());

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
			handShakeResult = "01";
		}

		String deviceType = "A0";
		StringBuilder builder = new StringBuilder();
		builder.append("3A");
		builder.append("90");
		builder.append(msg.getPath());
		builder.append(msg.getDeviceID());
		builder.append("02");
		builder.append(deviceType);
		builder.append(handShakeResult);
		String checkCode = EncoderUtil.int2HexStr(
				UnPack.xor(builder.toString()), 2);
		builder.append(checkCode);
		builder.append("EE");
		Channel channel = CommonData.deviceChannels.get(msg
				.getMasterGatewayID());
		SendMessage.sendMsg(channel, builder.toString());

	}

	/**
	 * 解析气象站历史数据
	 * 
	 * @param msg
	 */
	public void responseHistoryData(UpDataPackageV3 msg) {

		String handShakeResult = "00";
		try {
			int packageNumber = (msg.getDataLength() - 1) / 74;
			String body = msg.getDataBody();
			String deviceID = msg.getDeviceID();
			for (int i = 0; i < packageNumber; i++) {
				String weatherPackage = body.substring(i * 74 * 2,
						(i + 1) * 74 * 2);
				resolveWeatherData(weatherPackage, deviceID);
			}

		} catch (Exception e) {
			handShakeResult = "01";
		}

		String deviceType = "A0";
		StringBuilder builder = new StringBuilder();
		builder.append("3A");
		builder.append("92");
		builder.append(msg.getDeviceID());
		builder.append("02");
		builder.append(deviceType);
		builder.append(handShakeResult);
		String checkCode = EncoderUtil.int2HexStr(
				UnPack.xor(builder.toString()), 2);
		builder.append(checkCode);
		builder.append("EE");
		Channel channel = CommonData.deviceChannels.get(msg
				.getMasterGatewayID());
		SendMessage.sendMsg(channel, builder.toString());

	}
	
	private static void responseSensorData(UpDataPackageV3 msg){
		
		MuseumMsg museumMsg;
		try {
			museumMsg = ResolveData.resolveSensorData(msg.getDataBody(), msg.getMasterGatewayID(),msg.getDeviceID(),"00","00000000");
			String domainJson = JSONUtil.serialize(museumMsg);
			System.out.println("=================================================");
			System.out.println(domainJson);
			MuseumGateway.redisMQPushSender.sendNettyToEngine(domainJson);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * @description: 解析气象站数据工具方法
	 * @param body
	 * @param deviceID
	 */
	public void resolveWeatherData(String body, String deviceID) {

		MuseumMsg museumMsg = new MuseumMsg();

		MsgHeader header = new MsgHeader();
		header.setDeliveryMode(DeliveryMode.PTP);
		header.setTimeStamp(System.currentTimeMillis());
		museumMsg.setMsgHeader(header);

		MsgProperty msgProperty = new MsgProperty();
		msgProperty.setApplicationType(ApplicationType.METEOROLOGICAL_STATION);
		msgProperty.setCmdType(CmdType.REQUEST);
		museumMsg.setMsgProperty(msgProperty);

		WeatherMsg weatherMsg = new WeatherMsg();
		weatherMsg.setStationMacAddr(deviceID);

		List<WeatherDataContent> weatherDataList = weatherMsg.getWeatherDatas();

		for (int j = 1; j <= dataLengthArray.length; j++) {
			WeatherDataContent weatherDataContent = new WeatherDataContent();
			String value = body.substring(getIndex(j - 1), getIndex(j));
			switch (j) {

			case 1: {

				weatherMsg.setTimestamp(Long.valueOf(value, 16));
			}
				break;
			case 2: {
				weatherDataContent
						.setMonitorDataType(MonitorDataType.LONGITUDE);

				double lonValue = 0.0;
				long valueTemp = Long.valueOf(value, 16);
				long temp = Integer.MAX_VALUE - valueTemp;
				if (temp < 0) {
					lonValue = temp / 600000.0;
				} else {
					lonValue = valueTemp / 600000.0;
				}
				weatherDataContent.setValue(lonValue);
				weatherDataList.add(weatherDataContent);
			}
				break;
			case 3: {
				weatherDataContent.setMonitorDataType(MonitorDataType.LATITUDE);

				double latValue = 0.0;
				long valueTemp = Long.valueOf(value, 16);
				long temp = Integer.MAX_VALUE - valueTemp;
				if (temp < 0) {
					latValue = temp / 600000.0;
				} else {
					latValue = valueTemp / 600000.0;
				}
				weatherDataContent.setValue(latValue);
				weatherDataList.add(weatherDataContent);
			}
				break;
			case 4: {
				// GPS速度
				weatherDataContent
						.setMonitorDataType(MonitorDataType.GPS_SPEED);
				double data = Integer.valueOf(value, 16) / 10.0;
				weatherDataContent.setValue(data);
				weatherDataList.add(weatherDataContent);
			}
				break;
			case 5: {
				// GPS方向
				weatherDataContent
						.setMonitorDataType(MonitorDataType.GPS_DIRECTION);
				double data = Integer.valueOf(value, 16);
				weatherDataContent.setValue(data);
				weatherDataList.add(weatherDataContent);
			}
				break;
			case 6: {
				weatherDataContent.setMonitorDataType(MonitorDataType.HEIGHT);
				double data = 0;
				int valueTemp = Integer.valueOf(value, 16);
				int temp = 65535 - valueTemp;
				if (temp < 0) {
					data = temp * 1.0;
				} else {
					data = valueTemp * 1.0;
				}

				weatherDataContent.setValue(data);
				weatherDataList.add(weatherDataContent);

			}
				break;
			case 7: {
				weatherDataContent
						.setMonitorDataType(MonitorDataType.AIRPRESSURE);
				double data = Integer.valueOf(value, 16) * 10.0;
				weatherDataContent.setValue(data);
				weatherDataList.add(weatherDataContent);

			}
				break;
			case 8: {
				weatherDataContent
						.setMonitorDataType(MonitorDataType.WIND_DIRECTION);
				double data = Integer.valueOf(value, 16);
				weatherDataContent.setValue(data);
				weatherDataList.add(weatherDataContent);

			}
				break;
			case 9: {
				weatherDataContent
						.setMonitorDataType(MonitorDataType.WIND_SPEED);
				double data = Integer.valueOf(value, 16) / 10.0;
				weatherDataContent.setValue(data);
				weatherDataList.add(weatherDataContent);
			}
				break;
			case 10: {
				weatherDataContent
						.setMonitorDataType(MonitorDataType.TEMPERATURE);
				double data = 0;
				int valueTemp = Integer.valueOf(value, 16);
				int temp = 65535 - valueTemp;
				if (temp < 0) {
					data = temp / 10.0;
				} else {
					data = valueTemp / 10.0;
				}
				weatherDataContent.setValue(data);
				weatherDataList.add(weatherDataContent);
			}
				break;
			case 11: {
				weatherDataContent.setMonitorDataType(MonitorDataType.HUMIDITY);
				double data = 0;
				int valueTemp = Integer.valueOf(value, 16);
				int temp = 65535 - valueTemp;
				if (temp < 0) {
					data = temp / 10.0;
				} else {
					data = valueTemp / 10.0;
				}
				weatherDataContent.setValue(data);
				weatherDataList.add(weatherDataContent);
			}
				break;
			case 12: {
				weatherDataContent.setMonitorDataType(MonitorDataType.LIGHTING);
				double data = Long.valueOf(value, 16) / 100.0;
				weatherDataContent.setValue(data);
				weatherDataList.add(weatherDataContent);
			}
				break;
			case 13: {
				weatherDataContent.setMonitorDataType(MonitorDataType.UV);
				double data = Integer.valueOf(value, 16) / 100.0;
				weatherDataContent.setValue(data);
				weatherDataList.add(weatherDataContent);
			}
				break;
			case 14: {
				weatherDataContent.setMonitorDataType(MonitorDataType.PM1_0);
				double data = Integer.valueOf(value, 16) / 10;
				weatherDataContent.setValue(data);
				weatherDataList.add(weatherDataContent);
			}
				break;
			case 15: {
				weatherDataContent.setMonitorDataType(MonitorDataType.PM2_5);
				double data = Integer.valueOf(value, 16) / 10;
				weatherDataContent.setValue(data);
				weatherDataList.add(weatherDataContent);
			}
				break;
			case 16: {
				weatherDataContent.setMonitorDataType(MonitorDataType.PM10);
				double data = Integer.valueOf(value, 16) / 10;
				weatherDataContent.setValue(data);
				weatherDataList.add(weatherDataContent);
			}
				break;
			case 17: {
				weatherDataContent.setMonitorDataType(MonitorDataType.UM_01_03);
				double data = Integer.valueOf(value, 16);
				weatherDataContent.setValue(data);
				weatherDataList.add(weatherDataContent);
			}
				break;
			case 18: {
				weatherDataContent.setMonitorDataType(MonitorDataType.UM_01_05);
				double data = Integer.valueOf(value, 16);
				weatherDataContent.setValue(data);
				weatherDataList.add(weatherDataContent);
			}
				break;
			case 19: {
				weatherDataContent.setMonitorDataType(MonitorDataType.UM_01_10);
				double data = Integer.valueOf(value, 16);
				weatherDataContent.setValue(data);
				weatherDataList.add(weatherDataContent);
			}
				break;
			case 20: {
				weatherDataContent.setMonitorDataType(MonitorDataType.UM_01_25);
				double data = Integer.valueOf(value, 16);
				weatherDataContent.setValue(data);
				weatherDataList.add(weatherDataContent);
			}
				break;
			case 21: {
				weatherDataContent.setMonitorDataType(MonitorDataType.UM_01_50);
				double data = Integer.valueOf(value, 16);
				weatherDataContent.setValue(data);
				weatherDataList.add(weatherDataContent);
			}
				break;
			case 22: {
				weatherDataContent
						.setMonitorDataType(MonitorDataType.UM_01_100);
				double data = Integer.valueOf(value, 16);
				weatherDataContent.setValue(data);
				weatherDataList.add(weatherDataContent);
			}
				break;
			case 23: {
				// 保留位
				weatherDataContent.setMonitorDataType(MonitorDataType.X1);
				double data = Integer.valueOf(value, 16);
				weatherDataContent.setValue(data);
				weatherDataList.add(weatherDataContent);
			}
				break;
			case 24: {
				weatherDataContent.setMonitorDataType(MonitorDataType.CO2);
				double data = Integer.valueOf(value, 16) / 10.0;
				weatherDataContent.setValue(data);
				weatherDataList.add(weatherDataContent);
			}
				break;
			case 25: {
				weatherDataContent.setMonitorDataType(MonitorDataType.O2);
				double data = Integer.valueOf(value, 16) / 10.0;
				weatherDataContent.setValue(data);
				weatherDataList.add(weatherDataContent);
			}
				break;
			case 26: {
				weatherDataContent.setMonitorDataType(MonitorDataType.SO2);
				double data = Integer.valueOf(value, 16) / 100.0;
				weatherDataContent.setValue(data);
				weatherDataList.add(weatherDataContent);
			}
				break;
			case 27: {
				weatherDataContent.setMonitorDataType(MonitorDataType.TVOC);
				double data = Integer.valueOf(value, 16) / 100.0;
				weatherDataContent.setValue(data);
				weatherDataList.add(weatherDataContent);
			}
				break;
			case 28: {
				weatherDataContent.setMonitorDataType(MonitorDataType.CH2O);
				double data = Integer.valueOf(value, 16) / 100.0;
				weatherDataContent.setValue(data);
				weatherDataList.add(weatherDataContent);
			}
				break;
			case 29: {
				weatherDataContent
						.setMonitorDataType(MonitorDataType.MAIN_CONTROL_PANEL_VOLTAGE);
				double data = Integer.valueOf(value, 16) / 10.0;
				weatherDataContent.setValue(data);
				weatherDataList.add(weatherDataContent);
			}
				break;
			case 30: {
				weatherDataContent
						.setMonitorDataType(MonitorDataType.SOLAR_ENERGY_VOLTAGE);
				double data = Integer.valueOf(value, 16) / 10.0;
				weatherDataContent.setValue(data);
				weatherDataList.add(weatherDataContent);
			}
				break;
			case 31: {
				weatherDataContent.setMonitorDataType(MonitorDataType.X2);
				double data = Integer.valueOf(value, 16);
				weatherDataContent.setValue(data);
				weatherDataList.add(weatherDataContent);
			}
				break;
			default:
				break;
			}
		}

		weatherMsg.setWeatherDatas(weatherDataList);
		museumMsg.setMsgBody(weatherMsg);
		String sendJson = JSONUtil.serialize(museumMsg);
		log.info("气象站数据==\r\n" + sendJson);
		MuseumGateway.redisMQPushSender.sendNettyToEngine(sendJson);
	}

}
