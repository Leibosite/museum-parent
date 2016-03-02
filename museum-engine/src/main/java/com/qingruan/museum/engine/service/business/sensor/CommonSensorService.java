package com.qingruan.museum.engine.service.business.sensor;

import java.util.List;

import com.qingruan.museum.dao.entity.Device;
import com.qingruan.museum.msg.sensor.SensorMacAddrInfo;

public interface CommonSensorService {
	/**
	 * 获取网络拓扑
	 * 
	 * @param id
	 * @param deviceNo
	 * @return
	 */
	List<String> getTopologyList(Long id, String deviceNo);

	List<SensorMacAddrInfo> getTopologyListDetails(Device device);

	/**
	 * 获取单个中继管辖的Mac List
	 * 
	 * @param id
	 * @return
	 */
	SensorMacAddrInfo getMonitorStationMacList(Long id);

	/**
	 * 获取中继所属的主网关ID
	 * 
	 * @param id
	 * @return
	 */
	String getMasterGwId(Long id);
}
