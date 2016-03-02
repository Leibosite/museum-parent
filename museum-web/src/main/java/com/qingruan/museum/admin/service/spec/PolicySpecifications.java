package com.qingruan.museum.admin.service.spec;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.qingruan.museum.dao.entity.PolicyWrapper;
import com.qingruan.museum.framework.spring.data.jpa.util.PredicateUtil;

public class PolicySpecifications {
	
	/**
	 *  按参数对规则进行查找
	 * @param policyGroupId
	 * @param policyName
	 * @param status
	 * @param startTime
	 * @param endTime
	 * @return
	 * @author tommy
	 */
	public static Specification<PolicyWrapper> policySepc(final Long policyGroupId,
			final String policyName, final Integer status,
			final Long startTime, final Long endTime) {
		
		return new Specification<PolicyWrapper>() {

			@Override
			public Predicate toPredicate(Root<PolicyWrapper> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				
				Path<Long> policyGroupIdExp = root.get("policyGroupId");
//				Path<Long> policyGroupIdExp = root.get("policyGroup");
				Path<String> policyNameExp = root.get("policyContent");
				Path<Integer> statusExp = root.get("status");
				Path<Long> updateStampExp = root.get("updateStamp");
				
				Predicate temp = cb.conjunction();
				
				if(policyGroupId != null){
					temp = PredicateUtil.add(temp, cb.equal(policyGroupIdExp, policyGroupId), cb);
				}
				
				if(status != null){
					temp = PredicateUtil.add(temp, cb.equal(statusExp, status), cb);
				}
				
				if(StringUtils.isNotBlank(policyName)){
					// 去掉 salience%,如果优先级为空导致查找不到
					String queryName = "%policyName%" + policyName + "%salience%";
					temp = PredicateUtil.add(temp, cb.like(policyNameExp, queryName), cb);
				}
				
				
				if(startTime != null && endTime != null){
					temp = PredicateUtil.add(temp, cb.between(updateStampExp, startTime, endTime), cb);
				}else if(startTime != null && endTime == null){
					temp = PredicateUtil.add(temp, cb.greaterThan(updateStampExp, startTime), cb);
				}else if(startTime == null && endTime != null){
					temp = PredicateUtil.add(temp, cb.lessThan(updateStampExp, endTime), cb);
				}
				
				
				return temp;
			}
			
		};
		
	}

}
