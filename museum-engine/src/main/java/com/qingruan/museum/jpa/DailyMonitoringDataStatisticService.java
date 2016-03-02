package com.qingruan.museum.jpa;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.qingruan.museum.domain.models.enums.MonitorDataType;

/**
 * 每日监测数据归档方法:两种方法归档{1.通过数据库计算：2.通过代码计算} 
 * @author 雷德宝
 *
 */
@Service
@Slf4j
public class DailyMonitoringDataStatisticService {

	
	
	//test
	public static void main(String[] args) throws Exception  {
//		Long currentT = System.currentTimeMillis();
//		Long startT = getDayOfStartTime(currentT);
//		Long endT = getDayOfEndTime(currentT);
//		
	}
	
	/**
	 * 获得当前时间这一天的开始时间
	 * @param currentTime
	 * @return
	 */
	private  Long getDayOfStartTime(Long currentTime){
		Date startTime = new Date(currentTime);
		Calendar cal = Calendar.getInstance();
		cal.setTime(startTime);
		cal.set(Calendar.HOUR_OF_DAY, -23);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTimeInMillis();
	}
	/**
	 * 获得当前时间这一天的结束时间
	 * @param currentTime
	 * @return
	 */
	private Long getDayOfEndTime(Long currentTime){
		Date endTime = new Date(currentTime);
		Calendar cal = Calendar.getInstance();
		cal.setTime(endTime);
		//天数加一
		cal.add(Calendar.DAY_OF_MONTH,1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTimeInMillis();
	}

	// 获得具体对象的属性值
	public static Double getObjectValueOfDouble(Object object,MonitorDataType monitorDataType) throws Exception {
		Double tamp = 0.0;
		if (object != null) {
			// 拿到该类
			Class<?> clz = object.getClass();
			// 获取实体类的所有属性，返回Field数组
			Field[] fields = clz.getDeclaredFields();

			for (Field field : fields) {// --for() begin
				// 如果类型是Double
				if (field.getGenericType().toString()
						.equals("class java.lang.Double") && field.getName().equals( monitorDataType.name())) {
					Method m = (Method) object.getClass().getMethod(
               "get" + getMethodName(field.getName()));  
                    Double val = (Double) m.invoke(object);  
                    if (val != null) {  
                        tamp = val;  
                    }  
                    break; 
                }   
                continue;                
            }              
        }
        return tamp;
    }  	
  
    // 把一个字符串的第一个字母大写
    private static String getMethodName(String fildeName) throws Exception{  
        byte[] items = fildeName.getBytes();  
        items[0] = (byte) ((char) items[0] - 'a' + 'A');  
        return new String(items);  
    } 			
}
