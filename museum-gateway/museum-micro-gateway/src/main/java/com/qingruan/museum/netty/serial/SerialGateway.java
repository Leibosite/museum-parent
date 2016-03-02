package com.qingruan.museum.netty.serial;

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
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import com.qingruan.museum.framework.util.PropertiesUtils;
import com.qingruan.museum.netty.microclient.MicroDecoder;
import com.qingruan.museum.netty.microclient.MicroEncoder;

@Slf4j
@Component("serialGateway")
public class SerialGateway {
	
	static final String PORT = System.getProperty("port", PropertiesUtils.readValue("serialgateway.serialport").trim());
	
    public void runSerialGateway(){
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
                	 byte[] limiter = {0x7F,0x7F,0x7F,0x7F};
                	 ByteBuf delimiter = Unpooled.copiedBuffer(limiter);
                     ch.pipeline().addLast(                    		 
                    	 new DelimiterBasedFrameDecoder(8192,true,delimiter),
                         new MicroDecoder(),
                         new MicroEncoder(),
                         new RxtxClientHandler()
                     );
                 }
             });
            
            ChannelFuture f = b.connect(new RxtxDeviceAddress(PORT));
            f.addListener(new ChannelFutureListener() {
				public void operationComplete(ChannelFuture f) throws Exception {
					if(f.isSuccess()){
						log.info("433模块串口连接成功");
						System.out.println("433模块串口连接成功");
					}else{
						log.error("433模块串口连接不成功");
						System.out.println("433模块串口连接失败");
						f.channel().close();
					}
				}
			});
            
            /*new Thread(new Runnable() {
            	private Scanner scanner;
				public void run() {
					
					while(true){
						System.out.println("您好，请您输入AT指令：\n");
						scanner = new Scanner(System.in);
						String atCMD = scanner.nextLine()+"\n";
						if(CommonData.serialChannel!=null){
							CommonData.serialChannel.writeAndFlush(atCMD.getBytes());
						}
						if(!"end".equals(atCMD)){
							continue;
						}else{
							System.exit(0);
						}
					}
					
				}
			}).start();*/
        } finally {
            group.shutdownGracefully();
        }
    }

}
