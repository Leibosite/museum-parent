package com.qingruan.museum.dao.repository.criteria;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

public class CustomJpaRepositoryFactoryBean<T extends JpaRepository<S, ID>, S, ID extends Serializable>
		extends JpaRepositoryFactoryBean<T, S, ID> {

	protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
		return new CustomJpaRepositoryFactory(entityManager);
	}
}
