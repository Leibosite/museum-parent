package com.qingruan.museum.dao.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;


/**
 * Policy
 */
@Getter
@Setter
@Entity
@Table(name = "policy")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PolicyWrapper extends IdEntityTimeStamp implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final String POLICY_CONTENT_MARK = "policyContent";

	@Column(name = "policy_content", length = 2550)
	private String policyContent;

	@Column(name = "policy_group_id")
	private Long policyGroupId;

	@Column(name = "status")
	private Integer status;
	
	private String desp;

}
