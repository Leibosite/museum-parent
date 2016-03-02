package com.qingruan.museum.dao.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Setter
@Getter
@Table(name = "access_group")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AccessGroup extends IdEntity {

	private static final long serialVersionUID = 1L;

	private String name;
	@ManyToMany(fetch = FetchType.LAZY, targetEntity = AccessPermission.class, cascade = {
			CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "access_group_per_mapping", joinColumns = { @JoinColumn(name = "access_group_id") }, inverseJoinColumns = { @JoinColumn(name = "access_permission_id") })
	// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<AccessPermission> accessPermissions = new HashSet<AccessPermission>();
}
