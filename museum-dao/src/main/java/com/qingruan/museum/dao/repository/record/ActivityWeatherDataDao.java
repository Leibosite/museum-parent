package com.qingruan.museum.dao.repository.record;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qingruan.museum.dao.entity.record.ActivityWeatherData;
import com.qingruan.museum.dao.entity.record.DayDataRecord;
import com.qingruan.museum.domain.models.enums.MonitorDataType;

public interface ActivityWeatherDataDao extends
		JpaRepository<ActivityWeatherData, Long>,
		JpaSpecificationExecutor<ActivityWeatherData> {
	
	@Query("select h from ActivityWeatherData h where h.device.id = :deviceId order by h.updateStamp DESC")
	List<ActivityWeatherData> findActivityWeatherDataByDeviceId(
			@Param("deviceId") Long deviceId);
	
	
	@Query("select h from ActivityWeatherData h where h.repoAreaId = :areaId and h.updateStamp between :startTime and :endTime")
	List<ActivityWeatherData> findActivityWeatherDataByAreaIdBetweenStartTimeAndEndTime(
			@Param("areaId") Long areaId,
			@Param("startTime") Long startTime, @Param("endTime") Long endTime);
}
