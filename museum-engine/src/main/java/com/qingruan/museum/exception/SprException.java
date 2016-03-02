package com.qingruan.museum.exception;

import com.qingruan.museum.exception.enums.SprExceptionType;
/**
 * 定义数据库查询类异常
 * @author tommy
 *
 */
public class SprException extends Exception {
	private static final long serialVersionUID = -6519711570118135827L;
	private SprExceptionType exceptionType;

	public SprException(SprExceptionType sprExceptionType) {
		this.exceptionType = sprExceptionType;
	}

	public SprExceptionType getExceptionType() {
		return exceptionType;
	}

}
