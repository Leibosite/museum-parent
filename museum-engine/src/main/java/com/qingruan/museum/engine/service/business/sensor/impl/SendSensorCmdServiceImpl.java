package com.qingruan.museum.engine.service.business.sensor.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qingruan.museum.dao.entity.Device;
import com.qingruan.museum.dao.entity.record.ActivityConstantThData;
import com.qingruan.museum.dao.repository.DeviceDao;
import com.qingruan.museum.dao.repository.RepoAreaDao;
import com.qingruan.museum.dao.repository.record.ActivityConstantThDataDao;
import com.qingruan.museum.domain.enums.ResultCode;
import com.qingruan.museum.domain.models.enums.DeviceType;
import com.qingruan.museum.domain.models.enums.MonitorDataType;
import com.qingruan.museum.engine.exception.ExceptionLog;
import com.qingruan.museum.engine.service.business.sensor.CommonSensorService;
import com.qingruan.museum.engine.service.business.sensor.HandleSensorReqService;
import com.qingruan.museum.engine.service.business.sensor.SendSensorCmdService;
import com.qingruan.museum.engine.service.business.sensor.utils.SensorUtils;
import com.qingruan.museum.framework.esb.redispushpop.RedisMQPushSender;
import com.qingruan.museum.framework.jackson.JSONUtil;
import com.qingruan.museum.msg.MsgHeader;
import com.qingruan.museum.msg.MsgHeader.DeliveryMode;
import com.qingruan.museum.msg.MsgProperty;
import com.qingruan.museum.msg.MsgProperty.ApplicationType;
import com.qingruan.museum.msg.MsgProperty.CmdType;
import com.qingruan.museum.msg.MuseumMsg;
import com.qingruan.museum.msg.Result;
import com.qingruan.museum.msg.sensor.MtmNetInfo;
import com.qingruan.museum.msg.sensor.SensorData;
import com.qingruan.museum.msg.sensor.SensorDataContent;
import com.qingruan.museum.msg.sensor.SensorMacAddrInfo;
import com.qingruan.museum.msg.sensor.SensorMsg;
import com.qingruan.museum.msg.sensor.SensorMsg.SensorAppType;
import com.qingruan.museum.msg.sensor.SensorMsg.SensorMsgType;
import com.qingruan.museum.msg.sensor.SensorMsg.SensorVersion;
import com.qingruan.museum.msg.sensor.SensorNetInfo;
import com.qingruan.museum.msg.sensor.SensorNetStruct;
import com.qingruan.museum.msg.sensor.SensorNetTopology;

@Service
@Slf4j
public class SendSensorCmdServiceImpl implements SendSensorCmdService {
	@Autowired
	private HandleSensorReqService handleSensorReqService;
	@Autowired
	private CommonSensorService commonSensorService;
	@Autowired
	private RedisMQPushSender redisMQPushSender;
	@Autowired
	private DeviceDao deviceDao;
	@Autowired
	private RepoAreaDao repoAreaDao;
	@Autowired
	private ActivityConstantThDataDao activityConstantThDataDao;

	@Override
	public MuseumMsg sendSensorUpgradeCmd(Long stationId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendSensorStatusCheck(String macAddr) {

		if (StringUtils.isBlank(macAddr)) {
			log.info("@Handling--{sendSensorStatusCheck}--{监测站}------ "
					+ "{mac 地址为空跳过}");
			return;
		}
		MuseumMsg museumMsg = SensorUtils.assembSensorReq(macAddr,
				SensorAppType.ENGINE_GET_SENSOR_STATUS);

		log.info("@Handling--{sendSensorStatusCheck}--{发送消息到Netty,检查管辖的传感器状态}------");
		String json = JSONUtil.serialize(museumMsg);
		log.info("@Handling--{sendSensorStatusCheck}--{Msg is:}------" + json);
		redisMQPushSender.sendNettyToEngine(json);

	}

	@Override
	public MuseumMsg sendReportSensorHistoryData(Long sensorDeviceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendStationInfoUpdate(String macAddr) {

		if (StringUtils.isBlank(macAddr)) {
			log.info("@Handling--{sendStationInfoUpdate}--{监测站}------ "
					+ "{mac 地址为空跳过}");
			return;
		}
		MuseumMsg museumMsg = SensorUtils.assembSensorReq(macAddr,
				SensorAppType.ENGINE_POST_SENSOR_MAC_LIST);

		log.info("@Handling--{sendStationInfoUpdate}--{发送消息到Engine,要求更新中继管辖设备mac地址列表}------");
		String json = JSONUtil.serialize(museumMsg);
		log.info("@Handling--{sendStationInfoUpdate}--{Msg is:}------" + json);
		redisMQPushSender.sendEngineToNetty(json);

	}

	@Override
	public void sendSimulatorData(String sensorMacAddr, String stationMacAddr) {
		// TODO Auto-generated method stub
		MuseumMsg museumMsg = new MuseumMsg();
		Long currentTime = System.currentTimeMillis();
		MsgHeader header = new MsgHeader();
		header.setDeliveryMode(DeliveryMode.PTP);
		header.setTimeStamp(currentTime);
		museumMsg.setMsgHeader(header);

		MsgProperty msgProperty = new MsgProperty();
		msgProperty.setApplicationType(ApplicationType.SENSOR);
		msgProperty.setCmdType(CmdType.REQUEST);
		museumMsg.setMsgProperty(msgProperty);

		SensorMsg sensorMsg = new SensorMsg();
		sensorMsg.setSensorMsgType(SensorMsgType.TEXT);
		sensorMsg.setSensorAppType(SensorAppType.GATEWAY_POST_SENSOR_DATA);

		SensorData sensorInfo = new SensorData();
		MtmNetInfo mtmNetInfos = new MtmNetInfo();
		mtmNetInfos.setStationMacAddr(stationMacAddr);
		sensorInfo.setMtmNetInfo(mtmNetInfos);

		double power = 95L;
		sensorInfo.setPower(power);
		sensorInfo.setTimeStamp(currentTime);

		SensorNetInfo sensorNetInfos = new SensorNetInfo();
		sensorNetInfos.setSensorMacAddr(sensorMacAddr);
		sensorNetInfos.setRssi(10);
		sensorNetInfos.setTimeStamp(currentTime);
		sensorNetInfos.setMajor("FFFF");
		sensorNetInfos.setMinor("FF01");

		sensorInfo.setSensorNetInfo(sensorNetInfos);

		List<SensorDataContent> sensorDataContents = new ArrayList<SensorDataContent>();

		SensorDataContent co2 = new SensorDataContent();
		co2.setMonitorDataType(MonitorDataType.TEMPERATURE);
		co2.setValue(25D);
		co2.setCheckCode(1);
		sensorDataContents.add(co2);
		SensorDataContent tmp = new SensorDataContent();
		tmp.setMonitorDataType(MonitorDataType.CO2);
		tmp.setValue(419.00D);
		tmp.setCheckCode(1);
		sensorDataContents.add(tmp);

		sensorInfo.setDatas(sensorDataContents);
		sensorMsg.setData(sensorInfo);
		museumMsg.setMsgBody(sensorMsg);
		redisMQPushSender.sendNettyToEngine(JSONUtil.serialize(museumMsg));
		this.simulateConstantTh(20D,MonitorDataType.HUMIDITY);
		this.simulateConstantTh(20D,MonitorDataType.TEMPERATURE);
	}

	@Override
	public void engineUpdateSensorMacList(Long monitorStationId) {
		log.info("@Handling-[start]-{engineUpdateSensorMacList}--{Engine开始更新中继管辖的设备MAC地址列表信息}------ ");
		log.info("[monitorStation id is]" + monitorStationId);
		if (monitorStationId == null)
			return;
		MuseumMsg msg = this.generateSensorMacList(monitorStationId);
		if (msg == null)
			return;
		try {
			log.info("@Handling-[sending]-{send msg to MQ}--{开始发送到MQ}------ ");
			redisMQPushSender.sendEngineToNetty(JSONUtil.serialize(msg));
			log.info("@Handling-[sended]-{sended msg to MQ already}--{消息已经发送到MQ}------ ");

		} catch (Exception e) {
			log.error(ExceptionLog.getErrorStack(e));
		}

	}

	@Override
	public void engineUpdateNetTopologyList(Long masterGwId) {
		log.info("@Handling-[start]-{engineUpdateNetTopologyList}--{Engine开始更新主网关下的网络拓扑信息}------ ");
		MuseumMsg museumMsg = this.generateNetTopologyList(masterGwId);
		if (museumMsg == null) {
			log.info("@Handling-[start]-{generate updated nettopology list is null}--{生成的网络拓扑信息为空,跳过}------ ");
			return;
		}
		try {
			log.info("@Handling-[sending]-{send msg to MQ}--{开始发送到MQ}------ ");
			redisMQPushSender.sendEngineToNetty(JSONUtil.serialize(museumMsg));
			log.info("@Handling-[sended]-{sended msg to MQ already}--{消息已经发送到MQ}------ ");

		} catch (Exception e) {
			log.error(ExceptionLog.getErrorStack(e));
			
		}
	}

	private MuseumMsg generateNetTopologyList(Long masterGwId) {

		log.info("@Handling-[start]-{generateNetTopologyList}--{Engine开始更新主网关下的网络拓扑信息}------ ");
		if (masterGwId == null)
			return null;
		Device device = deviceDao.findById(masterGwId);
	   if(device==null||(!device.getDeviceType().equals(DeviceType.MASTER_GATEWAY)))
		   return null;
		MuseumMsg museumMsg = new MuseumMsg();
		MsgHeader msgHeader = new MsgHeader("1", "ENGINE", "NETTY", "BROKER",
				System.currentTimeMillis(), 1, DeliveryMode.PTP, 1);
		museumMsg.setMsgHeader(msgHeader);
		MsgProperty msgProperty = new MsgProperty(CmdType.REQUEST,
				ApplicationType.SENSOR);

		museumMsg.setMsgProperty(msgProperty);
		SensorMsg sensorMsg = new SensorMsg();
		sensorMsg
				.setSensorAppType(SensorAppType.ENGINE_UPDATE_NET_TOPOLOCY_INFO);
		sensorMsg.setSensorMsgType(SensorMsgType.TEXT);
		sensorMsg.setSensorVersion(SensorVersion.V2);
		SensorData sensorData = new SensorData();

		MtmNetInfo mtmNetInfo = new MtmNetInfo();
		SensorNetTopology sensorNetTopology = new SensorNetTopology();
		mtmNetInfo.setSensorNetTopology(sensorNetTopology);
		sensorData.setMtmNetInfo(mtmNetInfo);

		sensorMsg
				.setSensorAppType(SensorAppType.ENGINE_UPDATE_NET_TOPOLOCY_INFO);
	

		List<String> topologyList = commonSensorService.getTopologyList(
				device.getId(), device.getDeviceNo());

		if (topologyList == null || topologyList.size() == 0)
			return null;
		else {
			sensorMsg.setData(sensorData);
			sensorMsg.getData().getMtmNetInfo().getSensorNetTopology()
					.setRouterNetTopology(topologyList);
			sensorMsg.getData().getMtmNetInfo().getSensorNetTopology().setMasterGatewayId(device.getDeviceNo());
			sensorMsg.setResult(new Result(ResultCode.SUCCESS, "SUCCESS"));
			
			museumMsg.setMsgBody(sensorMsg);
			log.info("@Handling-[end]-{generateNetTopologyList}--{Engine结束更新主网关下的网络拓扑信息}------ ");
			return museumMsg;
		}
	

	}

	/**
	 * 根据中继的id生成需要下发的中继管辖的Mac List
	 * 
	 * @param monitorStationId
	 * @return
	 */
	private MuseumMsg generateSensorMacList(Long monitorStationId) {
		log.info("@Handling-[start]-{generateSensorMacList}--{Engine开始生成需要下发的Mac List列表}------ ");
		if (monitorStationId == null)
			return null;
		SensorMacAddrInfo sensorMacAddrInfo = commonSensorService
				.getMonitorStationMacList(monitorStationId);
		String masterGwId=commonSensorService.getMasterGwId(monitorStationId);
		if (sensorMacAddrInfo == null||StringUtils.isBlank(masterGwId))
			return null;
		MuseumMsg museumMsg = new MuseumMsg();
		MsgHeader msgHeader = new MsgHeader("1", "ENGINE", "NETTY", "BROKER",
				System.currentTimeMillis(), 1, DeliveryMode.PTP, 1);
		museumMsg.setMsgHeader(msgHeader);
		MsgProperty msgProperty = new MsgProperty(CmdType.REQUEST,
				ApplicationType.SENSOR);
		msgHeader.setSendFrom("ENGINE");
		msgHeader.setSendTo("NETTY");
		msgHeader.setTimeStamp(System.currentTimeMillis());
		museumMsg.setMsgHeader(msgHeader);
		msgProperty.setCmdType(CmdType.REQUEST);
		museumMsg.setMsgProperty(msgProperty);

		SensorMsg sensorMsg = new SensorMsg();
		sensorMsg.setSensorAppType(SensorAppType.ENGINE_UPDATE_SENSOR_MAC_LIST);
		sensorMsg.setSensorMsgType(SensorMsgType.TEXT);
		sensorMsg.setSensorVersion(SensorVersion.V2);
		SensorData sensorData = new SensorData();

		MtmNetInfo mtmNetInfo = new MtmNetInfo();

		SensorNetStruct sensorNetStruct = new SensorNetStruct();
		sensorNetStruct.setMasterGatewayId(masterGwId);
		List<SensorMacAddrInfo> sensorMacAddrInfos = new ArrayList<SensorMacAddrInfo>();

		sensorMacAddrInfos.add(sensorMacAddrInfo);
		sensorNetStruct.setSensorMacAddrInfos(sensorMacAddrInfos);
		mtmNetInfo.setSensorNetStruct(sensorNetStruct);
		sensorData.setMtmNetInfo(mtmNetInfo);
		sensorMsg.setData(sensorData);
		sensorMsg.setResult(new Result(ResultCode.SUCCESS, "SUCCESS"));
		SensorNetTopology sensorNetTopology = new SensorNetTopology();
		mtmNetInfo.setSensorNetTopology(sensorNetTopology);
		sensorData.setMtmNetInfo(mtmNetInfo);


		museumMsg.setMsgBody(sensorMsg);
		log.info("@Handling-[end]-{generateNetTopologyList}--{Engine结束更新主网关下的网络拓扑信息}------ ");
		return museumMsg;

	}
	private void simulateConstantTh(Double value,MonitorDataType type){
		Long deviceId=56L;
		Long areaId=4L;
		ActivityConstantThData activityConstantThData=new ActivityConstantThData();
		activityConstantThData.setObjectType(type);
		activityConstantThData.setValue(value);
		activityConstantThData.setDevice(deviceDao.findOne(deviceId));
		activityConstantThData.setRepoArea(repoAreaDao.findOne(areaId));
		activityConstantThData.setDateTime(new Timestamp(System.currentTimeMillis()));
		activityConstantThData.setStatus(1);
		activityConstantThDataDao.save(activityConstantThData);
		
	}
	
}
