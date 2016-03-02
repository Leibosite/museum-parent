package com.qingruan.museum.engine.service.business.constantth.impl;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qingruan.museum.dao.entity.CaseParams;
import com.qingruan.museum.dao.entity.Device;
import com.qingruan.museum.dao.entity.RepoArea;
import com.qingruan.museum.dao.repository.CaseParamsDao;
import com.qingruan.museum.dao.repository.DeviceDao;
import com.qingruan.museum.dao.repository.RepoAreaDao;
import com.qingruan.museum.domain.models.enums.DeviceType;
import com.qingruan.museum.domain.models.enums.MonitorDataType;
import com.qingruan.museum.engine.service.business.constantth.SendConstantThCmdService;
import com.qingruan.museum.framework.esb.redispushpop.RedisMQPushSender;
import com.qingruan.museum.framework.jackson.JSONUtil;
import com.qingruan.museum.msg.MsgHeader;
import com.qingruan.museum.msg.MsgHeader.DeliveryMode;
import com.qingruan.museum.msg.MsgProperty;
import com.qingruan.museum.msg.MsgProperty.ApplicationType;
import com.qingruan.museum.msg.MsgProperty.CmdType;
import com.qingruan.museum.msg.MuseumMsg;
import com.qingruan.museum.msg.constantth.ConstantThMsg;
import com.qingruan.museum.msg.constantth.ConstantThMsg.ThAppType;
import com.qingruan.museum.msg.constantth.ConstantThMsg.ThMsgType;
import com.qingruan.museum.msg.constantth.ThData;
import com.qingruan.museum.msg.constantth.ThDataContent;
import com.qingruan.museum.msg.constantth.ThDataContent.ShowCase;
import com.qingruan.museum.msg.constantth.ThNetInfos;
import com.qingruan.museum.msg.sensor.MtmNetInfo.IpAddrType;

@Service
@Slf4j
public class SendConstantThCmdServiceImpl implements SendConstantThCmdService {
	@Autowired
	private DeviceDao deviceDao;
	@Autowired
	private RedisMQPushSender redisMQPushSender;
	@Autowired
	private CaseParamsDao caseParamsDao;
	@Autowired
	private RepoAreaDao repoAreaDao;

	public SendConstantThCmdServiceImpl() {
	}

	@Override
	public MuseumMsg sendConstantCmd(Long areaId) {
		log.info("{Start SendConstantThCmdServiceImpl.sendConstantCmd()}");
		RepoArea repoArea = repoAreaDao.findOne(areaId);
		if (areaId == null)
			return null;
		List<CaseParams> caseParams = caseParamsDao.findByRepoArea(repoArea);
		if (caseParams == null || caseParams.size() == 0)
			return null;
		List<Device> devices = deviceDao.findByRepoArea(repoArea);
		if (devices == null || devices.size() == 0)
			return null;
		// 获取恒温恒湿机
		Device device = devices.get(0);

		Long plcId = device.getParentId();
		if (plcId == null)
			return null;
		Device plc = deviceDao.findOne(plcId);
		MuseumMsg museumMsg = new MuseumMsg();
		MsgHeader msgHeader = new MsgHeader("1", "ENGINE", "NETTY", "NETTY",
				System.currentTimeMillis(), 1, DeliveryMode.PTP, 0);
		museumMsg.setMsgHeader(msgHeader);
		MsgProperty msgProperty = new MsgProperty(CmdType.REQUEST,
				ApplicationType.CONSTANT_TH);
		museumMsg.setMsgProperty(msgProperty);
		ConstantThMsg constantThMsg = new ConstantThMsg();
		constantThMsg.setMsgId("1");
		constantThMsg.setQos(1);
		constantThMsg.setThAppType(ThAppType.ENGINE_WRITE_TH_PRESET_DATA);
		constantThMsg.setThMsgType(ThMsgType.TEXT);
		ThData thData = new ThData();
		ThNetInfos netInfo = new ThNetInfos();
		netInfo.setPlcIpv4Addr(plc.getIpAddr());
		netInfo.setPlcIpAddrType(IpAddrType.IPV4);
		thData.setThNetInfos(netInfo);
		for (CaseParams p : caseParams) {
			ThDataContent thDataContent = new ThDataContent();
			thDataContent.setMonitorDataType(p.getObjectType());
			thDataContent.setValue(p.getValue());
			thDataContent.setShowCase(device.getShowCase());
			thData.getDatas().add(thDataContent);
		}

		constantThMsg.setData(thData);
		museumMsg.setMsgBody(constantThMsg);
		log.info("{End SendConstantThCmdServiceImpl.sendConstantCmd()}");
		return museumMsg;
	}

	@Override
	public MuseumMsg setConstantCmd(Long id, Long presentValue, Long topLimit,
			Long lowerLimit, Long fluctuationValue, String monitorDataType) {
		MuseumMsg museumMsg = new MuseumMsg();
		MsgHeader msgHeader = new MsgHeader();
		msgHeader.setDeliveryMode(DeliveryMode.PTP);
		msgHeader.setSendFrom("ENGINE");
		msgHeader.setSendTo("NETTY");

		msgHeader.setTimeStamp(System.currentTimeMillis());
		museumMsg.setMsgHeader(msgHeader);
		MsgProperty msgProperty = new MsgProperty();
		msgProperty.setCmdType(CmdType.REQUEST);
		msgProperty.setApplicationType(ApplicationType.NEW_CONSTANT_TH);
		museumMsg.setMsgProperty(msgProperty);

		ConstantThMsg constantMsg = new ConstantThMsg();
		ThData thData = new ThData();
		this.setMasterGwIdAndRouting(id, thData);
		thData.setTopLimit(topLimit);
		thData.setLowerLimit(lowerLimit);
		thData.setPresentValue(presentValue);
		thData.setFluctuationValue(fluctuationValue);

		constantMsg.setData(thData);
		constantMsg.setThAppType(ThAppType.ENGINE_WRITE_TH_PRESET_DATA);
		constantMsg.setThMsgType(ThMsgType.TEXT);
		museumMsg.setMsgBody(constantMsg);
		String json = JSONUtil.serialize(museumMsg);
		this.saveSettedParams(id, presentValue, topLimit, lowerLimit,
				fluctuationValue, monitorDataType);
		redisMQPushSender.sendEngineToNetty(json);
		return museumMsg;
	}

	/**
	 * 获取主网关ID
	 * 
	 * @param id
	 * @return
	 */
	private void setMasterGwIdAndRouting(Long id, ThData thData) {
		Device device = null;
		List<Device> deviceList = deviceDao.findByRepoAreaId(id,DeviceType.CONSTANT_TH_MACHINE);
		if (deviceList == null || deviceList.size() == 0)
			return;
		device=deviceList.get(0);
		String mac = "";
		String path = "";
//		device = deviceDao.findById(id);
		if (device == null)
			return;
		// 中继
		Device parentDevice = deviceDao.findById(device.getParentId());
		if (parentDevice == null)
			return;
		// 主网关
		Device parentUpDevice = deviceDao.findById(parentDevice.getParentId());
	
		if (parentUpDevice.getDeviceType().equals(DeviceType.MASTER_GATEWAY)) {
			path = parentUpDevice.getDeviceNo() +"0000"+ parentDevice.getDeviceNo();
			thData.setMasterGatewayID(parentUpDevice.getDeviceNo());
			thData.setPath(path);
			thData.setPresentMAC(device.getExtraMacAddr());
			thData.setMacAddr(device.getMacAddr());
		} else {
			path = parentUpDevice.getDeviceNo() + parentDevice.getDeviceNo();
			Device masterGw = deviceDao.findById(parentUpDevice.getParentId());
			if (masterGw.getDeviceType().equals(DeviceType.MASTER_GATEWAY)) {
				path = masterGw.getDeviceNo() + path;
				thData.setMasterGatewayID(masterGw.getDeviceNo());
				thData.setPath(path);
				thData.setPresentMAC(device.getExtraMacAddr());
				thData.setMacAddr(device.getMacAddr());
			}

		}
	}

	private void saveSettedParams(Long id, Long presentValue, Long topLimit,
			Long lowerLimit, Long fluctuationValue, String monitorDataType) {
		Device device = deviceDao.findOne(id);
		if (device == null)
			return;
		RepoArea repoArea = device.getRepoArea();
		if (repoArea == null)
			return;
		MonitorDataType type = MonitorDataType.valueOf(monitorDataType);

		List<CaseParams> caseParamsList = caseParamsDao.findByRepoAreaAndType(
				repoArea, type);

		if (caseParamsList == null || caseParamsList.size() == 0) {
			CaseParams caseParams = new CaseParams();
			caseParams.setObjectType(type);
			caseParams.setRepoArea(repoArea);
			caseParams.setValue(Double.valueOf(presentValue.toString()));
			caseParamsDao.save(caseParams);

		} else {
			CaseParams caseParam = caseParamsList.get(0);
			caseParam.setValue(Double.valueOf(presentValue.toString()));
			caseParamsDao.save(caseParam);

		}

	}
}
