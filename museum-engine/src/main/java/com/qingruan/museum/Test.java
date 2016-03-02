package com.qingruan.museum;

public class Test {
	public static void main(String[] args) {

		Integer i = 10 + (int) (Math.random() * 90);
		Integer j = 60 + (int) (Math.random() * 40);
		for (int m = 0; m < 100000; m++) {

//			System.out.println("10---1000+ " + i);
			System.out.println("60---100----+ " + j);

		}

		Long currentTime = 1443065780220L;

		Long longTime = currentTime - 3600000L;
		System.out.println(currentTime);
		System.out.println(longTime);

		// String hourDay = TimeUtil.getStringTime(longTime,
		// TimeFormat.YEAR_MONTH_DAY);
		// String time = TimeUtil.getStringTime(longTime,
		// TimeFormat.YEAR_MONTH_DAY_HOUR_MIN_SEC);
		// System.out.println(hourDay);
		// System.out.println(time);
		// Long startTime = TimeUtil.getLongTimeByFormat(hourDay,
		// TimeUtil.DEFAULT_DATE_FORMAT);
		// Long startTime1 = TimeUtil.getLongTimeByFormat(hourDay+" 00:00:00",
		// TimeUtil.DEFAULT_DATETIME_FORMAT_EX);
		// System.out.println("1442973771314");
		// System.out.println(startTime);
		// System.out.println(startTime1);
		//
		//
		// Long currentTime=System.currentTimeMillis();
		// Long now = currentTime - 3600000L;
		// String hourDay1 = TimeUtil.getStringTime(currentTime,
		// TimeFormat.YEAR_MONTH_DAY_HOUR_MIN_SEC);
		// String time1 = TimeUtil.getStringTime(now,
		// TimeFormat.YEAR_MONTH_DAY_HOUR_MIN_SEC);
		// System.out.println(hourDay1);
		// System.out.println(time1);
		// System.out.println(365*24*3600*1000);

	}
}
