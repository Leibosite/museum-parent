package com.qingruan.museum.netty.gprs;

import java.nio.charset.Charset;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import com.qingruan.museum.framework.util.EncoderUtil;
import com.qingruan.museum.gateway.common.netty.SendMessage;
import com.qingruan.museum.netty.gprs.GPRSCommand.Command;
import com.qingruan.museum.netty.microclient.CommonData;

@Slf4j
public class GPRSMessageHandler extends SimpleChannelInboundHandler<String> {

	// Registered -> Active -> Read -> ReadComplete -> InActive ->UnRegistered

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

		
		if (CommonData.gprsChannel==null) {
			CommonData.gprsChannel = ctx.channel();
		}
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg)
			throws Exception {
		log.info(msg);
		String receiveMsg = msg.length()>=2 ? msg.substring(0,msg.length()-2) : "";
		if(receiveMsg.length()>5 && GPRSCommand.AT_DATA.equals(receiveMsg.substring(0, 5))){
			int index = receiveMsg.indexOf(":");
			log.info(EncoderUtil.getHexStr(msg.substring(index+1).getBytes(Charset.forName("ISO-8859-1"))));
			if(CommonData.serverGatewayChannel!=null){			
				SendMessage.sendByteData(CommonData.serverGatewayChannel, msg.substring(index+1).getBytes(Charset.forName("ISO-8859-1")));
			}
			
		}else if(receiveMsg.length()>11 && GPRSCommand.AT_SET_LEN.equals(receiveMsg.substring(0, 11))){
			CommonData.steps--;
			String message = CommonData.gprsQueue.peek();
			ctx.channel().writeAndFlush(message+GPRSCommand.AT_END);
			log.info("send message success:\n"+message+"\n");
		}else if(GPRSCommand.AT_SEND_OK.equals(receiveMsg)){
			CommonData.steps--;
			if(CommonData.steps==0){
				CommonData.command = Command.AT_SEND_OK;
				CommonData.gprsQueue.poll();
			}
		}else if(GPRSCommand.AT_SEND_FA.equals(receiveMsg)){
			CommonData.steps--;
			CommonData.command = Command.AT_SEND_FA;
		}else if(GPRSCommand.AT_SET_INIT.equals(receiveMsg)){
			CommonData.steps--;
		}else if(GPRSCommand.AT_HEAD.equals(receiveMsg)){
			CommonData.steps--;
		}else if(GPRSCommand.AT_SET_IP.equals(receiveMsg)){
			CommonData.steps--;
		}else if(GPRSCommand.AT_OK.equals(receiveMsg)){
			CommonData.steps--;
			if(CommonData.steps==0){
				
				switch (CommonData.command) {
				case AT_SET_INIT:
				{
					ctx.channel().writeAndFlush(GPRSCommand.AT_HEAD+GPRSCommand.AT_END);
					CommonData.steps = 2;
					CommonData.command = Command.AT_HEAD;
				}
					break;
				case AT_HEAD:
				{
					ctx.channel().writeAndFlush(GPRSCommand.AT_SET_IP+GPRSCommand.AT_END);
					CommonData.steps = 3;
					CommonData.command = Command.AT_SET_IP;
				}
					break;

				default:
					break;
				}
				
			}
		}else if(GPRSCommand.AT_CONN_OK.equals(receiveMsg)){
			CommonData.steps--;
			if(CommonData.steps==0){
				CommonData.command = Command.AT_CONN_OK;
			}
		}else if(GPRSCommand.AT_CONN_FA.equals(receiveMsg)){
			CommonData.command = Command.AT_CONN_FA;		
		}else if(GPRSCommand.AT_CLOSE.equals(receiveMsg)){
			CommonData.command = Command.AT_CLOSE;
			CommonData.steps--;			
		}else if(GPRSCommand.AT_CLOSE_OK.equals(receiveMsg)){
			CommonData.steps--;
			if(CommonData.steps==0){
				ctx.channel().writeAndFlush(GPRSCommand.AT_SHUT+GPRSCommand.AT_END);
				CommonData.steps = 2;
				CommonData.command = Command.AT_SHUT;
			}
		}else if(GPRSCommand.AT_SHUT.equals(receiveMsg)){
			CommonData.steps--;
		}else if(GPRSCommand.AT_SHUT_OK.equals(receiveMsg)){
			CommonData.steps--;
			if(CommonData.steps==0){
				CommonData.command = Command.AT_SHUT_OK;
			}
		}else if(GPRSCommand.AT_ERROR.equals(receiveMsg)){
			if(CommonData.command==Command.AT_SET_INIT){
				ctx.channel().writeAndFlush(GPRSCommand.AT_SHUT+GPRSCommand.AT_END);
				CommonData.steps = 2;
				CommonData.command = Command.AT_SHUT;
			}
		}else if(GPRSCommand.AT_CONN_CLOSE.equals(receiveMsg)){
			CommonData.command = Command.AT_CONN_CLOSE;
		}
	}


	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {

		log.info("GPRS Unregistered and channel is" + ctx.channel());
		
		if (CommonData.gprsChannel!=null) {		
			CommonData.gprsChannel = null;
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		if (CommonData.gprsChannel!=null) {
			CommonData.gprsChannel=null;		
		}
		log.error("Message Handler Exception:" + cause.getMessage());
	}
	
	public void connectGprs(){
		if(CommonData.gprsChannel!=null){
			CommonData.gprsChannel.writeAndFlush(GPRSCommand.AT_SET_INIT);
		}
	}
	
	public void closeGprs(){
		if(CommonData.gprsChannel!=null){
			CommonData.gprsChannel.writeAndFlush(GPRSCommand.AT_SHUT);
		}
	}
}
