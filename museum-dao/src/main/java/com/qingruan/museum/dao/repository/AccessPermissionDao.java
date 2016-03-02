package com.qingruan.museum.dao.repository;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.QueryHints;

import com.qingruan.museum.dao.entity.AccessPermission;
import com.qingruan.museum.framework.spring.data.jpa.util.HibernateJpaQueryHints;

public interface AccessPermissionDao extends
		JpaRepository<AccessPermission, Long>,
		JpaSpecificationExecutor<AccessPermission> {
	Page<AccessPermission> findByDisplayname(String name, Pageable pageable);

	List<AccessPermission> findByDisplayname(String name);
	
	@QueryHints({
		@QueryHint(name = HibernateJpaQueryHints.HINT_CACHEABLE, value ="true")
	})
	List<AccessPermission> findByValueOrderByPriorityAsc(String value);

	List<AccessPermission> findByDisplaynameLike(String name);
}
