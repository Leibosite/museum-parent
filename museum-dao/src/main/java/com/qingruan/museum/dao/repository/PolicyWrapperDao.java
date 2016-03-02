package com.qingruan.museum.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.qingruan.museum.dao.entity.PolicyWrapper;

public interface PolicyWrapperDao extends JpaRepository<PolicyWrapper, Long>,
		JpaSpecificationExecutor<PolicyWrapper> {

	@Query("select p from PolicyWrapper p where p.policyGroupId =:policyGroupId")
	List<PolicyWrapper> findByPolicyGroupId(
			@Param("policyGroupId") Long policyGroupId);

	@Query("select p from PolicyWrapper p where p.policyGroupId =:policyGroupId and p.policyContent like :policyContent")
	List<PolicyWrapper> findByPolicyGroupIdAndPolicyContent(
			@Param("policyGroupId") Long policyGroupId,
			@Param("policyContent") String policyContent);

	@Query("select p from PolicyWrapper p where p.policyContent like :policyContent")
	List<PolicyWrapper> findByPolicyContentLike(@Param("policyContent") String policyContent);

	@Transactional(readOnly = false)
	@Modifying
	@Query("update PolicyWrapper p set p.policyContent =:policyContent,p.desp =:desp where p.id=:id")
	void updatePolicyById(@Param("policyContent") String policyContent,
			@Param("desp") String desp, @Param("id") Long id);

//	@Transactional(readOnly = false)
	@Modifying
	@Query("update PolicyWrapper p set p.status =:status where p.id=:id")
	void updatePolicyStatusById(@Param("status") Integer status,
			@Param("id") Long id);

}
