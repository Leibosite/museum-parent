package com.qingruan.museum.dao.entity.record;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.qingruan.museum.dao.entity.Device;
import com.qingruan.museum.dao.entity.IdEntityTimeStamp;

/**
 * 气象站上报数据
 * 
 * @author tommy
 * 
 */
@Getter
@Setter
@Entity
@Table(name = "activity_weather_data")
@ToString(includeFieldNames = true)
public class ActivityWeatherData extends IdEntityTimeStamp implements
		Serializable {
	private static final long serialVersionUID = -2240045403182100924L;

	@ManyToOne
	@JoinColumn(name = "device_id")
	private Device device;
	
	@Column(name = "repo_area_id", nullable = false, length = 255)
	private Long repoAreaId;

	@Column(name = "status")
	private Integer status;

	@Column(name = "desp", nullable = false, length = 255)
	private String desp;

	@Column(name = "date_time")
	private Timestamp dateTime;

	@Column(name = "wind_speed")
	private Double windSpeed;

	@Column(name = "wind_direction")
	private String windDirection;

	@Column(name = "longitude")
	private Double longitude;

	@Column(name = "latitude")
	private Double latitude;

	@Column(name = "height")
	private Double height;

	@Column(name = "temperature")
	private Double temperature;

	@Column(name = "humidity")
	private Double humidity;

	@Column(name = "pm2_5")
	private Double pm2_5;
	@Column(name = "pm1_0")
	private Double pm1_0;
	@Column(name = "air_pressure")
	private Double airPressure;
	@Column(name = "lighting")
	private Double lighting;
	@Column(name = "uv")
	private Double uv;
	// 主控板电压
	@Column(name = "main_control_panel_voltage")
	private Double mainCtrlPanelWoltage;
	// 太阳能电池板电压
	@Column(name = "solar_energy_voltage")
	private Double solarEnergyVoltage;
	@Column(name = "gps_speed")
	private Double gpsSpeed;
	@Column(name = "gps_direction")
	private Double gpsDirection;

}