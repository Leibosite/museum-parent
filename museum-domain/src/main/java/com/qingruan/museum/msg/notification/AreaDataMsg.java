package com.qingruan.museum.msg.notification;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.qingruan.museum.msg.DataContent;

/**
 * 区域环境监测数据
 * 
 * @author tommy
 * 
 */
@Getter
@Setter
public class AreaDataMsg extends NotifMsg {

	private static final long serialVersionUID = -7222640145668802286L;
	// 区域Id
	private Long areaId;
	// 区域名臣
	private String name;

	private Integer score;

	private Long alarmNumber;

	private String desp;
	// 区域数据概览
	private List<DataContent> dataOverviews = new ArrayList<>();
	// 区域数据详情
	private List<DeviceDataMsg> dataDetails = new ArrayList<DeviceDataMsg>();
	
	private String status;

}
