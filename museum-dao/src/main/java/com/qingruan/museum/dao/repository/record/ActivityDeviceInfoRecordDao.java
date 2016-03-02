/**
 2015年3月10日
 14cells
 
 */
package com.qingruan.museum.dao.repository.record;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qingruan.museum.dao.entity.record.ActivityDeviceInfoRecord;

/**
 * @author tommy
 *
 */
public interface ActivityDeviceInfoRecordDao extends
		JpaRepository<ActivityDeviceInfoRecord, Long>,
		JpaSpecificationExecutor<ActivityDeviceInfoRecord> {
	@Query("select a from ActivityDeviceInfoRecord a where a.device.id = :deviceId and a.updateStamp between :startTime and :endTime")
	List<ActivityDeviceInfoRecord> findActivityDeviceInfoRecordByDeviceIdAndTimeScope(
			@Param("deviceId") Long deviceId,
			@Param("startTime") Long startTime, @Param("endTime") Long endTime);
}
