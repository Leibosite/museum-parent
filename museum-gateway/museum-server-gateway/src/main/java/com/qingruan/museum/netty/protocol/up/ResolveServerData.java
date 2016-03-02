package com.qingruan.museum.netty.protocol.up;

import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import com.qingruan.museum.domain.models.enums.MonitorDataType;
import com.qingruan.museum.framework.jackson.JSONUtil;
import com.qingruan.museum.framework.util.TextUtil;
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
import com.qingruan.museum.msg.constantth.ThDataContent;
import com.qingruan.museum.msg.constantth.ThDataContent.ShowCase;
import com.qingruan.museum.msg.constantth.ThNetInfos;
import com.qingruan.museum.netty.protocol.down.ThDownDataPackage;
import com.qingruan.museum.netty.tcpserver.CommonData;

/**
 * 以树莓派作为中继，读取恒温恒湿展柜的数据
 * GateWay 向thclient 下发协议 （AF 功能码 数据长度 数据 校验）
 * 功能码 命令:
 * 			B1读测量值命令 B2读设定值 B3写设定值
 *       返回结果:
 *       	A1为读测量值 A2为读设定值 A3写单个寄存器的值
 *       
 *       B1 包序号 ip length ip address
 *       B2 包序号 ip length ip address
 *       B3 包序号 ip length ip address 温湿度数据长度 温湿度Json数据
 */

/**
 * 这个中间有树莓派做的网关作为中间的透传路由
 * 读取恒温恒湿展柜的预设值
 * 带有语义的包头 A1为读测量值 A2为读设定值 A3写单个寄存器的值
 * thClient调用
 */

@Slf4j
public class ResolveServerData {
	
	
	public static void judgeCommand(Channel channel,String msg){
		String cmdHeader = msg.substring(0, 2);
		if ("B1".equals(cmdHeader)) {
			resolveMeasureData(channel, msg);
		} else if ("B2".equals(cmdHeader)) {
			resolvePresetData(channel, msg);
		} else if ("B3".equals(cmdHeader)) {
			resolveWriteData(channel, msg);
		} 
	}
	
	public static void resolveMeasureData(Channel channel,String msg){
		try {
			int ipAddressLength = Integer.valueOf(msg.substring(2,4), 16);
			String thIpAddress = TextUtil.hexToGBK(msg.substring(4, 4+ipAddressLength));
			
			String thClientData = msg.substring(4+ipAddressLength);
			
			int dataLength = Integer.valueOf(thClientData.substring(16, 18), 16);
			if(dataLength == 16){
				MuseumMsg message = new MuseumMsg();
				
				MsgHeader header = new MsgHeader();
				header.setDeliveryMode(DeliveryMode.PTP);
				header.setTimeStamp(System.currentTimeMillis());
				message.setMsgHeader(header);
				
				MsgProperty property = new MsgProperty();
				property.setApplicationType(ApplicationType.CONSTANT_TH);
				property.setCmdType(CmdType.ANSWER);
				message.setMsgProperty(property);
			
				ConstantThMsg msgBody = new ConstantThMsg();
				msgBody.setThAppType(ThAppType.GATEWAY_POST_TH_MONITOR_DATA);
				msgBody.setThMsgType(ThMsgType.TEXT);
				
				ThData data = new ThData();
				
				data.setTimeStamp(System.currentTimeMillis());
				
				List<ThDataContent> list = new ArrayList<ThDataContent>();
				for(int i=0;i<8;i++){
					ThDataContent content = new ThDataContent();
					ShowCase showCase = null;
					MonitorDataType type = null;
					switch (i) {
						case 0:
							showCase = ShowCase.ZEROTH;
							type     = MonitorDataType.TEMPERATURE;
							break;
						case 1:
							showCase = ShowCase.ZEROTH;
							type     = MonitorDataType.HUMIDITY;					
							break;
						case 2:
							showCase = ShowCase.FIRST;
							type     = MonitorDataType.TEMPERATURE;
							break;
						case 3:
							showCase = ShowCase.FIRST;
							type     = MonitorDataType.HUMIDITY;
							break;
						case 4:
							showCase = ShowCase.SECOND;
							type     = MonitorDataType.TEMPERATURE;
							break;
						case 5:
							showCase = ShowCase.SECOND;
							type     = MonitorDataType.HUMIDITY;					
							break;
						case 6:
							showCase = ShowCase.THIRD;
							type     = MonitorDataType.TEMPERATURE;
							break;
						case 7:
							showCase = ShowCase.THIRD;
							type     = MonitorDataType.HUMIDITY;
							break;	
						default:
							break;
					}
					content.setShowCase(showCase);
					content.setMonitorDataType(type);
					double value = Integer.valueOf(thClientData.substring(18+i*4, 22+i*4), 16) / 10.0;
					content.setValue(value);
					list.add(content);
				}
				data.setDatas(list);
				
				ThNetInfos info = new ThNetInfos();
				info.setPlcIpv4Addr(thIpAddress);
				data.setThNetInfos(info);
				
				int serialNumber = CommonData.getSerialNumber(thIpAddress);
				
				int headNumber = Integer.valueOf(msg.substring(2, 4), 16);
				if(serialNumber == headNumber){
					CommonData.setSerialNumber(thIpAddress, serialNumber+1);
				}
				
				msgBody.setData(data);
				message.setMsgBody(msgBody);
				
				String jsonString = JSONUtil.serialize(message);
				log.info("接收到的恒温恒湿展柜测量数据:"+jsonString);
				MuseumGateway.redisMQPushSender.sendNettyToEngine(jsonString);
			}		
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	public static void resolvePresetData(Channel channel,String msg){
		try {
			
			int ipAddressLength = Integer.valueOf(msg.substring(2,4), 16);
			String thIpAddress = TextUtil.hexToGBK(msg.substring(4, 4+ipAddressLength));
			int serialNumber = CommonData.getSerialNumber(thIpAddress);
			int headNumber = Integer.valueOf(msg.substring(2, 4), 16);
			if(serialNumber == headNumber){
				CommonData.setSerialNumber(thIpAddress, serialNumber+1);
			}
			
			String thClientData = msg.substring(4+ipAddressLength);
			String flag = thClientData.substring(14, 16);
			if("03".equals(flag)){
				CommonData.ThDownDataQueue.poll();
				sentNextTaskData(channel);
			}else{
				ThDownDataPackage thDownDataPackage =CommonData.ThDownDataQueue.peek();
				thDownDataPackage.resend();
				return;
			}			
			
			int dataLength = Integer.valueOf(thClientData.substring(16, 18), 16);
			if(dataLength == 16){
				MuseumMsg message = new MuseumMsg();
				
				MsgHeader header = new MsgHeader();
				header.setDeliveryMode(DeliveryMode.PTP);
				header.setTimeStamp(System.currentTimeMillis());
				message.setMsgHeader(header);
				
				MsgProperty property = new MsgProperty();
				property.setApplicationType(ApplicationType.CONSTANT_TH);
				property.setCmdType(CmdType.ANSWER);
				message.setMsgProperty(property);
			
				ConstantThMsg msgBody = new ConstantThMsg();
				msgBody.setThAppType(ThAppType.GATEWAY_POST_TH_PRESET_DATA);
				msgBody.setThMsgType(ThMsgType.TEXT);
				
				ThData data = new ThData();
				data.setTimeStamp(System.currentTimeMillis());
				
				List<ThDataContent> list = new ArrayList<ThDataContent>();
				for(int i=0;i<8;i++){
					ThDataContent content = new ThDataContent();
					ShowCase showCase = null;
					MonitorDataType type = null;
					switch (i) {
						case 0:
							showCase = ShowCase.ZEROTH;
							type     = MonitorDataType.TEMPERATURE;
							break;
						case 1:
							showCase = ShowCase.FIRST;
							type     = MonitorDataType.TEMPERATURE;					
							break;
						case 2:
							showCase = ShowCase.SECOND;
							type     = MonitorDataType.TEMPERATURE;
							break;
						case 3:
							showCase = ShowCase.THIRD;
							type     = MonitorDataType.TEMPERATURE;
							break;
						case 4:
							showCase = ShowCase.ZEROTH;
							type     = MonitorDataType.HUMIDITY;
							break;
						case 5:
							showCase = ShowCase.FIRST;
							type     = MonitorDataType.HUMIDITY;					
							break;
						case 6:
							showCase = ShowCase.SECOND;
							type     = MonitorDataType.HUMIDITY;
							break;
						case 7:
							showCase = ShowCase.THIRD;
							type     = MonitorDataType.HUMIDITY;
							break;	
						default:
							break;
					}
					content.setShowCase(showCase);
					content.setMonitorDataType(type);
					double value = Integer.valueOf(thClientData.substring(18+i*4, 22+i*4), 16) / 10.0;
					content.setValue(value);
					list.add(content);
				}
				data.setDatas(list);
				
				ThNetInfos info = new ThNetInfos();
				info.setPlcIpv4Addr(thIpAddress);
				data.setThNetInfos(info);	
				msgBody.setData(data);
				message.setMsgBody(msgBody);
				
				String jsonString = JSONUtil.serialize(message);
				log.info("接收到的恒温恒湿展柜设定数据"+jsonString);
				MuseumGateway.redisMQPushSender.sendNettyToEngine(jsonString);
			}		
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	public static void resolveWriteData(Channel channel,String msg){
		try {
			
			int ipAddressLength = Integer.valueOf(msg.substring(2,4), 16);
			String clientAddress = TextUtil.hexToGBK(msg.substring(4, 4+ipAddressLength));
			int serialNumber = CommonData.getSerialNumber(clientAddress);		
			int headNumber = Integer.valueOf(msg.substring(4+ipAddressLength+2, 4+ipAddressLength+2+2), 16);
			if(serialNumber == headNumber){
				CommonData.setSerialNumber(clientAddress, serialNumber+1);	
			}
			String resultString = msg.substring(4+ipAddressLength);
			String flag = resultString.substring(14, 16);
			if("06".equals(flag)){
				CommonData.ThDownDataQueue.poll();
				sentNextTaskData(channel);
			}else{
				ThDownDataPackage thDownDataPackage =CommonData.ThDownDataQueue.peek();
				thDownDataPackage.resend();
			}			
		} catch (Exception e) {
			log.info(e.getMessage());
		}
	}
	
	public static void sentNextTaskData(Channel channel){
		ThDownDataPackage thDownDataPackage = CommonData.ThDownDataQueue.peek();
		if(thDownDataPackage!=null){
			SendMessage.sendMsg(channel,thDownDataPackage.getDownSendString());
		}
	}
}
