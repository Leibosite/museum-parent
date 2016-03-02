package com.qingruan.museum.engine.service.business.sensor.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qingruan.museum.dao.entity.Device;
import com.qingruan.museum.dao.repository.DeviceDao;
import com.qingruan.museum.engine.service.business.sensor.CheckDeviceStatusService;
import com.qingruan.museum.framework.esb.redispushpop.RedisMQPushSender;
import com.qingruan.museum.framework.jackson.JSONUtil;
import com.qingruan.museum.msg.MsgHeader;
import com.qingruan.museum.msg.MsgProperty;
import com.qingruan.museum.msg.MuseumMsg;
import com.qingruan.museum.msg.MsgProperty.ApplicationType;
import com.qingruan.museum.msg.MsgProperty.CmdType;
import com.qingruan.museum.msg.sensor.MtmNetInfo;
import com.qingruan.museum.msg.sensor.SensorData;
import com.qingruan.museum.msg.sensor.SensorMsg;
import com.qingruan.museum.msg.sensor.SensorMsg.SensorAppType;

import lombok.extern.slf4j.Slf4j;

import com.qingruan.museum.msg.sensor.SensorStatusInfo;

@Service
@Slf4j
public class CheckDeviceStatusServiceImpl implements CheckDeviceStatusService{
	@Autowired
	private DeviceDao deviceDao;
	@Autowired
	private RedisMQPushSender redisMQPushSender;
	
	@Override
	public void sendDeviceStatusCheck(Device device) {
		log.info("Engine检查设备状态开始处理");
		
		if(device != null) {
			MuseumMsg museumMsg = new MuseumMsg();
			
			MsgHeader msgHeader = new MsgHeader();
			msgHeader.setSendFrom("ENGINE");
			msgHeader.setSendTo("NETTY");
			msgHeader.setTimeStamp(System.currentTimeMillis());
			museumMsg.setMsgHeader(msgHeader);
			
			MsgProperty msgProperty = new MsgProperty();
			msgProperty.setCmdType(CmdType.REQUEST);
			msgProperty.setApplicationType(ApplicationType.SENSOR);
			museumMsg.setMsgProperty(msgProperty);
			
			SensorMsg sensorMsg = new SensorMsg();
			sensorMsg.setSensorAppType(SensorAppType.ENGINE_GET_DEVICE_STATUS);
			
			SensorStatusInfo sensorStatusInfo = new SensorStatusInfo();
			sensorStatusInfo.setMacAddr(device.getMacAddr());
			sensorStatusInfo.setId(device.getDeviceNo());
			sensorStatusInfo.setDeviceType(device.getDeviceType());
			
			String masterId = "";
			Long parentId = device.getParentId();
			
			while(parentId != null) {
				Device topologyDevice = deviceDao.findById(parentId);
				masterId = topologyDevice.getDeviceNo();
				parentId = topologyDevice.getParentId();
			}
			
			if (StringUtils.isNotBlank(masterId)) {
				sensorStatusInfo.setMasterId(masterId);
			} else {
				log.info("主网关id为空");
			}
			
			sensorMsg.setSensorStatusInfo(sensorStatusInfo);
			
			// 由于 data 与 mtmNetInfo 属性判断非空，初始化一个空的实体
			SensorData SensorData = new SensorData();
			SensorData.setMtmNetInfo(new MtmNetInfo());
			sensorMsg.setData(SensorData);
			
			museumMsg.setMsgBody(sensorMsg);
			log.info("MuseumMsg数据实体准备完毕");
			
			String msg = JSONUtil.serialize(museumMsg);
			if (StringUtils.isNotBlank(msg)) {
				this.redisMQPushSender.sendEngineToNetty(msg);
				log.info("消息发送至Netty端完毕");
			} else {
				log.info("数据实体序列化为空");
			}
		} else {
			log.info("待检查的设备是空");
		}
	}
}
