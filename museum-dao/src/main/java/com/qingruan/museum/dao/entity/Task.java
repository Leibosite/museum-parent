package com.qingruan.museum.dao.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Task
 */
@ToString(includeFieldNames = true)
@Getter
@Setter
@Entity
@Table(name = "task")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Task extends IdEntity<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String DISABLED_STATUS = "0";
	public static final String ENABLE_STATUS = "1";

	@Column(name = "name", length = 255)
	private String name;

	@Column(name = "timmer", length = 45)
	private String timmer;

	@Column(name = "status", length = 45)
	private String status;
	private String title;
	private String description;
	private User user;
	private String url;
	private String data;
	private String desp;

}
