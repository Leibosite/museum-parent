package com.qingruan.museum.framework.spring.data.jpa.util;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

/**
 * Spring Data Specification Util.
 * 
 * @author tommy
 */
public class SpecificationUtil {
	
	/**
	 * 根据属性模糊查询.
	 * 
	 * @param clazz 实体类型
	 * @param name 属性名
	 * @param value 属性值
	 * 
	 * @return
	 */
	public static <T> Specification<T> like(Class<T> clazz, final String name, final String value) {
		
		return new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Path<String> nameExp = root.get(name);
				Predicate temp = cb.conjunction();
				if (StringUtils.isNotBlank(value)) {
					temp = PredicateUtil.add(temp, PredicateUtil.like(cb, nameExp, value), cb);
				}
				
				return temp;
			}
		};
	}
	
	/**
	 * 
	 * @param clazz
	 * @param name
	 * @param value
	 * @return
	 */
	public static <T> Specification<T> eq(Class<T> clazz, final String name, final Object value) {
		return new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.<Object>get(name), value);
			}
		};
	}
	                          
	/**
	 * 
	 * @param clazz
	 * @param name
	 * @param value
	 * @return
	 */
	public static <T> Specification<T> ne(Class<T> clazz, final String name, final Object value) {
		return new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.notEqual(root.<Object>get(name), value);
			}
		};
	}

}
