package com.qingruan.museum.api.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springside.modules.test.spring.SpringTransactionalTestCase;

import com.qingruan.museum.api.service.business.sensor.CheckDeviceStatusService;
import com.qingruan.museum.dao.entity.Device;
import com.qingruan.museum.dao.repository.DeviceDao;
import com.qingruan.museum.domain.models.enums.DeviceType;



@DirtiesContext
@ContextConfiguration(locations = { "classpath:applicationContext.xml","classpath*:/META-INF/spring/applicationContext-museum-*.xml"})
// 如果存在多个transactionManager，可以需显式指定
@TransactionConfiguration(transactionManager = "transactionManager")
public class CheckDeviceStatusTest extends SpringTransactionalTestCase {

	@Autowired
	public CheckDeviceStatusService checkDeviceStatusService;
	@Autowired
	private DeviceDao deviceDao;
	
	@Test
	public void allClassMapping() throws Exception {
		System.out.println("Test：开始探测设备存活");
		
		Device device = deviceDao.findById(296L);
		checkDeviceStatusService.sendDeviceStatusCheck(device);
		
		System.out.println("Test：测试结束");
	}
}
