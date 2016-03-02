package com.qingruan.museum.admin.shiro.spring.web;

import lombok.Getter;
import lombok.Setter;
/**
 * 
 * @author tommy
 *
 */
@Getter
@Setter
public class UrlPermission {
	private String path;
	private String filter;
	private String roles;
	private String permissions;
	
}
