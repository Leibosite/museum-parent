package com.qingruan.museum.engine.service.business.sensor.impl;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qingruan.museum.dao.entity.Device;
import com.qingruan.museum.dao.repository.DeviceDao;
import com.qingruan.museum.domain.models.enums.DeviceType;
import com.qingruan.museum.engine.service.business.sensor.CommonSensorService;
import com.qingruan.museum.msg.sensor.SensorMacAddrInfo;
import com.qingruan.museum.msg.sensor.SensorNetInfo;

@Slf4j
@Service
public class CommonSensorServiceImpl implements CommonSensorService {
	@Autowired
	private DeviceDao deviceDao;

	@Override
	public List<String> getTopologyList(Long id, String deviceNo) {
		log.info("--[start]---{getTopologyList}----");
		List<String> topologyList = new ArrayList<String>();
		List<Device> deviceList = deviceDao.findByParentId(id);

		if (deviceList != null) {
			for (Device device : deviceList) {
				// 从属设备链接中继或路由
				if (device.getDeviceType()
						.equals(DeviceType.MONITORING_STATION)) {
					// topologyList.add(deviceNo + "-" + device.getDeviceNo());
					topologyList.add(deviceNo +"0000" +device.getDeviceNo());
				} else {
					List<Device> secondDeviceList = deviceDao
							.findByParentId(device.getId());
					if (secondDeviceList != null) {
						for (Device secondDevice : secondDeviceList) {
							if (secondDevice.getDeviceType().equals(
									DeviceType.MONITORING_STATION)) {
								topologyList.add(deviceNo
										+ device.getDeviceNo()
										+ secondDevice.getDeviceNo());
							}
						}
					}
				}
			}
		}
		return topologyList;

	}

	public List<SensorMacAddrInfo> getTopologyListDetails(Device device) {
		List<SensorMacAddrInfo> sensorMacAddrInfos = new ArrayList<SensorMacAddrInfo>();

		List<Device> secondDeviceList = deviceDao
				.findByParentId(device.getId());
		if (secondDeviceList != null) {
			for (Device secondDevice : secondDeviceList) {
				// 从属设备链接中继或路由
				if (secondDevice.getDeviceType().equals(
						DeviceType.MONITORING_STATION)) {
					SensorMacAddrInfo sensorMacAddrInfo = new SensorMacAddrInfo();
					sensorMacAddrInfo
							.setRouterNetTopology(device.getDeviceNo()
									.toString()+"0000"
									+ secondDevice.getDeviceNo().toString());
					sensorMacAddrInfo
							.setSensorNetInfos(getMacAddrList(secondDevice
									.getId()));
					sensorMacAddrInfos.add(sensorMacAddrInfo);
				} else {
					List<Device> thirdDeviceList = deviceDao
							.findByParentId(secondDevice.getId());
					if (thirdDeviceList != null) {
						for (Device thirdDevice : thirdDeviceList) {
							if (thirdDevice.getDeviceType().equals(
									DeviceType.MONITORING_STATION)) {
								SensorMacAddrInfo sensorMacAddrInfo = new SensorMacAddrInfo();
								sensorMacAddrInfo.setRouterNetTopology(device
										.getDeviceNo().toString()
										+ secondDevice.getDeviceNo().toString()
										+ thirdDevice.getDeviceNo().toString());
								sensorMacAddrInfo
										.setSensorNetInfos(getMacAddrList(thirdDevice
												.getId()));
								sensorMacAddrInfos.add(sensorMacAddrInfo);
							}
						}
					}
				}
			}
		}
		return sensorMacAddrInfos;
	}

	@Override
	public SensorMacAddrInfo getMonitorStationMacList(Long id) {

		if (id == null)
			return null;
		Device monitorStation = deviceDao.findOne(id);
		if (monitorStation == null
				|| (!monitorStation.getDeviceType().equals(
						DeviceType.MONITORING_STATION)))
			return null;

		SensorMacAddrInfo sensorMacAddrInfo = new SensorMacAddrInfo();

		List<SensorNetInfo> sensorNetInfos = getMacAddrList(monitorStation
				.getId());
		String routing = this.getStationRouting(monitorStation);

		if (sensorNetInfos != null && sensorNetInfos.size() != 0
				&& StringUtils.isNoneBlank(routing)) {
			// 设置管辖的传感器的MAC列表
			sensorMacAddrInfo.setSensorNetInfos(getMacAddrList(monitorStation
					.getId()));
			sensorMacAddrInfo.setRouterNetTopology(routing);
			// 设置寻址到该中继的路由
			return sensorMacAddrInfo;
		}

		return null;

	}

	/**
	 * 获取中继的Routing路劲
	 * 
	 * @param device
	 * @return
	 */
	private String getStationRouting(Device device) {
		if (device == null)
			return null;
		else {
			if (device.getParentId() == null
					|| StringUtils.isBlank(device.getDeviceNo()))
				return null;
			else {

				String routing = "";
				Device stationParent = deviceDao.findById(device.getParentId());
				if (stationParent == null
						|| StringUtils.isBlank(stationParent.getDeviceNo()))
					return null;
				else {
					routing = device.getDeviceNo()
							+ stationParent.getDeviceNo();
					if (stationParent.getDeviceType().equals(
							DeviceType.MASTER_GATEWAY))
						return routing;
					else {

						Device masterGateway = deviceDao.findById(stationParent
								.getParentId());
						if (masterGateway != null
								&& StringUtils.isNoneBlank(masterGateway
										.getDeviceNo()))
							routing = routing + masterGateway.getDeviceNo();
						return routing;
					}
				}
			}
		}
	}

	private List<SensorNetInfo> getMacAddrList(Long id) {
		List<SensorNetInfo> sensorNetInfos = new ArrayList<SensorNetInfo>();

		List<Device> sensorList = deviceDao.findByParentId(id);
		if (sensorList != null && sensorList.size() > 0) {
			for (Device device : sensorList) {
				SensorNetInfo netInfo = new SensorNetInfo();
				netInfo.setSensorMacAddr(device.getMacAddr());
				netInfo.setMajor(device.getMajor());
				netInfo.setMinor(device.getMinor());
				sensorNetInfos.add(netInfo);
			}
		}

		return sensorNetInfos;
	}

	@Override
	public String getMasterGwId(Long id) {

		if (id == null)
			return null;
		else {
			Device stationParent = deviceDao.findById(id);
			if (stationParent == null)
				return null;
			else {
				if (stationParent.getDeviceType().equals(
						DeviceType.MASTER_GATEWAY))
					return stationParent.getDeviceNo();
				else {

					Device masterGateway = deviceDao.findById(stationParent
							.getParentId());
					if (masterGateway != null
							&& StringUtils.isNoneBlank(masterGateway
									.getDeviceNo()))
						return masterGateway.getDeviceNo();

				}

				return null;
			}
		}

	}

}
