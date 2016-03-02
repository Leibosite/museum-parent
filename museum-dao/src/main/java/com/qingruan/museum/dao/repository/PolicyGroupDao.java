package com.qingruan.museum.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.qingruan.museum.dao.entity.PolicyGroup;

public interface PolicyGroupDao extends JpaRepository<PolicyGroup, Long>,
		JpaSpecificationExecutor<PolicyGroup> {
	List<PolicyGroup> findByGroupNameLike(String name);
//	@Transactional(readOnly = false)
	@Modifying
	@Query("update PolicyGroup p set p.groupName =:groupName,p.desp =:desp where p.id=:id")
	void updatePolicyGroupById(
			@Param("groupName") String groupName,
			@Param("desp") String desp,
			@Param("id") Long id);
	
//	
//	
//	// @Transactional(readOnly = false)
//	@Modifying
//	@Query("update Device p set p.currentPower =:currentPower,p.updateStamp =:updateStamp,p.deviceStatus=:deviceStatus,p.currentMonitorValue=:currentMonitorValue where p.id=:id")
//	void updateRunTimeDeviceInfoById(
//			@Param("currentPower") Double currentPower,
//			@Param("updateStamp") Long updateStamp,
//			@Param("deviceStatus") DeviceStatus deviceStatus,
//			@Param("id") Long id,
//			@Param("currentMonitorValue") String currentMonitorValue);
}
