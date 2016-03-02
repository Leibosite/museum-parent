package com.qingruan.museum.netty.model;

import lombok.Data;

@Data
public class UpDataPackageV3 {

	/*
	 * 命令码
	 */
	private String command;
	
	/*
	 * 主网关的ID
	 */
	private String masterGatewayID;

	/*
	 * 设备路径
	 */

	private String path;
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
