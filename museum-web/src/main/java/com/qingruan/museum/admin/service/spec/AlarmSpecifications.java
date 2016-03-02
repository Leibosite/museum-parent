/**
 2015年1月13日
 14cells
 
 */
package com.qingruan.museum.admin.service.spec;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.qingruan.museum.dao.entity.Alarm;
import com.qingruan.museum.framework.spring.data.jpa.util.PredicateUtil;

/**
 * @author tommy
 * 
 */
public class AlarmSpecifications {

	/**
	 * 
	 * @param alarmName
	 * @param status
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static Specification<Alarm> alarmSepc(final String alarmName,
			final Integer status, final Long startTime, final Long endTime) {

		return new Specification<Alarm>() {

			@Override
			public Predicate toPredicate(Root<Alarm> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub

				Path<String> alarmNameExp = root.get("name");
				Path<Integer> statusExp = root.get("status");
				Path<Long> updateStampExp = root.get("updateStamp");

				Predicate temp = cb.conjunction();

				if (status != null) {
					temp = PredicateUtil.add(temp, cb.equal(statusExp, status),
							cb);
				}

				if (StringUtils.isNotBlank(alarmName)) {
					// 去掉 salience%,如果优先级为空导致查找不到
					String queryName = "%alarmName%" + alarmName + "%salience%";
					temp = PredicateUtil.add(temp,
							cb.like(alarmNameExp, queryName), cb);
				}

				if (startTime != null && endTime != null) {
					temp = PredicateUtil.add(temp,
							cb.between(updateStampExp, startTime, endTime), cb);
				} else if (startTime != null && endTime == null) {
					temp = PredicateUtil.add(temp,
							cb.greaterThan(updateStampExp, startTime), cb);
				} else if (startTime == null && endTime != null) {
					temp = PredicateUtil.add(temp,
							cb.lessThan(updateStampExp, endTime), cb);
				}

				return temp;
			}

		};

	}

}
