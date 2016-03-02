package com.qingruan.museum.netty.tcpserver;

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
import com.qingruan.museum.netty.task.AirCleanerTask;
import com.qingruan.museum.netty.task.ScheduleTask;
import com.qingruan.museum.netty.task.THTimeTask;

/**
 * 初始化netty channel handler 启动服务器
 * @author jcy
 *
 */

@Component("masterServer")
@Slf4j
public class MasterServer {

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
					channel.pipeline().addLast(new Decoder());
					channel.pipeline().addLast(new Encoder());
					channel.pipeline().addLast(new MessageHandler());
				}
			});

			bootStrap.childOption(ChannelOption.SO_KEEPALIVE, true);
			bootStrap.childOption(ChannelOption.SO_REUSEADDR, true);
			bootStrap.childOption(ChannelOption.TCP_NODELAY, true);
			
			//绑定监听的ip地址、端口号
			List<HashMap<String,Object>> ipList = PropertiesUtils.readIpAddressesFromConfigFile("gateway.addr.urls");
			for (HashMap<String, Object> hashMap : ipList) {
				bootStrap.bind(hashMap.get("ip").toString(), Integer.valueOf(hashMap.get("port").toString()));
				log.info("IP地址是"+hashMap.get("ip")+":"+hashMap.get("port")+"的GateWay启动...");
			}
			
			ScheduleTask.startScheduleTask();
			THTimeTask.runTimeTask();
			AirCleanerTask.runTimeTask();
	
			/* 			
			 * 0、查询		6、负离子
			 * 1、开启		7、紫外灯
			 * 2、关闭		8、加湿
			 * 3、风速		9、童锁
			 * 4、自动模式	10、滤网时间重置
			 * 5、定时关机
			 * 
			 */
			
			/*new Thread(new Runnable() {
				private Scanner scanner;
				
				@Override
				public void run() {
					
					while(true){
						//System.out.println("您好，请您输入远程空气净化器指令：\n");
						scanner = new Scanner(System.in);
						int remoteCMD = scanner.nextInt();

						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						AirDownDataPackage air = new AirDownDataPackage();
						air.remoteTest(remoteCMD, "ACCF236C4378");
						if(remoteCMD!=-1){
							continue;
						}else{
							System.exit(0);
						}
					}
					
				}
			}).start();*/
			
			
		} catch (Exception e) {
			log.error("TCPServer错误：" + e.toString());
			shutDownTCPServer();
		}

	}

	/**
	 * destory Method
	 */
	// TODO 关闭TCPServer,用Spring托管
	public void shutDownTCPServer() {
		System.out.println("关闭Netty");
		if (boss != null && worker != null) {
			System.out.println("关闭TCPServer...");
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}

}
