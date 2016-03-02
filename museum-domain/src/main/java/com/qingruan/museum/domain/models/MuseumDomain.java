package com.qingruan.museum.domain.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 定义业务模型
 * 
 * @author tommy
 * 
 */
@Getter
@Setter
@ToString
public class MuseumDomain extends BaseDomain {
	
	private static final long serialVersionUID = -1006656809517429333L;
	// 定义消息头
	public DomainHeader domainHeader;
	// 定义消息体
	public DomainBody domainBody;
	// 定义消息的路由列表
	public DomainRoutingInfo<?> domainRoutingInfo;
	// 定义消息处理结果：成功，未成功，未投递等
	public DomainHandleResult domainHandleResult;
    
}
