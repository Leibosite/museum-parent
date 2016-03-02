package com.qingruan.museum.agent.modified;

import java.io.Serializable;
import java.util.Collection;

/**
 * 实体改动处理.
 * 
 * @author tommy
 * 
 * @param <T>
 */
public interface DataModifiedDealer<T> {

	/**
	 * 要处理的实体类型.
	 * 
	 * @return
	 */
	Class<T> getObjectType();
	
	/**
	 * 增加时触发的回调函数.
	 * 
	 * @param entity 增加的实体
	 * @param id 增加的实体的标识
	 */
	void onInsert(Object entity, Serializable id);

	/**
	 * 删除时触发的回调函数.
	 * 
	 * @param entity 增加的实体
	 * @param id 增加的实体的标识
	 */
	void onDelete(Object entity, Serializable id);
	
	
	/**
	 * 更新时触发的回调函数.
	 * 
	 * @param entity
	 * @param oldStates
	 * @param newStates
	 * @param propertyNames
	 * @param dirtyProperties
	 */
	void onUpdate(Object entity, Object[] oldStates, Object[] newStates,
			String[] propertyNames, int[] dirtyProperties);
	
	/**
	 * 
	 * @param ownerEntity
	 * @param ownerEntityName
	 * @param referencingPropertyName
	 * @param newCollection
	 * @param oldCollection
	 */
	@SuppressWarnings("rawtypes")
	void onUpdateCollection(Object ownerEntity, String ownerEntityName, String referencingPropertyName,
			Collection newCollection, Collection oldCollection);
	
}
