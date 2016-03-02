package com.qingruan.museum.netty.model;

import lombok.Data;

/**
 * 
 * @author jcy
 * @description 针对新版本的通讯协议，把上传的数据解析成model,以便后续的解析
 */

@Data
public class UpDataPackageV2 {
	/*
	 * 命令码
	 */
	private String command;
	
	/*
	 * 设备唯一编号
	 */
	private String deviceID;
	
	/*
	 * 设备类型
	 */
	private String deviceType;
	
	/*
	 * 数据长度
	 */
	private Integer dataLength;
	
	/*
	 * 数据包
	 */
	private String dataBody;
	
}
