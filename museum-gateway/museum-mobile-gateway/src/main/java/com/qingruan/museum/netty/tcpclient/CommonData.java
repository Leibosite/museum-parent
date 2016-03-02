package com.qingruan.museum.netty.tcpclient;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CommonData {
	
	
	//盛放和设备连接的通道
	public static ConcurrentHashMap<String, Channel> deviceChannels = new ConcurrentHashMap<String, Channel>();
	
	//恒温恒湿展柜的连接通道
	public static ConcurrentHashMap<String, Channel> thChannels = new ConcurrentHashMap<String, Channel>();
	
	//包序号缓存
	public static ConcurrentHashMap<String, Integer> thSerialNumber = new ConcurrentHashMap<String, Integer>();
	
	//缓存上传上来的数据，防止粘包
	public static ConcurrentHashMap<Channel, StringBuilder> msgCache = new ConcurrentHashMap<Channel, StringBuilder>();
	
	
	public static ConcurrentHashMap<String, Thread>  connectThreads= new ConcurrentHashMap<String, Thread>();
	
	public static String connectGateWaySuccessIp = "";
	
	public static Channel connectThClientChannel;
	
	
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
	
	public static Channel getChannelByIpAddress(String ipAddress){
		if(thChannels.containsKey(ipAddress)){
			return thChannels.get(ipAddress);
		}
		return null;
	}
}
