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

import com.qingruan.museum.dao.entity.record.HourDataRecord;
import com.qingruan.museum.domain.models.enums.MonitorDataType;
import com.qingruan.museum.domain.models.enums.MonitorDeviceType;

/**
 * @author tommy
 *
 */
public interface HourDataRecordDao extends
JpaRepository<HourDataRecord, Long>,
JpaSpecificationExecutor<HourDataRecord>{

	@Query("select a from HourDataRecord a where a.repoArea.id = :areaId and a.objectType=:objectType and a.updateStamp between :startTime and :endTime order by a.dateTime DESC")
	List<HourDataRecord> findAllHourDataRecordByAreaAndTimeScopeAndType(
			@Param("areaId") Long areaId, @Param("startTime") Long startTime,
			@Param("endTime") Long endTime,
			@Param("objectType") MonitorDataType type);
	
	@Query("select h from HourDataRecord h where h.repoArea.id = :areaId and h.objectType = :objectType and h.monitorDeviceType=:monitorDeviceType and h.updateStamp between :startTime and :endTime")
	List<HourDataRecord> findHourDataRecordByAreaIdBetweenStartTimeAndEndTime(
			@Param("areaId") Long areaId,
			@Param("objectType") MonitorDataType objectType,
			@Param("startTime") Long startTime, @Param("endTime") Long endTime,
			@Param("monitorDeviceType") MonitorDeviceType monitorDeviceType);
	
	@Query("select a from HourDataRecord a where a.repoArea.id = :repoAreaId and a.objectType=:objectType and a.monitorDeviceType=:monitorDeviceType and a.updateStamp between :startTime and :endTime order by a.dateTime DESC")
	List<HourDataRecord> findAllHourDataRecordByDeviceAndTimeScopeAndType(
			@Param("repoAreaId") Long repoAreaId, @Param("startTime") Long startTime,
			@Param("endTime") Long endTime,
			@Param("objectType") MonitorDataType type,
			@Param("monitorDeviceType") MonitorDeviceType monitorDeviceType);
	@Query("select a from HourDataRecord a where a.repoArea.id = :areaId and a.updateStamp between :startTime and :endTime order by a.dateTime DESC")
	List<HourDataRecord> findOneHourDataRecordByTimeScope(
			@Param("areaId") Long areaId, @Param("startTime") Long startTime,
			@Param("endTime") Long endTime);

}
