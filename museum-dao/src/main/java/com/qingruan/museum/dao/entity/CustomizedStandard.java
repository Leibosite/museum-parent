package com.qingruan.museum.dao.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="customized_standards")
public class CustomizedStandard extends IdEntity{
	
	@Column(name = "name")
	private String name;
	
	//区域和区域定制标准是一对一的关系
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE},fetch=FetchType.EAGER)
	@JoinColumn(name="area_id")
	private RepoArea area;
	
	//标准和定制标准是，一对多的关系
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "standard_id")
	private Standard standard;
	
	@Column(name = "value")
	private String value;
	
	@Column(name="time_stamp")
	private Date timeStamp;
	
	@Column(name="desp")
	private String desp;
	
}
