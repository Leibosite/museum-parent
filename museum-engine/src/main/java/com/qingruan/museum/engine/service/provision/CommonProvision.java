package com.qingruan.museum.engine.service.provision;

import java.util.Date;

import lombok.extern.slf4j.Slf4j;

import org.drools.core.time.impl.CronExpression;
import org.springframework.stereotype.Service;

import com.qingruan.museum.engine.exception.ExceptionLog;

@Slf4j
@Service
public class CommonProvision {


	/**
	 * 
	 * @param hexstr
	 * @return
	 */
	public byte[] HexString2Bytes(String hexstr) {
		byte[] b = new byte[hexstr.length() / 2];
		int j = 0;
		for (int i = 0; i < b.length; i++) {
			char c0 = hexstr.charAt(j++);
			char c1 = hexstr.charAt(j++);
			b[i] = (byte) ((parse(c0) << 4) | parse(c1));
		}
		return b;
	}

	/**
	 * parse char to int
	 * 
	 * @param c
	 * @return
	 */
	private int parse(char c) {
		if (c >= 'a')
			return (c - 'a' + 10) & 0x0f;
		if (c >= 'A')
			return (c - 'A' + 10) & 0x0f;
		return (c - '0') & 0x0f;
	}



	/**
	 * 
	 * @param intervalBegin
	 * @param intervalEnd
	 * @return
	 */
	public boolean IntervalCheckOfCurTime(String cronExpress) {
		try{
			String[] crons = cronExpress.trim().split(";");
			for(String cron : crons){
				CronExpression cronExpression = new CronExpression(cron.trim());
				
				if(cronExpression.isSatisfiedBy(new Date(System.currentTimeMillis()))){
					return true;
				}
			}
			
		}catch (Exception e) {
			log.error(ExceptionLog.getErrorStack(e));
		}
		
		return false;
	}



}
