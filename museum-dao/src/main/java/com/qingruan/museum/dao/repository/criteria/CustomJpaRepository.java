package com.qingruan.museum.dao.repository.criteria;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CustomJpaRepository<T, ID extends Serializable> extends
		JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
	
	List<Tuple> findAll(CustomSpecification<T> spec);
	
	Tuple findOne(CustomSpecification<T> spec);
}
