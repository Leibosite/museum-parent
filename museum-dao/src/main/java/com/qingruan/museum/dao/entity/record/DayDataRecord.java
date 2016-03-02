/**
 2015年3月3日
 14cells
 
 */
package com.qingruan.museum.dao.entity.record;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.qingruan.museum.dao.entity.Device;
import com.qingruan.museum.dao.entity.IdEntityTimeStamp;
import com.qingruan.museum.dao.entity.UuidEntityTimeStamp;
import com.qingruan.museum.dao.entity.RepoArea;
import com.qingruan.museum.domain.models.enums.MonitorDataType;
import com.qingruan.museum.domain.models.enums.MonitorDeviceType;

/**
 * @author tommy
 * 
 */
@Getter
@Setter
@Entity
@Table(name = "day_data_record")
@ToString(includeFieldNames = true)
public class DayDataRecord extends IdEntityTimeStamp implements Serializable {

	private static final long serialVersionUID = -2240045403182207924L;

	@ManyToOne
	@JoinColumn(name = "device_id")
	private Device device;
	@ManyToOne
	@JoinColumn(name = "repo_area_id")
	private RepoArea repoArea;
	@Column(name = "status")
	private Integer status;
	@Column(name = "desp", nullable = false, length = 255)
	private String desp;
	// 上报其他信息
	@Column(name = "report_info")
	private String reportInfo;

	@Column(name = "date_time")
	private Timestamp dateTime;

	// 报警类型{设备告警、监测环境告警等}
	@Enumerated(EnumType.STRING)
	@Column(name = "object_type", length = 255, nullable = false)
	private MonitorDataType objectType;

	@Column(name = "score")
	private Double score;

	@Column(name = "avg")
	private Double avg;
	@Column(name = "min")
	private Double min;
	@Column(name = "max")
	private Double max;
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "monitor_device_type", length = 1, nullable = false)
	private MonitorDeviceType monitorDeviceType;

}
