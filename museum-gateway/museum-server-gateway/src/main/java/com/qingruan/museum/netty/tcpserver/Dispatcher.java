package com.qingruan.museum.netty.tcpserver;

import io.netty.channel.Channel;

import com.qingruan.museum.netty.model.UpDataPackage;
import com.qingruan.museum.netty.model.UpDataPackageV3;
import com.qingruan.museum.netty.protocol.up.ResolveAirCleanerData;
import com.qingruan.museum.netty.protocol.up.ResolveData;
import com.qingruan.museum.netty.protocol.up.ResolveServerData;

/**
 * 数据分发器，因为这个服务器需要接收和解析多重设备的协议 作为一个网关需要把不同协议的数据送给不同的解析器解析
 */

public class Dispatcher {

	public Dispatcher() {

	}

	/**
	 * 
	 * @param message
	 *            从命令到包尾的完整消息 3B800000000002B001FFFFFFFF08EE 新版的通信协议也是如此
	 *            message就是去掉0A0D0A0D的部分
	 * @param channel
	 *            和消息相关的连接通道
	 */

	public void receiveMsg(String message, Channel channel) {

		// 空气净化器单独走一个端口，防止协议冲突
		if (CommonData.getAirCleanerPort() == CommonData
				.getLocalIpAddressPort(channel)) {

			if (message.length() > 14 && "AA".equals(message.substring(12, 14))) {

				String airCleanerMac = message.substring(0, 12);
				CommonData.airCleanerChannels.put(airCleanerMac, channel);
				if (!CommonData.airCleanerCounter.containsKey(channel)) {
					CommonData.airCleanerCounter.put(channel,
							System.currentTimeMillis());
				}

				ResolveAirCleanerData.resolveAirCleanerData(channel, message);
			}

		} else {

			if (message.length() > 8) {
				// 截取两个字符的命令码
				String cmd = message.substring(0, 2).toUpperCase();
				// 解码和验证包数据的实例
				UnPack unPack = new UnPack();
				// 新版通讯协议 3B是客户端发出的 &&&& checkV3是用来校验消息本身和本身携带的校验码是否匹配
				// cheackv3来校验消息结构
				if ("3B".equals(cmd) && unPack.checkV3(message)) {
					// 解包数据编程实体类
					UpDataPackageV3 info = unPack.verifyV3(message);
					CommonData.deviceChannels.put(info.getMasterGatewayID(),
							channel);
					// V2 和v3同样适用.开始解析协议
					CommonData.getResolveDataV2().judgeCommand(info);
					return;
				}

				// 老版的气象站协议
				/*
				 * if("7E".equals(cmd)){
				 * ResolveWeatherData.resolveWeatherStationData(channel,
				 * message); return; }
				 */

				// 和树莓派交互的恒温恒湿展柜
				if ("B0".equals(cmd) || "B1".equals(cmd) || "B2".equals(cmd)
						|| "B3".equals(cmd)) {
					CommonData.connectThClientChannel = channel;
					ResolveServerData.judgeCommand(channel, message);
				}

				if (UnPack.check(message)) {
					UpDataPackage info = UnPack.verify(message);// 消息拆包
					CommonData.deviceChannels.put(info.getMonitorMacAddress(),
							channel);
					ResolveData.judgeCommand(info);
				}

			}
		}
	}

}
