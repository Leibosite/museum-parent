package com.qingruan.museum.framework.spring.data.jpa.util;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

/**
 * 
 * @author tommy
 *
 */
public class PredicateUtil {

	/**
	 * 
	 */
	public static Predicate like(final CriteriaBuilder cb, final Path<String> pathExp, final String searchTerm) {
		StringBuilder pattern = new StringBuilder();
		pattern.append("%");
		pattern.append(escapeSQLLike(searchTerm));
		pattern.append("%");
		
		return cb.like(pathExp, pattern.toString(), '!');
	}
	
	/**
	 * 
	 */
	private static String escapeSQLLike(String value) {
		return value.toString()
			.replaceAll("!", "!!")
			.replaceAll(String.valueOf((char)95), "!" + String.valueOf((char)95))			//半角_
			.replaceAll(String.valueOf((char)65343), "!" + String.valueOf((char)65343))		//全角＿
			.replaceAll(String.valueOf((char)37), "!" + String.valueOf((char)37))			//半角%
			.replaceAll(String.valueOf((char)65285), "!" + String.valueOf((char)65285));	//全角％
	}
	
	/**
	 * 
	 * @param aPredicate
	 * @param bPredicate
	 * @param cb
	 * @return
	 */
	public static Predicate add(Predicate aPredicate, Predicate bPredicate, CriteriaBuilder cb) {
		if (aPredicate == null) {
			return bPredicate;
		} else {
			return cb.and(aPredicate, bPredicate);
		}
	}
	
	/**
	 * 
	 * @param aPredicate
	 * @param bPredicate
	 * @param cb
	 * @return
	 */
	public static Predicate or(Predicate aPredicate, Predicate bPredicate, CriteriaBuilder cb) {
		if (aPredicate == null) {
			return bPredicate;
		} else {
			return cb.or(aPredicate, bPredicate);
		}
	}

}
