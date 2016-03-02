package com.qingruan.museum.admin.service.device;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qingruan.museum.dao.entity.Device;
import com.qingruan.museum.dao.repository.DeviceDao;

@Service
public class DeviceService {
	@Autowired
	private DeviceDao deviceDao;

	public List<Device> getAllInfosByNameLike(String name) {
		return deviceDao.findByNameLike(name);
	}
}
