package com.qingruan.museum.agent.modified.event;

import java.lang.reflect.Method;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.event.spi.PreUpdateEvent;
import org.hibernate.event.spi.PreUpdateEventListener;

@Slf4j
public class PreUpdateEventCommonListener implements PreUpdateEventListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public boolean onPreUpdate(PreUpdateEvent event) {
		try {
			Object entity = event.getEntity();
			Method method = entity.getClass().getMethod("ResetUpdateStamp");
			if (method != null)
				method.invoke(entity);

			String[] propertyNames = event.getPersister().getPropertyNames();
			for (int i = 0; i < propertyNames.length; i++) {
				if ("updateStamp".equals(propertyNames[i])) {
					Date curTime = new Date();
					event.getState()[i] = (Long) (curTime.getTime());
					break;
				}
			}
		} catch (Exception e) {
			log.info("fail to reset updateStamp.");
		}
		
		return false;
	}

}
