/**
 2015年1月20日
 tommy
 
 */
package com.qingruan.museum.dao.entity.record;

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
import com.qingruan.museum.dao.entity.RepoArea;
import com.qingruan.museum.domain.models.enums.MonitorDataType;

/**
 * 监测设备上报的实时采集数据表
 * 
 * @author tommy
 * 
 */
@Getter
@Setter
@Entity
@Table(name = "activity_data_record")
@ToString(includeFieldNames = true)
public class ActivityDataRecord extends IdEntityTimeStamp {

	private static final long serialVersionUID = -2240045403182107924L;

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

	@Enumerated(EnumType.STRING)
	@Column(name = "object_type", length = 255, nullable = false)
	private MonitorDataType objectType;
	@Column(name = "value")
	private Double value;
	@Column(name = "score")
	private Double score;
	@Column(name = "check_code")
	private Integer checkCode;

}
