package com.qingruan.museum.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qingruan.museum.dao.entity.CaseParams;
import com.qingruan.museum.dao.entity.RepoArea;
import com.qingruan.museum.domain.models.enums.MonitorDataType;

public interface CaseParamsDao extends JpaRepository<CaseParams, Long>,
		JpaSpecificationExecutor<CaseParams> {
	@Query("select p from CaseParams p where p.repoArea =:repoArea")
	List<CaseParams> findByRepoArea(@Param("repoArea") RepoArea repoArea);

	@Query("select p from CaseParams p where p.repoArea =:repoArea and p.objectType=:type")
	List<CaseParams> findByRepoAreaAndType(
			@Param("repoArea") RepoArea repoArea,
			@Param("type") MonitorDataType type);
}
