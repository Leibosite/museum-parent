package com.qingruan.museum.agent.modified.event;

import org.hibernate.persister.entity.EntityPersister;
import com.qingruan.museum.agent.modified.DataModifiedDealer;
import com.qingruan.museum.agent.modified.DataModifiedDealerHolder;
import com.qingruan.museum.framework.spring.utils.SpringContextHolder;

/**
 * Hibernate EventListener基类. 提供了获取DataModifiedDealer的方法.
 * 
 * @author tommy
 */
public abstract class AbstractEventListener {
	private DataModifiedDealerHolder dealerHolder;
	
	/**
	 * 活动实体变动处理类.
	 * 
	 * @param clazz
	 * @return
	 */
	protected <T> DataModifiedDealer<T> getDealer(Class<T> clazz) {
		if (this.dealerHolder == null) {
			this.dealerHolder = SpringContextHolder.getBean(DataModifiedDealerHolder.class);
		}
		
		return this.dealerHolder.getDealer(clazz);
	}
	
	public boolean requiresPostCommitHanding(EntityPersister persister) {
		return true;
	}
}
