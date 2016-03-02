package com.qingruan.museum.admin.web.websocket;


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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import net.sf.json.JSONObject;
 

@Controller
@RequestMapping(value = "/websocket")
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
			bootStrap.bind(8989);
			System.out.println("8989端口的WebSocketServer启动...");
//			log.info("8989端口的WebSocketServer启动...");
		} catch (Exception e) {
//			log.error("WebSocketServer error:" + e.toString());
			shutDownWebSocketServer();
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
	 
	@RequestMapping(value = "/run")
	public void run() {
		WebSocketServer server = new WebSocketServer();
		server.runServer();
		while (true) {
			int n = (int) (Math.random() * 100);
			JSONObject result = new JSONObject(); // Object对象
			result.put("x", new Date().getTime());// X轴时间
			result.put("y", n); // Y轴随机数
			System.out.println(result.toString());
			WebSocketHandler.pushMsg(result.toString());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	 public static void main(String[] args) {
		 WebSocketServer server = new WebSocketServer();
		 server.runServer();
		 
		 while(true){
			 int n = (int)(Math.random()*100);  
			 JSONObject result=new JSONObject(); //Object对象
			 result.put("x", new Date().getTime());//X轴时间
			 result.put("y", n); //Y轴随机数
			 System.out.println(result.toString());
			 WebSocketHandler.pushMsg(result.toString()); 
			 try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		 }
	 }
}
