package com.qingruan.museum.admin.utils;

import java.text.SimpleDateFormat;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;

@Slf4j
public class BusinessRecordUtil {
	
	public static Long convertTimeString2Long(String time){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if(StringUtils.isNotBlank(time)){
			time = time.concat(":00");
			
			try{
				Long temp = simpleDateFormat.parse(time).getTime();
				
				return temp;
			}catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		
		return null;
	}
}
