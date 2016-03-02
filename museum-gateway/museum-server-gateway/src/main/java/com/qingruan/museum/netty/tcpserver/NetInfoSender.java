package com.qingruan.museum.netty.tcpserver;

import io.netty.channel.Channel;

import java.util.ArrayList;

import lombok.extern.slf4j.Slf4j;

import com.qingruan.museum.framework.util.EncoderUtil;
import com.qingruan.museum.gateway.common.netty.SendMessage;

@Slf4j
public class NetInfoSender {

	public void sendNetInfo(String masterGetwayID) {
		System.out.println("engine端推送网络拓扑");
		String deviceType = "A0";
		StringBuilder builder = new StringBuilder();
		builder.append("3A");
		builder.append("87");
		builder.append("000000000000");
		builder.append("000000000000");
		ArrayList<String> arrayList = CommonData.netInfoMap.get(masterGetwayID);
		int size = arrayList.size() * 6;
		String int2HexStr = EncoderUtil.int2HexStr(size + 1, 4);
		builder.append(int2HexStr);
		builder.append(deviceType);
		for (int i = 0; i < arrayList.size(); i++) {
			builder.append(arrayList.get(i));
		}
		String checkCode = EncoderUtil.int2HexStr(
				UnPack.xor(builder.toString()), 2);
		builder.append(checkCode);
		builder.append("EE");
		Channel channel = CommonData.deviceChannels.get(masterGetwayID);
		SendMessage.sendMsg(channel, builder.toString());
	}
	
	public void sendNetInfo86(String masterGetwayID) {
		System.out.println("engine端推送网络拓扑");
		String deviceType = "A0";
		StringBuilder builder = new StringBuilder();
		builder.append("3A");
		builder.append("86");
		builder.append("000000000000");
		builder.append("000000000000");
		ArrayList<String> arrayList = CommonData.netInfoMap.get(masterGetwayID);
		int size = arrayList.size() * 6;
		String int2HexStr = EncoderUtil.int2HexStr(size + 1, 4);
		builder.append(int2HexStr);
		builder.append(deviceType);
		for (int i = 0; i < arrayList.size(); i++) {
			builder.append(arrayList.get(i));
		}
		String checkCode = EncoderUtil.int2HexStr(
				UnPack.xor(builder.toString()), 2);
		builder.append(checkCode);
		builder.append("EE");
		Channel channel = CommonData.deviceChannels.get(masterGetwayID);
		SendMessage.sendMsg(channel, builder.toString());
	}
}
