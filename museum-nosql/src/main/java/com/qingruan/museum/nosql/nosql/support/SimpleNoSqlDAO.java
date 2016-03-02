package com.qingruan.museum.nosql.nosql.support;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qingruan.museum.nosql.IdEntity;
import com.qingruan.museum.nosql.nosql.support.util.GenericUtil;

/**
 * 使用样例如下：
 * 
 * <p>
 * 
 * @Component public class OnlineUserModelDao extends
 *            SimpleNoSqlDAO<OnlineUserModel> { public OnlineUserModelDao(){
 *            super(OnlineUserModel.class); }
 * 
 *            public OnlineUserModel findById(Long id){ return this.findOne(id);
 *            } }
 *            </p>
 * @author tommy
 * 
 * @param <T>
 */
@Component
public class SimpleNoSqlDAO<T extends IdEntity<?>> {
	protected final Class<T> entityClass;

	@Autowired
	protected NoSqlClient noSqlClient;

	@SuppressWarnings("unchecked")
	public SimpleNoSqlDAO() {
		this.entityClass = (Class<T>) GenericUtil.getTypeArguments(
				SimpleNoSqlDAO.class, this.getClass()).get(0);
	}

	public SimpleNoSqlDAO(final Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	// @Override
	public <S extends T> S save(S entity) {
		noSqlClient.set(entity.getId(), 0, entity);
		return entity;
	}

	public <S extends T> Collection<S> save(Collection<S> entities) {
		for (S entity : entities) {
			save(entity);
		}

		return entities;
	}

	public T findOne(Object key) {
		return noSqlClient.get(entityClass, key);
	}

	public boolean exists(Object id) {
		return noSqlClient.exist(entityClass, id);
	}

	public void delete(Object key) {
		noSqlClient.delete(entityClass, key);
	}

	public void delete(T entity) {
		if (entity == null) {
			return;
		}

		Object id = entity.getId();
		if (id == null) {
			return;
		}

		this.delete(id);
	}

	public void delete(Collection<? extends T> entities) {
		if (entities == null || entities.size() == 0) {
			return;
		}

		for (T entity : entities) {
			this.delete(entity);
		}
	}

}
