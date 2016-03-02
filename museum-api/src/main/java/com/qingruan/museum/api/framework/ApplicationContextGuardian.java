package com.qingruan.museum.api.framework;

import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.qingruan.museum.api.exception.ExceptionLog;

/**
 * 
 * @author tommy
 * 
 */
@Slf4j
public class ApplicationContextGuardian {

	public static final String APPLICATION_CONTEXT_FILE_PATH = "applicationContext.xml";
	public static final String CONTEXT_FILE_PATH = "classpath*:/META-INF/spring/applicationContext-museum-*.xml";

	private ConfigurableApplicationContext appContext = null;

	private static ApplicationContextGuardian instance = null;


	private ApplicationContextGuardian() {
		try {
			if (appContext == null) {
				appContext = new ClassPathXmlApplicationContext(new String[] {
						APPLICATION_CONTEXT_FILE_PATH, CONTEXT_FILE_PATH });
			}
		} catch (Exception e) {
//			ExceptionLog.logException(e, log);
			log.error(ExceptionLog.getErrorStack(e));
		}
	}

	public static ApplicationContextGuardian getInstance() {
		if (instance == null)
			instance = new ApplicationContextGuardian();

		return instance;
	}

	public ApplicationContext GetAppContext() {
		return appContext;
	}

	public Properties getProperties() {
		return (Properties) ApplicationContextGuardian.getInstance()
				.GetAppContext().getBean("properties");
	}

}