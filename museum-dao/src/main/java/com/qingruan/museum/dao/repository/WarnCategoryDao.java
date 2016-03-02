package com.qingruan.museum.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qingruan.museum.dao.entity.WarnCategory;
import com.qingruan.museum.domain.enums.WarnCatego;
import com.qingruan.museum.domain.enums.WarnType;

public interface WarnCategoryDao extends JpaRepository<WarnCategory, Long>,
		JpaSpecificationExecutor<WarnCategory> {
	 WarnCategory findByType(WarnType type);
	
	 WarnCategory findByCategory(WarnCatego category);
}
