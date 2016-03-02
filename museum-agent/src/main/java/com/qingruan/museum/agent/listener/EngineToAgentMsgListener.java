package com.qingruan.museum.agent.listener;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qingruan.museum.dao.entity.record.ActivityConstantThData;
import com.qingruan.museum.dao.entity.record.ActivityDataRecord;
import com.qingruan.museum.dao.entity.record.ActivityDeviceInfoRecord;
import com.qingruan.museum.dao.entity.record.ActivityWeatherData;
import com.qingruan.museum.dao.repository.record.ActivityConstantThDataDao;
import com.qingruan.museum.dao.repository.record.ActivityDataRecordDao;
import com.qingruan.museum.dao.repository.record.ActivityDeviceInfoRecordDao;
import com.qingruan.museum.dao.repository.record.ActivityWeatherDataDao;
import com.qingruan.museum.framework.esb.redispushpop.RedisMQBlpopReceiver.EngineToAgentMessageListener;

/**
 * @author tommy
 * 
 */
@Service
@Slf4j
public class EngineToAgentMsgListener implements EngineToAgentMessageListener {
	@Autowired
	private ActivityConstantThDataDao activityConstantThDataDao;
	@Autowired
	private ActivityDataRecordDao activityDataRecordDao;
	@Autowired
	private ActivityDeviceInfoRecordDao activityDeviceInfoRecordDao;
	@Autowired
	private ActivityWeatherDataDao activityWeatherDataDao;
	
	@Override
	public void receiveMessage(Object message) {
		if(message instanceof ActivityConstantThData){
			log.info(" { receive message : ActivityConstantThData } ");
			activityConstantThDataDao.save((ActivityConstantThData)message);
		} else if(message instanceof ActivityDataRecord){
			log.info(" { receive message : ActivityDataRecord } ");
			activityDataRecordDao.save((ActivityDataRecord)message);
		} else if(message instanceof ActivityDeviceInfoRecord){
			log.info(" { receive message : ActivityDeviceInfoRecord } ");
			activityDeviceInfoRecordDao.save((ActivityDeviceInfoRecord)message);
		} else if(message instanceof ActivityWeatherData){
			log.info(" { receive message : ActivityWeatherData } ");
			activityWeatherDataDao.save((ActivityWeatherData)message);
		}
	}

}
