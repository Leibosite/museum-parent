package com.qingruan.museum.netty.task;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.qingruan.museum.framework.util.PropertiesUtils;
import com.qingruan.museum.netty.protocol.down.ThDownDataPackage;

import lombok.extern.slf4j.Slf4j;
/**
 * 
 * @author jcy
 * @description:
 * 定时发送读取恒温恒湿展柜温湿度任务
 */

@Slf4j
public class THTimeTask {
	
	private static Timer timer;
	private static TimerTask task;
	
	public static void runTimeTask(){
		System.out.println("start read temp humi device value task");
		timer = new Timer();
		task  = new TimerTask() {
			
			@Override
			public void run() {
				readValueAction();
			}
		};
		
		try {
			long timeInterval = Long.valueOf(PropertiesUtils.readValue("thclient.read.timeinterval")) * 1000;
			timer.schedule(task, timeInterval,timeInterval);
			log.info("读取恒温恒湿展柜的定时任务启动");
		} catch (Exception e) {
			log.error(e.getMessage());
		}	
	}
	
	public static void readValueAction(){
		//TODO 定时发送读取恒温恒湿展柜温湿度
		try {
			final List<HashMap<String,Object>> clientList = PropertiesUtils.readIpAddressesFromConfigFile("thclient.addr.urls");
			for (HashMap<String, Object> gatewayInfo : clientList) {
				String ipAddress = gatewayInfo.get("ip").toString()+":"+gatewayInfo.get("port").toString();
				ThDownDataPackage downDataPackage = new ThDownDataPackage(ipAddress);
				
				/*ConstantThMsg msgCmd = new ConstantThMsg();
				
				msgCmd.setThMsgType(ThMsgType.TEXT);
				msgCmd.setThAppType(ThAppType.ENGINE_WRITE_TH_PRESET_DATA);
				
				ThData thData = new ThData();
				List<ThDataContent> list = new ArrayList<ThDataContent>();
				
				ThDataContent thDataContent1 = new ThDataContent();
				thDataContent1.setMonitorDataType(MonitorDataType.TEMPERATURE);
				thDataContent1.setShowCase(ShowCase.FIRST);
				thDataContent1.setValue(25.5);
				list.add(thDataContent1);
				
				ThDataContent thDataContent2 = new ThDataContent();
				thDataContent2.setMonitorDataType(MonitorDataType.HUMIDITY);
				thDataContent2.setShowCase(ShowCase.FIRST);
				thDataContent2.setValue(47.7);
				list.add(thDataContent2);
				
				thData.setDatas(list);
				
				ThNetInfos thNetInfos = new ThNetInfos();
				thNetInfos.setPlcIpv4Addr(ipAddress);
				thData.setThNetInfos(thNetInfos);
				
				msgCmd.setData(thData);	
				downDataPackage.writeTHServerPresetValue(msgCmd);*/
				
				//downDataPackage.readTHServerMeasureValue();
				downDataPackage.readTHServerPresetValue();
				
			}
		} catch (Exception e) {
			log.error("发送定时读取任务error:"+e.getMessage());
		}
		
	}
	
	public static void stopTimeTask(){
		if(task!=null){
			task.cancel();
		}
		
		if(timer!=null){
			timer.cancel();
		}
	}
}
