package com.qingruan.museum.gateway.common.exception.handler;

import java.lang.Thread.UncaughtExceptionHandler;

public class ErrHandler implements UncaughtExceptionHandler {

	@Override
	public void uncaughtException(Thread a, Throwable e) {
		System.out.println("This is:" + a.getName() + ",Message:"
				+ e.getMessage());
		e.printStackTrace();

	}

}
