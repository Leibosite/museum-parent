package com.qingruan.museum.engine;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

import com.qingruan.framework.actor.mvc.ActorMVCBootstrap;
import com.qingruan.museum.domain.models.enums.MonitorDataType;
import com.qingruan.museum.engine.exception.ExceptionLog;
import com.qingruan.museum.engine.framework.ApplicationContextGuardian;
import com.qingruan.museum.engine.service.handlemsg.RuleMsgHandler;
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
public class MuseumEngine {

	public static ApplicationContextGuardian applicationContextGuardian = ApplicationContextGuardian
			.getInstance();
	public static RuleMsgHandler ruleMsgHandler = null;

	public static void main(String[] args) throws IOException,
			RuntimeException, NullPointerException {
		try {

			if (MuseumEngineProperties.isEmpty()) {
				log.error("miss museum-engine.properties.");

				System.exit(0);
			}
			// 初始化规则引擎x
			applicationContextGuardian.getRuleCore().constructKnowledgeBases();
			ruleMsgHandler = applicationContextGuardian.GetAppContext()
					.getBean(RuleMsgHandler.class);

			SpringContextHolder.getApplicationContext().getBean(
					ActorMVCBootstrap.class);
			System.out.println("---------------------------------------^o^----------------------------------------------");
			System.out.println("---------------------[Musuem-Engine started success]--[启动成功]--------------------------");
			log.info("Museum Engine Success");

		} catch (Exception e) {
			System.out
					.println("--------{Engine start failed,please check log}-------[启动失败，请查看日志文件]");
			System.out.println("------------------------------^-^----------------------------------");
			System.out.println("-----------------[进程已经退出，请查看{/var/logs/museum-engine.log}下的日志文件，修复问题，重新启动]-------------------");
			log.error(ExceptionLog.getErrorStack(e));
			System.exit(0);
		} finally {

		}

	}

	private static MuseumMsg newMuseumDomain(SensorAppType sensorAppType) {
		MuseumMsg domain = new MuseumMsg();
		MsgHeader msgHeader = new MsgHeader("1", "NETTY", "ENGINE", "", null,
				1, DeliveryMode.PTP, 0);
		domain.setMsgHeader(msgHeader);
		MsgProperty msgProperty = new MsgProperty(CmdType.REQUEST,
				ApplicationType.SENSOR);
		domain.setMsgBody(new SensorMsg());
		domain.setMsgProperty(msgProperty);
		SensorData data = new SensorData();
		data.setTimeStamp(1L);
		MtmNetInfo info = new MtmNetInfo();
		info.setStationMacAddr("8086F25FD5D8");

		SensorMsg sensorMsg = null;
		switch (sensorAppType) {
		case GATEWAY_GET_SENSOR_MAC_LIST:
			data.setMtmNetInfo(info);
			sensorMsg = new SensorMsg("1234", SensorMsgType.TEXT,
					SensorAppType.GATEWAY_GET_SENSOR_MAC_LIST, 0, data, null);
			break;
		case GATEWAY_POST_SENSOR_DATA:
			data.setSensorNetInfo(new SensorNetInfo("E406EEFA1A52", 1L, 4,
					"", "", ""));
			SensorDataContent content = new SensorDataContent();
			content.setMonitorDataType(MonitorDataType.CO2);
			content.setValue(1D);
			data.getDatas().add(content);
			SensorNetInfo sensorNetInfos = new SensorNetInfo();
			sensorNetInfos.setSensorMacAddr("E406EEFA1A52");
			info.getSensorNetInfos().add(sensorNetInfos);
			data.setMtmNetInfo(info);
			data.setSensorNetInfo(sensorNetInfos);
			sensorMsg = new SensorMsg("1234", SensorMsgType.TEXT,
					SensorAppType.GATEWAY_POST_SENSOR_DATA, 0, data, null);
			break;
		default:
			break;
		}

		domain.setMsgBody(sensorMsg);

		return domain;

	}
}
