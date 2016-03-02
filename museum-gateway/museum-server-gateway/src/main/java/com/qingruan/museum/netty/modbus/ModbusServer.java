package com.qingruan.museum.netty.modbus;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.HashMap;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import com.qingruan.museum.framework.util.PropertiesUtils;
import com.qingruan.museum.netty.tcpserver.Encoder;

@Slf4j
@Component("modbusServer")
public class ModbusServer {
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
					channel.pipeline().addLast(new ModbusDecoder());
					channel.pipeline().addLast(new Encoder());
					channel.pipeline().addLast(new ModbusHandler());
				}
			});

			bootStrap.childOption(ChannelOption.SO_KEEPALIVE, true);
			bootStrap.childOption(ChannelOption.SO_REUSEADDR, true);
			bootStrap.childOption(ChannelOption.TCP_NODELAY, true);
			
			List<HashMap<String,Object>> ipList = PropertiesUtils.readIpAddressesFromConfigFile("modbusserver.addr.urls");
			for (HashMap<String, Object> hashMap : ipList) {
				bootStrap.bind(hashMap.get("ip").toString(), Integer.valueOf(hashMap.get("port").toString()));
				log.info("IP地址是"+hashMap.get("ip")+":"+hashMap.get("port")+"的GateWay启动...");
			}
			ModbusTask.startScheduleTask();
		} catch (Exception e) {
			log.error("ModbusServer错误：" + e.toString());
			shutDownModbusServer();;
		}

	}

	/**
	 * destory Method
	 */
	public void shutDownModbusServer() {
		System.out.println("关闭Netty");
		if (boss != null && worker != null) {
			System.out.println("关闭TCPServer...");
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}
}
