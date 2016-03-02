package com.qingruan.museum.netty.tcpclient;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import com.qingruan.museum.framework.util.EncoderUtil;
import com.qingruan.museum.framework.util.TextUtil;
import com.qingruan.museum.gateway.common.netty.SendMessage;

@Slf4j
public class ThDecoder extends ByteToMessageDecoder{

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
				
			}
		}catch(Exception e){
			log.error(e.toString());
		}
				
		
		String ipAddress = CommonData.getChannelIpAddress(ctx.channel());
		
		if(!"".equals(CommonData.connectGateWaySuccessIp)  && ipAddress != null && CommonData.connectGateWaySuccessIp.equals(ipAddress)){
			
			CommonData.msgCache.get(ctx.channel()).append(sb);
			log.info("接受来自Gateway发送的信息" + sb.toString()+"\r\n");
		}else{
			transparentMessage(ctx.channel(), sb.toString());
			log.info("向Gateway转发信息" + sb.toString()+"\r\n");
		}
		
	}
	
	private void transparentMessage(Channel ch,String msgData){
		
		if(msgData.length()>2){
			String cmd = msgData.substring(0, 2).toUpperCase();
			
			StringBuilder builder = new StringBuilder();
			builder.append("0D0A0D0A");
			
			switch (cmd) {
				case "A1":
					builder.append("B1");
					break;
				case "A2":
					builder.append("B2");				
					break;
				case "A3":
					builder.append("B3");
					break;	
				default:
					break;
			}
			
			String thIpAddress = CommonData.getChannelIpAddress(ch);
			String hexIp = TextUtil.str2Hex(thIpAddress);
			String ipLength = EncoderUtil.int2HexStr(hexIp.length(), 2);
			builder.append(ipLength);
			builder.append(hexIp);			
			builder.append(msgData);						
			builder.append("0D0A0D0A");
			if(CommonData.thChannels.containsKey(CommonData.connectGateWaySuccessIp)){
				Channel channel = CommonData.thChannels.get(CommonData.connectGateWaySuccessIp);
				SendMessage.sendMsg(channel, builder.toString());
			}
		}
		
	}

}
