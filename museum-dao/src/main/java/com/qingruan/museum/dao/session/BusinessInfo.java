/**
 2015年2月10日
 14cells
 
 */
package com.qingruan.museum.dao.session;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import com.qingruan.museum.dao.entity.IdEntity;

/**
 * 业务运行实体，面向内存数据库Redis建模
 * 
 * @author tommy
 * 
 */
@Getter
@Setter
@Slf4j
public class BusinessInfo extends IdEntity<Long> implements Serializable {
	private static final long serialVersionUID = 3120775036160856108L;
	// 会话对应的设备ID
	private Long deviceId;
	// 会话ID格式{消息源头Host+时间戳+监测站ID+监测点ID}
	private String sessionId;
	// 消息上报时间
	private Date reportTime;
	// 消息下发时间
	private Date sendTime;

	public BusinessInfo() {
	}
}
