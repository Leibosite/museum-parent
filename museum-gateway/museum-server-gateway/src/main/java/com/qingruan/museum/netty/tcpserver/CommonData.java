package com.qingruan.museum.netty.tcpserver;

import io.netty.channel.Channel;
import io.netty.util.internal.ConcurrentSet;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.google.common.cache.LoadingCache;
import com.qingruan.museum.framework.util.PropertiesUtils;
import com.qingruan.museum.netty.protocol.down.DownDataPackage;
import com.qingruan.museum.netty.protocol.down.ThDownDataPackage;
import com.qingruan.museum.netty.protocol.up.ResolveDataV2;

/**
 * 
 * @author jcy
 * @description: 缓存数据结构
 */
public class CommonData {

	// 盛放和设备连接的通道
	public static ConcurrentHashMap<String, Channel> deviceChannels = new ConcurrentHashMap<String, Channel>();
	// 盛放和设备连接的通道
	public static ConcurrentHashMap<String, Timer> timerTask = new ConcurrentHashMap<String, Timer>();
	// 以主网关为键，主网关的缓存为值得map
	public static ConcurrentHashMap<String, LoadingCache> macList = new ConcurrentHashMap<String, LoadingCache>();
	// 以主网关为键，主网关的缓存为值得map
	public static ConcurrentHashMap<String, LoadingCache> updateMacList = new ConcurrentHashMap<String, LoadingCache>();
	// 主网关下面收到的82的成功的路径数
	public static ConcurrentHashMap<String, HashSet<String>> recev82PathNum = new ConcurrentHashMap<String, HashSet<String>>();
	// 下发的路径顺序
	public static ConcurrentHashMap<String, ArrayList<String>> sequenceListMap = new ConcurrentHashMap<String, ArrayList<String>>();
	// 更新是下发的路径顺序
	public static ConcurrentHashMap<String, ArrayList<String>> updateSequenceListMap = new ConcurrentHashMap<String, ArrayList<String>>();
	// 更新是下发的路径顺序
	public static ConcurrentHashMap<String, ArrayList<String>> netInfoMap = new ConcurrentHashMap<String, ArrayList<String>>();
	// 下发的成功的路径
	public static ConcurrentHashMap<String, Integer> reSent82PathNum = new ConcurrentHashMap<String, Integer>();
	// 盛放网络拓扑的
	public static ConcurrentHashMap<String, List<String>> netTopology = new ConcurrentHashMap<String, List<String>>();
	// 空气净化器的连接通道
	public static ConcurrentHashMap<String, Channel> airCleanerChannels = new ConcurrentHashMap<String, Channel>();
	// 过滤数据计时器
	public static ConcurrentHashMap<Channel, Long> airCleanerCounter = new ConcurrentHashMap<Channel, Long>();

	// 恒温恒湿展柜的连接通道
	public static ConcurrentHashMap<String, Channel> thChannels = new ConcurrentHashMap<String, Channel>();

	// 包序号缓存
	public static ConcurrentHashMap<String, Integer> thSerialNumber = new ConcurrentHashMap<String, Integer>();

	// 缓存上传上来的数据，防止粘包
	public static ConcurrentHashMap<Channel, StringBuilder> msgCache = new ConcurrentHashMap<Channel, StringBuilder>();

	// 缓存传感器下发的任务
	public static ConcurrentHashMap<String, ConcurrentLinkedQueue<DownDataPackage>> downTaskMap = new ConcurrentHashMap<String, ConcurrentLinkedQueue<DownDataPackage>>();

	// 缓存中继的macAddressList
	public static ConcurrentHashMap<String, DownDataPackage> macAddressList = new ConcurrentHashMap<String, DownDataPackage>();

	// 缓存下发恒温恒湿展柜的命令
	public static ConcurrentLinkedQueue<ThDownDataPackage> ThDownDataQueue = new ConcurrentLinkedQueue<ThDownDataPackage>();

	// 缓存websocket和浏览器之间的通信通道
	public static List<Channel> browserChannels = new ArrayList<Channel>();

	// 恒温恒湿展柜下发的命令，如果收到回复比较包序号如果相同则任务下发成功，如果下发失败，超时10秒从新下发，下发三次不成功就告警
	public static ConcurrentHashMap<String, ConcurrentLinkedQueue<ThDownDataPackage>> thDownTaskMap = new ConcurrentHashMap<String, ConcurrentLinkedQueue<ThDownDataPackage>>();

	public static ConcurrentHashMap<String, Thread> connectThreads = new ConcurrentHashMap<String, Thread>();

	public static ConcurrentSet<Channel> modbusChannelSet = new ConcurrentSet<Channel>();

	public static ResolveDataV2 resolverV2;

	public static String connectGateWaySuccessIp = "";

	public static Channel connectThClientChannel;

	public static int airCleanerPort = -1;

	public static int getSerialNumber(String clientAddress) {
		int serialNumber = 0;
		if (thSerialNumber.containsKey(clientAddress)) {
			serialNumber = thSerialNumber.get(clientAddress);
			if (serialNumber >= 255) {
				serialNumber = 0;
			}
		}
		return serialNumber;
	}

	public static ResolveDataV2 getResolveDataV2() {
		if (resolverV2 == null) {
			resolverV2 = new ResolveDataV2();
		}
		return resolverV2;
	}

	public static void setSerialNumber(String clientAddress, Integer number) {
		thSerialNumber.put(clientAddress, number);
	}

	public static ConcurrentLinkedQueue<ThDownDataPackage> getThDownTaskQueue(
			String clientAddress) {
		if (!thDownTaskMap.containsKey(clientAddress)) {
			ConcurrentLinkedQueue<ThDownDataPackage> queue = new ConcurrentLinkedQueue<ThDownDataPackage>();
			thDownTaskMap.put(clientAddress, queue);
		}
		return thDownTaskMap.get(clientAddress);
	}

	public static String getChannelIpAddress(Channel ch) {
		InetSocketAddress insocket = (InetSocketAddress) ch.remoteAddress();
		String clientAddress = "";
		if (insocket != null) {
			String clientIP = insocket.getHostString();
			int clientPort = insocket.getPort();
			clientAddress = clientIP + ":" + clientPort;
		}
		return clientAddress;
	}

	public static int getLocalIpAddressPort(Channel ch) {
		InetSocketAddress insocket = (InetSocketAddress) ch.localAddress();
		int localPort = -1;
		if (insocket != null) {
			localPort = insocket.getPort();
		}
		return localPort;
	}

	public static Channel getChannelByIpAddress(String ipAddress) {
		if (thChannels.containsKey(ipAddress)) {
			return thChannels.get(ipAddress);
		}
		return null;
	}

	public static int getAirCleanerPort() {
		try {
			if (airCleanerPort == -1) {
				airCleanerPort = Integer.valueOf(PropertiesUtils
						.readValue("aircleanerport"));
			}
			return airCleanerPort;
		} catch (Exception e) {

			return -1;
		}
	}
}
