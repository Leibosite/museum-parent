/**
 2015年2月5日
 14cells
 
 */
package com.qingruan.museum.domain.enums;

/**
 * 告警类型{设备告警；监测环境告警；基础硬件设施告警；基础软件设施告警等}
 * 
 * @author tommy
 * 
 */
public enum WarnType {

	MONITORING_EQUIPMENT_WARN("MONITORING_EQUIPMENT_WARN"), MONITORING_ENVIRONMENT_WARN(
			"MONITORING_ENVIRONMENT_WARN"), INFRASTRUCTURE_HARDWARE_WARN(
			"INFRASTRUCTURE_HARDWARE_WARN"), INFRASTRUCTURE_SOFTWARE_WARN(
			"INFRASTRUCTURE_SOFTWARE_WARN");

	private String value;


	private WarnType(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public static WarnType from(String value) {
		for (WarnType item : WarnType.values()) {
			if (item.value.equals(value))
				return item;
		}

		return null;
	}

}
