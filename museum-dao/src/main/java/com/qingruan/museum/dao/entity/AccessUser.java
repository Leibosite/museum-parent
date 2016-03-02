package com.qingruan.museum.dao.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;

@Setter
@Getter
@Entity
@Table(name = "access_user")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AccessUser extends IdEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "login_name")
	private String loginName;
	private String password;
	// JSR303 BeanValidator的校验规则
	@NotBlank
	private String name;
	private String email;
	private String state;

	// 设定JSON序列化时的日期格式，用来记录密码修改时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date passwordModifyStamp;
	// access_user:access_group
	@ManyToMany(fetch = FetchType.LAZY, targetEntity = AccessGroup.class, cascade = {
			CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "access_group_user_mapping", joinColumns = { @JoinColumn(name = "access_user_id") }, inverseJoinColumns = { @JoinColumn(name = "access_group_id") })
	// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<AccessGroup> accessGroups = new HashSet<AccessGroup>();

}
