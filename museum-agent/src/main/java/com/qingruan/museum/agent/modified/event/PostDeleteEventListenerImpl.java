package com.qingruan.museum.agent.modified.event;

import java.io.Serializable;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostDeleteEventListener;

import com.qingruan.museum.agent.modified.DataModifiedDealer;

/**
 * 持久化对象被删除时触发的响应事件.
 * 
 * @author tommy
 */
@Slf4j
public class PostDeleteEventListenerImpl extends AbstractEventListener implements PostDeleteEventListener {
	private final static long serialVersionUID = 1L;
	private boolean isDebugEnabled = log.isDebugEnabled();
	
	@Override
	public void onPostDelete(PostDeleteEvent event) {
		Object entity = event.getEntity();
		Serializable id = event.getId();
		
		if (isDebugEnabled) {
			log.debug("Entity {} deleted:\n" +
					"                id : {}\n",
					entity.getClass().getName(), id);
		}
		
		DataModifiedDealer<?> modifiedDealer =  getDealer(entity.getClass());
		if (modifiedDealer != null) {
			modifiedDealer.onDelete(entity, id);
		}
	}

}
