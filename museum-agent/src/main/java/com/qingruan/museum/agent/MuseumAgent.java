package com.qingruan.museum.agent;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

import com.qingruan.museum.agent.framework.ApplicationContextGuardian;
import com.qingruan.museum.framework.esb.redispushpop.RedisMQPushSender;
import com.qingruan.museum.framework.log.ExceptionLog;

/**
 * 
 * @author tommy
 * 
 */
@Slf4j
public class MuseumAgent {
	private static ApplicationContextGuardian applicationContextGuardian = null;
	public static RedisMQPushSender redisMQPushSender;

	public static void main(String[] args) throws IOException,
			RuntimeException, NullPointerException {
		try {
			applicationContextGuardian = ApplicationContextGuardian
					.getInstance();
			redisMQPushSender = (RedisMQPushSender) applicationContextGuardian
					.GetAppContext().getBean(RedisMQPushSender.class);
			if (MuseumAgentProperties.isEmpty()) {
				log.error("miss museum-agent.properties.");

				System.exit(0);
			}
			log.info("--------@Museum Agent Success@--------");
			System.out
					.println("-----------------------@Museum Agent Success@-----------------------");
		} catch (Exception e) {
			System.out
					.println("--------{Agent start failed,please check log}-------[启动失败，请查看日志文件]");
			System.out.println("------------------------------^-^----------------------------------");
			System.out.println("-----------------[进程已经退出，请查看{/var/logs/museum-agent.log}下的日志文件，修复问题，重新启动]-------------------");
			log.error(ExceptionLog.getErrorStack(e));
			System.exit(0);
		} finally {

		}

	}
}
