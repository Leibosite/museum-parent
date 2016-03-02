package com.qingruan.museum.pma.drlbuilder;

public class DrlBuilderUtil {
	public static String ConvertFirstCharToUpper(String varName) {
		return Character.toUpperCase(varName.charAt(0)) + varName.substring(1);
	}
	
	public static String ConvertFirstCharToLower(String className) {
		return Character.toLowerCase(className.charAt(0)) + className.substring(1);
	}
}
