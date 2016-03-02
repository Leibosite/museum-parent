package com.qingruan.museum.netty.tcpserver;

import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.qingruan.museum.netty.model.UpDataPackage;
import com.qingruan.museum.netty.model.UpDataPackageV2;
import com.qingruan.museum.netty.model.UpDataPackageV3;

public class UnPack {

	public static int xor(String str) {
		int x = 0;
		if (str != null && str.length() >= 2) {
			x = Integer.parseInt(str.substring(0, 2), 16);
			for (int i = 2; i < str.length(); i += 2) {
				x = x ^ Integer.parseInt(str.substring(i, i + 2), 16);
			}
		}
		return x;
	}

	public static boolean check(String message) {

		if (message.length() <= 16) {
			return false;
		}

		message = message.toUpperCase();
		String crc = message.substring(message.length() - 2, message.length());
		String data = message.substring(0, message.length() - 2);
		if (data != null && data.length() >= 2) {
			int x = xor(data);
			if (x == Integer.parseInt(crc, 16)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * 适用于新版通信协议的校验
	 * 
	 * @param message
	 *            出去0D0A0D0A的部分
	 * @return
	 */
	public boolean checkV2(String message) {

		if (message.length() <= 16) {
			return false;
		}
		// 加上0D0A0D0A用来校验，是包含前面的分隔符的
		message = "0D0A0D0A" + message.toUpperCase();
		// 校验包尾
		String tail = message.substring(message.length() - 2, message.length());
		if (!"EE".equals(tail)) {
			return false;
		}
		// 检查校验位
		String crc = message.substring(message.length() - 4,
				message.length() - 2);
		String data = message.substring(0, message.length() - 4);
		if (data != null && data.length() >= 2) {
			int x = xor(data);
			if (x == Integer.parseInt(crc, 16)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * 3B8000000000000200000000000201B0FFFFFFFF08EE
	 */
	public boolean checkV3(String message) {

		return checkV2(message);
	}

	/**
	 * 消息校验,把消息分解成简单对象，以便后续处理
	 * 
	 * @param message
	 *            消息字符串
	 * @return MessageInfo 预处理对象
	 */
	public static UpDataPackage verify(String message) {
		message = message.toUpperCase();
		UpDataPackage info = new UpDataPackage();

		String trim = message;
		info.setCommand(trim.substring(0, 2));
		info.setMonitorMacAddress(trim.substring(2, 14));
		info.setSequenceNumber(trim.substring(14, 16));
		info.setMsgBody(trim.substring(16, trim.length()));

		return info;
	}

	public static UpDataPackageV2 verifyV2(String message) {
		message = message.toUpperCase();
		UpDataPackageV2 info = new UpDataPackageV2();

		String trim = message;
		info.setCommand(trim.substring(2, 4));
		info.setDeviceID(trim.substring(4, 28));
		int length = Integer.valueOf(trim.substring(28, 30), 16);
		info.setDataLength(length);
		info.setDeviceType(trim.substring(30, 32));
		info.setDataBody(trim.substring(32, 30 + length * 2));

		return info;
	}

	/*
	 * 第三版通信协议
	 * 
	 * 0~2 2~4 4~16 16~28 28-30 30~32 32~38 0D0A0D0A 3B 80 0000000002B0
	 * 0000000002B1 01 B0 FFFFFF BB EE 0D0A0D0A
	 * 
	 * 第4版通信协议 数据长度增加了一byte 0~2 2~4 4~16 16~28 28-32 32~34 34~40 0D0A0D0A 
	 *                3B 80 0000000002B0 0000000002B1 0004 B0 FFFFFF BB EE 0D0A0D0A
	 */

	public UpDataPackageV3 verifyV3(String message) {
		message = message.toUpperCase();
		UpDataPackageV3 info = new UpDataPackageV3();
		String trim = message;
		info.setCommand(trim.substring(2, 4));
		info.setMasterGatewayID(trim.substring(4, 8));
		info.setPath(trim.substring(4, 16));
		info.setDeviceID(trim.substring(16, 28));
		int length = Integer.valueOf(trim.substring(28, 32), 16);
		info.setDataLength(length);
		info.setDeviceType(trim.substring(32, 34));
		info.setDataBody(trim.substring(34, 32 + length * 2));
		return info;
	}

	/**
	 * 
	 * @param channel
	 *            相关的通道连接
	 * @return 和此通道相关的完整的包的list
	 * 
	 *         8 ：只包含分割符的情况下
	 */

	public static List<String> msgDeal(Channel channel) {

		String input = CommonData.msgCache.get(channel).substring(0);
		if (input == null || input.length() <= 8)
			return null;
		// 切分和channel相关的input
		List<String> list = UnPack.resolveMsg(input, channel);
		return list;

	}

	public static List<String> resolveMsg(String string, Channel channel) {

		String end = string.substring(string.length() - 8, string.length());
		String[] stringlist = string.split("0D0A0D0A");

		if (stringlist.length <= 1) {
			CommonData.msgCache.get(channel).delete(0, string.length());
			return null;
		}
		if (end.equals("0D0A0D0A")) {

			CommonData.msgCache.get(channel).delete(0, string.length());
			List<String> list = Arrays.asList(stringlist);
			return list;
		} else {
			int index = string.lastIndexOf("0D0A0D0A");
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < stringlist.length - 1; i++) {
				list.add(stringlist[i]);
			}
			CommonData.msgCache.get(channel).delete(0, index);
			return list;
		}
	}

}
