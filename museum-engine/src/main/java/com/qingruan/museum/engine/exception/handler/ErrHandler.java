package com.qingruan.museum.engine.exception.handler;

import java.lang.Thread.UncaughtExceptionHandler;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class ErrHandler implements UncaughtExceptionHandler {

	@Override
	public void uncaughtException(Thread a, Throwable e) {
		log.info("This is:" + a.getName() + ",Message:"
				+ e.getMessage());
		e.printStackTrace();

	}

}
