package com.qingruan.museum.dao.repository;

import javax.persistence.QueryHint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import com.qingruan.museum.dao.entity.CustomizedStandard;
import com.qingruan.museum.dao.entity.RepoArea;
import com.qingruan.museum.dao.entity.Standard;
import com.qingruan.museum.framework.spring.data.jpa.util.HibernateJpaQueryHints;

public interface CustomizedStandardDao extends
		JpaRepository<CustomizedStandard, Long>,
		JpaSpecificationExecutor<CustomizedStandard> {
	
	Page<CustomizedStandard> findByName(String name, Pageable pageable);

	@QueryHints({ @QueryHint(name = HibernateJpaQueryHints.HINT_CACHEABLE, value = "true") })
	Standard findByName(String name);

	@Query("select c from CustomizedStandard c where c.area = ?1")
	CustomizedStandard findOneByArea(RepoArea area);

	@Modifying
	@Query("update CustomizedStandard p set p.value =:value where p.id=:id")
	void updateValueCustomizedStandardById(@Param("value") String value,
			@Param("id") Long id);
}
