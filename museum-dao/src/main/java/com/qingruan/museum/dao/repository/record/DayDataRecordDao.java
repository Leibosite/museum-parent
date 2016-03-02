/**
 2015年3月3日
 14cells
 
 */
package com.qingruan.museum.dao.repository.record;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qingruan.museum.dao.entity.record.DayDataRecord;
import com.qingruan.museum.domain.models.enums.MonitorDataType;
import com.qingruan.museum.domain.models.enums.MonitorDeviceType;

/**
 * @author tommy
 *
 */
public interface DayDataRecordDao extends
JpaRepository<DayDataRecord, Long>,
JpaSpecificationExecutor<DayDataRecord>{
	@Query("select h from DayDataRecord h where h.repoArea.id = :areaId and h.objectType = :objectType and h.monitorDeviceType=:monitorDeviceType and h.updateStamp between :startTime and :endTime")
	List<DayDataRecord> findDayDataRecordByAreaIdBetweenStartTimeAndEndTime(
			@Param("areaId") Long areaId,
			@Param("objectType") MonitorDataType objectType,
			@Param("startTime") Long startTime, @Param("endTime") Long endTime, @Param("monitorDeviceType") MonitorDeviceType monitorDeviceType);

	@Query("select a from DayDataRecord a where a.repoArea.id = :areaId and a.objectType=:objectType and a.updateStamp between :startTime and :endTime order by a.dateTime DESC")
	List<DayDataRecord> findAllDayDataRecordByAreaAndTimeScopeAndType(
			@Param("areaId") Long areaId, @Param("startTime") Long startTime,
			@Param("endTime") Long endTime,
			@Param("objectType") MonitorDataType type);
	
	@Query("select a from DayDataRecord a where a.repoArea.id = :areaId and a.dateTime=:dateTime")
	List<DayDataRecord> findAllDayDataRecordByAreaAndTime(
			@Param("areaId") Long areaId, @Param("dateTime") String dateTime);
	
	@Query("select a from DayDataRecord a where a.device.id = :deviceId and a.objectType=:objectType and a.monitorDeviceType=:monitorDeviceType and a.updateStamp between :startTime and :endTime order by a.dateTime DESC")
	List<DayDataRecord> findAllDayDataRecordByDeviceAndTimeScopeAndType(
			@Param("deviceId") Long deviceId, @Param("startTime") Long startTime,
			@Param("endTime") Long endTime,
			@Param("objectType") MonitorDataType type,
			@Param("monitorDeviceType") MonitorDeviceType monitorDeviceType);
	
}
