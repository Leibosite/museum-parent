package com.qingruan.museum.netty.tcpserver;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
/**
 * 
 * @author jcy
 * @description:
 * 把十六进制数据编码为二进制字节流发送出去
 */
public class Encoder extends MessageToByteEncoder<byte[]>{

	@Override
	protected void encode(ChannelHandlerContext ctx, byte[] msg, ByteBuf out)
			throws Exception {
		out.writeBytes(msg);
		
	}
	
}
