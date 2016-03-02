package com.qingruan.museum.exception;

import com.qingruan.museum.exception.enums.WebApiExceptionType;

public class WebApiException extends Exception{
	private static final long serialVersionUID = -6519711570118135827L;
	private WebApiExceptionType exceptionType;

	public WebApiException(WebApiExceptionType webApiExceptionType) {
		this.exceptionType = webApiExceptionType;
	}

	public WebApiExceptionType getExceptionType() {
		return exceptionType;
	}
}
