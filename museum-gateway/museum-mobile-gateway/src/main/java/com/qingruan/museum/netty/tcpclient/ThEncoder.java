package com.qingruan.museum.netty.tcpclient;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ThEncoder extends MessageToByteEncoder<byte[]>{

	@Override
	protected void encode(ChannelHandlerContext ctx, byte[] msg, ByteBuf out)
			throws Exception {
		out.writeBytes(msg);
		
	}
	
}
