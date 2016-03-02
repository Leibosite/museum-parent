package com.qingruan.museum.msg;

import java.io.Serializable;

import lombok.Data;

/**
 * 消息头
 * 
 * @author tommy
 * 
 */
@Data
public class MsgHeader implements Serializable {
	private static final long serialVersionUID = 5721561131851342008L;
	private String messageID;
	private String sendFrom;
	private String sendTo;
	private String replyTo;
	private Long timeStamp;
	private int priority;
	private DeliveryMode deliveryMode;
	private int qos;

	public MsgHeader(String messageID, String sendFrom, String sendTo,
			String replyTo, Long timeStamp, int priority,
			DeliveryMode deliveryMode, int qos) {
		this.messageID = messageID;
		this.sendFrom = sendFrom;
		this.sendTo = sendTo;
		this.replyTo = replyTo;
		this.timeStamp = timeStamp;
		this.priority = priority;
		this.deliveryMode = deliveryMode;
		this.qos = qos;

	}

	public MsgHeader() {

	}

	public static enum DeliveryMode implements Serializable {
		PTP, PUB_SUB
	}
}
