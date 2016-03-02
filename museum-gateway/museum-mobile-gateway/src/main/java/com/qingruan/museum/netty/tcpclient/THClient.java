package com.qingruan.museum.netty.tcpclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.locks.LockSupport;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import com.qingruan.museum.framework.util.PropertiesUtils;
import com.qingruan.museum.gateway.common.netty.SendMessage;

/*
 * kevin
 * 
 * T:temperature
 * H:humidity
 */

@Component("thClient")
@Slf4j
public class THClient {
	private EventLoopGroup worker;
	 
	public  void runClients(){
		worker = new NioEventLoopGroup();
		
		try {
			final Bootstrap clientStrap = new Bootstrap();
			clientStrap.group(worker);
			clientStrap.channel(NioSocketChannel.class);
			clientStrap.option(ChannelOption.SO_KEEPALIVE, true);
			clientStrap.option(ChannelOption.TCP_NODELAY, true);
			clientStrap.option(ChannelOption.SO_REUSEADDR, true);
			clientStrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
			
			clientStrap.handler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel channel) throws Exception {
					channel.pipeline().addLast(new ThDecoder());
					channel.pipeline().addLast(new ThEncoder());
					channel.pipeline().addLast(new THMessageHandler());
					
				}
			});
			
			final List<HashMap<String,Object>> ipList = PropertiesUtils.readIpAddressesFromConfigFile("gateway.addr.urls");
			final List<HashMap<String,Object>> clientList = PropertiesUtils.readIpAddressesFromConfigFile("thclient.addr.urls");
			
			ScheduledExecutorService service = Executors.newScheduledThreadPool(1+clientList.size());
			
			
			final Step numStep = new Step();
			numStep.setIpNumber(ipList.size());
			service.execute(new Runnable() {
				
				@Override
				public void run() {
					Step state = new Step();
					
					while(true){
						HashMap<String,Object> gatewayInfo = ipList.get(numStep.getIpIndex());
						String ipAddress = gatewayInfo.get("ip").toString()+":"+gatewayInfo.get("port").toString();
						CommonData.connectThreads.put(ipAddress, Thread.currentThread());
						connect(clientStrap, gatewayInfo,state);
						try {
							Thread.sleep(getReconnectOvertime());
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if(state.isSuccess){
							CommonData.connectGateWaySuccessIp = ipAddress;
							LockSupport.park();
						}else{
							if(state.number<4){
								log.info("IP地址是"+gatewayInfo.get("ip")+":"+gatewayInfo.get("port")+"重连第"+state.number+"次");
								continue;
							}else{
								state.number = 0;
								log.info("IP地址是"+gatewayInfo.get("ip")+":"+gatewayInfo.get("port")+"已经重连三次失败,切换IP");
								if(numStep.nextIp()){									
									continue;
								}else{
									continue;
								}
								
							}
						}
						
					}
				}
			});
			
			for(int i = 0; i < clientList.size();i++){
				final Step thStep = new Step();
				thStep.setNumber(i);
				service.execute(new Runnable() {
					
					@Override
					public void run() {
						Step state = new Step();
						while(true){
							HashMap<String,Object> gatewayInfo = clientList.get(thStep.getNumber());
							String ipAddress = gatewayInfo.get("ip").toString()+":"+gatewayInfo.get("port").toString();
							CommonData.connectThreads.put(ipAddress, Thread.currentThread());
							connect(clientStrap, gatewayInfo,state);
							
							try {
								Thread.sleep(getReconnectOvertime());
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
								
							if(state.isSuccess){
								LockSupport.park();
							}else{
								if(state.number<4){
									log.info("IP地址是"+gatewayInfo.get("ip")+":"+gatewayInfo.get("port")+"重连第"+state.number+"次");
									continue;
								}else{
									state.number = 0;
									log.info("IP地址是"+gatewayInfo.get("ip")+":"+gatewayInfo.get("port")+"已经重连三次失败,告警中控机挂掉");
									continue;
								}
							}
						}
					}
				});
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	
	public void connect(final Bootstrap clientStrap,final HashMap<String,Object> gatewayIpInfo,final Step step)
	{
		step.number++;
		ChannelFuture future = clientStrap.connect(gatewayIpInfo.get("ip").toString(), Integer.valueOf(gatewayIpInfo.get("port").toString()));
		
		future.addListener(new ChannelFutureListener() {
			
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if(future.isSuccess()){
					log.info("IP地址是"+gatewayIpInfo.get("ip")+":"+gatewayIpInfo.get("port")+"的THClient启动成功");
					SendMessage.sendMsg(future.channel(), "0D0A0D0AB0FFFFFFFF0D0A0D0A");
					step.setSuccess(true);
				}else
				{
					step.setSuccess(false);
					log.error("IP地址是"+gatewayIpInfo.get("ip")+":"+gatewayIpInfo.get("port")+"的THClient启动失败");					
				}				
			}
		});
	}
	
	public int getReconnectOvertime(){
		String reconnectTimeStr = PropertiesUtils.readValue("thclient.reconnect.overtime");
		return Integer.valueOf(reconnectTimeStr, 10) * 1000;
	}
	
	
	@Data
	public class Step{
		private int number = 0;
		private int ipNumber = 0;
		private int ipIndex = 0;
		private boolean isSuccess = false;
		
		public Boolean nextStep(){
			if(this.number<3){
				number++;
				return true;
			}
			return false;
		}
		
		public Boolean nextIp(){
			Boolean flag = false;
			if(this.ipIndex<ipNumber-1){
				ipIndex++;
				flag = true;
			}else{
				ipIndex = 0;
				flag = false;
			}
			return flag;
		}
	}
	
	public void closeAllClient(){
		log.info("关闭所有的Client连接");
		if(worker!=null){
			worker.shutdownGracefully();
		}
	}
}
