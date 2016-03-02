package com.qingruan.museum.netty.serial;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import com.qingruan.museum.netty.microclient.CommonData;

@Slf4j
public class RxtxClientHandler extends ChannelInboundHandlerAdapter{
	 	/* GPS指令
	 	 * AT+CGPSPWR=0\n 关闭
	 	 * AT+CGPSPWR=1\n 开启
	 	 * 
	 	 */
		public void channelActive(ChannelHandlerContext ctx) {
			if (CommonData.serialChannel==null) {
				CommonData.serialChannel = ctx.channel();
				CommonData.msgCache.put(ctx.channel(), new StringBuilder());
			}
	    }
		
		@Override
		public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

		}

		@Override
		public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {

			log.info("433 Serial Unregistered and channel is" + ctx.channel());
			if (CommonData.serialChannel!=null) {	
				CommonData.serialChannel = null;
				CommonData.msgCache.remove(ctx.channel());
			}
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
				throws Exception {

			if (CommonData.serialChannel!=null) {
				CommonData.serialChannel=null;
				CommonData.msgCache.remove(ctx.channel());
			}
			log.error("Rxtx Handler Exception:" + cause.getMessage());
		}

}
