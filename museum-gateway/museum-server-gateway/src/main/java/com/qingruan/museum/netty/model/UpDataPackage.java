package com.qingruan.museum.netty.model;

import lombok.Data;
/**
 * 
 * @author jcy
 * @description 把上传的数据解析成model,以便后续的解析
 *
 */

@Data
public class UpDataPackage {
	
	/**
	 * 监测站ID
	 */
	private String monitorMacAddress;
	
	/**
	 * 将要被解析的数据
	 */
	private String msgBody;
	
	/*
	 * 将要解析数据的指令码
	 */
	private String command;
	
	/*
	 * 状态码
	 */
	private String statusCode;
	
	/*
	 * 报文序列号
	 */
	private String sequenceNumber;
	
}
