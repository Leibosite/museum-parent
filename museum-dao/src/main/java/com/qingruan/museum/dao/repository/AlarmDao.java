/**
 2014年12月18日
 14cells
 
 */
package com.qingruan.museum.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qingruan.museum.dao.entity.Alarm;
import com.qingruan.museum.domain.enums.WarnCatego;

/**
 * @author tommy
 * 
 */
public interface AlarmDao extends JpaRepository<Alarm, Long>,
		JpaSpecificationExecutor<Alarm> {
	List<Alarm> findByNameLike(String name);

	// 查找某个时间区间的告警列表
	@Query("select a from Alarm a where a.updateStamp between :startTime and :endTime")
	List<Alarm> findByAlarmBetweenTime(@Param("startTime") Long startTime,
			@Param("endTime") Long endTime);

	// 查找某个区域时间区间内的告警列表
	@Query("select a from Alarm a where a.repoArea.id=:rePoAreaId and updateStamp between :startTime and :endTime")
	List<Alarm> findAlarmByAreaAndTimeScope(
			@Param("rePoAreaId") Long rePoAreaId,
			@Param("startTime") Long startTime, @Param("endTime") Long endTime);

	@Query("select count(*) from Alarm a where a.warnCategory.category= :warnCatego and updateStamp between :startTime and :endTime")
	Long findAlarmCountByTimeScopeAndWareCategory(
			@Param("startTime") Long startTime, @Param("endTime") Long endTime,
			@Param("warnCatego") WarnCatego warnCatego);

	// 查找某个区域时间区间内的告警列表
	@Query("select count(*) from Alarm a where a.repoArea.id=:rePoAreaId and updateStamp between :startTime and :endTime")
	Long findAlarmCountByAreaAndTimeScope(@Param("rePoAreaId") Long rePoAreaId,
			@Param("startTime") Long startTime, @Param("endTime") Long endTime);
}
