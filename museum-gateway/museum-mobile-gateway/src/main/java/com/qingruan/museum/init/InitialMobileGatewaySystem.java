/**
 2014年11月28日
 14cells
 
 */
package com.qingruan.museum.init;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import com.qingruan.museum.framework.util.PropertiesUtils;
import com.qingruan.museum.netty.tcpclient.THClient;

/**
 * @author tommy
 * 
 */
@Getter
@Setter
@Slf4j
public class InitialMobileGatewaySystem implements Serializable {
	private static final long serialVersionUID = -120011L;
	@Autowired
	private THClient     thClient;

	public InitialMobileGatewaySystem() {
	}

	private String message;

	/**
	 * 初始化方法
	 */
	public void init() {
		log.info("----------------------------Museum-Gateway is starting-------------------------------");
		
		
		if(thClient==null){
			System.out.println("thClient没注入进来无法运行");
		}
		else {
			try {
				
					thClient.runClients();
					System.out.println("thClient开始运行");
				
			} catch (Exception e) {
				log.error("thClient:"+e.getMessage());
			}
		}
		
		log.info("规则引擎已经启动");

		System.out.println("Netty网关已经启动");
		System.out.println("消息系统已经启动");
		System.out.println("----------------------------Museum-Gateway-System is started-------------------------------");
		log.info("----------------------------Museum-Gateway-System is started-------------------------------");
	}

	/**
	 * 关闭方法
	 */
	public void destory() {
		// 关闭 Netty
		System.out.println("springSystemStartService 被关闭");
		
		if(thClient==null){
			System.out.println("thClient为空无需关闭");
		}
		else {
			thClient.closeAllClient();
		}
	}
}
