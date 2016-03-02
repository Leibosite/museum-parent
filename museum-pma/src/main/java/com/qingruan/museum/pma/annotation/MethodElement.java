package com.qingruan.museum.pma.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MethodElement {
	// 名称
	String name() default "";

	// 描述
	String description() default "";

	// 省略name和description后，用来保存name值
	String value() default "";
	
	//冲突检测目标类
	Class<?> conflictTarget() default Object.class;
	
	//在参数列表中需要解决冲突的的参数的index
	String dependParam() default "";
}
