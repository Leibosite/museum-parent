package com.qingruan.museum.dao.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 组网信息表对应实体
 * @author 雷得宝
 *
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "network_infos")
public class NetworkInfo extends IdEntity{
	
	//组网与设备是多对一关系
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "device_id")
	private Device device;
	
	// 设备名称
	@Column(name = "name")
	private String name;

	// 设备描述
	@Column(name = "desc")
	private String desc;

	// 设备状态
	@Column(name = "status")
	private Integer status;
	
	// 组网端口
	@Column(name = "net_port")
	private String netPort;
	
	//组网类型
	@Column(name = "net_type")
	private String netType;
	
	// 本地ipv4地址
	@Column(name = "local_ipv4_address")
	private String localIpv4Address;

	// 本地ipv6地址
	@Column(name = "local_ipv6_address")
	private String localIpv6Address;

	// 本地mac地址
	@Column(name = "local_mac_address")
	private String localMacAddress;

	// 本地ip地址类型
	@Column(name = "local_ip_type")
	private String localIpType;

	// 本地ip应用
	@Column(name = "local_ip_application")
	private String localIpApplication;
	
	// 远程ipv4地址
	@Column(name = "remote_ipv4_address")
	private String remoteIpv4Address;

	// 远程ipv6地址
	@Column(name = "remote_ipv6_address")
	private String remoteIpv6Address;

	// 远程mac地址
	@Column(name = "remote_mac_address")
	private String remoteMacAddress;

	// 远程ip地址类型
	@Column(name = "remote_ip_type")
	private String remoteIpType;

	// 远程ip应用
	@Column(name = "remote_ip_application")
	private String remoteIpApplication;

	//创建时间
	@Column(name = "created_at")
	@DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
	private Date createdAt;
	
	//更新时间
	@Column(name = "updated_at")
	@DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
	private Date updatedAt;
	
}
