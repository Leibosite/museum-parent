package com.qingruan.museum.netty.protocol.down;

import java.util.List;
import java.util.Random;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import com.qingruan.museum.framework.util.EncoderUtil;
import com.qingruan.museum.msg.sensor.SensorNetInfo;
import com.qingruan.museum.msg.sensor.PatchData;
import com.qingruan.museum.msg.sensor.PatchInfo;
import com.qingruan.museum.msg.sensor.SensorMsg;
import com.qingruan.museum.netty.tcpserver.UnPack;
/**
 * 
 * @author jcy
 * @description:
 * 下发传感器采集设备指令
 */
@Data
@Slf4j
public class DownDataPackage {
	
	private String command;
	private String monitorMacAddress;
	private PatchData upgradeData; 
	private String downSendData;
	private SensorMsg sensorMsg;
	private int sequenceNumber;
	private String statusCode;
	private String checkNumber;//传感器数据上报需要的验证号
	private String beaconMacAddress;
	
	public DownDataPackage(String command,String monitorMacAddress){
		this.command = command;
		this.monitorMacAddress = monitorMacAddress;
		Random random = new Random();
		this.sequenceNumber = Math.abs(random.nextInt())%255;
	}
	
	public void connectPackage(){
		

		if("01".equals(this.command)){
			
			this.downSendData = downData01();
		}else if("02".equals(this.command)){
			
			this.downSendData = downData02();
		}
		else if("03".equals(this.command)){
		
			this.downSendData = downData03();
		}
		else if("04".equals(this.command)){
			
			this.downSendData = downData04();
		}
		else if("05".equals(this.command)){
			
			this.downSendData = downData05();
		}
		else if("06".equals(this.command)){
			
			this.downSendData = downData06();
		}
		else if("07".equals(this.command)){
			
			this.downSendData = downData07();
		}
		else if("08".equals(this.command)){
			
			this.downSendData = downData08();
		}else {
			this.downSendData = "";
		}
	}
	
	/**
	 * @description:
	 * 下发同步时间戳
	 * 		** 01  序号(1个字节) 时间戳(4个字节)  状态码(1字节) 校验码(1字节) \n\r
	 * @return
	 */
	public String downData01(){
		StringBuilder builder = new StringBuilder();
		long timestamp = System.currentTimeMillis() / 1000;
		String timestampString = EncoderUtil.long2HexStr(timestamp, 8);
		builder.append("01");
		builder.append(EncoderUtil.int2HexStr(this.sequenceNumber,2));
		builder.append(timestampString);
		String checkCode = EncoderUtil.int2HexStr(UnPack.xor(builder.toString()),2);
		builder.append(checkCode);
		builder.append("0D0A");
		builder.insert(0, "2A2A");
		return builder.toString();
	}
	
	/**
	 * @description:
	 * 下发每个树莓派网关所管辖的采集设备的Mac地址列表
	 * 		** 02 序号(1个字节)  List的个数(1字节)   Mac地址(6字节) Major(2字节) Minor(2字节)   …   状态码(1字节) 校验码(1字节) \n\r
	 * @return
	 */
	public String downData02(){
		StringBuilder builder = new StringBuilder();
		builder.append("02");
		builder.append(EncoderUtil.int2HexStr(this.sequenceNumber,2));
		
		List<SensorNetInfo> sensorInfos = sensorMsg.getData().getMtmNetInfo().getSensorNetInfos();
		String macListSize = EncoderUtil.int2HexStr(sensorInfos.size(), 2);
		builder.append(macListSize);
		for (SensorNetInfo sensorInfo : sensorInfos) {
			String macAddress = sensorInfo.getSensorMacAddr();
			String majorHex = sensorInfo.getMajor();
			String minorHex = sensorInfo.getMinor();
			if(macAddress==null || majorHex==null || minorHex==null || macAddress.length()!=12){
				log.error("sensorMacAddress  or majorHex or minorHex is null");
				continue;
			}
			builder.append(macAddress);
			String major = EncoderUtil.int2HexStr(Integer.valueOf(majorHex,16), 4);
			String minor = EncoderUtil.int2HexStr(Integer.valueOf(minorHex,16), 4);
			builder.append(major);
			builder.append(minor);
		}
	
		String checkCode = EncoderUtil.int2HexStr(UnPack.xor(builder.toString()),2);
		builder.append(checkCode);
		builder.append("0D0A");
		builder.insert(0, "2A2A");
		return builder.toString();
	}
	
	/**
	 * @description:
	 * 采集设备固件升级通知指令
	 * 		** 03 序号(1个字节) 要升级的Beacon Mac地址(0xff:ff:ff:ff:ff:ff 全部的Beacon升级 6个字节) 升级包得长度(3个字节) 总包数(2个字节) 状态码(1个字节) 校验码(1字节) \n\r
	 * @return
	 */
	public String downData03(){
		StringBuilder builder = new StringBuilder();
		builder.append("03");
		builder.append(EncoderUtil.int2HexStr(this.sequenceNumber,2));
		
		List<SensorNetInfo> sensorInfos = sensorMsg.getData().getMtmNetInfo().getSensorNetInfos();
		String beaconNumber = EncoderUtil.int2HexStr(sensorInfos.size(), 2); 
		builder.append(beaconNumber);
		
		for (SensorNetInfo sensorInfo : sensorInfos) {
			builder.append(sensorInfo.getSensorMacAddr());
		}
		PatchInfo patchInfo = sensorMsg.getData().getPatchInfo();
		builder.append(patchInfo.getPatchLength());
		builder.append(patchInfo.getTotalPatchNumber());
		
		String checkCode = EncoderUtil.int2HexStr(UnPack.xor(builder.toString()), 2);
		builder.append(checkCode);
		builder.append("0D0A");
		builder.insert(0, "2A2A");
		
		return builder.toString();
	}
	
	/**
	 * @description:
	 * 下发Beacon升级数据包
	 * 		** 04序号 (1个字节) 当前包序号(2个字节) 总共的包数(2个字节) 当前数据包长度(2个字节) 数据包(最长1000字节) 校验码(1字节) \n\r
	 * @return
	 */
	public String downData04(){
		StringBuilder builder = new StringBuilder();
		
		builder.append("04");
		builder.append(EncoderUtil.int2HexStr(this.sequenceNumber,2));
		builder.append(EncoderUtil.int2HexStr(upgradeData.getIdNumber(),4)); //包序号
		builder.append(EncoderUtil.int2HexStr(upgradeData.getTotalPackageNumber(),4));//总包数
		builder.append(EncoderUtil.int2HexStr(upgradeData.getPackageSize(),4));
		builder.append(EncoderUtil.getHexStr(upgradeData.getUpgradeData()));// 每包数据最大1KB 
		String checkCode = EncoderUtil.int2HexStr(UnPack.xor(builder.toString()),2);
		builder.append(checkCode);
		builder.append("0D0A");
		builder.insert(0, "2A2A");
		
		return builder.toString();
	}
	
	/**
	 * @description:
	 * 数据接收应答，服务器每接收一条指令,要响应接受的状态
	 * 		**  05 序号 (1个字节) 状态码(01成功 02失败 03 队列里没有任务指令)(1个字节) 校验码(1个字节) \n\r
	 * @return
	 */
	public String downData05(){
		StringBuilder builder = new StringBuilder();
		builder.append("05");
		builder.append(EncoderUtil.int2HexStr(this.sequenceNumber,2));
		builder.append(this.checkNumber);
		builder.append(this.statusCode);
		String checkCode = EncoderUtil.int2HexStr(UnPack.xor(builder.toString()),2);
		builder.append(checkCode);
		builder.append("0D0A");
		builder.insert(0, "2A2A");
		return builder.toString();
	}
	
	/**
	 * @description:
	 * 服务器应答
	 * 		** 0x06 序号（1字节）信息码（1个字节 01成功）校验码（1个字节）\r\n
	 * @return
	 */
	public String downData06(){
		StringBuilder builder = new StringBuilder();
		builder.append("06");
		builder.append(EncoderUtil.int2HexStr(this.sequenceNumber,2));
		builder.append(this.statusCode);
		String checkCode = EncoderUtil.int2HexStr(UnPack.xor(builder.toString()),2);
		builder.append(checkCode);
		builder.append("0D0A");
		builder.insert(0, "2A2A");
		return builder.toString();
	}
	
	/**
	 * @description:
	 * 存活设备检查
	 * 		** 0x07 序号（1字节）信息码（1字节 0x01）校验码（1字节） \r\n
	 * @return
	 */
	public String downData07(){
		StringBuilder builder = new StringBuilder();
		builder.append("07");
		builder.append(EncoderUtil.int2HexStr(this.sequenceNumber,2));
		builder.append("01");
		String checkCode = EncoderUtil.int2HexStr(UnPack.xor(builder.toString()),2);
		builder.append(checkCode);
		builder.append("0D0A");
		builder.insert(0, "2A2A");
		return builder.toString();
	}
	
	/**
	 * @description:
	 * 读取采集设备历史数据
	 * 		** 0x08 序号（1byte）传感器Mac地址（6byte）校验码（1byte）\r\n
	 * @return
	 */
	public String downData08(){
		StringBuilder builder = new StringBuilder();
		builder.append("08");
		builder.append(EncoderUtil.int2HexStr(this.sequenceNumber,2));
		builder.append(this.beaconMacAddress);
		String checkCode = EncoderUtil.int2HexStr(UnPack.xor(builder.toString()),2);
		builder.append(checkCode);
		builder.append("0D0A");
		builder.insert(0, "2A2A");
		return builder.toString();
	}
}
