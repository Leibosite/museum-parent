package com.qingruan.museum.dao.repository.criteria;

import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public interface CustomSpecification<T> extends Specification<T> {
	
	CriteriaQuery<Tuple> toCriteriaQuery(Root<T> root, CriteriaBuilder cb);
	
}
