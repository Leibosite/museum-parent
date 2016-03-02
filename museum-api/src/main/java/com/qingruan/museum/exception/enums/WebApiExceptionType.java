package com.qingruan.museum.exception.enums;

public enum WebApiExceptionType {
	MISS_WEATHER_DEVICE_ID(2002), MISS_WEATHER_HISTORY_PARAMS(2003), RECORDS_NOT_EXIST_IN_DB(
			2004), MISS_AIR_CLEANER_DEVICE_ID(2005), DEVICE_NOT_EXIST_IN_DB(
			2006), MISS_LIMIT(2008), MISS_PAGE(2007), POLICY_GROUP_NOT_EXIST_IN_DB(
			2009), MISS_POLICY_GROUP_NAME(2010), MISS_POLICY_GROUP(2011), JSON_DESERIALIZE_ERROR(
			-1000), POLICY_GROUP_PARAMETER_ERROR(2012), MISS_POLICY_GROUP_ID(
			2012), OPERATION_OF_DATABASE_ERROR(2013), MISS_POLICY(2014), MISS_POLICY_CONTENT(
			2015), MISS_POLICY_ID(2016), POLICY_NOT_EXIST_IN_DB(2017), MISS_POLICY_NAME(
			2018), MISS_SMSTEMPLATE_Name(2019), MISS_AREA_Name(2020), MISS_MONITOR_POINT_Name(
			2021), MISS_MONITOR_STATION_Name(2022), MISS_CONSTANT_TH_NAME(2023), MISS_SHOW_CASE_NAME(2024), MISS_EAMIL_TEMPLATE_NAME(2025), ID_ERROR(2026), TOKEN_ERROR(2027), OPERATOR_ERROR(2028);

	private final Integer value;

	private WebApiExceptionType(Integer value) {
		this.value = value;
	}

	public Integer value() {
		return value;
	}

	public static WebApiExceptionType from(Integer value) {
		for (WebApiExceptionType item : WebApiExceptionType.values()) {
			if (item.value.equals(value))
				return item;
		}

		return null;
	}
}
