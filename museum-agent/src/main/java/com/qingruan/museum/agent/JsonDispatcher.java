package com.qingruan.museum.agent;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.qingruan.museum.framework.jackson.JSONUtil;

/**
 * 
 * @author tommy
 */
@Slf4j
@Service
public class JsonDispatcher {
	private final static Map<String, Class<?>> nameClazzMap = new HashMap<String, Class<?>>();
	private final static Map<Class<?>, String> clazzNameMap = new HashMap<Class<?>, String>();
	
	static {
		Properties properties = new Properties();
		try {
			properties.load(JsonDispatcher.class.getResourceAsStream("/META-INF/json-dispatcher.properties"));
			
			for (Object key : properties.keySet()) {
				if (key == null) {
					continue;
				}
				
				String clazzName = properties.getProperty(key.toString());
				if(clazzName != null){
					clazzName = clazzName.trim();
				}
				try {
					Class<?> clazz = Class.forName(clazzName);
					nameClazzMap.put(key.toString(), clazz);
					clazzNameMap.put(clazz, key.toString());
				} catch (Exception e) {
//					log.error(ExceptionLog.getErrorStack(e));
				}
			}
		} catch (Exception e) {
//			log.error(ExceptionLog.getErrorStack(e));
		}
	}
	
	public Object jsonToDomain(String msg) {
		log.debug(msg);
		
		try {
			int firstColonIdx = msg.indexOf(':');
			
			if (firstColonIdx < 0) {
				return null;
			}
			
			String domainName = msg.substring(1, firstColonIdx - 1);
			String jsonString = msg.substring(firstColonIdx + 1);
			
			Class<?> clazz = nameClazzMap.get(domainName);
			if (clazz == null) {
				log.error("Not found class for name [{}]", domainName);
				
				return null;
			}
			
			return JSONUtil.deserialize(jsonString, clazz);
		} catch (Exception e) {
//			log.error(ExceptionLog.getErrorStack(e));
		}
		
		return null;
	}
	
	public String domainToJson(Object domain) {
		String json = JSONUtil.serialize(domain);
		
		String name = clazzNameMap.get(domain.getClass());
		if (name == null) {
			log.error("Not found name for class [{}]", domain.getClass().getName());
			
			return null;
		}
		
		String result = "\"" + name + "\":" + json;
		
		log.debug(result);
		
		return result;
	}
}