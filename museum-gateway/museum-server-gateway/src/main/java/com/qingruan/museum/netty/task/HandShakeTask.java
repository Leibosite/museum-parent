package com.qingruan.museum.netty.task;

import io.netty.channel.Channel;

import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import com.qingruan.museum.framework.util.EncoderUtil;
import com.qingruan.museum.framework.util.TimeUtil;
import com.qingruan.museum.gateway.common.netty.SendMessage;
import com.qingruan.museum.netty.tcpserver.CommonData;
import com.qingruan.museum.netty.tcpserver.UnPack;

public class HandShakeTask {

	public void startHandShakeTask() {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				 Set<String> keySet = CommonData.deviceChannels.keySet();
				 if(0 != keySet.size()){
					 Iterator<String> iterator = keySet.iterator();
					 
					 while(iterator.hasNext()){
						 String masterGatewayID = iterator.next();
						 sendHandShake(masterGatewayID);
					 }
				 }else{
					 System.out.println("没有设备连接过来");
				 }
				
				
			}
		};
		Timer timer = new Timer();
		long delay = 5000;
		long intevalPeriod = 30 * 60 * 1000;
		timer.scheduleAtFixedRate(task, delay, intevalPeriod);
	}

	private void sendHandShake(String masterGatewayID) {
		System.out.println("向主网关id为"+masterGatewayID+"主动握手");
		String handShakeResult = "00";
		String deviceType = "A0";
		String hexTime = TimeUtil.hexTimeString();
		StringBuilder builder = new StringBuilder();
		builder.append("3A");
		builder.append("80");
		builder.append("000000000000");
		builder.append("000000000000");
		builder.append("0006");
		builder.append(deviceType);
		builder.append(handShakeResult);
		builder.append(hexTime);
		String checkCode = EncoderUtil.int2HexStr(
				UnPack.xor(builder.toString()), 2);
		builder.append(checkCode);
		builder.append("EE");
		Channel channel = CommonData.deviceChannels.get(masterGatewayID);
		SendMessage.sendMsg(channel, builder.toString());
		System.out.println("向主网关id为"+masterGatewayID+"主动握手结束");
	}

}
