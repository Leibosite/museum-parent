/**
 2014年11月23日
 14cells
 
 */
package com.qingruan.museum.domain.models;

import java.util.List;

/**
 * @author tommy
 * @param <T>
 * 
 */
public class DomainRoutingInfo<T> extends BaseDomain {
	private static final long serialVersionUID = -100004L;
	// 消息路由列表
	public List routingLists;
	// 下一跳
	public String nextHop;
	// 上一跳
	public String topHop;

}
