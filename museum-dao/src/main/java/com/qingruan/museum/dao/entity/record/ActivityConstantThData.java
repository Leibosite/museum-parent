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

/**
 * 恒温恒湿上报数据
 * 
 * @author tommy
 * 
 */
@Getter
@Setter
@Entity
@Table(name = "activity_constant_thdata")
@ToString(includeFieldNames = true)
public class ActivityConstantThData extends IdEntityTimeStamp implements Serializable {
	private static final long serialVersionUID = -2240045403182100924L;
    //所属设备
	@ManyToOne
	@JoinColumn(name = "device_id")
	private Device device;
	   //所属区域
	@ManyToOne
	@JoinColumn(name = "repo_area_id")
	private RepoArea repoArea;
    //数据状态
	@Column(name = "status")
	private Integer status;
	  //数据描述
	@Column(name = "desp", nullable = false, length = 255)
	private String desp;
	//上报数据
	@Column(name = "date_time")
	private Timestamp dateTime;
    //数据类型
	@Enumerated(EnumType.STRING)
	@Column(name = "object_type", length = 255, nullable = false)
	private MonitorDataType objectType;
	//数据值
	@Column(name = "value")
	private Double value;
}
