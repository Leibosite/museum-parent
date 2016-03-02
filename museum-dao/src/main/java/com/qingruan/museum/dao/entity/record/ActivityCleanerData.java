package com.qingruan.museum.dao.entity.record;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.qingruan.museum.dao.entity.Device;
import com.qingruan.museum.dao.entity.IdEntityTimeStamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(name = "activity_cleaner_data")
@ToString(includeFieldNames = true)
public class ActivityCleanerData extends IdEntityTimeStamp implements
		Serializable {

	private static final long serialVersionUID = 395274382314951992L;

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
	@Column(name = "temperature")
	private Double temperature;

	@Column(name = "humidity")
	private Double humidity;

	@Column(name = "pm2_5")
	private Double pm2_5;
	@Column(name = "pm1_0")
	private Double pm1_0;
}
