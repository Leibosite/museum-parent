package com.qingruan.museum.netty.task;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;

import com.qingruan.museum.netty.protocol.down.AirDownDataPackage;
import com.qingruan.museum.netty.tcpserver.CommonData;
/**
 * 
 * @author jcy
 * @description:
 * 空气净化器定时任务，固定时间要向空气净化器下发设备查询指令，保证空气净化器处于联网状态
 */
@Slf4j
public class AirCleanerTask {
	private static Timer timer;
	private static TimerTask task;
	private static AirDownDataPackage air;
	public static void runTimeTask(){
		
		System.out.println("start air cleaner response task");
		air = new AirDownDataPackage();
		timer = new Timer();
		task  = new TimerTask() {
			
			@Override
			public void run() {
				readValueAction();
			}

		};
		
		try {
			
			timer.schedule(task, 3000,3000);
		} catch (Exception e) {
			log.error(e.getMessage());
		}	
	}
	
	public static void readValueAction() {
		try {
			ConcurrentHashMap<String, Channel> macChannel = CommonData.airCleanerChannels;
			for (Map.Entry<String, Channel> m : macChannel.entrySet()) {
				air.remoteTest(0, m.getKey());
			}
		} catch (Exception e) {
			log.error(e.toString());
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
