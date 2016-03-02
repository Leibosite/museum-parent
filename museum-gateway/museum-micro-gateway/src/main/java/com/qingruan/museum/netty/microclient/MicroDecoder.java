package com.qingruan.museum.netty.microclient;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.Charset;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import com.qingruan.museum.framework.util.EncoderUtil;
import com.qingruan.museum.gateway.common.netty.SendMessage;

@Slf4j
public class MicroDecoder extends ByteToMessageDecoder{

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
				if(ctx.channel().equals(CommonData.serialChannel) || ctx.channel().equals(CommonData.gprsChannel)){
					SendMessage.sendMsg(CommonData.serverGatewayChannel, sb.toString());
				}else{
					
					if("on".equals(CommonData.gprsPower)){
						String messageString = new String(msg,Charset.forName("ISO-8859-1"));
						//gprs send queue ,if queue length greater 10000 ,if will remove head and add a tail element
						if(CommonData.gprsQueue.size()<=10000){
							CommonData.gprsQueue.add(messageString);
							log.info("GPRS Queue add a message :"+EncoderUtil.getHexStr(msg));
						}else{
							CommonData.gprsQueue.add(messageString);
							CommonData.gprsQueue.poll();
							log.info("GPRS Queue is over 500");
						}
					}
					
					if("on".equals(CommonData.fourTTPower)){
						sb.append("7F7F7F7F");
						SendMessage.sendMsg(CommonData.serialChannel, sb.toString());
					}
				}
			}
		}catch(Exception e){
			log.error(e.toString());
		}

		log.info("MicroGateway接受信息" + sb.toString()+"\n");
	
	}

}
