package com.qingruan.museum.dao.repository.record;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qingruan.museum.dao.entity.record.ActivityCleanerData;

public interface ActivityCleanerDataDao extends
		JpaRepository<ActivityCleanerData, Long>,
		JpaSpecificationExecutor<ActivityCleanerData> {
	@Query("select h from ActivityCleanerData h where h.device.id = :deviceId order by h.updateStamp DESC")
	List<ActivityCleanerData> findActivityCleanerDataByDeviceId(
			@Param("deviceId") Long deviceId);
	
	@Query("select h from ActivityCleanerData h where h.device.id = :deviceId and h.updateStamp between :startTime and :endTime")
	List<ActivityCleanerData> findActivityCleanerDataByAreaIdBetweenStartTimeAndEndTime(
			@Param("deviceId") Long deviceId,
			@Param("startTime") Long startTime, @Param("endTime") Long endTime);
	
	
}
