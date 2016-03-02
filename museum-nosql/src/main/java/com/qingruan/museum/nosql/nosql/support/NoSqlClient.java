package com.qingruan.museum.nosql.nosql.support;

 
/**
 * 
 * @author tommy
 */
public interface NoSqlClient {
	void set(Object key, Object value);

	void set(Object key, int expiry, Object value);
	
	<T> boolean exist(Class<T> clazz, Object key);
	
	<T> T get(Class<T> clazz, Object key);
	
	<T> void delete(Class<T> clazz, Object key);
	
	void stop();
}
