package com.qingruan.museum.engine.exception;

import com.qingruan.museum.engine.exception.enums.DaoExceptionType;


public class DaoException extends Exception {
	private static final long serialVersionUID = -6519711570118135827L;
	private DaoExceptionType exceptionType;

	public DaoException(DaoExceptionType daoExceptionType) {
		this.exceptionType = daoExceptionType;
	}

	public DaoExceptionType getExceptionType() {
		return exceptionType;
	}

}
