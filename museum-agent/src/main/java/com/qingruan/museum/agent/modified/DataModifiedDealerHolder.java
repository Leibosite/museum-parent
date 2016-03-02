package com.qingruan.museum.agent.modified;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class DataModifiedDealerHolder implements InitializingBean {
	@SuppressWarnings("rawtypes")
	private Map<String, DataModifiedDealer> entityModifiedDealers = new HashMap<String, DataModifiedDealer>();
	
	@Autowired
	private ApplicationContext applicationContext;

	@Override
	@SuppressWarnings("rawtypes")
	public void afterPropertiesSet() throws Exception {
		Map<String, DataModifiedDealer> beans = applicationContext.getBeansOfType(DataModifiedDealer.class);
		
		if (beans == null || beans.size() == 0) {
			return;
		}
		
		for (Entry<String, DataModifiedDealer> entry : beans.entrySet()) {
			DataModifiedDealer dealer = entry.getValue();
			if (dealer == null) {
				continue;
			}
			
			entityModifiedDealers.put(dealer.getObjectType().getName(), dealer);
		}
	}
	
	/**
	 * 
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> DataModifiedDealer<T> getDealer(Class<T> clazz) {
		if (clazz == null) {
			return null;
		}
		
		return this.entityModifiedDealers.get(clazz.getName());
	}
	
	/**
	 * 
	 * @param clazzName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> DataModifiedDealer<T> getDealer(String clazzName) {
		if (StringUtils.isBlank(clazzName)) {
			return null;
		}
		
		return (DataModifiedDealer<T>) this.entityModifiedDealers.get(clazzName);
	}
}
