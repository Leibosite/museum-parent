package com.qingruan.museum.netty.modbus;

import com.qingruan.museum.netty.tcpserver.CommonData;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ModbusHandler extends ChannelInboundHandlerAdapter{
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		/**
		 * 检测消息缓存里面是否有此缓存，没有就加上
		 */
		CommonData.modbusChannelSet.add(ctx.channel());
		log.info("Modbus 连接成功"+ctx.channel());
	}
	
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		if(CommonData.modbusChannelSet.contains(ctx.channel())){
			CommonData.modbusChannelSet.remove(ctx.channel());
		}
		log.info("Modbus 离线 "+ctx.channel());

	}
	
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		if(CommonData.modbusChannelSet.contains(ctx.channel())){
			CommonData.modbusChannelSet.remove(ctx.channel());
		}
		log.info("Modbus Exception "+cause.getLocalizedMessage());
	}
}
