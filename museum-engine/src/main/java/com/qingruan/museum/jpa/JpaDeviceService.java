/**
 2015年2月9日
 14cells
 
 */
package com.qingruan.museum.jpa;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qingruan.museum.dao.entity.Device;
import com.qingruan.museum.dao.repository.DeviceDao;

/**
 * @author tommy
 * 
 */
@Service
@Slf4j
public class JpaDeviceService {
	@Autowired
	private DeviceDao deviceDao;

	public JpaDeviceService() {

	}

	public Device findByNoAndType(String no, String type) {

		return deviceDao.findByDeviceNo(no);
	}

	public Device findDeviceByMacAddr(String macAddr) {
		log.info("{mac addr is : } " + macAddr);
		return deviceDao.findByMacAddr(macAddr);
	}

	public Device findDeviceByIpv4Addr(String ipAddr) {
		return deviceDao.findByIpAddr(ipAddr);
	}

	public List<Device> findDevicesByParentId(Long parentId) {

		return deviceDao.findByParentId(parentId);
	}
	public Device findDeviceNo(String deviceNo) {
		log.info("{mac addr is : } " + deviceNo);
		return deviceDao.findByDeviceNo(deviceNo);
				
	}
}
