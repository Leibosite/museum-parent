package com.qingruan.museum.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 系统配置入库
 * 
 * @author tommy
 * 
 */
@Setter
@Getter
@Entity
@Table(name = "sys_config")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SysConfig extends IdEntity {
	@Column(name = "cfg_name")
	private String cfgName;
	@Column(name = "cfg_value")
	private String cfgValue;
	@Column(name = "cfg_model")
	private String cfgModel;
	@Column(name = "cfg_desc")
	private String cfgDesc;
}
