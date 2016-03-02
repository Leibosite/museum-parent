package com.qingruan.museum.gateway.common.netty;

import com.qingruan.museum.framework.util.EncoderUtil;

import lombok.extern.slf4j.Slf4j;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.util.CharsetUtil;
import static io.netty.buffer.Unpooled.*;

@Slf4j
public class SendMessage {

	public static void sendByteData(Channel channel, byte[] msg) {

		if (channel != null && channel.isActive() && channel.isRegistered()
				&& channel.isWritable()) {
			String message = EncoderUtil.getHexStr(msg);
			log.info(message);
			channel.writeAndFlush(msg);
			log.info("byte[]***消息发送成功***");
		} else {
			log.info("byte[]***消息没有发出去***");
		}
	}

	public static void sendStringData(Channel channel, String msg) {
		if (channel != null && channel.isActive() && channel.isRegistered()
				&& channel.isWritable()) {
			ByteBuf buffer = copiedBuffer(msg, CharsetUtil.US_ASCII);
			channel.writeAndFlush(buffer);
			log.info("String***消息发送成功***");
		} else {
			log.info("String***消息没有发出去***");
		}
	}

	// 发送消息
	public static void sendMsg(Channel channel, String OriginMsg) {
		if (channel != null && channel.isActive() && channel.isRegistered()
				&& channel.isWritable() && channel.isOpen()) {
			if (!"".equals(OriginMsg)) {
				byte[] send = EncoderUtil.HexString2Bytes(OriginMsg,
						OriginMsg.length() / 2);
				channel.writeAndFlush(send);
				log.info("数据发送成功:" + OriginMsg + "\r\n");
			}
		} else {
			// autoware alarm to do
			log.error("数据没有发送出去!!!!-->" + OriginMsg);
		}
	}

}
