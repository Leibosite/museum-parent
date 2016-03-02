package com.qingruan.museum.netty.task;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.qingruan.museum.gateway.MuseumGateway;
import com.qingruan.museum.msg.MsgHeader;
import com.qingruan.museum.msg.MsgHeader.DeliveryMode;
import com.qingruan.museum.msg.MsgProperty;
import com.qingruan.museum.msg.MsgProperty.ApplicationType;
import com.qingruan.museum.msg.MsgProperty.CmdType;
import com.qingruan.museum.msg.MuseumMsg;
import com.qingruan.museum.msg.sensor.MtmNetInfo;
import com.qingruan.museum.msg.sensor.SensorData;
import com.qingruan.museum.msg.sensor.SensorMsg;
import com.qingruan.museum.msg.sensor.SensorMsg.SensorAppType;
import com.qingruan.museum.msg.sensor.SensorMsg.SensorMsgType;
import com.qingruan.museum.msg.sensor.SensorNetInfo;
import com.qingruan.museum.netty.protocol.down.DownDataPackage;
import com.qingruan.museum.netty.protocol.up.ResolveData;
import com.qingruan.museum.netty.tcpserver.CommonData;
/**
 * 
 * @author jcy
 * @description:
 * 定时下发时间同步指令
 */
public class ScheduleTask extends TimerTask{
		
	public static void startScheduleTask() {
		System.out.println("start time synchronization task");
		Timer timer = new Timer();
		timer.schedule(new ScheduleTask(), 1000*15, 1000*15);//在0秒后执行此任务,每次间隔一天,1000*60*60*24

	}

	@Override
	public void run() {
		
		//服务器下发时间同步任务
		Set<String> devicesKeys = CommonData.deviceChannels.keySet();
		for (String string : devicesKeys) {
			DownDataPackage downSendData = new DownDataPackage("01", string);
			ResolveData.addDownTask(downSendData);
		}
		
		//sendCheckStatus();
		//sendGetHistoryData();
	}
	
	//模拟数据方法
	public static void sendCheckStatus(){
		String stationMacAddress = "8086F25FD5D8";
		DownDataPackage downSendData = new DownDataPackage("07", stationMacAddress);
		ResolveData.addDownTask(downSendData);
	}
	//模拟数据方法
	public static void sendGetHistoryData(){
		String stationMacAddress = "8086F25FD5D8";
		DownDataPackage downSendData = new DownDataPackage("08", stationMacAddress);
		downSendData.setBeaconMacAddress("DA6C3B27BAB3");
		ResolveData.addDownTask(downSendData);
	}
	//模拟数据方法
	public static void sendMacList(){
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
		sensorMsg.setSensorAppType(SensorAppType.ENGINE_POST_SENSOR_MAC_LIST);
		
		SensorData data = new SensorData();
		
		
		MtmNetInfo mtmNetInfos = new MtmNetInfo();
		mtmNetInfos.setStationMacAddr("48022A25CBB2");
		
		List<SensorNetInfo> sensorNets = new ArrayList<SensorNetInfo>();
		String[] maclist = {"DA6C3B27BAB3","C3CEAC21B9D8","E4B0D98D945D","E406EEFA1A52","FAD18D8A79DC","DB124DA3C2F6"};
		for (int i = 0; i < maclist.length; i++) {
			SensorNetInfo infos = new SensorNetInfo();
			infos.setSensorMacAddr(maclist[i]);
			infos.setMinor("23");
			infos.setMajor("49");
			sensorNets.add(infos);
			
		}
		mtmNetInfos.setSensorNetInfos(sensorNets);
		data.setMtmNetInfo(mtmNetInfos);
		sensorMsg.setData(data);
		museumMsg.setMsgBody(sensorMsg);
		
		Gson gson = new Gson();
		Type type = new TypeToken<MuseumMsg>() {
			private static final long serialVersionUID = 1L;
		}.getType();
		String jsonString = gson.toJson(museumMsg,type);
		
		MuseumGateway.redisMQPushSender.sendEngineToNetty(jsonString);
		
	}
}
