package com.qingruan.museum.dao.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "standards")
public class Standard extends IdEntity{
	
	@Column(name="type_name")
	private String typeName;
	
	@Column(name="name")
	private String name;
	
	@Column(name="time_stamp")
	private Date timeStamp;
	
	@Column(name="value")
	private String value;
	
	@Column(name="desp")
	private String desp;
	
	@Column(name="explain_desp")
	private String explain;
	
}
