/**
 2015年2月9日
 14cells
 
 */
package com.qingruan.museum.domain.enums.device;

/**
 * 设备状态
 * 
 * @author tommy
 * 
 */
public enum DeviceStatus {

	DISCONNECT("断开连接"), CONNECTED("连接"), NO_POWER("设备没电"), DEMAGED("损坏");
	// TODO:带扩展

	private final String value;

	private DeviceStatus(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public static DeviceStatus from(String value) {
		for (DeviceStatus item : DeviceStatus.values()) {
			if (item.value.equals(value))
				return item;
		}

		return null;
	}

}
