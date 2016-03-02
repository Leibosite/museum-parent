package com.qingruan.museum.dao.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@ToString
@Entity
@Table(name = "images")
public class Image extends IdEntity{
	
	//图片与文物是多对一
	@ManyToOne
	@LazyToOne(LazyToOneOption.FALSE)
	@JoinColumn(name = "cultural_relic_id")
	private CulturalRelic culturalRelicId;
	
	@Column(name="name")
	private String name;
	
	@Column(name="image_file_name")
	private String imageFileName;
	
	@Column(name="image_content_type")
	private String imageContentType;
	
	@Column(name="image_file_size")
	private Integer imageFileSize;
	
	@Column(name="image_updated_at")
	@DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
	private Date imageUpdatedAt;
	
	@Column(name="image_meta")
	private String imageMeta;
	
	@Column(name="created_at")
	@DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
	private Date createdAt;
	
	@Column(name="updated_at")
	@DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
	private Date updatedAt;
}
