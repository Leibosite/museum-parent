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
import com.qingruan.museum.dao.entity.record.MonthDataRecord;
import com.qingruan.museum.domain.models.enums.MonitorDataType;
import com.qingruan.museum.domain.models.enums.MonitorDeviceType;

/**
 * @author 14cells
 *
 */
public interface MonthDataRecordDao extends
		JpaRepository<MonthDataRecord, Long>,
		JpaSpecificationExecutor<MonthDataRecord> {

	@Query("select a from MonthDataRecord a where a.repoArea.id = :areaId and a.objectType=:objectType and a.updateStamp between :startTime and :endTime order by a.dateTime DESC")
	List<MonthDataRecord> findAllMonthDataRecordByAreaAndTimeScopeAndType(
			@Param("areaId") Long areaId, @Param("startTime") Long startTime,
			@Param("endTime") Long endTime,
			@Param("objectType") MonitorDataType type);

	@Query("select h from MonthDataRecord h where h.repoArea.id = :areaId and h.objectType = :objectType and h.monitorDeviceType=:monitorDeviceType and h.updateStamp between :startTime and :endTime")
	List<MonthDataRecord> findMonthDataRecordByAreaIdBetweenStartTimeAndEndTime(
			@Param("areaId") Long areaId,
			@Param("objectType") MonitorDataType objectType,
			@Param("startTime") Long startTime, @Param("endTime") Long endTime, @Param("monitorDeviceType") MonitorDeviceType monitorDeviceType);
	
	@Query("select a from MonthDataRecord a where a.device.id = :deviceId and a.objectType=:objectType and a.monitorDeviceType=:monitorDeviceType and a.updateStamp between :startTime and :endTime order by a.dateTime DESC")
	List<MonthDataRecord> findAllMonthDataRecordByDeviceAndTimeScopeAndType(
			@Param("deviceId") Long deviceId, @Param("startTime") Long startTime,
			@Param("endTime") Long endTime,
			@Param("objectType") MonitorDataType type,
			@Param("monitorDeviceType") MonitorDeviceType monitorDeviceType);

}
