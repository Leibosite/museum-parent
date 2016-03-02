package com.qingruan.museum.engine.service.business.sensor.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qingruan.museum.dao.entity.Device;
import com.qingruan.museum.dao.entity.record.ActivityDataRecord;
import com.qingruan.museum.dao.entity.record.ActivityDeviceInfoRecord;
import com.qingruan.museum.dao.repository.DeviceDao;
import com.qingruan.museum.domain.enums.ResultCode;
import com.qingruan.museum.domain.enums.device.DeviceStatus;
import com.qingruan.museum.domain.models.enums.DeviceType;
import com.qingruan.museum.domain.models.enums.MonitorDataType;
import com.qingruan.museum.domain.models.enums.RecordDataType;
import com.qingruan.museum.engine.service.business.sensor.CommonSensorService;
import com.qingruan.museum.engine.service.business.sensor.HandleSensorReqService;
import com.qingruan.museum.engine.utils.CommonUtils;
import com.qingruan.museum.engine.utils.TimeUtils;
import com.qingruan.museum.framework.jackson.JSONUtil;
import com.qingruan.museum.jpa.ActivityMonitoringDataService;
import com.qingruan.museum.msg.DataContent;
import com.qingruan.museum.msg.MsgBody;
import com.qingruan.museum.msg.MsgHeader;
import com.qingruan.museum.msg.MsgProperty;
import com.qingruan.museum.msg.MsgProperty.ApplicationType;
import com.qingruan.museum.msg.MsgProperty.CmdType;
import com.qingruan.museum.msg.MuseumMsg;
import com.qingruan.museum.msg.Result;
import com.qingruan.museum.msg.notification.DeviceDataMsg;
import com.qingruan.museum.msg.sensor.SensorDataContent;
import com.qingruan.museum.msg.sensor.SensorMsg;
import com.qingruan.museum.msg.sensor.SensorMsg.SensorAppType;
import com.qingruan.museum.msg.sensor.SensorNetStruct;
import com.qingruan.museum.msg.sensor.SensorNetTopology;
import com.qingruan.museum.msg.sensor.SensorStatusInfo;
import com.qingruan.museum.session.IpcanContext;

@Service
@Slf4j
public class HandleSensorReqServiceImpl implements HandleSensorReqService {
	@Autowired
	private DeviceDao deviceDao;
	@Autowired
	private ActivityMonitoringDataService activityMonitoringDataService;
	@Autowired
	private CommonSensorService commonSensorService;

	@Override
	public MuseumMsg getSensorMacList(SensorNetStruct sensorNetStruct,
			MsgHeader msgHeader, MsgProperty msgProperty, SensorMsg sensorMsg) {

		MuseumMsg museumMsg = new MuseumMsg();
		msgHeader.setSendFrom("ENGINE");
		msgHeader.setSendTo("NETTY");
		msgHeader.setTimeStamp(System.currentTimeMillis());
		museumMsg.setMsgHeader(msgHeader);
		msgProperty.setCmdType(CmdType.ANSWER);
		museumMsg.setMsgProperty(msgProperty);
		sensorMsg.setSensorAppType(SensorAppType.ENGINE_POST_SENSOR_MAC_LIST);

		// 上报中继的MAC地址为空
		if (null == sensorNetStruct
				|| (StringUtils.isBlank(sensorNetStruct.getMasterGatewayId()) && StringUtils
						.isBlank(sensorNetStruct.getMasterGatewayMacAddr()))) {
			sensorMsg.setResult(new Result(ResultCode.ERROR_MSG_FORMAT,
					"MAC ADDR IS NULL AND DEVICE NUMBER IS NULL"));
		} else {
			Device device = null;

			if (StringUtils.isNotBlank(sensorNetStruct.getMasterGatewayId())) {
				device = deviceDao.findByDeviceNo(sensorNetStruct
						.getMasterGatewayId());
			} else {
				device = deviceDao.findByMacAddr(sensorNetStruct
						.getMasterGatewayMacAddr());
			}

			if (device == null) {
				sensorMsg.setResult(new Result(ResultCode.UNSUCCESS,
						"DEVICE IS NOT EXIST"));
			} else {
				if (device.getDeviceType().equals(DeviceType.MASTER_GATEWAY)) {
					sensorNetStruct.setSensorMacAddrInfos(commonSensorService
							.getTopologyListDetails(device));
					sensorMsg.getData().getMtmNetInfo()
							.setSensorNetStruct(sensorNetStruct);

					sensorMsg.setResult(new Result(ResultCode.SUCCESS,
							"SUCCESS"));
				} else {
					sensorMsg.setResult(new Result(ResultCode.UNSUCCESS,
							"NOT A MASTER_GATEWAY"));
				}
			}
		}

		museumMsg.setMsgBody(sensorMsg);
		return museumMsg;
	}

	// private List<SensorMacAddrInfo> getTopologyList(Device device) {
	// List<SensorMacAddrInfo> sensorMacAddrInfos = new
	// ArrayList<SensorMacAddrInfo>();
	//
	// List<Device> secondDeviceList = deviceDao.findByParentId(device.getId());
	// if(secondDeviceList != null){
	// for(Device secondDevice: secondDeviceList) {
	// // 从属设备链接中继或路由
	// if(secondDevice.getDeviceType().equals(DeviceType.MONITORING_STATION)) {
	// SensorMacAddrInfo sensorMacAddrInfo = new SensorMacAddrInfo();
	// sensorMacAddrInfo.setRouterNetTopology(device.getDeviceNo().toString() +
	// secondDevice.getDeviceNo().toString());
	// sensorMacAddrInfo.setSensorNetInfos(getMacAddrList(secondDevice.getId()));
	// sensorMacAddrInfos.add(sensorMacAddrInfo);
	// }
	// else {
	// List<Device> thirdDeviceList =
	// deviceDao.findByParentId(secondDevice.getId());
	// if(thirdDeviceList != null){
	// for(Device thirdDevice: thirdDeviceList) {
	// if(thirdDevice.getDeviceType().equals(DeviceType.MONITORING_STATION)) {
	// SensorMacAddrInfo sensorMacAddrInfo = new SensorMacAddrInfo();
	// sensorMacAddrInfo.setRouterNetTopology(device.getDeviceNo().toString() +
	// secondDevice.getDeviceNo().toString() +
	// thirdDevice.getDeviceNo().toString());
	// sensorMacAddrInfo.setSensorNetInfos(getMacAddrList(thirdDevice.getId()));
	// sensorMacAddrInfos.add(sensorMacAddrInfo);
	// }
	// }
	// }
	// }
	// }
	// }
	// return sensorMacAddrInfos;
	// }
	//
	// private List<SensorNetInfo> getMacAddrList(Long id) {
	// List<SensorNetInfo> sensorNetInfos = new ArrayList<SensorNetInfo>();
	//
	// List<Device> sensorList = deviceDao.findByParentId(id);
	// if (sensorList != null && sensorList.size() > 0) {
	// for(Device device: sensorList) {
	// SensorNetInfo netInfo = new SensorNetInfo();
	// netInfo.setSensorMacAddr(device.getMacAddr());
	// netInfo.setMajor(device.getMajor());
	// netInfo.setMinor(device.getMinor());
	// sensorNetInfos.add(netInfo);
	// }
	// }
	// return sensorNetInfos;
	// }

	@Override
	public void saveSensorDataReport(IpcanContext ipcanContext,
			RecordDataType recordDataType) {

		if (ipcanContext == null || ipcanContext.getRecMuseumMsg() == null)
			;
		else {

			if (ipcanContext.getRecMuseumMsg() == null)
				;
			else {
				log.info("开始保存上报监测数据到数据库中" + ipcanContext.toString());
				this.saveRecordData(ipcanContext);
				log.info("结束保存上报监测数据到数据库中" + ipcanContext.toString());
				// 更新监测点的电量
				this.updateRunTimeDeviceInfo(ipcanContext);
			}

		}

	}

	/**
	 * 保存上报的监测数据指标
	 * 
	 * @return
	 */
	public Boolean postProcess(IpcanContext ipcanContext,
			RecordDataType recordDataType) {
		if (ipcanContext == null || ipcanContext.getRecMuseumMsg() == null)
			return Boolean.FALSE;
		else {

			if (ipcanContext.getRecMuseumMsg() == null)
				return Boolean.FALSE;
			else {
				log.info("开始保存上报监测数据到数据库中" + ipcanContext.toString());
				this.saveRecordData(ipcanContext);
				log.info("结束保存上报监测数据到数据库中" + ipcanContext.toString());
				// 更新监测点的电量
				this.updateRunTimeDeviceInfo(ipcanContext);
			}

		}
		return Boolean.TRUE;
	}

	// 保存上报的监测数据
	private Boolean saveRecordData(IpcanContext ipcanContext) {
		if (ipcanContext.getRecMuseumMsg() == null
				|| ipcanContext.getRecMuseumMsg().getMsgBody() == null)
			return Boolean.FALSE;
		else {
			// 消息为传感器数据
			if (ipcanContext.getRunTimeInfo().getApplicationType()
					.equals(ApplicationType.SENSOR)) {
				// 保存传感器数据
				this.saveSonsorData(ipcanContext.getRunTimeInfo()
						.getSensorMsg(), ipcanContext);
			}

		}
		return Boolean.FALSE;
	}

	// 保存传感器数据
	private Boolean saveSonsorData(MsgBody msgBody, IpcanContext ipcanContext) {
		SensorMsg sensormsg = (SensorMsg) msgBody;
		if (sensormsg != null && sensormsg.getData() != null
				&& sensormsg.getData().getDatas() != null) {

			List<SensorDataContent> datas = sensormsg.getData().getDatas();
			for (SensorDataContent content : datas) {

				if (content.getMonitorDataType().equals(
						MonitorDataType.TEMPERATURE)
						&& content.getValue() > 100L)
					log.info("正在执行保存上报监测数据到数据库中--{TEMPERATURE}--[大于100度，丢弃数据]");

				else {

					ActivityDataRecord record = new ActivityDataRecord();
					record.setDevice(ipcanContext.getRunTimeInfo()
							.getMonitoringPoint());
					record.setRepoArea(ipcanContext.getRunTimeInfo()
							.getRepoArea());
					record.setObjectType(content.getMonitorDataType());
					record.setCheckCode(content.getCheckCode());
					record.setValue(content.getValue());
					record.setDateTime(new Timestamp(System.currentTimeMillis()));
					Integer i = CommonUtils.generatRandom();
					log.info("正在执行保存上报监测数据到数据库中--{score is :}" + i);
					record.setScore(Double.valueOf(i.toString()));
					// 发送到Agent归档
					// redisMQPushSender.sendEngineToAgent(record);

					activityMonitoringDataService
							.saveOneActivityDataRecord(record);
					log.info("正在执行保存上报监测数据到数据库中--{ActivityDataRecord is :}"
							+ record.toString());

				}

			}

			return Boolean.TRUE;
		}

		return Boolean.FALSE;
	}

	/**
	 * 更新监测点的当前电量
	 * 
	 * @param ipcanContext
	 * @return
	 */
	private Boolean updateRunTimeDeviceInfo(IpcanContext ipcanContext) {
		log.info("开始更新运行时状态到数据库中");
		if (ipcanContext.getRecMuseumMsg() == null
				|| ipcanContext.getRecMuseumMsg().getMsgBody() == null) {
			log.info("{上报的ipcanContext.MsgInterface为空}");
			return Boolean.FALSE;

		}

		else {
			SensorMsg msg = (SensorMsg) ipcanContext.getRecMuseumMsg()
					.getMsgBody();
			Double power = msg.getData().getPower();
			Device device = ipcanContext.getRunTimeInfo().getMonitoringPoint();
			log.info("{开始---更新运行时状态到数据库中}");

			activityMonitoringDataService.updateRunTimeDeviceInfo(power,
					System.currentTimeMillis(), DeviceStatus.CONNECTED,
					device.getId(), getCurrentMonitorData(device, msg));
			activityMonitoringDataService.updateRunTimeDeviceInfo(100D,
					System.currentTimeMillis(), DeviceStatus.CONNECTED,
					ipcanContext.getRunTimeInfo().getMonitoringStation()
							.getId(), "");
			log.info("{结束---更新运行时状态到数据库中}");
			log.info("{开始---记录当前电量到活动设备信息记录表中}");
			ActivityDeviceInfoRecord record = new ActivityDeviceInfoRecord();
			record.setDevice(device);
			record.setDeviceStation(ipcanContext.getRunTimeInfo()
					.getMonitoringStation());
			record.setPower(power);
			record.setDateTime(TimeUtils.currentTime());
			activityMonitoringDataService.saveActivityDeviceInfoRecord(record);
			log.info("{结束---记录当前电量到活动设备信息记录表中}");
			return Boolean.TRUE;

		}

	}

	// 获取设备当前监测数据
	private String getCurrentMonitorData(Device device, SensorMsg msg) {
		String currentMonitorValue = device.getCurrentMonitorValue();
		DeviceDataMsg deviceDataMsg = JSONUtil.deserialize(currentMonitorValue,
				DeviceDataMsg.class);
		List<DataContent> result = new ArrayList<DataContent>();
		if (deviceDataMsg == null)
			deviceDataMsg = new DeviceDataMsg();

		for (DataContent con : msg.getData().getDatas()) {

			result.add(con);

		}

		deviceDataMsg.setDatas(result);
		deviceDataMsg.setDeviceId(device.getId());
		deviceDataMsg.setDeviceName(device.getName());
		return JSONUtil.serialize(deviceDataMsg);
	}

	@Override
	public MuseumMsg reportSensorStatus(IpcanContext ipcanContext) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MuseumMsg reportSensorUpgradeStatus(IpcanContext ipcanContext) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MuseumMsg getNetTopologyList(SensorNetStruct sensorNetStruct,
			MsgHeader msgHeader, MsgProperty msgProperty, SensorMsg sensorMsg) {
		MuseumMsg museumMsg = new MuseumMsg();
		msgHeader.setSendFrom("ENGINE");
		msgHeader.setSendTo("NETTY");
		msgHeader.setTimeStamp(System.currentTimeMillis());
		museumMsg.setMsgHeader(msgHeader);

		msgProperty.setCmdType(CmdType.ANSWER);
		museumMsg.setMsgProperty(msgProperty);

		sensorMsg.setSensorAppType(SensorAppType.ENGINE_POST_NET_TOPOLOCY_INFO);
		if (sensorNetStruct == null)
			return null;
		String masterGatewayId = sensorNetStruct.getMasterGatewayId();
		String masterGatewayMacAddr = sensorNetStruct.getMasterGatewayMacAddr();
		// 上报主网关的编号和MAC地址为空
		if (StringUtils.isBlank(masterGatewayId)
				&& StringUtils.isBlank(masterGatewayMacAddr)) {
			sensorMsg.setResult(new Result(ResultCode.ERROR_MSG_FORMAT,
					"ENGINE ID_NUM OR MAC_ADDR IS NULL"));
		} else {
			Device device = null;
			if (!StringUtils.isBlank(masterGatewayId)) {
				device = deviceDao.findByDeviceNo(masterGatewayId);
			}
			if (!StringUtils.isBlank(masterGatewayMacAddr)) {
				device = deviceDao.findByMacAddr(masterGatewayMacAddr);
			}

			if (device == null) {
				sensorMsg.setResult(new Result(ResultCode.ERROR_MSG_FORMAT,
						"UNKNOWN ENGINE ID_NUM OR MAC"));
			} else {
				List<String> topologyList = commonSensorService
						.getTopologyList(device.getId(), device.getDeviceNo());

				if (topologyList == null || topologyList.size() == 0)
					sensorMsg.setResult(new Result(ResultCode.ERROR_MSG_FORMAT,
							"ENGIN_HAS_NOT_TOPOLOGY_LIST"));
				else {
					SensorNetTopology sensorNetTopology = new SensorNetTopology();
					sensorNetTopology.setRouterNetTopology(topologyList);
					sensorNetTopology.setMasterGatewayId(masterGatewayId);
					sensorMsg.getData().getMtmNetInfo()
							.setSensorNetTopology(sensorNetTopology);
					sensorMsg.setResult(new Result(ResultCode.SUCCESS,
							"SUCCESS"));

				}
			}
		}
		museumMsg.setMsgBody(sensorMsg);
		return museumMsg;
	}

	@Override
	public MuseumMsg reportSensorAlarmStatus(MsgHeader msgHeader,
			MsgProperty msgProperty, SensorMsg sensorMsg) {
		MuseumMsg museumMsg = new MuseumMsg();
		msgHeader.setSendFrom("ENGINE");
		msgHeader.setSendTo("NETTY");
		msgHeader.setTimeStamp(System.currentTimeMillis());
		museumMsg.setMsgHeader(msgHeader);

		msgProperty.setCmdType(CmdType.ANSWER);
		museumMsg.setMsgProperty(msgProperty);

		sensorMsg.setSensorAppType(SensorAppType.ENGINE_NOTIFY_ALARM);

		SensorStatusInfo sensorStatusInfo = sensorMsg.getSensorStatusInfo();
		if (sensorStatusInfo != null
				&& sensorStatusInfo.getDeviceStatus() != null) {
			if (StringUtils.isBlank(sensorStatusInfo.getId())
					&& StringUtils.isBlank(sensorStatusInfo.getMacAddr())) {
				sensorMsg.setResult(new Result(ResultCode.ERROR_MSG_FORMAT,
						"DEVICE ID OR MAC_ADDR IS NULL"));
			} else {
				Device device = null;
				if (StringUtils.isNotBlank(sensorStatusInfo.getId())) {
					device = deviceDao.findByDeviceNo(sensorStatusInfo.getId());
				} else {
					device = deviceDao.findByMacAddr(sensorStatusInfo
							.getMacAddr());
				}

				if (device != null) {
					deviceDao.updateDeivceStatusById(
							sensorStatusInfo.getDeviceStatus(), device.getId());
					sensorMsg.setResult(new Result(ResultCode.SUCCESS,
							"DEVICE ALARM PROCESSED SUCCESSFUL"));
				} else {
					sensorMsg.setResult(new Result(ResultCode.ERROR_MSG_FORMAT,
							"DEVICE IS NOT EXIST"));
				}
			}
		} else {
			sensorMsg.setResult(new Result(ResultCode.ERROR_MSG_FORMAT,
					"DEVICE OR DEVICE_STATUS IS NULL"));
		}

		museumMsg.setMsgBody(sensorMsg);
		return museumMsg;
	}

}
