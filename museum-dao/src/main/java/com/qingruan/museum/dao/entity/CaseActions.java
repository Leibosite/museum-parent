package com.qingruan.museum.dao.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "case_actions")
public class CaseActions implements Serializable {

	private static final long serialVersionUID = 4826904351440533067L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	// repo_area_id
	@Column(name = "repo_area_id")
	private Long repoAreaId;

	// actionType
	@Column(name = "action_type")
	private String actionType;
	// value
	@Column(name = "value")
	private Long value;
	// createdAt
	@Column(name = "created_at")
	private Timestamp createdAt;
	// updatedAt
	@Column(name = "updated_at")
	private Timestamp updatedAt;
}
