package com.qingruan.museum.netty.tcpserver;

import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

import com.google.common.cache.LoadingCache;
import com.qingruan.museum.framework.util.EncoderUtil;
import com.qingruan.museum.gateway.common.netty.SendMessage;
import com.qingruan.museum.msg.sensor.SensorNetInfo;
import com.qingruan.museum.netty.protocol.up.ResolveDataV2;

@Slf4j
public class MacListSender {

	// public static boolean cheackFixTimer(String masterGetwayid) {
	// // 检查是否发送的82和硬件处理成功的83是否一致 如果一致就返回flase 不需要开启定时任务
	// if (CommonData.sequenceListMap.get(masterGetwayid).size() ==
	// CommonData.sequenceSuccessListMap
	// .get(masterGetwayid).size()) {
	// return false;
	// } else {
	// return true;
	// }
	// }

	/**
	 * 发送第一包 前提：engine端发送的请求已经完整的缓存（macList和sequenceListMap的缓存初始化成功）
	 * 
	 * @param masterGetwayID
	 */
	// public void sendFirstPathPackage(String masterGetwayID) {
	// System.out.println("1开始82发送");
	// String deviceType = "A0";
	// // 初始化的netty收到的82的数量 以具体的网关的的id为单位 设为0
	// CommonData.recev82PathNum.put(masterGetwayID, 0);
	// ArrayList<String> arrayList = CommonData.sequenceListMap
	// .get(masterGetwayID);
	// // 第一包的路径
	// String pathKey = arrayList.get(0);
	// StringBuilder builder = new StringBuilder();
	// builder.append("3A");
	// builder.append("82");
	// builder.append(pathKey);
	// builder.append("000000000000");
	// LoadingCache loadingCache = CommonData.macList.get(masterGetwayID);
	// Object present = loadingCache.getIfPresent(pathKey);
	// List macList = null;
	// if (present instanceof List) {
	// macList = (List) present;
	// } else {
	// System.out.println("类型错误");
	// log.error("类型错误");
	// return;
	// }
	//
	// String length = EncoderUtil.int2HexStr(macList.size() * 6 + 2, 4);
	//
	// builder.append(length);
	// builder.append(deviceType);
	// builder.append(EncoderUtil.int2HexStr(macList.size(), 2));
	// for (int i = 0; i < macList.size(); i++) {
	// Object object = macList.get(i);
	// SensorNetInfo sensorNetInfo = null;
	// if (object instanceof SensorNetInfo) {
	// sensorNetInfo = (SensorNetInfo) object;
	// } else {
	// System.out.println("类型错误");
	// log.error("类型错误");
	// return;
	// }
	// String sensorMacAddr = sensorNetInfo.getSensorMacAddr();
	// builder.append(sensorMacAddr);
	//
	// }
	// String checkCode = EncoderUtil.int2HexStr(
	// UnPack.xor(builder.toString()), 2);
	// builder.append(checkCode);
	// builder.append("EE");
	// Channel channel = CommonData.deviceChannels.get(masterGetwayID);
	// SendMessage.sendMsg(channel, builder.toString());
	// System.out.println("1发送82拓扑响应结束");
	//
	// }

	/**
	 * 根据recev82PathNum来发送的
	 * 
	 * 前提：engine端发送的请求已经完整的缓存（macList和sequenceListMap的缓存初始化成功）
	 * 
	 * @param masterGetwayID
	 */
	public void sendPathPackage(String masterGetwayID) {

		try {
			Thread.sleep(3 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// 应该发送的路径序号
		HashSet<String> hashSet = CommonData.recev82PathNum.get(masterGetwayID);
		if(null == hashSet){
			hashSet = new HashSet<String>();
		}
		int seqNum = hashSet.size();
		System.out.println("开始82发送" + seqNum);
		String deviceType = "A0";
		// 发送顺序表
		ArrayList<String> arrayList = CommonData.sequenceListMap
				.get(masterGetwayID);
		// 当前发送顺序对应的路径
		String pathKey = arrayList.get(seqNum);
		StringBuilder builder = new StringBuilder();
		builder.append("3A");
		builder.append("82");
		builder.append(pathKey);
		builder.append("000000000000");
		LoadingCache loadingCache = CommonData.macList.get(masterGetwayID);
		Object present = loadingCache.getIfPresent(pathKey);
		List macList = null;
		if (present instanceof List) {
			macList = (List) present;
		} else {
			System.out.println("类型错误");
			log.error("类型错误");
			return;
		}

		String length = EncoderUtil.int2HexStr(macList.size() * 6 + 2, 4);

		builder.append(length);
		builder.append(deviceType);
		builder.append(EncoderUtil.int2HexStr(macList.size(), 2));
		for (int i = 0; i < macList.size(); i++) {
			Object object = macList.get(i);
			SensorNetInfo sensorNetInfo = null;
			if (object instanceof SensorNetInfo) {
				sensorNetInfo = (SensorNetInfo) object;
			} else {
				System.out.println("类型错误");
				log.error("类型错误");
				return;
			}

			String sensorMacAddr = sensorNetInfo.getSensorMacAddr();
			if(!StringUtils.isBlank(sensorMacAddr)){
				builder.append(sensorMacAddr);
			}
			

		}
		String checkCode = EncoderUtil.int2HexStr(
				UnPack.xor(builder.toString()), 2);
		builder.append(checkCode);
		builder.append("EE");
		Channel channel = CommonData.deviceChannels.get(masterGetwayID);
		SendMessage.sendMsg(channel, builder.toString());
		System.out.println("发送82拓扑" + "seqNum" + "响应结束");

		// 应该发送的路径序号 等于 缓存中的大小就代表发送完成
		if (CommonData.sequenceListMap.get(masterGetwayID).size() == seqNum) {
			System.out.println("完整发送  发送结束  ");
			new ResolveDataV2().writeFlash(masterGetwayID);
			return;
		}else{
			sendPathPackage(masterGetwayID);
		}

	}
	
	/**
	 * 根据recev82PathNum来发送的
	 * 
	 * 前提：engine端发送的请求已经完整的缓存（macList和sequenceListMap的缓存初始化成功）
	 * 
	 * @param masterGetwayID
	 */
	public void sendUpdatePathPackage(String masterGetwayID) {

		try {
			Thread.sleep(3 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// 应该发送的路径序号
		HashSet<String> hashSet = CommonData.recev82PathNum.get(masterGetwayID);
		int seqNum = hashSet.size();
		System.out.println("开始82发送" + seqNum);
		String deviceType = "A0";
		// 发送顺序表
		ArrayList<String> arrayList = CommonData.updateSequenceListMap
				.get(masterGetwayID);
		// 当前发送顺序对应的路径
		String pathKey = arrayList.get(seqNum);
		StringBuilder builder = new StringBuilder();
		builder.append("3A");
		builder.append("82");
		builder.append(pathKey);
		builder.append("000000000000");
		LoadingCache loadingCache = CommonData.updateMacList.get(masterGetwayID);
		Object present = loadingCache.getIfPresent(pathKey);
		List macList = null;
		if (present instanceof List) {
			macList = (List) present;
		} else {
			System.out.println("类型错误");
			log.error("类型错误");
			return;
		}

		String length = EncoderUtil.int2HexStr(macList.size() * 6 + 2, 4);

		builder.append(length);
		builder.append(deviceType);
		builder.append(EncoderUtil.int2HexStr(macList.size(), 2));
		for (int i = 0; i < macList.size(); i++) {
			Object object = macList.get(i);
			SensorNetInfo sensorNetInfo = null;
			if (object instanceof SensorNetInfo) {
				sensorNetInfo = (SensorNetInfo) object;
			} else {
				System.out.println("类型错误");
				log.error("类型错误");
				return;
			}

			String sensorMacAddr = sensorNetInfo.getSensorMacAddr();
			builder.append(sensorMacAddr);

		}
		String checkCode = EncoderUtil.int2HexStr(
				UnPack.xor(builder.toString()), 2);
		builder.append(checkCode);
		builder.append("EE");
		Channel channel = CommonData.deviceChannels.get(masterGetwayID);
		SendMessage.sendMsg(channel, builder.toString());
		System.out.println("发送82拓扑" + "seqNum" + "响应结束");

		// 应该发送的路径序号 等于 缓存中的大小就代表发送完成
		if (CommonData.sequenceListMap.get(masterGetwayID).size() == seqNum) {
			System.out.println("完整发送  发送结束  ");
			new ResolveDataV2().writeFlash(masterGetwayID);
			return;
		}else{
			sendUpdatePathPackage(masterGetwayID);
		}

	}
}
