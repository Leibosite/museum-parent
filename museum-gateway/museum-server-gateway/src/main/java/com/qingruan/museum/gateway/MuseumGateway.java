package com.qingruan.museum.gateway;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

import com.qingruan.museum.framework.esb.redispushpop.RedisMQPushSender;
import com.qingruan.museum.gateway.common.exception.ExceptionLog;
import com.qingruan.museum.gateway.framework.ApplicationContextGuardian;

/**
 * 启动协议栈（Netty),启动网关
 * 
 * @author tommy
 * 
 */
@Slf4j
public class MuseumGateway {

	private static ApplicationContextGuardian applicationContextGuardian = null;

	public static RedisMQPushSender redisMQPushSender;

	public static void main(String[] args) throws IOException,
			RuntimeException, NullPointerException {
		try {

			applicationContextGuardian = ApplicationContextGuardian
					.getInstance();
			redisMQPushSender = (RedisMQPushSender) applicationContextGuardian
					.GetAppContext().getBean(RedisMQPushSender.class);
			
			if (MuseumGatewayProperties.isEmpty()) {
				log.error("miss museum-gateway.properties.");
				System.exit(0);
			}
			log.info("Museum Gateway Success");
			
			
		} catch (Exception e) {
			System.out.println("--------{Server gateway start failed,please check log}-------[启动失败，请查看日志文件]");
			System.out.println("------------------------------^-^----------------------------------");
			System.out.println("-----------------[进程已经退出，请查看{/var/logs/museum-gateway.log}下的日志文件，修复问题，重新启动]-------------------");
			log.error(ExceptionLog.getErrorStack(e));
			System.exit(0);
		} finally {

		}

	}
}
