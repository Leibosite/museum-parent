package com.qingruan.museum.netty.tcpclient;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

import lombok.extern.slf4j.Slf4j;

import com.qingruan.museum.framework.util.TextUtil;
import com.qingruan.museum.gateway.common.netty.SendMessage;

@Slf4j
public class THMessageHandler extends ChannelInboundHandlerAdapter {

	// Registered -> Active -> Read -> ReadComplete -> InActive ->UnRegistered

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

		String clientAddress = CommonData.getChannelIpAddress(ctx.channel());
		if (!CommonData.thChannels.containsKey(clientAddress)) {
			CommonData.thChannels.put(clientAddress, ctx.channel());
			CommonData.thSerialNumber.put(clientAddress, 0);
			CommonData.msgCache.put(ctx.channel(), new StringBuilder());
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {

		/*
		 * ByteBuf in = (ByteBuf)msg; int msgLen = in.readableBytes(); byte[]
		 * message = (byte[]) null; StringBuffer sb = new StringBuffer();
		 * 
		 * try{ if ((in.isReadable()) && (msgLen > 0)) { message = new
		 * byte[msgLen]; in.readBytes(message);
		 * sb.append(EncoderUtil.getHexStr(message)); }
		 * 
		 * String msgData = sb.toString(); if(msgData.length() > 4){
		 * 
		 * String cmdHeader = msgData.substring(0, 2);
		 * 
		 * if("B4".equals(cmdHeader)){
		 * 
		 * }else{
		 * 
		 * } }
		 * 
		 * }catch(Exception e){ log.error(e.toString()); e.printStackTrace();
		 * }finally{ ReferenceCountUtil.release(msg); }
		 */
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

		Channel channel = ctx.channel();

		List<String> msgList = msgDeal(channel);

		if (msgList == null || msgList.size() == 0)
			return;

		for (int i = 0; i < msgList.size(); i++) {
			String msgData = msgList.get(i);
			if (msgData.length() > 4) {
				retransmitMessageByIpAddress(msgData);
			}

		}
	}

	// retransmit 转发,树莓派中继接收到Gateway发送的消息后，由Ip地址路由转发
	private void retransmitMessageByIpAddress(String message) {
		try {
			int ipAddressLength = Integer.valueOf(message.substring(2, 4), 16);
			String thIpAddress = TextUtil.hexToGBK(message.substring(4,
					4 + ipAddressLength));
			String transmitMessage = message.substring(4 + ipAddressLength);
			log.info("向" + thIpAddress + "发送信息\r\n");
			Channel channel = CommonData.getChannelByIpAddress(thIpAddress);
			if (channel != null && transmitMessage.length() > 0) {
				SendMessage.sendMsg(channel, transmitMessage);
			}

		} catch (Exception e) {
			log.error(THMessageHandler.class.getName() + ":" + e.getMessage());
		}

	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channel is channelUnregistered" + ctx.channel());
		log.info("channel is Unregistered and channel is" + ctx.channel());
		String clientAddress = CommonData.getChannelIpAddress(ctx.channel());
		if (CommonData.thChannels.containsKey(clientAddress)) {
			CommonData.thChannels.remove(clientAddress);
			CommonData.thSerialNumber.remove(clientAddress);
			CommonData.msgCache.remove(ctx.channel());

			Thread thread = CommonData.connectThreads.get(clientAddress);
			if (thread != null && thread.getState() == State.WAITING) {
				LockSupport.unpark(thread);
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {

		String clientAddress = CommonData.getChannelIpAddress(ctx.channel());
		if (CommonData.thChannels.containsKey(clientAddress)) {
			CommonData.thChannels.remove(clientAddress);
			CommonData.thSerialNumber.remove(clientAddress);
			CommonData.msgCache.remove(ctx.channel());
			Thread thread = CommonData.connectThreads.get(clientAddress);
			if (thread != null && thread.getState() == State.WAITING) {
				LockSupport.unpark(thread);
			}
		}
		log.error("Message Handler Exception:" + cause.getMessage());
	}

	private static List<String> msgDeal(Channel channel) {
		String input = CommonData.msgCache.get(channel).substring(0);

		if (input == null || input.length() < 4)
			return null;
		List<String> list = resolveMsg(input, channel);

		return list;

	}

	private static List<String> resolveMsg(String string, Channel channel) {

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
