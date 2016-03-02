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

import com.qingruan.museum.dao.entity.record.MonthDataRecord;
import com.qingruan.museum.dao.entity.record.YearDataRecord;
import com.qingruan.museum.domain.models.enums.MonitorDataType;
import com.qingruan.museum.domain.models.enums.MonitorDeviceType;

/**
 * @author 14cells
 * 
 */
public interface YearDataRecordDao extends JpaRepository<YearDataRecord, Long>,
		JpaSpecificationExecutor<YearDataRecord> {
	@Query("select a from YearDataRecord a where a.repoArea.id = :areaId and a.objectType=:objectType and a.updateStamp between :startTime and :endTime order by a.dateTime DESC")
	List<YearDataRecord> findAllYearDataRecordByAreaAndTimeScopeAndType(
			@Param("areaId") Long areaId, @Param("startTime") Long startTime,
			@Param("endTime") Long endTime,
			@Param("objectType") MonitorDataType type);

	@Query("select a from YearDataRecord a where a.device.id = :deviceId and a.objectType=:objectType and a.monitorDeviceType=:monitorDeviceType and a.updateStamp between :startTime and :endTime order by a.dateTime DESC")
	List<YearDataRecord> findAllYearDataRecordByDeviceAndTimeScopeAndType(
			@Param("deviceId") Long deviceId,
			@Param("startTime") Long startTime, @Param("endTime") Long endTime,
			@Param("objectType") MonitorDataType type,
			@Param("monitorDeviceType") MonitorDeviceType monitorDeviceType);
}
