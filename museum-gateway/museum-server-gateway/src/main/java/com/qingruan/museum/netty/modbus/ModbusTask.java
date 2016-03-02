package com.qingruan.museum.netty.modbus;

import io.netty.channel.Channel;

import java.util.Timer;
import java.util.TimerTask;

import lombok.extern.slf4j.Slf4j;

import com.qingruan.museum.gateway.common.netty.SendMessage;
import com.qingruan.museum.netty.tcpserver.CommonData;

@Slf4j
public class ModbusTask extends TimerTask{
		
	public static void startScheduleTask() {
		
		Timer timer = new Timer();
		timer.schedule(new ModbusTask(), 1000*30, 1000*30);//在0秒后执行此任务,每次间隔一天,1000*60*60*24

	}

	@Override
	public void run() {
		
		for (Channel channel : CommonData.modbusChannelSet) {
			log.info("query soil device data ... ");
			SendMessage.sendMsg(channel, "030400010003E029");
		}
	}
}
