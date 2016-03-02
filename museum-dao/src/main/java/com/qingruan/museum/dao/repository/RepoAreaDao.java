package com.qingruan.museum.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qingruan.museum.dao.entity.Device;
import com.qingruan.museum.dao.entity.RepoArea;

public interface RepoAreaDao extends JpaRepository<RepoArea, Long>,JpaSpecificationExecutor<RepoArea>{
	List<RepoArea> findByNameLike(String name);
	
	@Query("select p from RepoArea p where p.areaType in('COMMON_DISPLAY', 'CONSTANT_TH_DISPLAY') and p.name like:name")
	public List<RepoArea> findShowCase(@Param("name") String name);
}
