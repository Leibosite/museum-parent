package com.qingruan.museum.msg.notification;

import java.io.Serializable;

import lombok.Data;

/**
 * 通知类Base消息
 * 
 * @author tommy
 * 
 */
@Data
public class NotifMsg  implements Serializable {

	private static final long serialVersionUID = -8838165605239872778L;
	// 消息ID
	private String msgId;
	// 时间戳
	private Long timeStamp;

}
