package com.qingruan.museum.dao.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Setter
@Getter
@Table(name = "access_permission")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AccessPermission extends IdEntity {
	private static final long serialVersionUID = 7745837584605751646L;
	private String value;
	private String displayname;
	private Integer level;
	private String desp;
	private String link;
	private Integer priority;

	@ManyToOne
	@JoinColumn(name = "super_permission")
	private AccessPermission superPermission;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "superPermission")
	@OrderBy("priority ASC")
	// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<AccessPermission> subPermission;

}
