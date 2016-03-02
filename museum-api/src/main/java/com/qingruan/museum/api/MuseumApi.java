package com.qingruan.museum.api;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

import com.qingruan.framework.actor.mvc.ActorMVCBootstrap;
import com.qingruan.museum.api.exception.ExceptionLog;
import com.qingruan.museum.api.framework.ApplicationContextGuardian;
import com.qingruan.museum.domain.models.enums.MonitorDataType;
import com.qingruan.museum.framework.spring.utils.SpringContextHolder;
import com.qingruan.museum.msg.MsgHeader;
import com.qingruan.museum.msg.MsgHeader.DeliveryMode;
import com.qingruan.museum.msg.MsgProperty;
import com.qingruan.museum.msg.MsgProperty.ApplicationType;
import com.qingruan.museum.msg.MsgProperty.CmdType;
import com.qingruan.museum.msg.MuseumMsg;
import com.qingruan.museum.msg.sensor.MtmNetInfo;
import com.qingruan.museum.msg.sensor.SensorData;
import com.qingruan.museum.msg.sensor.SensorDataContent;
import com.qingruan.museum.msg.sensor.SensorMsg;
import com.qingruan.museum.msg.sensor.SensorMsg.SensorAppType;
import com.qingruan.museum.msg.sensor.SensorMsg.SensorMsgType;
import com.qingruan.museum.msg.sensor.SensorNetInfo;

/**
 * 启动协议栈（Netty）、加载规则引擎、启动消息队列
 * 
 * @author tommy
 * 
 */
@Slf4j
public class MuseumApi {

	public static ApplicationContextGuardian applicationContextGuardian = ApplicationContextGuardian
			.getInstance();

	public static void main(String[] args) throws IOException,
			RuntimeException, NullPointerException {
		try {

			if (MuseumApiProperties.isEmpty()) {
				log.error("miss museum-api.properties.");

				System.exit(0);
			}
			System.out.println("---------------------------------------^o^----------------------------------------------");
			System.out.println("---------------------[Musuem-Api started success]--[启动成功]--------------------------");
			log.info("Museum API Success");

		} catch (Exception e) {
			System.out
					.println("--------{Engine start failed,please check log}-------[启动失败，请查看日志文件]");
			System.out.println("------------------------------^-^----------------------------------");
			System.out.println("-----------------[进程已经退出，请查看{/var/logs/museum-api.log}下的日志文件，修复问题，重新启动]-------------------");
			log.error(ExceptionLog.getErrorStack(e));
			System.exit(0);
		} finally {

		}

	}
}
