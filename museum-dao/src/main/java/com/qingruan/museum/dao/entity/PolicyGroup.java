package com.qingruan.museum.dao.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * PolicyGroup
 */

@Getter
@Setter
@Entity
@Table(name = "policy_group")
@ToString(includeFieldNames = true)
public class PolicyGroup extends IdEntityTimeStamp implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final String GROUP_NAME_MARK = "groupName";

	@Column(name = "group_name")
	private String groupName;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name="policy_group_id", insertable = false, updatable = false)
	@OrderBy("id DESC")
	private Set<PolicyWrapper> policies = new HashSet<PolicyWrapper>(0);
	
	private String desp;
}
