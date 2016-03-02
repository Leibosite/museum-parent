package com.qingruan.museum.dao.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

/**
 * 定义基于uuid主键方式的entity基类.
 * 
 * 基类统一定义id的属性名称、数据类型、列名映射及生成策略.
 * Oracle需要每个Entity独立定义id的SEQUCENCE时，不继承于本类而改为实现一个Idable的接口。
 * 
 * @author john
 */

@Getter
@Setter
@MappedSuperclass
public abstract class UuidEntityTimeStamp implements Serializable {
	private static final long serialVersionUID = 1947961636324648604L;
	
	@GenericGenerator(name = "generator", strategy = "uuid")
	
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false, length=32)
	private String id;

	@Column(name = "update_stamp")
	private Long updateStamp;
	
	public UuidEntityTimeStamp() {
		ResetUpdateStamp();
	}
	
	public void ResetUpdateStamp() {
		Date date = new Date();
		
		updateStamp = date.getTime();
	}
	
	public Long getUpdateStamp() {
		if (updateStamp == null) {
			return 0L;
		}
		
		return updateStamp;
	}
}
