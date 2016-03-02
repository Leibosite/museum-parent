package com.qingruan.museum.pma.service;

public class RuleExecuteHelper {
	public static String getInterfaceName(String callingExpression) {
		int firstDotIndex = callingExpression.indexOf(".");

		if (firstDotIndex < 0) {
			return callingExpression;
		}

		return callingExpression.substring(0, firstDotIndex);
	}

	public static String getInterfaceMethod(String callingExpression) {
		int firstDotIndex = callingExpression.indexOf(".");

		if (firstDotIndex < 0) {
			return callingExpression;
		}

		return callingExpression.substring(firstDotIndex + 1,
				callingExpression.length());
	}

	public static String convertFirstToLowerCase(String str) {
		return str.replaceFirst(str.substring(0, 1), str.substring(0, 1)
				.toLowerCase());
	}
}
