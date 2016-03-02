package com.qingruan.museum.dao.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.format.annotation.DateTimeFormat;

import com.qingruan.museum.domain.models.enums.AreaType;

/**
 * @desp 区域
 * @author 雷得宝
 * 
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "repo_areas")
public class RepoArea extends IdEntity {

	// 文物放置单元编号
	// TODO:数据库中编号需要全写，不使用缩写
	// @Column(name ="repo_no")
	// private String repoNo;

	// 文物放置单元名称
	@Column(name = "name")
	private String name;

	// 文物放置单元描述
	@Column(name = "desp")
	private String desp;
	// 文物放置单元的类型
	@Enumerated(EnumType.STRING)
	@Column(name = "area_type", length = 255, nullable = false)
	private AreaType areaType;

	// 文物放置单元负责人id
	@Column(name = "user_id")
	private Long userId;

	// 所属建筑物
	@Column(name = "build")
	private String build;

	// 所属楼层
	@Column(name = "floor")
	private String floor;

	// 区域是否已经绑定设备 1已经绑定 0没有绑定任何文物
	@Column(name = "bundle_cultural_relic")
	private Integer bundleCulturalRelic;

	// 区域是否已经绑定文物 1已经绑定 0没有绑定任何的设备
	@Column(name = "bundle_device")
	private Integer bundleDevice;

	// //文物放置单元所属区域编号
	// @Column(name = "parent_repo_id")
	// private Long parentRepoId;

	// 文物放置单元创建时间
	@Column(name = "created_at")
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date createdAt;

	// 文物放置单元更新时间
	@Column(name = "updated_at")
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date updatedAt;

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER, mappedBy = "repoArea")
	private List<CulturalRelic> culturalRelics = new ArrayList<CulturalRelic>();
	// 当前监测数据
	@Column(name = "current_monitor_value")
	private String currentMonitorValue;

}
