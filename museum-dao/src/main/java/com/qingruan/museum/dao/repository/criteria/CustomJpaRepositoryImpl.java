package com.qingruan.museum.dao.repository.criteria;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

public class CustomJpaRepositoryImpl<T, ID extends Serializable> extends
		SimpleJpaRepository<T, ID> implements CustomJpaRepository<T, ID> {

	private final JpaEntityInformation<T, ?> entityInformation;
	private final EntityManager em;
	
	public CustomJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.entityInformation = entityInformation;
		this.em = entityManager;
		
	}
	
	public CustomJpaRepositoryImpl(Class<T> domainClass, EntityManager em) {
		this(JpaEntityInformationSupport.getMetadata(domainClass, em), em);
	}

	@Override
	public List<Tuple> findAll(CustomSpecification<T> spec) {
		return getQuery(spec).getResultList();
	}
	
//	@QueryHints({
//		@QueryHint(name = HibernateJpaQueryHints.HINT_CACHEABLE, value ="true")
//	})
//	@Override
//	public T findOne(ID id) {
//		return super.findOne(id);
//	}
	
	public Tuple findOne(CustomSpecification<T> spec) {
		try{
			return getQuery(spec).getSingleResult();
		}catch (NoResultException e) {
			return null;
		}
		
	}
	
	private TypedQuery<Tuple> getQuery(CustomSpecification<T> spec){
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> query = builder.createTupleQuery();
		
		Root<T> root = query.from(getDomainClass());
		if(spec != null){
			query = spec.toCriteriaQuery(root, builder);
		}
		
		return em.createQuery(query);
	}
	
	public Class<T> getDomainClass() {
		return entityInformation.getJavaType();
	}
	
}
