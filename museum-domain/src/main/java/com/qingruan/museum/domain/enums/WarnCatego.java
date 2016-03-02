/**
 2015年2月5日
 14cells
 
 */
package com.qingruan.museum.domain.enums;

/**
 * 告警类别
 * 
 * @author tommy
 * 
 */

public enum WarnCatego {

	MONITORING_EQUIPMENT_POWER("监测设备电量低报警"), MONITORING_EQUIPMENT_DAMAGE(
			"监测设备损坏报警"), MONITORING_ENVIRONMENT_SO2("二氧化硫报警"), MONITORING_ENVIRONMENT_CO2(
			"二氧化碳报警"), MONITORING_ENVIRONMENT_TEMPERATURE("温度报警"), MONITORING_ENVIRONMENT_HUMIDITY(
			"湿度报警"), MONITORING_ENVIRONMENT_LIGHTING("光照报警"), MONITORING_ENVIRONMENT_UV(
			"紫外线报警"), MONITORING_ENVIRONMENT_VOC("VOC有害气体报警"), MONITORING_ENVIRONMENT_PM2_5(
			"PM2.5报警"), MONITORING_ENVIRONMENT_PM10("PM10报警"), MONITORING_ENVIRONMENT_PM1_0(
			"PM1.0报警"), MONITORING_ENVIRONMENT_PM_01_03(
			"0.1升空气中直径在0.3um以上的颗粒报警"), MONITORING_ENVIRONMENT_PM_01_05(
			"0.1升空气中直径在0.5um以上的颗粒报警"), MONITORING_ENVIRONMENT_PM_01_10(
			"0.1升空气中直径在1.0um以上的颗粒报警"), MONITORING_ENVIRONMENT_PM_01_50(
			"0.1升空气中直径在5.0um以上的颗粒报警"), MONITORING_ENVIRONMENT_PM_01_100(
			"0.1升空气中直径在10um以上的颗粒报警"), MONITORING_ENVIRONMENT_CH4("甲醛报警"), INFRASTRUCTURE_HARDWARE_POWER(
			"基础硬件电量报警"), INFRASTRUCTURE_HARDWARE_DAMAGE("基础硬件损坏报警"), INFRASTRUCTURE_SOFTWARE_EXPIRE(
			"基础软件过期报警"), INFRASTRUCTURE_SOFTWARE_RPOCESS_DOWNTIME("基础软件进程宕机报警");

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

	private WarnCatego(String value) {
		this.value = value;
	}



	public static WarnCatego from(String value) {
		for (WarnCatego item : WarnCatego.values()) {
			if (item.value.equals(value))
				return item;
		}

		return null;
	}

}
