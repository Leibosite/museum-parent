package com.qingruan.museum.netty.microserver;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import com.qingruan.museum.framework.util.PropertiesUtils;
import com.qingruan.museum.netty.microclient.MicroDecoder;
import com.qingruan.museum.netty.microclient.MicroEncoder;
import com.qingruan.museum.netty.microclient.MicroMessageHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

@Slf4j
@Component("microServer")
public class MicroServer {
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
				protected void initChannel(SocketChannel channel)
						throws Exception {
					channel.pipeline().addLast(new MicroDecoder());
					channel.pipeline().addLast(new MicroEncoder());
					channel.pipeline().addLast(new MicroMessageHandler());
				}
			});

			bootStrap.childOption(ChannelOption.SO_KEEPALIVE, true);
			bootStrap.childOption(ChannelOption.SO_REUSEADDR, true);
			bootStrap.childOption(ChannelOption.TCP_NODELAY, true);
			
			List<HashMap<String,Object>> ipList = PropertiesUtils.readIpAddressesFromConfigFile("microserver.urls");
			for (HashMap<String, Object> hashMap : ipList) {
				bootStrap.bind(hashMap.get("ip").toString(), Integer.valueOf(hashMap.get("port").toString()));
				log.info("IP地址是"+hashMap.get("ip")+":"+hashMap.get("port")+"的MicroServer启动...");
			}
			
		} catch (Exception e) {
			log.error("TCPServer错误：" + e.toString());
		}
	}
	
	public void shutdownMicroServer(){
		if (boss != null && worker != null) {
			System.out.println("关闭MicroServer...");
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}
}
