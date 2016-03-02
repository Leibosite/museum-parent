package com.qingruan.museum.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import com.qingruan.museum.domain.enums.WarnCatego;
import com.qingruan.museum.domain.enums.WarnType;

@Entity
@Setter
@Getter
@Table(name = "warncategory")
public class WarnCategory extends IdEntity {
	// 告警类别{SO2,CO2等}

	@Enumerated(EnumType.STRING)
	@Column(name = "category", length = 50, nullable = false)
	private WarnCatego category;
	// 报警类型{设备告警、监测环境告警等}
	@Enumerated(EnumType.STRING)
	@Column(name = "type", length = 50, nullable = false)
	private WarnType type;
}
