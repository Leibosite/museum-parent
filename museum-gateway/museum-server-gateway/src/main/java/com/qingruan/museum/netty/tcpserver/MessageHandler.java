package com.qingruan.museum.netty.tcpserver;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.Collection;
import java.util.List;

import com.qingruan.museum.msg.sensor.SensorOperatingMode.OperatingMode;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author jcy
 * @description: 侦测通信通道状态的处理器
 */
@Slf4j
public class MessageHandler extends ChannelInboundHandlerAdapter {
	// 控制调试模式的变量 默认关闭调试
	public static volatile OperatingMode operatingMode = OperatingMode.PRODUCTION;
	private Dispatcher dispatcher;

	public MessageHandler() {
		this.dispatcher = new Dispatcher();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		/**
		 * 检测msgCache里面是否有此缓存，没有就加上
		 */
		// System.out.println("channel is active"+ctx.channel());
		log.info("channel is active and channel is" + ctx.channel());
		if (!CommonData.msgCache.containsKey(ctx.channel())) {
			CommonData.msgCache.put(ctx.channel(), new StringBuilder());
		}
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		super.channelUnregistered(ctx);
		log.info("channel is Unregistered and channel is" + ctx.channel());
		Channel channel = ctx.channel();
		if (CommonData.msgCache.containsKey(channel)) {
			CommonData.msgCache.remove(channel);
		}

		Collection<Channel> channelCollection = CommonData.deviceChannels
				.values();
		if (channelCollection.contains(channel)) {
			channelCollection.remove(channel);
		}

		Collection<Channel> airChannelCollection = CommonData.airCleanerChannels
				.values();
		if (airChannelCollection.contains(channel)) {
			CommonData.airCleanerCounter.remove(channel);
			airChannelCollection.remove(channel);
		}

		Collection<Channel> thChannelCollection = CommonData.thChannels
				.values();
		if (thChannelCollection.contains(channel)) {
			thChannelCollection.remove(channel);
		}

	}

	/**
	 * 原来的通信的格式的解析完全的适用：0D0A0D0A3B800000000002B001FFFFFFFF08EE0D0A0D0A 可以转换成
	 * 3B800000000002B001FFFFFFFF08EE
	 * 
	 * 
	 * 新版的通信协议 ：0D0A0D0A3B8000000000000200000000000201B0FFFFFFFF08EE0D0A0D0A
	 * 也可以转换成 ： 3B8000000000000200000000000201B0FFFFFFFF08EE
	 * 
	 */
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		// 到CommonData中根据channel来截取可被解析的完整的数据包
		List<String> msgList = UnPack.msgDeal(channel);
		System.out.println(msgList.toString());
		// 如果等于OperatingMode.DEBUG就忽略，接受engine端发送的模式
		if (msgList == null || msgList.size() == 0
				|| OperatingMode.DEBUG == operatingMode)
			return;

		for (int i = 0; i < msgList.size(); i++) {
			String msgData = msgList.get(i);
			// 处理接受的消息
			dispatcher.receiveMsg(msgData, channel);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

		log.info("channel is exception and channel is" + ctx.channel());
		Channel channel = ctx.channel();
		channel.close();

		if (CommonData.msgCache.containsKey(channel)) {
			CommonData.msgCache.remove(channel);
		}

		if (CommonData.deviceChannels.containsValue(channel)) {
			Collection<Channel> channelCollection = CommonData.deviceChannels
					.values();
			if (channelCollection.contains(channel)) {
				channelCollection.remove(channel);
			}
		}

		if (CommonData.airCleanerChannels.containsValue(channel)) {
			Collection<Channel> channelCollection = CommonData.airCleanerChannels
					.values();
			if (channelCollection.contains(channel)) {
				CommonData.airCleanerCounter.remove(channel);
				channelCollection.remove(channel);
			}
		}

		if (CommonData.thChannels.containsValue(channel)) {
			Collection<Channel> channelCollection = CommonData.thChannels
					.values();
			if (channelCollection.contains(channel)) {
				channelCollection.remove(channel);
			}
		}
		log.error("Message Handler Exception:" + cause.getMessage());
	}
}
