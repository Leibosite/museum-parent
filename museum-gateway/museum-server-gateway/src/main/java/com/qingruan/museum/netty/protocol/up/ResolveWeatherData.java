package com.qingruan.museum.netty.protocol.up;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import io.netty.channel.Channel;

import com.qingruan.museum.domain.models.enums.MonitorDataType;
import com.qingruan.museum.framework.jackson.JSONUtil;
import com.qingruan.museum.framework.util.TextUtil;
import com.qingruan.museum.framework.util.TimeUtil;
import com.qingruan.museum.gateway.MuseumGateway;
import com.qingruan.museum.msg.MsgHeader;
import com.qingruan.museum.msg.MsgHeader.DeliveryMode;
import com.qingruan.museum.msg.MsgProperty;
import com.qingruan.museum.msg.MsgProperty.ApplicationType;
import com.qingruan.museum.msg.MsgProperty.CmdType;
import com.qingruan.museum.msg.MuseumMsg;
import com.qingruan.museum.msg.weather.WeatherDataContent;
import com.qingruan.museum.msg.weather.WeatherMsg;
/**
 * @deprecated
 * @author jcy
 * @description:
 * 解析老版本的气象站协议，已经弃用，采用新版本的气象站协议解析
 */

@Deprecated
@Slf4j
public class ResolveWeatherData {
	
	public static void resolveWeatherStationData(Channel channel,String msg){
		String weatherDataString= TextUtil.hex2Ascii(msg);
		String weatherData = weatherDataString.substring(2);
		log.info("###气象站数据==="+weatherData);
		String[] dataList = weatherData.split(",");
		
		
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
		
		List<WeatherDataContent> weatherDataList = weatherMsg.getWeatherDatas();
		
		for (String string : dataList) {
			WeatherDataContent weatherDataContent = new WeatherDataContent();
			String[] keyValue = string.split("=");
			
			if(keyValue[1].equals("?")){
				continue;
			}
			
			switch (keyValue[0]) {
				case "macAddress":
				{
					weatherMsg.setStationMacAddr(keyValue[1]);
				}
					break;
				case "temp":
				{
					weatherDataContent.setMonitorDataType(MonitorDataType.TEMPERATURE);
					double data = Integer.valueOf(keyValue[1])/100.0;
					weatherDataContent.setValue(data);
					weatherDataList.add(weatherDataContent);
				}
					break;
				case "humi":
				{
					weatherDataContent.setMonitorDataType(MonitorDataType.HUMIDITY);
					double data = Integer.valueOf(keyValue[1])/100.0;
					weatherDataContent.setValue(data);
					weatherDataList.add(weatherDataContent);
				}
					break;
				case "light":
				{
					weatherDataContent.setMonitorDataType(MonitorDataType.LIGHTING);
					double data = Integer.valueOf(keyValue[1]);
					weatherDataContent.setValue(data);
					weatherDataList.add(weatherDataContent);
				}
					break;
				case "speed":
				{
					weatherDataContent.setMonitorDataType(MonitorDataType.WIND_SPEED);
					double data = Integer.valueOf(keyValue[1]);
					weatherDataContent.setValue(data);
					weatherDataList.add(weatherDataContent);
				}
					break;
				case "dir":
				{
					weatherDataContent.setMonitorDataType(MonitorDataType.WIND_DIRECTION);
					weatherDataContent.setWindDirection(keyValue[1]);
					weatherDataList.add(weatherDataContent);
				}
					break;
				case "pm1.0":
				{
					weatherDataContent.setMonitorDataType(MonitorDataType.PM1_0);
					double data = Integer.valueOf(keyValue[1]);
					weatherDataContent.setValue(data);
					weatherDataList.add(weatherDataContent);
				}
					break;
				case "pm2.5":
				{
					weatherDataContent.setMonitorDataType(MonitorDataType.PM2_5);
					double data = Integer.valueOf(keyValue[1]);
					weatherDataContent.setValue(data);
					weatherDataList.add(weatherDataContent);
				}
					break;
				case "pm10":
				{
					weatherDataContent.setMonitorDataType(MonitorDataType.PM10);
					double data = Integer.valueOf(keyValue[1]);
					weatherDataContent.setValue(data);
					weatherDataList.add(weatherDataContent);
				}
					break;
				case "d>0.3":
				{
					weatherDataContent.setMonitorDataType(MonitorDataType.UM_01_03);
					double data = Integer.valueOf(keyValue[1]);
					weatherDataContent.setValue(data);
					weatherDataList.add(weatherDataContent);
				}
					break;
				case "d>0.5":
				{
					weatherDataContent.setMonitorDataType(MonitorDataType.UM_01_05);
					double data = Integer.valueOf(keyValue[1]);
					weatherDataContent.setValue(data);
					weatherDataList.add(weatherDataContent);
				}
					break;
				case "d>1.0":
				{
					weatherDataContent.setMonitorDataType(MonitorDataType.UM_01_10);
					double data = Integer.valueOf(keyValue[1]);
					weatherDataContent.setValue(data);
					weatherDataList.add(weatherDataContent);
				}
					break;
				case "d>2.5":
				{
					weatherDataContent.setMonitorDataType(MonitorDataType.UM_01_25);
					double data = Integer.valueOf(keyValue[1]);
					weatherDataContent.setValue(data);
					weatherDataList.add(weatherDataContent);
				}
					break;
				case "d>5":
				{
					weatherDataContent.setMonitorDataType(MonitorDataType.UM_01_50);
					double data = Integer.valueOf(keyValue[1])*1.0;
					weatherDataContent.setValue(data);
					weatherDataList.add(weatherDataContent);
				}
					break;
				case "d>10":
				{
					weatherDataContent.setMonitorDataType(MonitorDataType.UM_01_100);
					double data = Integer.valueOf(keyValue[1])*1.0;
					weatherDataContent.setValue(data);
					weatherDataList.add(weatherDataContent);
				}
					break;
				case "Time":
				{
					weatherMsg.setTimestamp(TimeUtil.timestampByHourMinuteSecond(keyValue[1]));
				}
					break;
				case "Lat":
				{
					weatherDataContent.setMonitorDataType(MonitorDataType.LATITUDE);
					String[] latString = keyValue[1].split("\\|");
					double latValue = 0.0;
					if("N".equals(latString[0])){
						latValue = Integer.valueOf(latString[1])/1000000.0;
					}else{
						latValue = 0 - Integer.valueOf(latString[1])/1000000.0;
					}
					weatherDataContent.setValue(latValue);
					weatherDataList.add(weatherDataContent);
				}
					break;
				case "Lon":
				{
					weatherDataContent.setMonitorDataType(MonitorDataType.LONGITUDE);
					String[] lonString = keyValue[1].split("\\|");
					double lonValue = 0.0;
					if("E".equals(lonString[0])){
						lonValue = Integer.valueOf(lonString[1])/1000000.0;
					}else{
						lonValue = 0 - Integer.valueOf(lonString[1])/1000000.0;
					}
					weatherDataContent.setValue(lonValue);
					weatherDataList.add(weatherDataContent);
				}
					break;
				case "Height":
				{
					weatherDataContent.setMonitorDataType(MonitorDataType.HEIGHT);
					double data = Integer.valueOf(keyValue[1])*1.0;
					weatherDataContent.setValue(data);
					weatherDataList.add(weatherDataContent);
				}
					break;
				case "uv":
				{
					weatherDataContent.setMonitorDataType(MonitorDataType.UV);
					double data = Integer.valueOf(keyValue[1])*1.0;
					weatherDataContent.setValue(data);
					weatherDataList.add(weatherDataContent);
				}
					break;
				case "pressure":
				{
					weatherDataContent.setMonitorDataType(MonitorDataType.AIRPRESSURE);
					double data = Integer.valueOf(keyValue[1])*1.0;
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
		log.info("气象站数据==\r\n"+sendJson);
		MuseumGateway.redisMQPushSender.sendNettyToEngine(sendJson);
	}
}
