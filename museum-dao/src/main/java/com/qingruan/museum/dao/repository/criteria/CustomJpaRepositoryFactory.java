package com.qingruan.museum.dao.repository.criteria;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.RepositoryMetadata;

public class CustomJpaRepositoryFactory extends JpaRepositoryFactory {
	
	public CustomJpaRepositoryFactory(EntityManager entityManager) {
		super(entityManager);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected <T, ID extends Serializable> JpaRepository<?, ?> getTargetRepository(
			RepositoryMetadata metadata, EntityManager entityManager) {
		JpaEntityInformation<?, Serializable> entityInformation = getEntityInformation(metadata.getDomainType());
		return new CustomJpaRepositoryImpl(entityInformation, entityManager);
	}
	
	@Override
	protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
		return CustomJpaRepositoryImpl.class;
	}
	
}
