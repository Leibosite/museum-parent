package com.qingruan.museum.netty.protocol.down;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import lombok.Data;

import com.qingruan.museum.domain.models.enums.MonitorDataType;
import com.qingruan.museum.framework.util.EncoderUtil;
import com.qingruan.museum.framework.util.PropertiesUtils;
import com.qingruan.museum.framework.util.TextUtil;
import com.qingruan.museum.gateway.common.netty.SendMessage;
import com.qingruan.museum.msg.constantth.ConstantThMsg;
import com.qingruan.museum.msg.constantth.ThDataContent;
import com.qingruan.museum.netty.tcpserver.CommonData;

/**
 * 
 * @author jcy
 * @description:
 * 下发恒温恒湿展柜控制指令
 */
@Data
public class ThDownDataPackage {
	
	public ThDownDataPackage(String ipAddress){
		this.ipAddress = ipAddress;
	}
	public final String flag = "0D0A0D0A";
	private int resendNumber;
	private int serialNumber;
	private String downSendString;
	private long   timestamp;//判断超时，重发
	private String ipAddress;
	
	private Timer timer;
	private TimerTask task;
	
	/**
	 * 重发机制，如没有响应就重发
	 */
	public void startTaskTimer(){
		timer = new Timer();
		task = new TimerTask() {
			
			@Override
			public void run() {
				resend();
			}
		};
		String resendOverTime = PropertiesUtils.readValue("thclient.resend.overtime");
		long overTime = Long.valueOf(resendOverTime, 10);
		timer.schedule(task, 0, overTime*1000);
	}
	
	public void resend(){
		
		String resendNumberString = PropertiesUtils.readValue("thclient.resend.number");
		int maxResendNumber = Integer.valueOf(resendNumberString, 10);
		if(resendNumber<=maxResendNumber){
			SendMessage.sendMsg(CommonData.connectThClientChannel, this.downSendString);
			resendNumber = resendNumber +1;
		}else{
			//TODO 重发次数超出预定次数，告警
			stop();
			CommonData.ThDownDataQueue.remove(this);
		}
	}
	
	public void stop(){
		if(task!=null){
			task.cancel();
			task = null;
		}
		
		if(timer!=null){
			timer.cancel();
			timer = null;
		}
	}
	
	/**
	 * 读取恒温恒湿展柜的测量值
	 * 带有语义的包头 A1为读测量值 A2为读设定值 A3写单个寄存器的值
	 * ThClient 调用
	 */
	public void readTHServerMeasureValue(){
		
		StringBuilder readCmd = new StringBuilder();
		String hexIp = TextUtil.str2Hex(this.ipAddress);
		String ipLength = EncoderUtil.int2HexStr(hexIp.length(), 2);
		readCmd.append("01");
		readCmd.append("03");
		readCmd.append("270F");
		readCmd.append("0008");
		String length = EncoderUtil.int2HexStr(readCmd.length()/2, 4);
		readCmd.insert(0, length);
		readCmd.insert(0, "0000");
		String serialNumberString = EncoderUtil.int2HexStr(CommonData.getSerialNumber(this.ipAddress), 2);
		readCmd.insert(0, serialNumberString);
		readCmd.insert(0, "A1");
		readCmd.insert(0, hexIp);
		readCmd.insert(0, ipLength);
		readCmd.insert(0, "B4");
		readCmd.insert(0,flag);
		readCmd.append(flag);
		this.downSendString = readCmd.toString();		
		SendMessage.sendMsg(CommonData.connectThClientChannel, readCmd.toString());
	}
	
	/**
	 * 读取恒温恒湿展柜的预设值
	 */
	public  void readTHServerPresetValue(){
		
		StringBuilder readCmd = new StringBuilder();
		String hexIp = TextUtil.str2Hex(this.ipAddress);
		String ipLength = EncoderUtil.int2HexStr(hexIp.length(), 2);
		readCmd.append("01");
		readCmd.append("03");
		readCmd.append("0000");
		readCmd.append("0008");
		String length = EncoderUtil.int2HexStr(readCmd.length()/2, 4);
		readCmd.insert(0, length);
		readCmd.insert(0, "0000");
		String serialNumberString = EncoderUtil.int2HexStr(CommonData.getSerialNumber(this.ipAddress), 2);
		readCmd.insert(0, serialNumberString);
		readCmd.insert(0, "A2");
		readCmd.insert(0, hexIp);
		readCmd.insert(0, ipLength);
		readCmd.insert(0, "B4");
		readCmd.insert(0,flag);
		readCmd.append(flag);
		
		if(CommonData.ThDownDataQueue.size()==0){
			SendMessage.sendMsg(CommonData.connectThClientChannel, readCmd.toString());
		}
		 
		int serialNumber = CommonData.getSerialNumber(this.ipAddress);		
		ThDownDataPackage downTask = new ThDownDataPackage(this.ipAddress);
		downTask.setDownSendString(readCmd.toString());
		downTask.setSerialNumber(serialNumber);
		downTask.setTimestamp(System.currentTimeMillis());
				
		
		CommonData.ThDownDataQueue.add(downTask);		
		this.downSendString = readCmd.toString();
	}
	
	/**
	 * 写入恒温恒湿展柜的预设值
	 * 带有语义的包头 A1为读测量值 A2为读设定值 A3写单个寄存器的值
	 * thClient 调用 ConstantThMsg
	 */
	public  void writeTHServerPresetValue(ConstantThMsg msgCmd){
		
		List<ThDataContent> list = msgCmd.getData().getDatas();
		String hexIp = TextUtil.str2Hex(this.ipAddress);
		String ipLength = EncoderUtil.int2HexStr(hexIp.length(), 2);
		
		for (ThDataContent thDataContent : list) {
			
			StringBuilder readCmd = new StringBuilder();
			
			readCmd.append("01");
			readCmd.append("06");
			MonitorDataType type = thDataContent.getMonitorDataType();
			switch (thDataContent.getShowCase()) {
				case ZEROTH:
					if(type == MonitorDataType.TEMPERATURE){
						readCmd.append("0001");
					}else{
						readCmd.append("0005");
					}
					break;
				case FIRST:
					if(type == MonitorDataType.TEMPERATURE){
						readCmd.append("0002");
					}else{
						readCmd.append("0006");
					}
					break;
				case SECOND:
					if(type == MonitorDataType.TEMPERATURE){
						readCmd.append("0003");
					}else{
						readCmd.append("0007");
					}
					break;
				case THIRD:
					if(type == MonitorDataType.TEMPERATURE){
						readCmd.append("0004");
					}else{
						readCmd.append("0008");
					}
					break;
				default:
					break;
			}
			
			int presetValue = (int)(thDataContent.getValue() * 10); //这个值需要规则引擎进行限制
			String valueString = EncoderUtil.int2HexStr(presetValue, 4);
			readCmd.append(valueString);
			String length = EncoderUtil.int2HexStr(readCmd.length()/2, 4);
			readCmd.insert(0, length);
			readCmd.insert(0, "0000");
			int serialNumber = CommonData.getSerialNumber(this.ipAddress);
			String serialNumberString = EncoderUtil.int2HexStr(serialNumber, 2);
			readCmd.insert(0, serialNumberString);
			readCmd.insert(0, "A3");
			readCmd.insert(0, hexIp);
			readCmd.insert(0, ipLength);
			readCmd.insert(0, "B4");
			readCmd.insert(0,flag);
			readCmd.append(flag);
			
			if(CommonData.ThDownDataQueue.size()==0){
				SendMessage.sendMsg(CommonData.connectThClientChannel, readCmd.toString());
			}
			
			//下面是把下发的命令加入到任务队列缓存中,以便检查发送是否成功,是否需要重发
			ThDownDataPackage downTask = new ThDownDataPackage(this.ipAddress);
			downTask.setDownSendString(readCmd.toString());
			downTask.setSerialNumber(serialNumber);
			downTask.setTimestamp(System.currentTimeMillis());
			CommonData.ThDownDataQueue.add(downTask);

			this.downSendString = readCmd.toString();
		}
	}
}
