package com.qingruan.museum.admin.web.pma;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qingruan.museum.admin.service.sms.SmsTemplateManager;
import com.qingruan.museum.dao.entity.SmsTemplate;

@Controller
@RequestMapping("/pma/json")
public class PmaJsonDataController {
	@Autowired
	private SmsTemplateManager smsTemplateManager;

	@RequestMapping(value = "showSmsTemplate/{id}")
	public @ResponseBody
	SmsTemplate getSmsTemplate(@PathVariable("id") Long id) {
		return smsTemplateManager.getOne(id);
	}

//	@RequestMapping(value = "showArea/{id}")
//	public @ResponseBody
//	RepoArea getArea(@PathVariable("id") Long id) {
//
//		RepoArea repoArea = areaService.getOne(id);
//		RepoArea newRepoArea = new RepoArea();
//		newRepoArea.setId(repoArea.getId());
//		newRepoArea.setName(repoArea.getName());
//		return newRepoArea;
//	}
//
//	@RequestMapping(value = "showMonitorPoint/{id}")
//	public @ResponseBody
//	Device getMonitorPoint(@PathVariable("id") Long id) {
//
//		Device point = deviceService.findById(id);
//		Device newPoint = new Device();
//		newPoint.setId(point.getId());
//		newPoint.setName(point.getName());
//		return newPoint;
//	}
//
//	@RequestMapping(value = "showMonitorStation/{id}")
//	public @ResponseBody
//	Device getMonitorStation(@PathVariable("id") Long id) {
//
//		Device station = deviceService.findById(id);
//		Device newStation = new Device();
//		newStation.setId(station.getId());
//		newStation.setName(station.getName());
//		return newStation;
//	}
}
