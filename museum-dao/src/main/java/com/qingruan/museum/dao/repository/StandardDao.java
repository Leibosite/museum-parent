package com.qingruan.museum.dao.repository;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import com.qingruan.museum.dao.entity.RepoArea;
import com.qingruan.museum.dao.entity.Standard;
import com.qingruan.museum.framework.spring.data.jpa.util.HibernateJpaQueryHints;

public interface StandardDao extends JpaRepository<Standard, Long>,
		JpaSpecificationExecutor<Standard> {
	
	Page<Standard> findByName(String name, Pageable pageable);
	List<Standard> findByNameLike(String name);
//	@QueryHints({ @QueryHint(name = HibernateJpaQueryHints.HINT_CACHEABLE, value = "true") })
//	Standard findByName(String name);

	@Modifying
	@Query("update Standard p set p.value =:value where p.id=:id")
	void updateValueStandardById(@Param("value") String value,
			@Param("id") Long id);

	// Page<Standard> findAll(Specifications<Standard> where, Pageable
	// pageable);
	/*
	 * @Query("select s from standards s where s.name = ?1") Standard
	 * findStandardsByName(String name);
	 */
}
