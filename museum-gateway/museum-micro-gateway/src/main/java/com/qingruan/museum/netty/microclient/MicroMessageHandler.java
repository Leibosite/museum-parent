package com.qingruan.museum.netty.microclient;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.Thread.State;
import java.util.concurrent.locks.LockSupport;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MicroMessageHandler extends ChannelInboundHandlerAdapter {

	// Registered -> Active -> Read -> ReadComplete -> InActive ->UnRegistered

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

		
		if (CommonData.serverGatewayChannel==null) {
			CommonData.serverGatewayChannel = ctx.channel();
			CommonData.msgCache.put(ctx.channel(), new StringBuilder());
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		
	}


	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {

		log.info("Micro Unregistered and channel is" + ctx.channel());
		String clientAddress = CommonData.getChannelIpAddress(ctx.channel());
		if (CommonData.serverGatewayChannel!=null) {
			
			CommonData.serverGatewayChannel = null;
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
		if (CommonData.serverGatewayChannel!=null) {
			CommonData.serverGatewayChannel=null;
			CommonData.thSerialNumber.remove(clientAddress);
			CommonData.msgCache.remove(ctx.channel());
			Thread thread = CommonData.connectThreads.get(clientAddress);
			if (thread != null && thread.getState() == State.WAITING) {
				LockSupport.unpark(thread);
			}
		}
		log.error("Message Handler Exception:" + cause.getMessage());
	}

}
