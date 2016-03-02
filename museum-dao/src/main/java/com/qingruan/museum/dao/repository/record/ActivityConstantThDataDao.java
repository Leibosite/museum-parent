package com.qingruan.museum.dao.repository.record;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qingruan.museum.dao.entity.record.ActivityConstantThData;
import com.qingruan.museum.domain.models.enums.MonitorDataType;

public interface ActivityConstantThDataDao extends
		JpaRepository<ActivityConstantThData, Long>,
		JpaSpecificationExecutor<ActivityConstantThData> {
	@Query("select h from ActivityConstantThData h where h.repoArea.id = :areaId and h.updateStamp between :startTime and :endTime order by h.objectType,h.value DESC ")
	List<ActivityConstantThData> findActivityConstantThDataByAreaIdBetweenAndTimeScope(
			@Param("areaId") Long areaId, @Param("startTime") Long startTime,
			@Param("endTime") Long endTime);

	@Query("select h from ActivityConstantThData h where h.repoArea.id = :areaId and h.objectType = :objectType and h.updateStamp between :startTime and :endTime order by h.value DESC")
	List<ActivityConstantThData> findActivityConstantThDataByAreaIdBetweenStartTimeAndEndTime(
			@Param("areaId") Long areaId,
			@Param("objectType") MonitorDataType objectType,
			@Param("startTime") Long startTime, @Param("endTime") Long endTime);
}
