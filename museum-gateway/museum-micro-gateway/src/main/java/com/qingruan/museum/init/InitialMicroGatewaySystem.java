/**
 2014年11月28日
 14cells
 
 */
package com.qingruan.museum.init;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import com.qingruan.museum.framework.util.PropertiesUtils;
import com.qingruan.museum.gateway.common.exception.ExceptionLog;
import com.qingruan.museum.netty.gprs.GPRSGateway;
import com.qingruan.museum.netty.microclient.CommonData;
import com.qingruan.museum.netty.microclient.MicroClient;
import com.qingruan.museum.netty.microserver.MicroServer;
import com.qingruan.museum.netty.serial.SerialGateway;

/**
 * @author tommy
 * 
 */
@Getter
@Setter
@Slf4j
public class InitialMicroGatewaySystem implements Serializable {
	private static final long serialVersionUID = -120011L;
	@Autowired
	private MicroClient   microClient;
	@Autowired
	private SerialGateway serialGateway;
	@Autowired
	private GPRSGateway   gprsGateway;
	@Autowired
	private MicroServer   microServer;

	public InitialMicroGatewaySystem() {
	}

	private String message;

	/**
	 * 初始化方法
	 */
	public void init() {
		log.info("----------------------------Museum-Gateway is starting-------------------------------");
		
		String microType = PropertiesUtils.readValue("microtype").trim();
		if("server".equals(microType)){
			if(microServer==null){
				System.out.println("microServer没注入进来无法运行");
			}else{
				try {
					
					microServer.runServer();
					System.out.println("microServer开始运行");
					
				} catch (Exception e) {
					log.error("microServer:"+e.getMessage());
				}
			}
			
		}else{
			if(microClient==null){
				System.out.println("microClient没注入进来无法运行");
			}
			else {
				try {
					
					microClient.runClients();					
				} catch (Exception e) {
					log.error("microClient:"+e.getMessage());
				}
			}
		}
		
		ExecutorService executor = Executors.newFixedThreadPool(2);
		
		if("on".equals(CommonData.fourTTPower)){
			executor.execute(new Runnable() {
				
				@Override
				public void run() {
					
					if(serialGateway==null){
						System.out.println("serialGateway没注入进来无法运行");
					}else{
						try {
							serialGateway.runSerialGateway();
							System.out.println("433串口网关开始运行");
						} catch (Exception e) {
							e.printStackTrace();
							log.error(ExceptionLog.getErrorStack(e));
						}
					}
					
				}
			});
		}
		
		
		if("on".equals(CommonData.gprsPower)){
			executor.execute(new Runnable() {
				
				@Override
				public void run() {
					
					if(gprsGateway==null){
						System.out.println("serialGateway没注入进来无法运行");
					}else{
						try {
							gprsGateway.runGprsGateway();
							System.out.println("GPRS串口网关开始运行");
						} catch (Exception e) {
							e.printStackTrace();
							log.error(ExceptionLog.getErrorStack(e));
						}
					}
					
				}
			});
		}

		System.out.println("----------------------------Museum-Gateway-System is started-------------------------------");
		log.info("----------------------------Museum-Gateway-System is started-------------------------------");
	}

	/**
	 * 关闭方法
	 */
	public void destory() {
		
		if(microClient==null){
			System.out.println("microClient为空无需关闭");
		}
		else {
			microClient.closeAllClient();
		}
		
		if(microServer==null){
			System.out.println("microServer为空无需关闭");
		}else{
			microServer.shutdownMicroServer();
		}
	}
}
