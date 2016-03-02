package com.qingruan.museum.dao.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import org.springframework.format.annotation.DateTimeFormat;
/**
 * @desp 设备版本表
 * @author 雷德宝
 *
 */
@Setter
@Getter
@Entity
@Table(name = "device_versions")
public class DeviceVersion extends IdEntity{
	
	//设备功能
	@Column(name = "name")
	private String name;
	
	//设备描述
	@Column(name = "desc")
	private String desc;
	
	//设备安装时间
	@Column(name = "created_at")
	@DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
	private Date createAt;
	
	//设备更新时间
	@Column(name = "updated_at")
	@DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
	private Date UpdateAt;
	
	@OneToMany(cascade={CascadeType.PERSIST,CascadeType.MERGE},fetch=FetchType.EAGER,mappedBy="deviceVersion")
	private List<Device> devices = new ArrayList<Device>(); 
}
