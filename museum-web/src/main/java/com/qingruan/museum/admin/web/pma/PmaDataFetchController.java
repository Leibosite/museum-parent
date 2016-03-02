package com.qingruan.museum.admin.web.pma;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qingruan.museum.admin.service.area.AreaService;
import com.qingruan.museum.admin.service.common.SysLogService;
import com.qingruan.museum.admin.service.device.DeviceService;
import com.qingruan.museum.admin.service.pma.PolicyManager;
import com.qingruan.museum.admin.service.sms.SmsTemplateManager;
import com.qingruan.museum.dao.entity.Device;
import com.qingruan.museum.dao.entity.RepoArea;
import com.qingruan.museum.dao.entity.SmsTemplate;
import com.qingruan.museum.pma.model.Policy;

/**
 * 获取数据
 * 
 * @author tommy
 * 
 */
@Controller
@RequestMapping("/pma/datafetch")
public class PmaDataFetchController {
	@Autowired
	private PolicyManager policyManager;

	@Autowired
	private SysLogService sysLogService;
	@Autowired
	private SmsTemplateManager smsTemplateManager;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private AreaService areaService;

	/**
	 * 策略规则查询
	 */
	@RequestMapping(value = "fetchPolicy")
	public void fetchPolicy(
			@RequestParam(value = "term", required = false) String name,
			HttpServletRequest request, HttpServletResponse response)
			throws JsonGenerationException, JsonMappingException, IOException {
		sysLogService.operatingStartLog(request);
		name = "%" + name + "%";
		List<Policy> infos = policyManager.findByNameLike(name);
		Map<Long, String> values = new HashMap<Long, String>();
		for (Policy a : infos) {
			values.put(a.getId(), a.getPolicyName());
		}
		ObjectMapper mapper = new ObjectMapper();
		String responseString = mapper.writeValueAsString(values);
		System.out.println("fetchPolicy values"+values);

		response.getWriter().write(responseString);
		System.out.println("fetchPolicy responseString"+responseString);
		sysLogService.operatingSuccessLog(request);
	}

	/**
	 * 获取配置的短信模板
	 * 
	 * @param name
	 * @param request
	 * @param response
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@RequestMapping(value = "fetchSmsTemplate")
	public void fetchSmsTemplateList(
			@RequestParam(value = "term", required = false) String name,
			HttpServletRequest request, HttpServletResponse response)
			throws JsonGenerationException, JsonMappingException, IOException {
		sysLogService.operatingStartLog(request);
		name = "%" + name + "%";
		List<SmsTemplate> infos = smsTemplateManager
				.getAllInfosByNameLike(name);
		Map<Long, String> values = new HashMap<Long, String>();
		for (SmsTemplate a : infos) {
			values.put(a.getId(), a.getTemplateContent());
		}
		ObjectMapper mapper = new ObjectMapper();
		System.out.println("fetchPolicy values"+values);


	
		String responseString = mapper.writeValueAsString(values);
		System.out.println("fetchPolicy responseString"+responseString);
		response.getWriter().write(responseString);
		sysLogService.operatingSuccessLog(request);
	}

	@RequestMapping(value = "fetchArea")
	public void fetchAreaList(
			@RequestParam(value = "term", required = false) String name,
			HttpServletRequest request, HttpServletResponse response)
			throws JsonGenerationException, JsonMappingException, IOException {
		sysLogService.operatingStartLog(request);
		name = "%" + name + "%";
		List<RepoArea> infos = areaService.getAllInfosByNameLike(name);

		Map<Long, String> values = new HashMap<Long, String>();
		for (RepoArea a : infos) {
			values.put(a.getId(), a.getName());
		}
		ObjectMapper mapper = new ObjectMapper();
		String responseString = mapper.writeValueAsString(values);
		response.getWriter().write(responseString);
		sysLogService.operatingSuccessLog(request);
	}

	@RequestMapping(value = "fetchMonitorPoint")
	public void fetchMonitorPointList(
			@RequestParam(value = "term", required = false) String name,
			HttpServletRequest request, HttpServletResponse response)
			throws JsonGenerationException, JsonMappingException, IOException {
		sysLogService.operatingStartLog(request);
		name = "%" + name + "%";
		List<Device> infos = deviceService.getAllInfosByNameLike(name);

		Map<Long, String> values = new HashMap<Long, String>();
		for (Device a : infos) {
			values.put(a.getId(), a.getName());
		}
		ObjectMapper mapper = new ObjectMapper();
		String responseString = mapper.writeValueAsString(values);
		response.getWriter().write(responseString);
		sysLogService.operatingSuccessLog(request);
	}
//
	@RequestMapping(value = "fetchMonitorStation")
	public void fetchMonitorStationList(
			@RequestParam(value = "term", required = false) String name,
			HttpServletRequest request, HttpServletResponse response)
			throws JsonGenerationException, JsonMappingException, IOException {

		sysLogService.operatingStartLog(request);
		name = "%" + name + "%";
		List<Device> infos = deviceService.getAllInfosByNameLike(name);

		Map<Long, String> values = new HashMap<Long, String>();
		for (Device a : infos) {
			values.put(a.getId(), a.getName());
		}
		ObjectMapper mapper = new ObjectMapper();
		String responseString = mapper.writeValueAsString(values);
		response.getWriter().write(responseString);
		sysLogService.operatingSuccessLog(request);

	}

}
