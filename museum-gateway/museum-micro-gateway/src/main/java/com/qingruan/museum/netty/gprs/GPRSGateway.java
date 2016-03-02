

package com.qingruan.museum.netty.gprs;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.rxtx.RxtxChannel;
import io.netty.channel.rxtx.RxtxChannelConfig;
import io.netty.channel.rxtx.RxtxChannelConfig.Databits;
import io.netty.channel.rxtx.RxtxChannelConfig.Paritybit;
import io.netty.channel.rxtx.RxtxChannelConfig.Stopbits;
import io.netty.channel.rxtx.RxtxDeviceAddress;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import com.qingruan.museum.framework.util.PropertiesUtils;
import com.qingruan.museum.gateway.common.exception.ExceptionLog;
import com.qingruan.museum.netty.gprs.GPRSCommand.Command;
import com.qingruan.museum.netty.microclient.CommonData;

@Slf4j
@Component("gprsGateway")
public class GPRSGateway {
	static final String PORT = System.getProperty("port", PropertiesUtils.readValue("gprsgateway.serialport").trim());
	
	public void runGprsGateway(){
		EventLoopGroup group = new OioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(RxtxChannel.class)
             .handler(new ChannelInitializer<RxtxChannel>() {
                 @Override
                 public void initChannel(RxtxChannel ch) throws Exception {
                	 
                	 RxtxChannelConfig config = ch.config();
                	 config.setBaudrate(9600);
                	 config.setDatabits(Databits.DATABITS_8);
                	 config.setStopbits(Stopbits.STOPBITS_1);
                	 config.setParitybit(Paritybit.NONE);
                	 config.setDtr(true);
                	 byte[] limiter = {0x0D,0x0A};
                	 ByteBuf delimiter = Unpooled.copiedBuffer(limiter);
                     ch.pipeline().addLast(                    		 
                    	 new DelimiterBasedFrameDecoder(8192,false,delimiter),
                    	 new StringDecoder(Charset.forName("ISO-8859-1")),
                         new StringEncoder(Charset.forName("ISO-8859-1")),
                         new GPRSMessageHandler()
                     );
                 }
             });
            
            ChannelFuture f = b.connect(new RxtxDeviceAddress(PORT));
            f.addListener(new ChannelFutureListener() {
				public void operationComplete(ChannelFuture f) throws Exception {
					if(f.isSuccess()){
						log.info("GPRS串口连接成功");
						System.out.println("GPRS串口连接成功");
					}else{
						log.error("GPRS串口连接不成功");
						System.out.println("GPRS串口连接失败");
					}
				}
			});
            
            
            
            ExecutorService service = Executors.newSingleThreadExecutor();
            
            service.execute(new Runnable() {
				
				@Override
				public void run() {
					while(true){
						
						if(CommonData.gprsChannel!=null && (CommonData.command==Command.AT_CONN_CLOSE || CommonData.command == Command.AT_CONN_FA) ){
							try {
								Thread.sleep(20*1000);
							} catch (InterruptedException e) {
								log.error(ExceptionLog.getErrorStack(e));
							}
							CommonData.gprsChannel.writeAndFlush(GPRSCommand.AT_SET_IP+GPRSCommand.AT_END);
							CommonData.steps = 3;
							CommonData.command = Command.AT_SET_IP;
						}
						
						if(CommonData.gprsChannel!=null && CommonData.command==Command.AT_SHUT_OK){
							CommonData.gprsChannel.writeAndFlush(GPRSCommand.AT_SET_INIT+GPRSCommand.AT_END);
							CommonData.command = Command.AT_SET_INIT;
							CommonData.steps = 2;
						}
						
						String message = CommonData.gprsQueue.peek();
						
						if(message!=null && CommonData.gprsChannel!=null &&
								(CommonData.command==Command.AT_CONN_OK || CommonData.command==Command.AT_SEND_OK)){
							log.info("start to send message...");
							CommonData.gprsChannel.writeAndFlush(GPRSCommand.AT_SET_LEN+message.length()+GPRSCommand.AT_END);
							CommonData.command = Command.AT_SET_LEN;
							CommonData.steps = 2;
						}
						
						if(message!=null && CommonData.gprsChannel!=null &&
								CommonData.command==Command.AT_SEND_FA){
							if(CommonData.gprsSendNumber<CommonData.gprsResendTimes){
								CommonData.gprsChannel.writeAndFlush(GPRSCommand.AT_SET_LEN+message.length()+GPRSCommand.AT_END);
								CommonData.command = Command.AT_SET_LEN;
								CommonData.steps = 2;
								
								CommonData.gprsSendNumber++;
							}else{
								
								CommonData.gprsChannel.writeAndFlush(GPRSCommand.AT_CLOSE+GPRSCommand.AT_END);
								CommonData.command = Command.AT_CLOSE;
								CommonData.steps = 2;
								CommonData.gprsSendNumber=0;
							}
							
						}
						
						
						
						try {
							Thread.sleep(500);
							//log.info("- - - - - - - - - -");
						} catch (InterruptedException e) {
							log.error(ExceptionLog.getErrorStack(e));
						}
					}
					
				}
			});
            
            new Thread(new Runnable() {
            	private Scanner scanner;
				public void run() {
					
					while(true){
						System.out.println("您好，请您输入AT指令：\n");
						scanner = new Scanner(System.in);
						String atCMD = scanner.nextLine()+"\n";
						if(CommonData.gprsChannel!=null){
							if("1\n".equals(atCMD)){
								CommonData.gprsChannel.writeAndFlush(GPRSCommand.AT_SET_INIT+GPRSCommand.AT_END);
								CommonData.command = Command.AT_SET_INIT;
								CommonData.steps = 2;
							}else if("2\n".equals(atCMD)){
								CommonData.gprsChannel.writeAndFlush(GPRSCommand.AT_SHUT+GPRSCommand.AT_END);
							}
							
						}
						if(!"end".equals(atCMD)){
							continue;
						}else{
							System.exit(0);
						}
					}
					
				}
			}).start();
            
        }finally {
                group.shutdownGracefully();
        }
	}
	
}
