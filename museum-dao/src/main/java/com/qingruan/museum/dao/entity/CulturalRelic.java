package com.qingruan.museum.dao.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude={"repoArea"})
@Entity
@Table(name = "cultural_relics")
public class CulturalRelic extends IdEntity{
	
	@Column(name="name")
	private String name;
	
	@Column(name="year")
	private String year;
	
	@Column(name="source")
	private String source;
	
	@Column(name="texture")
	private String texture;
	
	@Column(name="feature")
	private String feature;
	
	@Column(name="color")
	private String color;
	
	@Column(name="cr_weight")
	private Integer crWeight;
	
	@Column(name="integrity")
	private Integer integrity;
	
	@Column(name="grade")
	private Integer grade;
	
	@Column(name="desc")
	private String desc;
	
	@Column(name="in_date")
	@DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss") 
	private Date inDate;
	
	@Column(name="user_id")
	private Integer userId;
	
	@Column(name="logout_date")
	@DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
	private Date logoutDate;
	
	@Column(name="status")
	private Integer status;

	@Column(name="latitude")
	private Double latitude;//TODO 数据库中的varchar字段转换成Double类型
	
	@Column(name="longitude")
	private Double longitude; //TODO 同上
	
	
	@Column(name="created_at")
	@DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
	private Date createdAt;
	
	@Column(name="updated_at")
	@DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
	private Date updatedAt;

	@ManyToOne
	@JoinColumn(name="repo_area_id")
	private RepoArea repoArea;
}
