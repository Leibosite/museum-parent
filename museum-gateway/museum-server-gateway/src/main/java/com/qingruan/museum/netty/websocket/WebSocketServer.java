package com.qingruan.museum.netty.websocket;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import java.util.Date;

import org.springframework.stereotype.Component;

@Component("webSocketServer")
public class WebSocketServer {
	private EventLoopGroup boss;
	private EventLoopGroup worker;

	public void runServer() {
		boss = new NioEventLoopGroup();
		worker = new NioEventLoopGroup();

		try {
			ServerBootstrap bootStrap = new ServerBootstrap();
			bootStrap.group(boss, worker);
			bootStrap.channel(NioServerSocketChannel.class);
			bootStrap.childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline pipeline = ch.pipeline();
					pipeline.addLast("decoder", new HttpRequestDecoder());
					pipeline.addLast("aggregator", new HttpObjectAggregator(
							65536));
					pipeline.addLast("encoder", new HttpResponseEncoder());
					pipeline.addLast("handler", new WebSocketHandler());
				}
			});

			bootStrap.childOption(ChannelOption.SO_KEEPALIVE, true);
			bootStrap.childOption(ChannelOption.SO_REUSEADDR, true);
			bootStrap.childOption(ChannelOption.TCP_NODELAY, true);
			bootStrap.bind(16888);
			System.out.println("16888端口的WebSocketServer启动...");
//			log.info("8989端口的WebSocketServer启动...");
		} catch (Exception e) {
//			log.error("WebSocketServer error:" + e.toString());
			shutDownWebSocketServer();
		}
	}
	
	public static void main(String[] args) {
		WebSocketServer server = new WebSocketServer();
		server.runServer();
		
		while (true) {
			WebSocketHandler.pushMsg("报警！报警！报警！报警！报警！报警！报警！报警！");
			System.out.println("***发送报警信息***"+new Date());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// TODO 关闭WebSocketServer,用Spring托管
	public void shutDownWebSocketServer() {
		if (boss != null && worker != null) {
			System.out.println("关闭WebSocketServer...");
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}
	
}
