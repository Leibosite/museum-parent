package com.qingruan.museum.netty.httpsqs;

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

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qingruan.framework.redis.ShardedJedisSentinelPoolTemplate;
import com.qingruan.museum.engine.MuseumEngine;
import com.qingruan.museum.engine.service.rest.RestfulService;
import com.qingruan.museum.framework.util.PropertiesUtils;

@Slf4j
@Component("httpSQSServer")
public class HttpSQSServer {
//	public static RestfulAction action = null;
	public static ConcurrentHashMap<String, Object> urlAndActionMethondMapingMap = null;
	private EventLoopGroup boss;
	private EventLoopGroup worker;
	// 初始化配置文件:url和ActionMethond对应关系
	static {

		ObjectMapper mapper = new ObjectMapper();
		try {
			URL url = PropertiesUtils.class.getResource("/urlAndMethondmaping.json");
			URI uri = url.toURI();			
			// read JSON from a file
			File mapFile = new File(uri);
			Map<String, Object> map = mapper.readValue(mapFile,
					new TypeReference<Map<String, Object>>() {
					});
//			action = new RestfulAction();

			urlAndActionMethondMapingMap = new ConcurrentHashMap<String, Object>(map);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Autowired
	private ShardedJedisSentinelPoolTemplate jedisSentinelTemplate;

	public <T> void runServer() {
//		action =  (RestfulAction) MuseumEngine.applicationContextGuardian
//		.GetAppContext().getBean(RestfulAction.class);
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
					pipeline.addLast("handler", new HttpSQSHandler());
				}
			});

			bootStrap.childOption(ChannelOption.SO_KEEPALIVE, true);
			bootStrap.childOption(ChannelOption.SO_REUSEADDR, true);
			bootStrap.childOption(ChannelOption.TCP_NODELAY, true);
			List<HashMap<String, Object>> ipList = PropertiesUtils
					.readIpAddressesFromConfigFile("httpsqs.addr.url");
			for (HashMap<String, Object> hashMap : ipList) {
				bootStrap.bind(hashMap.get("ip").toString(),
						Integer.valueOf(hashMap.get("port").toString()));
				log.info("IP地址是" + hashMap.get("ip") + ":"
						+ hashMap.get("port") + "的HttpSQSServer启动...");

			}

		} catch (Exception e) {
			log.error("HttpSQSServer error:" + e.toString());
			shutDownHttpSQSServer();
		}
	}

	public class Step {
		public long num;
	}

	public static void main(String[] args) {
		HttpSQSServer server = new HttpSQSServer();
		server.runServer();
	}

	// TODO 关闭HttpSQSServer,用Spring托管
	public void shutDownHttpSQSServer() {
		if (boss != null && worker != null) {
			log.error("关闭HttpSQSServer...");
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}

}
