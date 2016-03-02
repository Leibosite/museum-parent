package com.qingruan.museum.engine.service.business.sensor;

import com.qingruan.museum.msg.MuseumMsg;

/**
 * Engine下发传感器控制命令，包括：
 * 
 * 1.Engine下发传感器升级命令 2.Engine下发传感器传感器存活状态检查命令 3.Engine下发上报传感器历史数据命令
 * 
 * @author tommy
 * 
 */
public interface SendSensorCmdService {
	/**
	 * 引擎下发中继升级命令
	 * 
	 * @param stationId
	 * @return
	 */
	MuseumMsg sendSensorUpgradeCmd(Long stationId);

	/**
	 * 引擎下发中继下所有Sensor存活状态检查命令
	 * 
	 * @param stationId
	 */
	void sendSensorStatusCheck(String stationMacAddr);

	/**
	 * 引擎下发某通知某传感器上报历史数据命令
	 * 
	 * @param sensorDeviceId
	 * @return
	 */
	MuseumMsg sendReportSensorHistoryData(Long sensorDeviceId);

	void sendStationInfoUpdate(String macAddr);

	void sendSimulatorData(String sensorMacAddr, String stationMacAddr);

	/**
	 * 引擎主动更新中继管辖的Mac List,场景:有设备增加、删除、更换 参数为中继的ID
	 * 
	 * @return
	 */

	void engineUpdateSensorMacList(Long monitorStationId);

	/**
	 * 引擎主动更新网络拓扑,场景:有设备增加、删除、更换 参数为主网关的ID
	 * 
	 * @return
	 */
	void engineUpdateNetTopologyList(Long masterGwId);
}
