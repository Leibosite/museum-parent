/**
 *2014年11月23日
 *tommy
 
 */
package com.qingruan.museum.domain.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.qingruan.museum.domain.enums.MessageAction;
import com.qingruan.museum.domain.enums.MessageType;

/**
 * @author tommy
 * 
 */
@Setter
@Getter
@ToString
public class DomainHeader extends BaseDomain {

	private static final long serialVersionUID = 5740000684378576758L;
	// 一个会话的唯一标识
	public String sessionId;
	// 消息源编码
	public String orignalId;
	// 消息目的编码
	public String destinationId;

	// 时间戳
	public Long timeStamp;

	// 消息优先级
	public Integer priority;

	//  消息的类型：请求：回复？

	public MessageType messageType;

	// 消息的方向

	public MessageAction messageAction;

}
