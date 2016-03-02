package com.qingruan.museum.msg;

import lombok.Setter;

/**
 * 消息结构：消息头+消息属性+消息内容
 * 
 * @author tommy
 * 
 */
@Setter
public class MuseumMsg {
	// 消息头
	private MsgHeader msgHeader;
	// 消息属性
	private MsgProperty msgProperty;
	// 消息内容接口
	private MsgBody msgBody;
	// 接口类型标志

	private String klassName;

	public MsgHeader getMsgHeader() {
		return msgHeader;
	}

	public void setMsgHeader(MsgHeader msgHeader) {
		this.msgHeader = msgHeader;
	}

	public MsgProperty getMsgProperty() {
		return msgProperty;
	}

	public void setMsgProperty(MsgProperty msgProperty) {
		this.msgProperty = msgProperty;
	}

	public MsgBody getMsgBody() {
		return msgBody;
	}

	public void setMsgBody(MsgBody msgBody) {
		this.setKlassName(msgBody.getClass().getName());
		this.msgBody = msgBody;
	}

	public String getKlassName() {
		return klassName;
	}

	public void setKlassName(String klassName) {
		this.klassName = klassName;
	}

}
