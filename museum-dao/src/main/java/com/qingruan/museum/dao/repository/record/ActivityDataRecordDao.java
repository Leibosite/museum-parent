/**
 2015年3月3日
 14cells
 
 */
package com.qingruan.museum.dao.repository.record;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qingruan.museum.dao.entity.record.ActivityDataRecord;
import com.qingruan.museum.domain.models.enums.MonitorDataType;

/**
 * @author tommy
 * 
 */
public interface ActivityDataRecordDao extends
		JpaRepository<ActivityDataRecord, Long>,
		JpaSpecificationExecutor<ActivityDataRecord> {
	@Query("select h from ActivityDataRecord h where h.repoArea.id = :areaId and h.objectType = :objectType and h.updateStamp between :startTime and :endTime order by h.value DESC")
	List<ActivityDataRecord> findActivityDataRecordByAreaIdBetweenStartTimeAndEndTime(
			@Param("areaId") Long areaId,
			@Param("objectType") MonitorDataType objectType,
			@Param("startTime") Long startTime, @Param("endTime") Long endTime);

	@Query("select h from ActivityDataRecord h where h.repoArea.id = :areaId and h.updateStamp between :startTime and :endTime order by h.objectType,h.value DESC ")
	List<ActivityDataRecord> findActivityDataRecordByAreaIdBetweenAndTimeScope(
			@Param("areaId") Long areaId,
			@Param("startTime") Long startTime, @Param("endTime") Long endTime);

	@Query("select h from ActivityDataRecord h where h.device.id = :deviceId and h.objectType = :objectType and h.updateStamp between :startTime and :endTime order by h.updateStamp DESC ")
	List<ActivityDataRecord> findActivityDataRecordByDeviceIdBetweenAndTimeScopeAndObjectType(
			@Param("deviceId") Long deviceId,@Param("objectType") MonitorDataType objectType,
			@Param("startTime") Long startTime, @Param("endTime") Long endTime);
	
	@Query("select h from ActivityDataRecord h where h.objectType = :objectType and h.updateStamp between :startTime and :endTime order by h.updateStamp DESC ")
	List<ActivityDataRecord> findActivityDataRecordByObjectTypeBetweenAndTimeScope(
			@Param("objectType") MonitorDataType objectType,
			@Param("startTime") Long startTime, @Param("endTime") Long endTime);
	@Query("select h from ActivityDataRecord h where h.device.id = :deviceId and h.updateStamp between :startTime and :endTime order by h.updateStamp DESC ")
	List<ActivityDataRecord> findActivityDataRecordByDeviceIdBetweenAndTimeScope(
			@Param("deviceId") Long deviceId,
			@Param("startTime") Long startTime, @Param("endTime") Long endTime);
	
	@Modifying
	@Query("delete from ActivityDataRecord h where h.updateStamp <= :nowTime")
	void deleteByNowTime(@Param("nowTime") Long nowTime);
}
