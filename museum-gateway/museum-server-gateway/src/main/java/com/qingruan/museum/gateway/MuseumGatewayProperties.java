package com.qingruan.museum.gateway;

import java.io.IOException;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.ClassPathResource;

import com.qingruan.museum.gateway.common.exception.ExceptionLog;


/**
 * 
 * @author tommy
 */
@Slf4j
public final class MuseumGatewayProperties {
	public static final String SPRING_CONTEXT_CONFIG_LOCATION = "spring.contextConfigLocation";
	public static final String ZEROMQ_CONTEXT_NUMBER = "zeromq.context.number";
	
	final static Properties properties;
	
	static {
		properties = new Properties();
		try {
			properties.load(new ClassPathResource("application.properties").getInputStream());
		} catch (IOException e) {
			log.error(ExceptionLog.getErrorStack(e));
//			ExceptionLog.logException(e, log);
		}
	}
	
	/**
	 * 获取配置属性.
	 * 
	 * @param key
	 * @return
	 */
	public static String getProperty(String key) {
		String property = properties.getProperty(key);
		
		if(property != null){
			property = property.trim();
		}
		
		return property;
	}
	
	/**
	 * 
	 * @return
	 */
	public static Properties getProperties() {
		return MuseumGatewayProperties.properties;
	}
	
	/**
	 * 配置文件是否为空.
	 * 
	 * @return
	 */
	public static boolean isEmpty() {
		return properties == null || properties.isEmpty();
	}
}
