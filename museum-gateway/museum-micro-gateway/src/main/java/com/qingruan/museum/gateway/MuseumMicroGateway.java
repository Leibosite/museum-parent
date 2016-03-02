package com.qingruan.museum.gateway;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import com.qingruan.museum.gateway.common.exception.ExceptionLog;
import com.qingruan.museum.gateway.framework.ApplicationContextGuardian;

/**
 * 433模块，GPS，GPRS，WIFI
 * 
 * @author jcy
 * 
 */
@Slf4j
public class MuseumMicroGateway {
	

	public static void main(String[] args) throws IOException,RuntimeException,NullPointerException{
		try {
			
			ApplicationContextGuardian.getInstance();

			if (MuseumMicroGwProperties.isEmpty()) {
				log.error("miss museum-mobile-gateway.properties.");
				System.exit(0);
			}	
			log.info("Museum Mobile Gateway Success");
			
			
		} catch (Exception e) {
			log.error(ExceptionLog.getErrorStack(e));
		}
		finally {

		}

	}
}
