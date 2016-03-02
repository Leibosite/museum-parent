package com.qingruan.museum.netty.protocol.down;

import io.netty.channel.Channel;

import com.qingruan.museum.framework.util.EncoderUtil;
import com.qingruan.museum.gateway.common.netty.SendMessage;
import com.qingruan.museum.netty.tcpserver.CommonData;

/**
 * 
 * @author jcy
 * @description:
 * 下发空气净化器的控制指令：
 * 1、查询空气净化器状态
 * 2、开启或者关闭
 * 3、调整风扇的速度
 * 4、调整睡眠模式
 * 5、调整自动关机时间
 * 6、开关负离子
 * 7、开关紫外线
 * 8、童锁
 */
public class AirDownDataPackage {
	
	
		public void remoteTest(int cmd,String macAddress){
			switch (cmd) {
			case 0:
				deviceStatusQuery(macAddress);
				break;
			case 1:
				deviceStartup(macAddress);
				break;
			case 2:
				deviceShutdown(macAddress);
				break;
			case 3:
				deviceFanSpeed(macAddress, 2);
				break;
			case 4:
				deviceMode(macAddress, 1);
				break;
			case 5:
				deviceTimerShutdown(macAddress, 2, 0);
				break;
			case 6:
				deviceAnion(macAddress, 1);
				break;
			case 7:
				deviceUv(macAddress, 1);
				break;
			case 8:
				deviceHumidification(macAddress, 1);
				break;
			case 9:
				deviceChildLock(macAddress, 1);
				break;
			case 10:
				deviceReplaceFilter(macAddress);
				break;
			default:
				break;
			}
		}
	
	
		public void deviceStatusQuery(String macAddress){
			packDataAndSendToDevice(macAddress, 0, 30, 0, 0);
		}
		
		public void deviceStartup(String macAddress){
			packDataAndSendToDevice(macAddress, 0, 31, 0, 0);
		}
		
		public void deviceShutdown(String macAddress){
			packDataAndSendToDevice(macAddress, 0, 32, 0, 0);
		}
		
		public void deviceFanSpeed(String macAddress,int speed){//speed 1~3
			packDataAndSendToDevice(macAddress, 0, 33, 0, speed);
		}
		
		public void deviceMode(String macAddress,int mode){ //0:自动模式 1:睡眠模式
			packDataAndSendToDevice(macAddress, 0, 34, 0, mode);
		}
		
		public void deviceTimerShutdown(String macAddress,int minute,int hour){//minute 0~59 hour 0~23
			packDataAndSendToDevice(macAddress, 0, 36, minute, hour);
		}
		
		public void deviceAnion(String macAddress,int onOrOff){//onOroff 0:关闭 1:开启
			packDataAndSendToDevice(macAddress, 0, 37, 0, onOrOff);
		}
		
		public void deviceUv(String macAddress,int onOrOff){//onOroff 0:关闭 1:开启
			packDataAndSendToDevice(macAddress, 0, 38, 0, onOrOff);
		}
		
		public void deviceHumidification(String macAddress,int onOrOff){//onOroff 0:关闭 1:开启
			packDataAndSendToDevice(macAddress, 0, 39, 0, onOrOff);
		}
		
		public void deviceChildLock(String macAddress,int onOroff){//onOroff 0:关闭 1:开启
			packDataAndSendToDevice(macAddress, 0, 40, 0, onOroff);
		}
		
		public void deviceReplaceFilter(String macAddress){
			packDataAndSendToDevice(macAddress, 0, 41, 0, 0);
		}
		
		//应答
		public void deviceReplaceFilterResult(String macAddress){
			
		}

	
		
		private void packDataAndSendToDevice(String macAddress,int reqResp,int cmd,int messageResolve,int messageData){
			
			StringBuilder builder = new StringBuilder();
			
			String header = EncoderUtil.hex2BinStr("AA", 8);
			builder.append(header);
			String length = EncoderUtil.hex2BinStr("13", 8);
			builder.append(length);
			String versionBin = "0001";
			builder.append(versionBin);
			String serialNumber = "000000000001";
			builder.append(serialNumber);
			String src_id = EncoderUtil.hex2BinStr("00000000", 32);
			builder.append(src_id);
			String dst_id = EncoderUtil.hex2BinStr("00000000", 32);
			builder.append(dst_id);
			String major_type = "0001";
			builder.append(major_type);
			String req_resp = Integer.toString(reqResp, 2);
			builder.append(req_resp);
			String minor_type = EncoderUtil.hex2BinStr(Integer.toHexString(cmd), 11);
			builder.append(minor_type);
			String reslove_0 = EncoderUtil.hex2BinStr("0000", 16);
			builder.append(reslove_0);
			String message_reslove = EncoderUtil.hex2BinStr(Integer.toHexString(messageResolve), 8);
			builder.append(message_reslove);
			String message = EncoderUtil.hex2BinStr(Integer.toHexString(messageData), 8);
			builder.append(message);
			byte[] messageBytes = EncoderUtil.binaryStrToBytes(builder.toString(), 18);
			int crc = 0;
			for (byte b : messageBytes) {
				crc = crc + b;
			}
			crc = (~crc&0xff) + 1;
			String crcString = EncoderUtil.long2BinaryStr(crc, 8);
			builder.append(crcString);
			Channel channel = CommonData.airCleanerChannels.get(macAddress);
			SendMessage.sendByteData(channel, EncoderUtil.binaryStrToBytes(builder.toString(), 19));

		}
		
}
