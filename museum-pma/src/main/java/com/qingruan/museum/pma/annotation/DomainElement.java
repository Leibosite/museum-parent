package com.qingruan.museum.pma.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD,
		ElementType.PARAMETER })
public @interface DomainElement {
	// 名称
	String name() default "";

	// 描述
	String description() default "";

	// 省略name和description后，用来保存name值
	String value() default "";

	// static value for params
	String staticValue() default "";

	// use
	String chosenClassName() default "";
	
	/*
	e.g extraProp = "datepick", use for add time control*/
	String extraProp() default "";
	
	boolean isMultiple() default false;
}
