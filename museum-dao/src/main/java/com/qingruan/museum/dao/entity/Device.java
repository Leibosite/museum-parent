package com.qingruan.museum.dao.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import org.springframework.format.annotation.DateTimeFormat;

import com.qingruan.museum.domain.enums.WarnFlag;
import com.qingruan.museum.domain.enums.device.DeviceStatus;
import com.qingruan.museum.domain.models.enums.DeviceType;
import com.qingruan.museum.msg.constantth.ThDataContent.ShowCase;

/**
 * @desp 设备信息表包括{监测点、监测站、服务器、防火墙、路由器、恒温恒湿机、气象站等}
 * @author 雷德宝
 * 
 */
@Setter
@Getter
@Entity
@Table(name = "devices")
public class Device extends IdEntity<Device> {

	
	private static final long serialVersionUID = 2281332697590883458L;

	// 设备与区域的映射关系多对一,区域的ID
	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST,
			CascadeType.MERGE })
	@JoinColumn(name = "repo_area_id")
	private RepoArea repoArea;

	// parent_id
	@Column(name = "parent_id")
	private Long parentId;

	// 设备与设备版本是多对一的对应关系TODO；
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "device_version_id")
	private DeviceVersion deviceVersion;

	// 设备名称
	@Column(name = "name")
	private String name;

	// 设备描述
	@Column(name = "desp")
	private String desp;

	// 设备状态
	@Column(name = "status")
	private Integer status;

	// 设备标记
	@Column(name = "marker")
	private String marker;

	/*
	 * // 设备唯一识别码
	 * 
	 * @Column(name = "uuid") private String uuid;
	 */
	// 设备编号
	@Column(name = "device_no")
	private String deviceNo;

	// 设备电量
	@Column(name = "power")
	private Double power;

	// 开始使用时间
	@Column(name = "use_start_time")
	private Date useStartTime;

	// 到期时间
	@Column(name = "expiry_time")
	private Date expiry;

	// 使用的月份数
	@Column(name = "used_month")
	private Integer usedMonth;

	// 使用者id
	@Column(name = "user_id")
	private Integer userId;

	// 维修时间
	@Column(name = "maintenance_time")
	private Date maintenanceTime;

	// 维修信息
	@Column(name = "maintenance_info")
	private String maintenanceInfo;

	// 设备信号覆盖范围
	@Column(name = "signal_cover")
	private Integer signalCover;

	// 设备安装时间
	@Column(name = "created_at")
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date createdAt;

	// 设备更新时间
	@Column(name = "updated_at")
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date updatedAt;

	// 经度
	@Column(name = "longitude")
	private Double longitude;

	// 纬度
	@Column(name = "latitude")
	private Double latitude;
	// 报警Flag，标记该设备是否发生告警
	@Enumerated(EnumType.STRING)
	@Column(name = "warn_flag", length = 50, nullable = false)
	private WarnFlag warnFlag;

	// 当前电量
	@Column(name = "current_power")
	private Double currentPower;
	@Column(name = "update_stamp")
	private Long updateStamp;
	// 设备状态ShowCase
	@Enumerated(EnumType.STRING)
	@Column(name = "device_status", length = 50, nullable = false)
	private DeviceStatus deviceStatus;

	// Mac地址
	@Column(name = "mac_addr")
	private String macAddr;
	// ip地址
	@Column(name = "ip_addr")
	private String ipAddr;

	// major
	@Column(name = "major")
	private String major;

	// minor
	@Column(name = "minor")
	private String minor;

	@Column(name = "current_monitor_value")
	private String currentMonitorValue;

	@Enumerated(EnumType.STRING)
	@Column(name = "device_type", length = 50, nullable = false)
	private DeviceType deviceType;

	@Enumerated(EnumType.STRING)
	@Column(name = "show_case", length = 50, nullable = false)
	private ShowCase showCase;
	
	@Column(name = "setting_monitor_value")
	private String settingMonitorValue;
	
	// Mac地址
	@Column(name = "extra_mac_addr")
	private String extraMacAddr; 
	
}
