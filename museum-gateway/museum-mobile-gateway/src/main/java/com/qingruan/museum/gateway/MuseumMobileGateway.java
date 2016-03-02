package com.qingruan.museum.gateway;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;



import com.qingruan.museum.gateway.common.exception.ExceptionLog;
import com.qingruan.museum.gateway.framework.ApplicationContextGuardian;

/**
 * 恒温恒湿机网关
 * 
 * @author jcy
 * 
 */
@Slf4j
public class MuseumMobileGateway {
	

	public static void main(String[] args) throws IOException,RuntimeException,NullPointerException{
		try {
			
			ApplicationContextGuardian.getInstance();

			if (MuseumMobileGatewayProperties.isEmpty()) {
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
