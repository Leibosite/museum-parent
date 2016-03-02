package com.qingruan.museum.netty.microclient;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.qingruan.museum.framework.util.PropertiesUtils;
import com.qingruan.museum.netty.gprs.GPRSCommand.Command;

public class CommonData {
	
	
	//包序号缓存
	public static ConcurrentHashMap<String, Integer> thSerialNumber = new ConcurrentHashMap<String, Integer>();
	//缓存上传上来的数据，防止粘包
	public static ConcurrentHashMap<Channel, StringBuilder> msgCache = new ConcurrentHashMap<Channel, StringBuilder>();
	
	public static ConcurrentHashMap<String, Thread>  connectThreads= new ConcurrentHashMap<String, Thread>();
	
	public static ConcurrentLinkedQueue<String>  gprsQueue = new ConcurrentLinkedQueue<String>();
	
	public static Channel serverGatewayChannel;
	
	public static Channel serialChannel;
	
	public static Channel gprsChannel;
	
	public static int steps;
	
	public static int gprsResendTimes = Integer.valueOf(PropertiesUtils.readValue("gprsgateway.resend.times").trim());
	
	public static String gprsPower =  PropertiesUtils.readValue("gprsgateway.power").trim();
	
	public static String fourTTPower =  PropertiesUtils.readValue("fourttgateway.power").trim();
	
	public static int gprsSendNumber;
	
	public static Command command = Command.AT_SHUT_OK;
	
	
	
	public static int getSerialNumber(String clientAddress){
		int serialNumber = 0;
		if(thSerialNumber.containsKey(clientAddress)){
			serialNumber = thSerialNumber.get(clientAddress); 
			if(serialNumber>=255){
				serialNumber = 0;
			}
		}
		return serialNumber;
	}
	
	public static void setSerialNumber(String clientAddress,Integer number){
		thSerialNumber.put(clientAddress, number);
	}
	
	
	
	public static String getChannelIpAddress(Channel ch){
		InetSocketAddress insocket = (InetSocketAddress) ch.remoteAddress();
		String clientAddress = "";
		if(insocket!=null){
			String clientIP = insocket.getHostString();
			int clientPort = insocket.getPort();
			clientAddress = clientIP+":"+clientPort;
		}	
		return clientAddress;
	}
	
	public static List<String> msgDeal(Channel channel) {
		String input = msgCache.get(channel).substring(0);

		if (input == null || input.length() < 4)
			return null;
		List<String> list = resolveMsg(input, channel);

		return list;

	}

	public static List<String> resolveMsg(String string, Channel channel) {

		String end = string.substring(string.length() - 8, string.length());
		String[] stringlist = string.split("0D0A0D0A");
		if (stringlist.length <= 1) {
			msgCache.get(channel).delete(0, string.length());
			return null;
		}
		if (end.equals("0D0A0D0A")) {

			msgCache.get(channel).delete(0, string.length());
			List<String> list = Arrays.asList(stringlist);
			return list;
		} else {
			int index = string.lastIndexOf("0D0A0D0A");
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < stringlist.length - 1; i++) {
				list.add(stringlist[i]);
			}
			msgCache.get(channel).delete(0, index);
			return list;
		}
	}
}
