package com.qingruan.museum.dao.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

/**
 * 统一定义id的entity基类.
 * 
 * 基类统一定义id的属性名称、数据类型、列名映射及生成策略.
 * Oracle需要每个Entity独立定义id的SEQUCENCE时，不继承于本类而改为实现一个Idable的接口。
 * 
 * @author tommy
 */

@Getter
@Setter
@MappedSuperclass
public abstract class IdEntityTimeStamp implements Serializable {
	private static final long serialVersionUID = 1947961636324648604L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "update_stamp")
	private Long updateStamp;
	
	public IdEntityTimeStamp() {
		ResetUpdateStamp();
	}
	
	public void ResetUpdateStamp() {
		
		if(this.updateStamp==null){
			Date date = new Date();
			
			updateStamp = date.getTime();
		}
	
	}
	
	public Long getUpdateStamp() {
		if (updateStamp == null) {
			return 0L;
		}
		
		return updateStamp;
	}
}
