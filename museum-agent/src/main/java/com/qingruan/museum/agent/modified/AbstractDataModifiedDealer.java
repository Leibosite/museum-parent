package com.qingruan.museum.agent.modified;

import java.io.Serializable;
import java.util.Collection;


/**
 * 抽象的实体修改处理类.
 * 
 * @author tommy
 * 
 * @param <T>
 */
public abstract class AbstractDataModifiedDealer<T> implements
		DataModifiedDealer<T> {

	@Override
	public void onInsert(Object entity, Serializable id) {

	}

	@Override
	public void onDelete(Object entity, Serializable id) {

	}

	@Override
	public void onUpdate(Object entity, Object[] oldStates, Object[] newStates,
			String[] propertyNames, int[] dirtyProperties) {

	}

	@Override
	@SuppressWarnings("rawtypes")
	public void onUpdateCollection(Object ownerEntity, String ownerEntityName,
			String referencingPropertyName, Collection newCollection,
			Collection oldCollection) {

	}

	public static Integer checkSpecifiedPropertyChange(String[] propertyNames,
			int[] dirtyProperties, String specifiedPropertyMark) {
		if (dirtyProperties == null) {
			return null;
		}

		for (int dirtyProperty : dirtyProperties) {
			if (propertyNames[dirtyProperty].equals(specifiedPropertyMark)) {
				return dirtyProperty;
			}
		}
		
		return null;
	}
	//TODO:
	public void flushPcc(Long id){
		
	}

}
