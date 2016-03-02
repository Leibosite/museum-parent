package com.qingruan.museum.netty.protocol.up;

import io.netty.channel.Channel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import lombok.extern.slf4j.Slf4j;

import com.qingruan.museum.domain.models.enums.MonitorDataType;
import com.qingruan.museum.framework.jackson.JSONUtil;
import com.qingruan.museum.framework.util.FileUtils;
import com.qingruan.museum.gateway.MuseumGateway;
import com.qingruan.museum.gateway.common.netty.SendMessage;
import com.qingruan.museum.msg.MsgHeader;
import com.qingruan.museum.msg.MsgHeader.DeliveryMode;
import com.qingruan.museum.msg.MsgProperty;
import com.qingruan.museum.msg.MsgProperty.ApplicationType;
import com.qingruan.museum.msg.MsgProperty.CmdType;
import com.qingruan.museum.msg.MuseumMsg;
import com.qingruan.museum.msg.sensor.MtmNetInfo;
import com.qingruan.museum.msg.sensor.PatchData;
import com.qingruan.museum.msg.sensor.SensorData;
import com.qingruan.museum.msg.sensor.SensorDataContent;
import com.qingruan.museum.msg.sensor.SensorMsg;
import com.qingruan.museum.msg.sensor.SensorMsg.SensorAppType;
import com.qingruan.museum.msg.sensor.SensorMsg.SensorMsgType;
import com.qingruan.museum.msg.sensor.SensorNetInfo;
import com.qingruan.museum.netty.model.UpDataPackage;
import com.qingruan.museum.netty.protocol.down.DownDataPackage;
import com.qingruan.museum.netty.tcpserver.CommonData;

/**
 * 
 * @author jcy
 * @description:
 * 解析树莓派版本协议上传的数据
 */
@Slf4j
public class ResolveData {
	
	private static final  String SUCCESS = "01";
	private static final  String ERROR = "02";
	private static final  String NOCMD = "03";
	
	public static void judgeCommand(UpDataPackage msg){
		switch(msg.getCommand().toUpperCase()){
			case "FF":
				requestCommand(msg);
				break;
			case "FE":
				requestMacAddressList(msg);
				break;
			case "FD":
				responseSensorData(msg);
				break;
			case "FC":
				requestUpgradeDataPackage(msg);
				break;
			case "FB":
				responseUpgradeStatus(msg);
				break;
			case "FA":
				responseCheckLiveDevice(msg);
				break;
			case "F9":
				responseSynchronousTimeStatus(msg);
				break;
			case "F8":
				responseHistorySensorData(msg);
				break;
			default: 
				break;
		}
	}
	
	public static void requestCommand(UpDataPackage msg){
		
		String macAddress = msg.getMonitorMacAddress();
		//String msgData = msg.getMsgBody();
		ConcurrentLinkedQueue<DownDataPackage> queue = CommonData.downTaskMap.get(macAddress);
		if(queue==null){
			responseCmdConfirm(msg,NOCMD);
			return;
		}
		
		//DownDataPackage data = queue.peek();
		DownDataPackage data = queue.poll();
		if(data!=null){
//			if(msg.getSequenceNumber().equals(data.getSequenceNumber())){
//				queue.poll();
//				data = queue.peek();
//				if(data==null){
//					response(msg,NOCMD);
//					return;
//				}
//			}
			Channel channel = CommonData.deviceChannels.get(macAddress);
			data.connectPackage();
			SendMessage.sendMsg(channel, data.getDownSendData());
		}else{
			responseCmdConfirm(msg,NOCMD);
		}
		
	}
	
	//中继向服务器发送中继同步成功的Beacon,TODO 现在直接回复正确就可以，以后会扩展时再处理
	private static void responseSynchronousTimeStatus(UpDataPackage msg){
		//String msgData = msg.getMsgBody();
		try {
			/*
			int successNumber = Integer.valueOf(msgData.substring(0, 4), 16);
			
			ArrayList<String> list = new ArrayList<String>();
			for(int i=0;i<successNumber;i++){
				String beaconMacAddress = msgData.substring(4+i*12, 4+12*(i+1));
				list.add(beaconMacAddress);
			}
			*/
			//TODO 可能会把同步时间服务成功的Beacon传给Engine
			
			responseCmdConfirm(msg,SUCCESS);
		} catch (Exception e) {
			responseCmdConfirm(msg, ERROR);
		}
		
	}
	
	//根据中继的MacAddress,Engine返回此中继下面管辖的Beacon的List
	private static void requestMacAddressList(UpDataPackage msg){
		String monitorMacAddress = msg.getMonitorMacAddress();
		try {
			MuseumMsg museumMsg = new MuseumMsg();
			
			MsgHeader header = new MsgHeader();
			header.setDeliveryMode(DeliveryMode.PTP);
			header.setTimeStamp(System.currentTimeMillis());
			museumMsg.setMsgHeader(header);
			
			MsgProperty msgProperty = new MsgProperty();
			msgProperty.setApplicationType(ApplicationType.SENSOR);
			msgProperty.setCmdType(CmdType.REQUEST);
			museumMsg.setMsgProperty(msgProperty);
			
			SensorMsg sensorMsg = new SensorMsg();
			sensorMsg.setSensorMsgType(SensorMsgType.TEXT);
			sensorMsg.setSensorAppType(SensorAppType.GATEWAY_GET_SENSOR_MAC_LIST);
			
			SensorData sensorData = new SensorData();
			
			MtmNetInfo mtmNetInfos = new MtmNetInfo();
			mtmNetInfos.setStationMacAddr(monitorMacAddress);
			
			sensorData.setMtmNetInfo(mtmNetInfos);
			
			sensorMsg.setData(sensorData);
			museumMsg.setMsgBody(sensorMsg);
			
			String jsonData = JSONUtil.serialize(museumMsg);
			sendMessageToEngine(jsonData);		
			//ModbusTask.sendMacList();
			responseCmdConfirm(msg,SUCCESS);
		} catch (Exception e) {
			responseCmdConfirm(msg,ERROR);
			log.error(e.getMessage());
		}
		
	}
	
	//响应传感器的数据上报
	private static void responseSensorData(UpDataPackage msg){
		
		String sensorData = msg.getMsgBody();
		String checkCode = sensorData.substring(0, 2); //验证号
		int packageNumber = Integer.valueOf(sensorData.substring(2, 4), 16); //数据报个数,其实是多少个Beacon同时上传数据
		try {
			
			for (int i = 0,j = 0; i < packageNumber; i++) {
				
				String beaconMacAddress = sensorData.substring(j+4, j+4+12);
				String rssi				= sensorData.substring(j+4+12, j+4+12+2);
				String timestamp		= sensorData.substring(j+4+12+2, j+4+12+2+8);
				int    dataLength 		= Integer.valueOf(sensorData.substring(j+4+12+2+8, j+4+12+2+8+2), 16);
				String tempData = sensorData.substring(j+4+12+2+8+2+10, j+4+12+2+8+2+dataLength*2);
				
				MuseumMsg museumMsg = resolveSensorData(tempData, msg.getMonitorMacAddress(),beaconMacAddress,rssi,timestamp);
				String domainJson = JSONUtil.serialize(museumMsg);
				log.info("netty to engine msg:}" + domainJson);
				sendMessageToEngine(domainJson);
				j = j+4+12+2+8+2+dataLength*2;
			}			
			responseDataConfirm(msg, SUCCESS,checkCode);
		} catch (Exception e) {
			responseDataConfirm(msg, ERROR,checkCode);
			log.error(e.getMessage());
		}
	}
	
	//响应传感器上报的历史数据
	public static void responseHistorySensorData(UpDataPackage msg){
		try {
			String beaconData = msg.getMsgBody();
			String beaconMacAddress =  beaconData.substring(0, 12);
			int packageNumber = Integer.valueOf(beaconData.substring(12, 14), 16);
			String beaconPackages = beaconData.substring(14, beaconData.length()-2);
			for (int i = 0,j=0; i < packageNumber;i++) {
				int dataLength = Integer.parseInt(beaconPackages.substring(j, j+2), 16);
				String tempData = beaconPackages.substring(j+2, j+2+dataLength*2);
				
				StringBuilder sensorData = new StringBuilder(tempData);
				sensorData.insert(0, "00");
				sensorData.append("00000000");
				MuseumMsg museumMsg = resolveSensorData(sensorData.toString(), msg.getMonitorMacAddress(),beaconMacAddress,"00","00000000");
				String domainJson = JSONUtil.serialize(museumMsg);
				log.info("netty to engine msg:"+i+"}" + domainJson);
				sendMessageToEngine(domainJson);
				
				j = j + 2 + dataLength*2;
			}
			responseCmdConfirm(msg,SUCCESS);
		} catch (Exception e) {
			responseCmdConfirm(msg,ERROR);
			log.error("ResolveData has Exception: "+e.toString());
		}
		
	}
	
	public static void requestUpgradeDataPackage(UpDataPackage msg){
		DownDataPackage downPackage = new DownDataPackage("04", msg.getMonitorMacAddress());
		try {
			
			//TODO 这里的Hex的升级数据包需要从Redis中获取
			
			String filePath = "/opt/nettyserver/ble_app_beacon.hex";
			//String filePath = "/Users/jcy/Desktop/ble_app_beacon.hex";
			FileUtils util = new FileUtils(filePath);
			String msgData = msg.getMsgBody();
			//String beaconMacAddress = msgData.substring(0, 12);
			int idNumber = Integer.valueOf(msgData.substring(12, 16), 16);
			PatchData data = new PatchData();
			data.setIdNumber(Integer.valueOf(msgData.substring(12, 16), 16));
			data.setTotalPackageNumber(util.getPackageNumber());
			byte[] upgradeData = util.readPackageData(idNumber);
			data.setPackageSize(upgradeData.length);
			data.setUpgradeData(upgradeData);	
			downPackage.setUpgradeData(data);
			addDownTask(downPackage);
			util.endFileOperate();
			responseCmdConfirm(msg,SUCCESS);
		} catch (Exception e) {
			responseCmdConfirm(msg,ERROR);
			log.error(e.getMessage());
		}
		
	}
	
	//中继返回Beacon升级的状态
	public static void responseUpgradeStatus(UpDataPackage msg){
		
		//TODO 向engine发送Beacon的升级状态 需要和朱怀琦对接
		String msgData = msg.getMsgBody();
		
		try {
			MuseumMsg museumMsg = new MuseumMsg();
			
			MsgHeader header = new MsgHeader();
			header.setDeliveryMode(DeliveryMode.PTP);
			header.setTimeStamp(System.currentTimeMillis());
			museumMsg.setMsgHeader(header);
			
			MsgProperty msgProperty = new MsgProperty();
			msgProperty.setApplicationType(ApplicationType.SENSOR);
			msgProperty.setCmdType(CmdType.ANSWER);
			museumMsg.setMsgProperty(msgProperty);
			
			SensorMsg sensorMsg = new SensorMsg();
			sensorMsg.setSensorMsgType(SensorMsgType.TEXT);
			sensorMsg.setSensorAppType(SensorAppType.GATEWAY_POST_SENSOR_STATUS);
			
			SensorData sensorData = new SensorData();
			MtmNetInfo mtmNetInfos = new MtmNetInfo();
			mtmNetInfos.setStationMacAddr(msg.getMonitorMacAddress());
			
			int upgradeSuccessNumber = Integer.valueOf(msgData.substring(0, 4), 16);
			List<SensorNetInfo> sensorNets = new ArrayList<SensorNetInfo>();
			for (int i = 0; i < upgradeSuccessNumber; i++) {
				SensorNetInfo sensorNetInfos = new SensorNetInfo();
				String temp = msgData.substring(4+i*12, 16+i*12);
				sensorNetInfos.setSensorMacAddr(temp);
				sensorNets.add(sensorNetInfos);
			}
			mtmNetInfos.setSensorNetInfos(sensorNets);
			sensorData.setMtmNetInfo(mtmNetInfos);
			sensorMsg.setData(sensorData);
			museumMsg.setMsgBody(sensorMsg);
			
			String sendJson = JSONUtil.serialize(museumMsg);
			sendMessageToEngine(sendJson);
			responseCmdConfirm(msg,SUCCESS);
			
		} catch (Exception e) {
			responseCmdConfirm(msg,ERROR);
			log.error(e.getMessage());
		}
		
	}
	
	public static void addDownTask(DownDataPackage downPackage){
		String monitorMacAddress = downPackage.getMonitorMacAddress();
		if (!CommonData.downTaskMap.containsKey(monitorMacAddress)) {
			ConcurrentLinkedQueue<DownDataPackage> queue = new ConcurrentLinkedQueue<DownDataPackage>();
			CommonData.downTaskMap.put(monitorMacAddress, queue);
		}
		ConcurrentLinkedQueue<DownDataPackage> queue = CommonData.downTaskMap.get(monitorMacAddress);
		queue.offer(downPackage);
	}
	
	private static void responseCmdConfirm(UpDataPackage msg,String responseCode) {
		String macAddress = msg.getMonitorMacAddress();
		DownDataPackage data = new DownDataPackage("06", macAddress);
		data.setStatusCode(responseCode);
		data.connectPackage();
		Channel channel = CommonData.deviceChannels.get(macAddress);
		SendMessage.sendMsg(channel, data.getDownSendData());
	}
	
	private static void responseDataConfirm(UpDataPackage msg,String responseCode,String checkCode) {
		String macAddress = msg.getMonitorMacAddress();
		DownDataPackage data = new DownDataPackage("05", macAddress);
		data.setStatusCode(responseCode);
		data.setCheckNumber(checkCode);
		data.connectPackage();
		Channel channel = CommonData.deviceChannels.get(macAddress);
		SendMessage.sendMsg(channel, data.getDownSendData());
	}
	

	private static void sendMessageToEngine(String jsonMessage){
		MuseumGateway.redisMQPushSender.sendNettyToEngine(jsonMessage);
	}
	
	//中继向服务器返回传感器的存活状态, TODO 需要和朱怀琦对接
	private static void responseCheckLiveDevice(UpDataPackage msg){
		String msgData = msg.getMsgBody();
		try {
			int liveBeaconNumber = Integer.valueOf(msgData.substring(0, 4), 16);
			
			MuseumMsg museumMsg = new MuseumMsg();
			
			MsgHeader header = new MsgHeader();
			header.setDeliveryMode(DeliveryMode.PTP);
			header.setTimeStamp(System.currentTimeMillis());
			museumMsg.setMsgHeader(header);
			
			MsgProperty msgProperty = new MsgProperty();
			msgProperty.setApplicationType(ApplicationType.SENSOR);
			msgProperty.setCmdType(CmdType.ANSWER);
			museumMsg.setMsgProperty(msgProperty);
			
			SensorMsg sensorMsg = new SensorMsg();
			sensorMsg.setSensorMsgType(SensorMsgType.TEXT);
			sensorMsg.setSensorAppType(SensorAppType.GATEWAY_POST_SENSOR_STATUS);
			
			
			SensorData sensorData = new SensorData();
			sensorData.setTimeStamp(System.currentTimeMillis());
			
			MtmNetInfo mtmNetInfos = new MtmNetInfo();
			mtmNetInfos.setStationMacAddr(msg.getMonitorMacAddress());
			
			
			List<SensorNetInfo> sensorNets = new ArrayList<SensorNetInfo>();
			
			for(int i=0;i<liveBeaconNumber;i++){
				SensorNetInfo sensorNetInfos = new SensorNetInfo();
				String beaconMacAddress = msgData.substring(4+22*i, (4+12)+22*i);
				long timeStamp        = Long.valueOf(msgData.substring(4+12+22*i, 4+12+8+22*i), 16);
				int rssi				= Integer.valueOf(msgData.substring(4+12+8+22*i, 4+12+8+2+22*i), 16);
				sensorNetInfos.setSensorMacAddr(beaconMacAddress);
				sensorNetInfos.setTimeStamp(timeStamp);
				sensorNetInfos.setRssi(rssi);
				sensorNets.add(sensorNetInfos);
			}
			mtmNetInfos.setSensorNetInfos(sensorNets);
			sensorData.setMtmNetInfo(mtmNetInfos);
			sensorMsg.setData(sensorData);
			museumMsg.setMsgBody(sensorMsg);
			String jsonData = JSONUtil.serialize(museumMsg);
			sendMessageToEngine(jsonData);
			responseCmdConfirm(msg, SUCCESS);
		} catch (Exception e) {
			responseCmdConfirm(msg, ERROR);
			log.error(e.getMessage());
		}
	}
	
	
	/**
	 * 此函数的功能是把中继上报数据切割成，中继Mac地址，Beacon Mac地址，时间戳，数据体，响应码
	 * 
	 */
	public static MuseumMsg resolveSensorData(String sensorData,String stationMacAddress,
			String beaconMacAddress,String rssi,String timestamp) 
			throws Exception{
			
		MuseumMsg museumMsg = new MuseumMsg();
		
		MsgHeader header = new MsgHeader();
		header.setDeliveryMode(DeliveryMode.PTP);
		header.setTimeStamp(System.currentTimeMillis());
		museumMsg.setMsgHeader(header);
		
		MsgProperty msgProperty = new MsgProperty();
		msgProperty.setApplicationType(ApplicationType.SENSOR);
		msgProperty.setCmdType(CmdType.ANSWER);
		museumMsg.setMsgProperty(msgProperty);
		
		SensorMsg sensorMsg = new SensorMsg();
		sensorMsg.setSensorMsgType(SensorMsgType.TEXT);
		sensorMsg.setSensorAppType(SensorAppType.GATEWAY_POST_SENSOR_DATA);

		
		SensorData sensorInfo = new SensorData();
		MtmNetInfo mtmNetInfos = new MtmNetInfo();
		mtmNetInfos.setStationMacAddr(stationMacAddress);
		sensorInfo.setMtmNetInfo(mtmNetInfos);
		
		double power = Integer.valueOf(sensorData.substring(38, 40), 16)*1.0;
		sensorInfo.setPower(power);
		sensorInfo.setTimeStamp(Long.valueOf(timestamp, 16));
		
		SensorNetInfo sensorNetInfos = new SensorNetInfo();
		sensorNetInfos.setSensorMacAddr(beaconMacAddress);
		sensorNetInfos.setRssi(Integer.valueOf(rssi, 16));
		sensorNetInfos.setTimeStamp(Long.valueOf(sensorData.substring(30, 38), 16));
		sensorNetInfos.setMajor(sensorData.substring(40, 44));
		sensorNetInfos.setMinor(sensorData.substring(44, 48));
		
		sensorInfo.setSensorNetInfo(sensorNetInfos);
		
		List<SensorDataContent> sensorDataContents = new ArrayList<SensorDataContent>();		
		String cmdString = sensorData.substring(4, 8);
		
		switch (cmdString) {
			case "0101":
			{
				setTemperature(sensorData, sensorDataContents);
				setHumidity(sensorData, sensorDataContents);
			}
				break;
			case "0102":
			{
				setUv(sensorData, sensorDataContents);
			}	
				break;
			case "0103":
			{
				setTemperature(sensorData, sensorDataContents);
				setHumidity(sensorData, sensorDataContents);
				setUv(sensorData, sensorDataContents);
			}				
				break;
			case "0104":
			{
				setLight(sensorData, sensorDataContents);
			}				
				break;
			case "0105":
			{
				setTemperature(sensorData, sensorDataContents);
				setHumidity(sensorData, sensorDataContents);
				setLight(sensorData, sensorDataContents);
			}				
				break;
			case "0106":
			{
				setUv(sensorData, sensorDataContents);
				setLight(sensorData, sensorDataContents);
			}				
				break;
			case "0107":
			{
				setTemperature(sensorData, sensorDataContents);
				setHumidity(sensorData, sensorDataContents);
				setUv(sensorData, sensorDataContents);
				setLight(sensorData, sensorDataContents);
			}				
				break;
			case "0201":
			{
				setPM10(sensorData, sensorDataContents);
				setPM2_5(sensorData, sensorDataContents);
				setPM1_0(sensorData, sensorDataContents);
				setUM_01_25(sensorData, sensorDataContents);
				setUM_01_100(sensorData, sensorDataContents);
			}			
				break;
			case "0202":
			{
				setCH2O(sensorData, sensorDataContents);
			}
				break;
			case "0203":
			{
				setSO2(sensorData, sensorDataContents);
			}
				break;
			case "0204":
			{
				setTVOC(sensorData, sensorDataContents);
			}
				break;
			case "0205":
			{
				setCO2(sensorData, sensorDataContents);
			}
				break;
			case "0206":
			{
				setCO2(sensorData, sensorDataContents);
				setUnion_Temperature(sensorData, sensorDataContents);
				setUnion_Humidity(sensorData, sensorDataContents);
			}
				break;
			case "0207":
			{
				setMicroWindSpeed(sensorData, sensorDataContents);
			}	
				break;
			default:
				break;
		}
		sensorInfo.setDatas(sensorDataContents);
		sensorMsg.setData(sensorInfo);
		museumMsg.setMsgBody(sensorMsg);
			
		return museumMsg;
	}
	
	public static void setTemperature(String sensorData,List<SensorDataContent> sensorDataContents){
		SensorDataContent content = new SensorDataContent();
		double temperature = Integer.valueOf(sensorData.substring(8, 12), 16)/100.0;
		content.setMonitorDataType(MonitorDataType.TEMPERATURE);
		content.setValue(temperature);
		content.setCheckCode(Integer.valueOf(sensorData.substring(48,50), 16));
		sensorDataContents.add(content);
	}
	
	public static void setHumidity(String sensorData,List<SensorDataContent> sensorDataContents){
		SensorDataContent content = new SensorDataContent();
		double  humidity   = Integer.valueOf(sensorData.substring(12, 16), 16)/100.0;
		content.setMonitorDataType(MonitorDataType.HUMIDITY);
		content.setValue(humidity);
		content.setCheckCode(Integer.valueOf(sensorData.substring(48,50), 16));
		sensorDataContents.add(content);
	}
	
	public static void setUv(String sensorData,List<SensorDataContent> sensorDataContents){
		SensorDataContent content = new SensorDataContent();
		double uv = Integer.valueOf(sensorData.substring(16, 20), 16)*1.0;
		content.setMonitorDataType(MonitorDataType.UV);
		content.setValue(uv);
		content.setCheckCode(Integer.valueOf(sensorData.substring(48,50), 16));
		sensorDataContents.add(content);
	}
	
	public static void setLight(String sensorData,List<SensorDataContent> sensorDataContents){
		SensorDataContent content = new SensorDataContent();
		double light = Integer.valueOf(sensorData.substring(20, 24), 16)*1.0;
		content.setMonitorDataType(MonitorDataType.LIGHTING);
		content.setValue(light);
		content.setCheckCode(Integer.valueOf(sensorData.substring(48,50), 16));
		sensorDataContents.add(content);
	}
	
	public static void setPM10(String sensorData,List<SensorDataContent> sensorDataContents){
		SensorDataContent content = new SensorDataContent();
		double pm10 = Integer.valueOf(sensorData.substring(8, 12), 16)*1.0;
		content.setMonitorDataType(MonitorDataType.PM10);
		content.setValue(pm10);
		content.setCheckCode(Integer.valueOf(sensorData.substring(48,50), 16));
		sensorDataContents.add(content);
	}
	
	public static void setPM2_5(String sensorData,List<SensorDataContent> sensorDataContents){
		SensorDataContent content = new SensorDataContent();
		double pm2_5 = Integer.valueOf(sensorData.substring(12, 16), 16)*1.0;
		content.setMonitorDataType(MonitorDataType.PM2_5);
		content.setValue(pm2_5);
		content.setCheckCode(Integer.valueOf(sensorData.substring(48,50), 16));
		sensorDataContents.add(content);
	}
	
	public static void setPM1_0(String sensorData,List<SensorDataContent> sensorDataContents){
		SensorDataContent content = new SensorDataContent();
		double pm1_0 = Integer.valueOf(sensorData.substring(16, 20), 16)*1.0;
		content.setMonitorDataType(MonitorDataType.PM1_0);
		content.setValue(pm1_0);
		content.setCheckCode(Integer.valueOf(sensorData.substring(48,50), 16));
		sensorDataContents.add(content);
	}
	
	public static void setUM_01_25(String sensorData,List<SensorDataContent> sensorDataContents){
		SensorDataContent content = new SensorDataContent();
		double um_01_25 = Integer.valueOf(sensorData.substring(20, 24), 16)*1.0;
		content.setMonitorDataType(MonitorDataType.UM_01_25);
		content.setValue(um_01_25);
		content.setCheckCode(Integer.valueOf(sensorData.substring(48,50), 16));
		sensorDataContents.add(content);
	}
	
	public static void setUM_01_100(String sensorData,List<SensorDataContent> sensorDataContents){
		SensorDataContent content = new SensorDataContent();
		double um_01_100 = Integer.valueOf(sensorData.substring(24, 28), 16)*1.0;
		content.setMonitorDataType(MonitorDataType.PM1_0);
		content.setValue(um_01_100);
		content.setCheckCode(Integer.valueOf(sensorData.substring(48,50), 16));
		sensorDataContents.add(content);
	}
	
	public static void setCH2O(String sensorData,List<SensorDataContent> sensorDataContents){
		SensorDataContent content = new SensorDataContent();
		double ch2o = Integer.valueOf(sensorData.substring(8, 12), 16)/100.0;
		content.setMonitorDataType(MonitorDataType.CH2O);
		content.setValue(ch2o);
		content.setCheckCode(Integer.valueOf(sensorData.substring(48,50), 16));
		sensorDataContents.add(content);
	}
	
	public static void setSO2(String sensorData,List<SensorDataContent> sensorDataContents){
		SensorDataContent content = new SensorDataContent();
		double so2 = Integer.valueOf(sensorData.substring(8, 12), 16)/100.0;
		content.setMonitorDataType(MonitorDataType.SO2);
		content.setValue(so2);
		content.setCheckCode(Integer.valueOf(sensorData.substring(48,50), 16));
		sensorDataContents.add(content);
	}
	
	public static void setTVOC(String sensorData,List<SensorDataContent> sensorDataContents){
		SensorDataContent content = new SensorDataContent();
		double tvoc = Integer.valueOf(sensorData.substring(8, 12), 16)*1.0;
		content.setMonitorDataType(MonitorDataType.TVOC);
		content.setValue(tvoc);
		content.setCheckCode(Integer.valueOf(sensorData.substring(48,50), 16));
		sensorDataContents.add(content);
	}
	
	public static void setCO2(String sensorData,List<SensorDataContent> sensorDataContents){
		SensorDataContent content = new SensorDataContent();
		double co2 = Integer.valueOf(sensorData.substring(8, 12), 16)*1.0;
		content.setMonitorDataType(MonitorDataType.CO2);
		content.setValue(co2);
		content.setCheckCode(Integer.valueOf(sensorData.substring(48,50), 16));
		sensorDataContents.add(content);
	}
	
	public static void setUnion_Temperature(String sensorData,List<SensorDataContent> sensorDataContents){
		SensorDataContent content = new SensorDataContent();
		double temp = Integer.valueOf(sensorData.substring(12, 16), 16)/100.0;
		content.setMonitorDataType(MonitorDataType.TEMPERATURE);
		content.setValue(temp);
		content.setCheckCode(Integer.valueOf(sensorData.substring(48,50), 16));
		sensorDataContents.add(content);
	}
	
	public static void setUnion_Humidity(String sensorData,List<SensorDataContent> sensorDataContents){
		SensorDataContent content = new SensorDataContent();
		double humi = Integer.valueOf(sensorData.substring(16, 20), 16)/100.0;
		content.setMonitorDataType(MonitorDataType.HUMIDITY);
		content.setValue(humi);
		content.setCheckCode(Integer.valueOf(sensorData.substring(48,50), 16));
		sensorDataContents.add(content);
	}
	
	public static void setMicroWindSpeed(String sensorData,List<SensorDataContent> sensorDataContents){
		SensorDataContent content = new SensorDataContent();
		double speed = Integer.valueOf(sensorData.substring(8, 12), 16)/100.0;
		content.setMonitorDataType(MonitorDataType.WIND_SPEED);
		content.setValue(speed);
		content.setCheckCode(Integer.valueOf(sensorData.substring(48,50), 16));
		sensorDataContents.add(content);
	}
		
	/**
	 * BCD　时间转成　2000-00-00形式
	 * 
	 * @param s
	 * @return
	 */
	public static String getDatetime(String datetime) {
		String date = datetime.substring(0, 6);
		String time = datetime.substring(6);
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		String yearString = String.valueOf(year);
		StringBuffer sb = new StringBuffer(yearString.substring(0, 2));
		sb.append(date.substring(0, 2));
		sb.append("-").append(date.substring(2, 4));
		sb.append("-").append(date.substring(4));
		sb.append(" ");
		sb.append(time.substring(0, 2));
		sb.append(":").append(time.substring(2, 4));
		if (datetime.length() == 12) {
			sb.append(":").append(time.substring(4));
		}
		String stime = sb.toString();
		return stime;
	}

	public static String getTimeStamp() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String timeStamp = format.format(new Date());
		return timeStamp;
	}
	
}
