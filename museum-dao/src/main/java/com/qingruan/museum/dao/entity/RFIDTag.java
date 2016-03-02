package com.qingruan.museum.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author jcy
 * RFID 电子标签 和文物一一对应
 */

@Getter
@Setter
@ToString
@Entity
@Table(name = "tags")
public class RFIDTag extends IdEntity {
	
	@Column(name="rfid")
	private String rfid; //电子标签的唯一标识，序列号
	
	@Column(name ="encode")
	private String encode;
	
	@Column(name="cultural_relic_id")
	private Integer culturalRelicId;
}
