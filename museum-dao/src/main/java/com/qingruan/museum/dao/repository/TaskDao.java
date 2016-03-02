package com.qingruan.museum.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qingruan.museum.dao.entity.Task;

public interface TaskDao extends JpaRepository<Task, Long>,
JpaSpecificationExecutor<Task> {

	@Modifying
	@Query("update Task s set s.status = 0 where s.id =:id")
	void disableTask(@Param("id")Long id);

	@Modifying
	@Query("update Task s set s.status = 1 where s.id =:id")
	void enableTask(@Param("id")Long id);
	
//	@Query("select p from Task p where p.subUserFilter.id =:subuserfilterId")
//	List<Task> findBySubUserFilterId(@Param("subuserfilterId") Long subuserfilterId);

	List<Task> findByNameLike(String name);
}
