package com.qingruan.museum.msg.notification;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.qingruan.museum.msg.DataContent;

/**
 * 传感器数据详情
 * 
 * @author tommy
 * 
 */
@Getter
@Setter
public class DeviceDataMsg extends NotifMsg {

	private static final long serialVersionUID = 6554950373179401529L;
	private Long deviceId;
	private String deviceName;
	private String deviceDesp;
	// 传感数据
	private List<DataContent> datas = new ArrayList<>();
}
