package com.qingruan.museum.api.utils;

/**
 * 获取随机数
 * 
 * @author tommy
 * 
 */
public class CommonUtils {
	public static Integer generatRandom() {
		return 60 + (int) (Math.random() * 40);
	}
}
