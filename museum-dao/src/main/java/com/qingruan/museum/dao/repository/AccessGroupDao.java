package com.qingruan.museum.dao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qingruan.museum.dao.entity.AccessGroup;

public interface AccessGroupDao extends
		JpaRepository<AccessGroup, Long>,
		JpaSpecificationExecutor<AccessGroup> {
	Page<AccessGroup> findByName(String name, Pageable pageable);
}
