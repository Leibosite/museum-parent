/**
 2014年11月23日
 tommy
 
 */
package com.qingruan.museum.domain.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.qingruan.museum.domain.enums.ResultCode;

/**
 * 定义Domain处理结果{成功；失败；异常}
 * 
 * @author tommy
 * 
 */
@Setter
@Getter
@ToString
public class DomainHandleResult extends BaseDomain {
	private static final long serialVersionUID = -100006L;
	// 处理结果
	public ResultCode resultCode;
	// 排错描述信息
	public String troubleShootingMessage;
}
