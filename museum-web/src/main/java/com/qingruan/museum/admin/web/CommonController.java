/**
 2015年2月1日
 14cells
 
 */
package com.qingruan.museum.admin.web;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qingruan.museum.admin.service.common.SysLogService;

/**
 * @author tommy
 * 
 */
@Slf4j
@Controller
@RequestMapping("/common/area")
public class CommonController {
	@Autowired
	private SysLogService sysLogService;
//	private AreaService areaService;
//	@Autowired
//	private DeviceService deviceService;

//	@RequestMapping(value = "fetchAreaList")
//	public void fetchAreaList(
//			@RequestParam(value = "term", required = false) String name,
//			HttpServletResponse response, HttpServletRequest request)
//			throws JsonGenerationException, JsonMappingException, IOException {
//		sysLogService.operatingStartLog(request);
//		AreaQueryForm form = new AreaQueryForm();
//		form.setName(name);
//		name = "%" + name + "%";
//		List<RepoArea> u_list = areaService.getAllInfosByNameLike(name);
//		Map<Long, String> values = new HashMap<Long, String>();
//		for (RepoArea u : u_list) {
//			values.put(u.getId(), u.getName());
//		}
//		ObjectMapper mapper = new ObjectMapper();
//		String responseString = mapper.writeValueAsString(values);
//		sysLogService.operatingSuccessLog(request);
//		response.getWriter().write(responseString);
//	}

//	@RequestMapping(value = "fetchDeviceList")
//	public void fetchDeviceList(
//			@RequestParam(value = "term", required = false) String name,
//			HttpServletResponse response, HttpServletRequest request)
//			throws JsonGenerationException, JsonMappingException, IOException {
//		sysLogService.operatingStartLog(request);
//		DeviceQueryForm form = new DeviceQueryForm();
//		form.setName(name);
//		name = "%" + name + "%";
//		List<Device> u_list = deviceService.getAllInfosByNameLike(name);
//		Map<Long, String> values = new HashMap<Long, String>();
//		for (Device u : u_list) {
//			values.put(u.getId(), u.getName());
//		}
//		ObjectMapper mapper = new ObjectMapper();
//		String responseString = mapper.writeValueAsString(values);
//		sysLogService.operatingSuccessLog(request);
//		response.getWriter().write(responseString);
//	}

}
