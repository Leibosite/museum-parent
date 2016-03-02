package com.qingruan.museum.agent.modified.event;

import java.io.Serializable;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;

import com.qingruan.museum.agent.modified.DataModifiedDealer;

/**
 * 持久化对象被添加时触发的响应事件.
 * 
 * @author tommy
 */
@Slf4j
public class PostInsertEventListenerImpl extends AbstractEventListener implements PostInsertEventListener {
	private final static long serialVersionUID = 1L;
	private boolean isDebugEnabled = log.isDebugEnabled();
	
	@Override
	public void onPostInsert(PostInsertEvent event) {
		Object entity = event.getEntity();
		Serializable id = event.getId();
		
		if (isDebugEnabled) {
			log.debug("Entity {} added:\n" +
					"                id : {}\n",
					entity.getClass().getName(), id);
		}
		
		DataModifiedDealer<?> modifiedDealer =  getDealer(entity.getClass());
		if (modifiedDealer != null) {
			modifiedDealer.onInsert(entity, id);
		}
	}

}
