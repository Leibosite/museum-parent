package com.qingruan.museum.agent.modified.event;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;

import com.qingruan.museum.agent.modified.DataModifiedDealer;

/**
 * 持久化对象被更新时触发的响应事件.
 * 
 * @author tommy
 */
@Slf4j
public class PostUpdateEventListenerImpl extends AbstractEventListener implements PostUpdateEventListener {
	private final static long serialVersionUID = 1L;
	private boolean isDebugEnabled = log.isDebugEnabled();
	
	@Override
	public void onPostUpdate(PostUpdateEvent event) {
		int[] dirtyProperties = event.getDirtyProperties();
		
		if (dirtyProperties == null || dirtyProperties.length == 0) {
			return;
		}

		Object entity = event.getEntity();
		Object[] newStates = event.getState().clone();
		Object[] oldStates = event.getOldState().clone();
		String[] propertyNames = event.getPersister().getPropertyNames();
		
		if (isDebugEnabled) {
			for (int index : dirtyProperties) {
				log.debug("Entity {} property changed:\n" +
						"     property : {}\n" +
						"          old : {}\n" +
						"          new : {}",
						entity.getClass().getName(), propertyNames[index], oldStates[index], newStates[index]);
			}
		}
		
//		for (int i = 0; i < propertyNames.length; ++i ) {
//			if (!entityPersister.getPropertyUpdateability()[i] ) {
//				newStates[i] = event.getOldState()[i];
//			}
//		}
		
		DataModifiedDealer<?> modifiedDealer =  getDealer(entity.getClass());
		if (modifiedDealer != null) {
			modifiedDealer.onUpdate(entity,
					oldStates, newStates, propertyNames, dirtyProperties);
		}
	}
}
