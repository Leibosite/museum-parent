package com.qingruan.museum.netty.tcpserver;

import io.netty.channel.Channel;

import com.qingruan.museum.framework.util.EncoderUtil;
import com.qingruan.museum.gateway.common.netty.SendMessage;

public class BeginHistoryData {

	public void beginHistoryData(String masterGatewayID, String path, String mac) {

		System.out.println("打开数据上报");
		String deviceType = "A0";
		StringBuilder builder = new StringBuilder();
		builder.append("3A");
		builder.append("92");
		builder.append(path);
		builder.append(mac);
		builder.append("0001");
		builder.append(deviceType);
		String checkCode = EncoderUtil.int2HexStr(
				UnPack.xor(builder.toString()), 2);
		builder.append(checkCode);
		builder.append("EE");
		Channel channel = CommonData.deviceChannels.get(masterGatewayID);
		SendMessage.sendMsg(channel, builder.toString());
		System.out.println("发送打开数据上报结束");

	}

}
