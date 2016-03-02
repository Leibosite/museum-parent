/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.qingruan.museum.dao.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Setter
@Getter
@ToString
@Table(name = "users")
public class User extends IdEntity {
	
	@Column(name="name")
	private String name;
	
	@Column(name="mobile")
	private String mobile;//移动电话
	
	@Column(name="phone")
	private String phone;//座机电话
	
	@Column(name="status")
	private Integer status;
	
	@Column(name="email")
	private String email;
	
	@Column(name="encrypted_password")
	private String encryptedPassword;
	
	@Column(name="reset_password_token")
	private String resetPasswordToken;
	
	@Column(name="reset_password_sent_at")
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date resetPasswordSentAt;
	
	@Column(name="remember_created_at")
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date rememberCreatedAt;
	
	@Column(name="sign_in_count")
	private Integer signInCount;
	
	@Column(name="current_sign_in_at")
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date currentSignInAt;
	
	@Column(name="last_sign_in_at")
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date lastSignInAt;
	
	@Column(name="current_sign_in_ip")
	private String currentSignInIp;
	
	@Column(name="last_sign_in_ip")
	private String lastSignInIp;
	
	@Column(name="confirmation_token")
	private String confirmationToken;
	
	@Column(name="confirmed_at")
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date confirmedAt;
	
	@Column(name="confirmation_sent_at")
	private Date confirmationSentAt;
	
	@Column(name = "created_at")
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date createdAt;

	@Column(name = "updated_at")
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date updatedAt;
}