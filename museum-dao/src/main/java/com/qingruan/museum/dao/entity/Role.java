package com.qingruan.museum.dao.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "roles")
public class Role extends IdEntity{
	
	@Column(name = "name")
	private String name;

	@Column(name = "resource_id")
	private Integer resourceId;

	@Column(name = "resource_type")
	private String resourceType;

	@Column(name = "created_at")
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date createdAt;

	@Column(name = "updated_at")
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date updatedAt;
}
