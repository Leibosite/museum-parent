package com.qingruan.museum.netty.tcpserver;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import com.qingruan.museum.framework.util.EncoderUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
/**
 * 
 * @author jcy
 * @description:
 * 解码器，读取数据放入缓存
 */
@Slf4j
public class Decoder extends ByteToMessageDecoder{

	
	/**
	 * 把接受的数据放入StringBuilder，在放入和这个channl先关的ConmonData的msgCache中
	 */
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,List<Object> out)  {
		
		int msgLen = in.readableBytes();
		byte[] msg = (byte[]) null;
		StringBuffer sb = new StringBuffer();
		try{
			if ((in.isReadable()) && (msgLen > 0)) {
				msg = new byte[msgLen];
				in.readBytes(msg);	
				sb.append(EncoderUtil.getHexStr(msg));
				CommonData.msgCache.get(ctx.channel()).append(sb);
			}
		}catch(Exception e){
			log.error(e.toString());
		}
		log.info("Gataway_Receive_Data:" + sb.toString()+"\r\n");
	}

}
