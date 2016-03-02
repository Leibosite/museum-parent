package com.qingruan.museum.dao.repository;


import javax.persistence.QueryHint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.QueryHints;

import com.qingruan.museum.dao.entity.AccessUser;
import com.qingruan.museum.framework.spring.data.jpa.util.HibernateJpaQueryHints;

public interface AccessUserDao extends
		JpaRepository<AccessUser, Long>,
		JpaSpecificationExecutor<AccessUser> {
	Page<AccessUser> findByName(String name, Pageable pageable);

	@QueryHints({
		@QueryHint(name = HibernateJpaQueryHints.HINT_CACHEABLE, value ="true")
	})
	AccessUser findByLoginName(String loginName);
}
