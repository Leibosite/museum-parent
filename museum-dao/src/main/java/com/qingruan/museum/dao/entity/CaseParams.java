package com.qingruan.museum.dao.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import com.qingruan.museum.domain.models.enums.MonitorDataType;

@Getter
@Setter
@Entity
@Table(name = "case_params")
public class CaseParams extends IdEntity<CaseParams> {

	private static final long serialVersionUID = 5933732362324014307L;
	@ManyToOne
	@LazyToOne(LazyToOneOption.FALSE)
	@JoinColumn(name = "repo_area_id")
	private RepoArea repoArea;
	@Enumerated(EnumType.STRING)
	@Column(name = "object_type", length = 255, nullable = false)
	private MonitorDataType objectType;
	@JoinColumn(name = "value")
	private Double value;
	@JoinColumn(name = "created_at")
	private Timestamp createdAt;
	@JoinColumn(name = "updated_at")
	private Timestamp updatedAt;
}
