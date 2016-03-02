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

import com.qingruan.museum.framework.esb.redispushpop.RedisMQBlpopReceiver;
import com.qingruan.museum.framework.esb.redispushpop.RedisMQBroker;
import com.qingruan.museum.gateway.common.exception.ExceptionLog;
import com.qingruan.museum.netty.modbus.ModbusServer;
import com.qingruan.museum.netty.tcpserver.MasterServer;
import com.qingruan.museum.netty.tcpserver.ReceiveEngineMessage;

/**
 * @author tommy
 * 
 */
@Getter
@Setter
@Slf4j
public class InitialServerGatewaySystem implements Serializable {
	private static final long serialVersionUID = -120011L;
	@Autowired
	private MasterServer masterServer;
	@Autowired
	private RedisMQBroker redisMQBroker;
	@Autowired
	private ModbusServer modbusServer;

	public InitialServerGatewaySystem() {
	}

	private String message;

	/**
	 * 初始化方法
	 */
	public void init() {
		log.info("----------------------------Museum-Gateway is starting-------------------------------");
		
			if (masterServer == null)
				System.out.println("协议栈-Netty为空");
			else{
	
				try {
					
						masterServer.runServer();
						System.out.println("协议栈" + "masterServer.runServer()");
									
				} catch (Exception e) {
					log.error("gateway:"+e.getMessage());
	
				}
			}
				
			if (modbusServer == null)
				System.out.println("Modbus-Netty为空");
			else{
	
				try {
					
					modbusServer.runServer();
					System.out.println("modbusServer.runServer()");
									
				} catch (Exception e) {
					log.error("modbusServer:"+e.getMessage());
	
				}
			}
			
			if (redisMQBroker == null)
				System.out.println("Redis消息队列代理为空");
			else {

				try {
					RedisMQBlpopReceiver redisMQBlpopReceiver = new RedisMQBlpopReceiver();
					this.registeredListener(redisMQBlpopReceiver);
					redisMQBroker.startListenRedisMQ(redisMQBlpopReceiver);
				} catch (Exception e) {
					log.error(ExceptionLog.getErrorStack(e));
					throw new RuntimeException();
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
		if (masterServer == null)
			System.out.println("协议栈-Netty为空");
		else
			masterServer.shutDownTCPServer();
		
		if(modbusServer==null){
			System.out.println("modbusServer-Netty为空");
		}else
			modbusServer.shutDownModbusServer();;
			
	}

	protected void registeredListener(RedisMQBlpopReceiver redisMQBlpopReceiver) {
		try {

			System.out.println("开始注册消息队列EngineToNettyMsgListener");
			redisMQBlpopReceiver.setMessageListener(new ReceiveEngineMessage());
			System.out.println("结束注册消息队列EngineToNettyMsgListener");
		} catch (Exception e) {

			e.printStackTrace();
			throw new RuntimeException("{registeredListener runtime exception!}");
		}

	}
}
