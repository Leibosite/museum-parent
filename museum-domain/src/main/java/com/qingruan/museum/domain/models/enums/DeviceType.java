package com.qingruan.museum.domain.models.enums;

/**
 * 设备类型
 * 
 * @author tommy
 * 
 */
public enum DeviceType {

	MONITORING_POINT_ONE("一类监测点"), MONITORING_POINT_TWO("二类监测点"), MONITORING_STATION(
			"监测站"), CONSTANT_TH_MACHINE("恒温恒湿机"), CONSTANT_TH_PLC("恒温恒湿PLC中控"), RFID_READER(
			"湿度器"), WEATHER_STATION("气象站"), SERVER("服务器"), ROUTER(
			"路由器"), FIREWALL("防火墙"), STORAGE("存储"),AIR_CLEANER("空气净化器"),ROUTER_GATEWAY("路由网关"),MASTER_GATEWAY("主网关");

	private String value;

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public String value() {
		return value;
	}

	private DeviceType(String value) {
		this.value = value;
	}

	public static DeviceType from(String value) {
		for (DeviceType item : DeviceType.values()) {
			if (item.value.equals(value))
				return item;
		}

		return null;
	}

}
