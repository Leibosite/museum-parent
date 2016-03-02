package com.qingruan.museum.netty.protocol.up;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import com.qingruan.museum.domain.models.enums.MonitorDataType;
import com.qingruan.museum.framework.jackson.JSONUtil;
import com.qingruan.museum.gateway.MuseumGateway;
import com.qingruan.museum.gateway.common.exception.ExceptionLog;
import com.qingruan.museum.msg.MsgHeader;
import com.qingruan.museum.msg.MsgProperty;
import com.qingruan.museum.msg.MuseumMsg;
import com.qingruan.museum.msg.MsgHeader.DeliveryMode;
import com.qingruan.museum.msg.MsgProperty.ApplicationType;
import com.qingruan.museum.msg.MsgProperty.CmdType;
import com.qingruan.museum.msg.aircleaner.AirCleanerDataContent;
import com.qingruan.museum.msg.aircleaner.AirCleanerMsg;
import com.qingruan.museum.netty.tcpserver.CommonData;

import io.netty.channel.Channel;
/**
 * 
 * @author jcy
 * @description:
 * 解析空气净化器数据，具体解析详情其参照通信协议文档
 */
@Slf4j
public class ResolveAirCleanerData {
	
	public static void resolveAirCleanerData(Channel channel,String msg){
		
		try {
			
			Long timestamp  = CommonData.airCleanerCounter.get(channel);

			Long currentTimestamp = System.currentTimeMillis();
			if(currentTimestamp - timestamp < 5*60*1000){
				return;
			}else{
				CommonData.airCleanerCounter.put(channel, currentTimestamp);
			}
			
			String macAddress = msg.substring(0, 12);
			String msgData    = msg.substring(12);
			int length = msgData.length();
			if(length==34*2){
				String data = msgData.substring(32, length-2);
				
				//int online = Integer.valueOf(data.substring(0, 2),16);
				//int fanSpeed = Integer.valueOf(data.substring(2, 4),16);
				//int mode = Integer.valueOf(data.substring(4, 6),16);
				//int anion = Integer.valueOf(data.substring(6, 8),16);
				//int uv   = Integer.valueOf(data.substring(8, 10), 16);
				//int timer_shutdown_min = Integer.valueOf(data.substring(10, 12), 16);
				//int timer_shutdown_hour = Integer.valueOf(data.substring(12, 14), 16);
				//int humidification = Integer.valueOf(data.substring(14, 16), 16);
				//int childlock = Integer.valueOf(data.substring(16, 18), 16);
				int air_quality = Integer.valueOf(data.substring(18, 22), 16);
				int temperature = Integer.valueOf(data.substring(22, 24), 16);
				//int TVOC = Integer.valueOf(data.substring(24, 26), 16);
				int humidity = Integer.valueOf(data.substring(26, 28), 16);
				//int fliter_life = Integer.valueOf(data.substring(28, 32), 16);
				//int error_code = Integer.valueOf(data.substring(32, 34), 16);
				
				MuseumMsg museumMsg = new MuseumMsg();
				
				MsgHeader header = new MsgHeader();
				header.setDeliveryMode(DeliveryMode.PTP);
				header.setTimeStamp(System.currentTimeMillis());
				museumMsg.setMsgHeader(header);
				
				MsgProperty msgProperty = new MsgProperty();
				msgProperty.setApplicationType(ApplicationType.AIR_CLEANER);
				msgProperty.setCmdType(CmdType.REQUEST);
				museumMsg.setMsgProperty(msgProperty);
				
				AirCleanerMsg airCleanerMsg = new AirCleanerMsg();
				airCleanerMsg.setStationMacAddr(macAddress);
				airCleanerMsg.setTimestamp(System.currentTimeMillis());
				
				List<AirCleanerDataContent> list = airCleanerMsg.getAirCleanerDatas();
				AirCleanerDataContent airQuality = new AirCleanerDataContent();
				airQuality.setMonitorDataType(MonitorDataType.PM2_5);
				airQuality.setValue(air_quality*1.0);
				list.add(airQuality);
				
				AirCleanerDataContent temper = new AirCleanerDataContent();
				temper.setMonitorDataType(MonitorDataType.TEMPERATURE);
				temper.setValue(temperature*1.0 - 128.0);
				list.add(temper);
				
				AirCleanerDataContent humi = new AirCleanerDataContent();
				humi.setMonitorDataType(MonitorDataType.HUMIDITY);
				humi.setValue(humidity*1.0);
				list.add(humi);
				
				airCleanerMsg.setAirCleanerDatas(list);
				museumMsg.setMsgBody(airCleanerMsg);
				String json = JSONUtil.serialize(museumMsg);
				MuseumGateway.redisMQPushSender.sendNettyToEngine(json);
			}
		} catch (Exception e) {
			log.error(ExceptionLog.getErrorStack(e));
		}	
	}
}
