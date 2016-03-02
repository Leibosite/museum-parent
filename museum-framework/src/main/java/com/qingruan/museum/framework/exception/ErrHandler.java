package com.qingruan.museum.framework.exception;

import java.lang.Thread.UncaughtExceptionHandler;

public class ErrHandler implements UncaughtExceptionHandler {

	@Override
	public void uncaughtException(Thread a, Throwable e) {
		System.out.println("This is:" + a.getName() + ",Message:"
				+ e.getMessage());
		e.printStackTrace();

	}

}
