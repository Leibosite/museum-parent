package com.qingruan.museum.dao.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 短信模板
 * @author tommy
 *
 */
@ToString(includeFieldNames = true)
@Getter
@Setter
@Entity
@Table(name = "sms_template")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SmsTemplate extends IdEntity<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "template_content", length = 600)
	private String templateContent;

	/**
	 * 模板名称
	 */
	private String name;
}
