/**
 2015年1月16日
 tommy
 
 */
package com.qingruan.museum.engine.service.workflow;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qingruan.museum.dao.entity.Device;
import com.qingruan.museum.engine.exception.ExceptionLog;
import com.qingruan.museum.engine.framework.ApplicationContextGuardian;
import com.qingruan.museum.engine.log.LogicalLogParamUtil;
import com.qingruan.museum.engine.service.business.sensor.HandleSensorReqService;
import com.qingruan.museum.engine.service.rule.core.DelayTaskSuperadder;
import com.qingruan.museum.engine.service.rule.core.RuleCore;
import com.qingruan.museum.framework.esb.redispushpop.RedisMQPushSender;
import com.qingruan.museum.framework.jackson.JSONUtil;
import com.qingruan.museum.jpa.JpaDeviceService;
import com.qingruan.museum.msg.MsgProperty.ApplicationType;
import com.qingruan.museum.msg.MuseumMsg;
import com.qingruan.museum.msg.aircleaner.AirCleanerMsg;
import com.qingruan.museum.msg.constantth.ConstantThMsg;
import com.qingruan.museum.msg.constantth.ConstantThMsg.ThAppType;
import com.qingruan.museum.msg.constantth.ThData;
import com.qingruan.museum.msg.sensor.MtmNetInfo;
import com.qingruan.museum.msg.sensor.SensorData;
import com.qingruan.museum.msg.sensor.SensorMsg;
import com.qingruan.museum.msg.sensor.SensorMsg.SensorAppType;
import com.qingruan.museum.msg.sensor.SensorNetStruct;
import com.qingruan.museum.msg.weather.WeatherMsg;
import com.qingruan.museum.session.IpcanContext;
import com.qingruan.museum.session.RunTimeInfo;

/**
 * 处理流程的抽象类
 * 
 * @author tommy
 * 
 */
@Slf4j
@Service
public class AbstractWorkflow {
	@Autowired
	private JpaDeviceService jpaDeviceService;
	@Autowired
	private HandleSensorReqService handleSensorReqService;
	@Autowired
	private RedisMQPushSender redisMQPushSender;
	// @Autowired
	// private RealTimeEvaluation realTimeEvaluation;

	private final static String MONITORIN_POINT = "监测点";
	private final static String MONITORIN_STATION = "监测站";

	/**
	 * 接收消息进行处理
	 * 
	 * @param message
	 */
	@SuppressWarnings("finally")
	public void onReceive(Object message) {
		log.info("Redis queue with path [{}] receives message [{}]." + message);
		IpcanContext ipCanContext = null;

		// 处理不同的消息流程

		try {
			// 接收到的Netty发来的消息对象
			if (message instanceof MuseumMsg) {
				MuseumMsg msg = (MuseumMsg) message;
				log.info("Received Message: {}" + msg.toString());
				ipCanContext = this.preProcess(msg);
				if (ipCanContext == null)
					return;
				else

				{

					if (ipCanContext.getIsRunEngine().equals(Boolean.TRUE))
						// 调用规则引擎
						this.runRuleEngine(ipCanContext);

					// this.runScore(ipCanContext);

				}
				// 规则引擎完成后消息收集和处理
				this.postPrecess(ipCanContext);
			}

		} catch (Exception e) {
			log.error(ExceptionLog.getErrorStack(e));
			e.printStackTrace();
		} finally {

		}

	}

	private void runScore(IpcanContext ipcanContext) {
		log.info("欢迎进入评分系统，现在开始评分");
		try {
			// realTimeEvaluation.doWithRealTimeData(ipcanContext);
			log.info("评分结束，下次再见");
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	/**
	 * 组装成IpcanContext,供规则引擎调用
	 * 
	 * @param museumDomainReceived
	 * @return
	 */
	private IpcanContext preProcess(MuseumMsg msg) {
		IpcanContext ipCanContext = new IpcanContext();
		ipCanContext.setRecMuseumMsg(msg);
		// 传感器数据
		if (msg.getMsgProperty().getApplicationType()
				.equals(ApplicationType.SENSOR)) {
			return prePrecessSensorData(ipCanContext, msg);
		}
		// 恒温恒湿机交互
		if (msg.getMsgProperty().getApplicationType()
				.equals(ApplicationType.CONSTANT_TH)) {

			return prePrecessThData(ipCanContext, msg);

		}
		if (msg.getMsgProperty().getApplicationType()
				.equals(ApplicationType.METEOROLOGICAL_STATION)) {

			return prePrecessWeatherData(ipCanContext, msg);

		}
		// 预处理空气净化器数据
		if (msg.getMsgProperty().getApplicationType()
				.equals(ApplicationType.AIR_CLEANER)) {

			return prePrecessAirCleanerData(ipCanContext, msg);

		}
		return null;
	}

	private IpcanContext prePrecessWeatherData(IpcanContext ipCanContext,
			MuseumMsg msg) {

		// 设置运行时数据
		WeatherMsg weatherMsg = (WeatherMsg) msg.getMsgBody();
		if (weatherMsg == null)
			return null;
		String macAddr = weatherMsg.getStationMacAddr();
		log.info("--------------{weather statin mac address is: }--------{mac_address}"
				+ macAddr);
		// TODO:丑陋的代码，需要删除
		// if (StringUtils.isBlank(macAddr))
		// macAddr = "000000000005";
		try {
			Device device = jpaDeviceService.findDeviceNo(macAddr);

			// 查询运行时数据
			RunTimeInfo runTimeInfo = new RunTimeInfo();
			// 设置消息头
			runTimeInfo.setMsgHeader(msg.getMsgHeader());
			runTimeInfo.setMsgProperty(msg.getMsgProperty());
			runTimeInfo
					.setApplicationType(ApplicationType.METEOROLOGICAL_STATION);
			runTimeInfo.setWeatherMsg(weatherMsg);
			log.info("--------------{weather statin device is: }--------{device}"
					+ device);
			if (device != null && device.getRepoArea() != null) {
				runTimeInfo.setWeatherStation(device);
				runTimeInfo.setRepoArea(device.getRepoArea());
				ipCanContext.setRunTimeInfo(runTimeInfo);
				return ipCanContext;

			}
			ipCanContext.setRunTimeInfo(runTimeInfo);
			return ipCanContext;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;

	}

	/**
	 * 预处理空气净化器数据
	 * 
	 * @param ipCanContext
	 * @param msg
	 * @return
	 */
	private IpcanContext prePrecessAirCleanerData(IpcanContext ipCanContext,
			MuseumMsg msg) {

		// 设置运行时数据
		AirCleanerMsg airCleanerMsg = (AirCleanerMsg) msg.getMsgBody();
		if (airCleanerMsg == null)
			return null;
		String macAddr = airCleanerMsg.getStationMacAddr();
		log.info("--------------{AirCleaner  mac address is: }--------{mac_address}"
				+ macAddr);
		try {
			Device device = jpaDeviceService.findDeviceByMacAddr(macAddr);
			if (device == null) {
				log.info("--------------{AirCleaner is not exist in database}--------{空气净化器在数据库中不存在,请在组网信息中配置}");
				return null;
			}

			// 查询运行时数据
			RunTimeInfo runTimeInfo = new RunTimeInfo();
			// 设置消息头
			runTimeInfo.setMsgHeader(msg.getMsgHeader());
			runTimeInfo.setMsgProperty(msg.getMsgProperty());
			runTimeInfo.setApplicationType(ApplicationType.AIR_CLEANER);
			runTimeInfo.setAirCleanerMsg(airCleanerMsg);
			log.info("--------------{AirCleaner device is: }--------{device}"
					+ device);
			if (device != null && device.getRepoArea() != null) {
				runTimeInfo.setAirCleaner(device);
				runTimeInfo.setRepoArea(device.getRepoArea());
				ipCanContext.setRunTimeInfo(runTimeInfo);
				return ipCanContext;

			}
			ipCanContext.setRunTimeInfo(runTimeInfo);
			return ipCanContext;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;

	}

	private IpcanContext prePrecessSensorData(IpcanContext ipCanContext,
			MuseumMsg msg) {

		// 设置运行时数据
		SensorMsg sensorMsg = (SensorMsg) msg.getMsgBody();
		SensorData data = sensorMsg.getData();
		if (sensorMsg.getSensorAppType() != null) {
			// 中继获取所管辖的传感器Mac地址列表
			if (sensorMsg.getSensorAppType().equals(
					SensorAppType.GATEWAY_GET_SENSOR_MAC_LIST)) {

				data = sensorMsg.getData();

				if (data.getMtmNetInfo() == null) {

					MtmNetInfo mtmNetInfo = new MtmNetInfo();
					mtmNetInfo.setStationMacAddr("E84E062A1B0F");
					data.setMtmNetInfo(mtmNetInfo);

				}

				if (data != null && data.getMtmNetInfo() != null) {
					SensorNetStruct sensorNetStruct = data.getMtmNetInfo()
							.getSensorNetStruct();

					MuseumMsg result = handleSensorReqService.getSensorMacList(
							sensorNetStruct, msg.getMsgHeader(),
							msg.getMsgProperty(), sensorMsg);
					if (result != null) {
						ipCanContext.getSendingMuseumMsgs().add(result);
						ipCanContext.setIsRunEngine(Boolean.FALSE);
						return ipCanContext;
					}

				}

			}
			// 中继获取所管辖的传感器Mac地址列表
			if (sensorMsg.getSensorAppType().equals(
					SensorAppType.ENGINE_GET_SENSOR_STATUS)) {

				ipCanContext.setIsRunEngine(Boolean.FALSE);
				return ipCanContext;

			}

			// 中继上报传感器数据
			if (sensorMsg.getSensorAppType().equals(
					SensorAppType.GATEWAY_POST_SENSOR_DATA)) {

				data = sensorMsg.getData();
				if (data != null && data.getMtmNetInfo() != null) {
					String stationMacAddr = data.getMtmNetInfo()
							.getStationMacAddr();
					// TODO 确认代码结构
					String sensoMacAddr = data.getMtmNetInfo()
							.getStationMacAddr();

					// 查询运行时数据
					RunTimeInfo runTimeInfo = new RunTimeInfo();
					// 设置消息头
					runTimeInfo.setMsgHeader(msg.getMsgHeader());
					runTimeInfo.setMsgProperty(msg.getMsgProperty());
					runTimeInfo.setApplicationType(ApplicationType.SENSOR);
					runTimeInfo.setSensorMsg(sensorMsg);
					log.info("------------{GATEWAY_POST_SENSOR_DATA}-----{stationMacAddr} is :"
							+ stationMacAddr);
					log.info("------------{GATEWAY_POST_SENSOR_DATA}-----{sensoMacAddr} is :"
							+ sensoMacAddr);

					if (StringUtils.isBlank(sensoMacAddr)
							|| StringUtils.isBlank(stationMacAddr)) {
						log.info("------------{GATEWAY_POST_SENSOR_DATA}-----{sensoMacAddr} ||-{stationMacAddr} is NULL,[丢弃该数据]");
						return null;
					}

					Device monitorPoint = jpaDeviceService
							.findDeviceByMacAddr(sensoMacAddr);
					Device monitorStation = jpaDeviceService
							.findDeviceByMacAddr(stationMacAddr);
					if (monitorPoint == null
							|| monitorPoint.getRepoArea() == null
							|| monitorStation == null) {
						log.info("------------{GATEWAY_POST_SENSOR_DATA}-----{monitorPoint} ||-{monitorPoint.getRepoArea()}||-{monitorStation} is NULL,[丢弃该数据]");
						return null;
					}

					runTimeInfo.setMonitoringPoint(monitorPoint);
					runTimeInfo.setMonitoringStation(monitorStation);
					runTimeInfo.setRepoArea(monitorPoint.getRepoArea());
					ipCanContext.setRunTimeInfo(runTimeInfo);
					return ipCanContext;

				}

			}
			// 主网关获取网络拓扑信息
			if (sensorMsg.getSensorAppType().equals(
					SensorAppType.GATEWAY_GET_NET_TOPOLOCY_INFO)) {
				data = sensorMsg.getData();
				if (data != null && data.getMtmNetInfo() != null) {
					// TODO:统一放置位置
					// 传感器的组网信息

					SensorNetStruct sensorNetStruct = data.getMtmNetInfo()
							.getSensorNetStruct();
					if (sensorNetStruct == null)
						return null;
					// SensorNetTopology sensorNetTopology =
					// data.getMtmNetInfo()
					// .getSensorNetTopology();

					MuseumMsg result = handleSensorReqService
							.getNetTopologyList(sensorNetStruct,
									msg.getMsgHeader(), msg.getMsgProperty(),
									sensorMsg);
					if (result != null) {
						ipCanContext.getSendingMuseumMsgs().add(result);
						ipCanContext.setIsRunEngine(Boolean.FALSE);
						return ipCanContext;
					}
				}
			}

			// 设备告警
			if (sensorMsg.getSensorAppType().equals(
					SensorAppType.GATEWAY_REPORT_ALARM)) {
				log.info("开始处理设备告警消息");
				MuseumMsg result = handleSensorReqService
						.reportSensorAlarmStatus(msg.getMsgHeader(),
								msg.getMsgProperty(), sensorMsg);

				if (result != null) {
					ipCanContext.getSendingMuseumMsgs().add(result);
					ipCanContext.setIsRunEngine(Boolean.FALSE);
					return ipCanContext;
				}
			}

		}
		return null;
	}

	private IpcanContext prePrecessThData(IpcanContext ipCanContext,
			MuseumMsg msg) {

		ConstantThMsg constantMsg = (ConstantThMsg) msg.getMsgBody();
		// 恒温恒湿机上报监测数据
		if (constantMsg.getThAppType().equals(
				ThAppType.GATEWAY_POST_TH_MONITOR_DATA)) {

			ThData data = constantMsg.getData();
			String mac = data.getPresentMAC();

			if (StringUtils.isNotBlank(mac)) {
				Device device = jpaDeviceService.findDeviceByMacAddr(mac);
		
				// 查询运行时数据
				RunTimeInfo runTimeInfo = new RunTimeInfo();
				runTimeInfo.setApplicationType(ApplicationType.CONSTANT_TH);
				// 设置消息头
				runTimeInfo.setMsgHeader(msg.getMsgHeader());
				runTimeInfo.setMsgProperty(msg.getMsgProperty());
				runTimeInfo.setConstantThMsg(constantMsg);

				runTimeInfo.setConstantTh(device);

				runTimeInfo.setRepoArea(device.getRepoArea());
				ipCanContext.setRunTimeInfo(runTimeInfo);
				return ipCanContext;

			}


		}

		return ipCanContext;
	}

	/**
	 * 运行规则引擎
	 * 
	 * @param ipcanContext
	 */
	private void runRuleEngine(IpcanContext ipcanContext)
			throws RuntimeException {
		log.info(LogicalLogParamUtil.composeLogicalLogParam(ipcanContext)
				+ "runRuleEngine------------------start.");

		String knowledgeBaseId = "MUSEUM_CONTROL";

		RuleCore ruleCore = ApplicationContextGuardian.getInstance()
				.getRuleCore();

		/**
		 * 执行 1.规则引擎匹配 2.冲突检测 3.执行delay task
		 */
		ruleCore.fireAndExecute(ipcanContext, knowledgeBaseId,
				getDelayTaskSuperadder());

		log.info(LogicalLogParamUtil.composeLogicalLogParam(ipcanContext)
				+ "runRuleEngine------------------end.");
	}

	/**
	 * 标识当前workflow对应的规则库
	 * 
	 * @return
	 */
	// abstract protected String getKnowledgeBaseId();

	/**
	 * 添加额外的task（这些task由knowledge base之外的流程添加）
	 * 
	 * @see 参考
	 * 
	 * @return
	 */
	protected DelayTaskSuperadder getDelayTaskSuperadder() {
		return null;
	}

	/**
	 * 规则引擎处理后消息处理
	 * 
	 * @param ipCanContext
	 */
	private void postPrecess(IpcanContext ipCanContext) {
		if (ipCanContext == null || ipCanContext.getSendingMuseumMsgs() == null)
			return;
		else {

			for (MuseumMsg museumMsg : ipCanContext.getSendingMuseumMsgs()) {
				String msg = JSONUtil.serialize(museumMsg);
				if (StringUtils.isNotBlank(msg)) {
					this.redisMQPushSender.sendEngineToNetty(msg);
				}

			}

		}

	}
}
