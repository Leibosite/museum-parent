package com.qingruan.museum.pma.util;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 国际化
 * 
 * @author tommy
 * 
 */
public class I18nUtils {

	public static String getI18nValue(String key) {
		Locale locale = Locale.getDefault();

		ResourceBundle boundle = ResourceBundle.getBundle("i18n/pma", locale);
		try {
			key = boundle.getString(key);
		} catch (Exception e) {
			return key;
		}

		return key;
	}
}
