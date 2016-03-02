/**
 2014年12月18日
 tommy
 
 */
package com.qingruan.museum.dao.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 告警信息表
 * 
 * @author tommy
 * 
 */
@Getter
@Setter
@ToString(exclude = { "device", "warnCategory" })
@Entity
@Table(name = "alarm")
public class Alarm extends IdEntityTimeStamp {
	// 名称
	@Column(name = "name")
	private String name;
	// 描述
	@Column(name = "desp")
	private String desp;
	// 告警状态
	@Column(name = "status")
	private String status;
	// 告警优先级
	@Column(name = "priority")
	private String priority;
	// 告警类型
	@Column(name = "alarm_type")
	private String alarmType;
	// 告警处理提示
	@Column(name = "alarm_handling_tips")
	private String alarmHandlingTips;
	// 告警详情,设备序列化成JSON格式
	@JsonFormat
	@Column(name = "alarm_details")
	private String alarmDetails;
	// 报警处理状态，0未处理1已处理
	@Column(name = "deal_status")
	private Integer dealStatus;

	// 告警与设备id是多对一
	@ManyToOne
	@LazyToOne(LazyToOneOption.FALSE)
	@JoinColumn(name = "device_id")
	private Device device;

	@ManyToOne
	@LazyToOne(LazyToOneOption.FALSE)
	@JoinColumn(name = "warn_category_id")
	private WarnCategory warnCategory;

	@ManyToOne
	@LazyToOne(LazyToOneOption.FALSE)
	@JoinColumn(name = "repo_area_id")
	private RepoArea repoArea;
	// 日期
	@Column(name = "date_stamp")
	private String dateStamp;
	@Column(name = "alarm_handler")
	private String alarmHandler;
	@Column(name = "alarm_handler_time")
	private Date alarmHandlerTime;
	@Column(name = "alarm_handler_details")
	private String alarmHandlerDetails;
}
