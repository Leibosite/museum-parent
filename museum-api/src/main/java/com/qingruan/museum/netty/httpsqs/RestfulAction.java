package com.qingruan.museum.netty.httpsqs;

import static io.netty.buffer.Unpooled.copiedBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.qingruan.museum.api.service.rest.RestfulEngineService;

/**
 * RESTFUL鎺ュ彛鐨凞ispatcher
 * 
 * @author 鍒樹粫楣�
 * 
 */

public class RestfulAction {

	private RestfulEngineService restfulEngineService;

	public RestfulAction(RestfulEngineService restfulEngineService) {
		this.restfulEngineService = restfulEngineService;
	}

	public static ByteBuf resq = null;

	/**
	 * 鑾峰彇瑙勫垯缁勫垪琛�
	 * 
	 * @param map
	 * @return
	 */
	public ByteBuf listPolicyGroup(Map map) {
		ByteBuf res = null;
		String page = (String) map.get("page");
		String limit = (String) map.get("limit");
		res = restfulEngineService.getpolicyGroupList(Integer.valueOf(page),
				Integer.valueOf(limit));
		return res;
	}

	/**
	 * 妫�绱㈣鍒欑粍
	 * 
	 * @param map
	 * @return
	 */
	public ByteBuf findPolicyGroup(Map map) {
		ByteBuf res = null;
		String limit = (String) map.get("limit");
		String policyGroupName = (String) map.get("policyGroupName");
		res = restfulEngineService.getPolicyGroupByName(policyGroupName,
				Integer.valueOf(limit));

		return res;
	}

	/**
	 * 鏂板瑙勫垯缁�
	 * 
	 * @param map
	 * @return
	 */
	public ByteBuf createPolicyGroup(Map map) {
		ByteBuf res = null;
		String policyGroup = (String) map.get("policyGroup");

		res = restfulEngineService.createPolicyGroup(policyGroup);

		return res;
	}

	/**
	 * 缂栬緫瑙勫垯缁�-鑾峰彇缂栬緫淇℃伅
	 * 
	 * @param map
	 * @return
	 */
	public ByteBuf findOnePolicyGroup(Map map) {
		ByteBuf res = null;
		String id = (String) map.get("id");
		res = restfulEngineService.getPolicyGroupById(Long.valueOf(id));
		return res;
	}

	/**
	 * 缂栬緫瑙勫垯缁�-鎻愪氦缂栬緫淇℃伅
	 * 
	 * @param map
	 * @return
	 */
	public ByteBuf editPolicyGroup(Map map) {
		ByteBuf res = null;
		String policyGroup = (String) map.get("policyGroup");

		res = restfulEngineService.editPolicyGroup(policyGroup);
		return res;
	}

	/**
	 * 鍒犻櫎瑙勫垯缁�
	 * 
	 * @param map
	 * @return
	 */
	public ByteBuf deletePolicyGroup(Map map) {
		ByteBuf res = null;
		String id = (String) map.get("id");

		res = restfulEngineService.deletePolicyGroup(Long.valueOf(id));
		return res;
	}

	// ************************************************************
	/**
	 * 瑙勫垯鍒楄〃
	 * 
	 * @param map
	 * @return
	 */
	public ByteBuf showPolicy(Map map) {
		ByteBuf res = null;
		String page = (String) map.get("page");
		String limit = (String) map.get("limit");
		String policyGroupId = (String) map.get("policyGroupId");
		res = restfulEngineService.getpolicyList(Integer.valueOf(page),
				Integer.valueOf(limit), policyGroupId);
		return res;
	}

	public ByteBuf findPolicy(Map map) {
		ByteBuf res = null;
		Object page = map.get("page");
		Object policyName = map.get("policyName");
		Object status = map.get("status");
		if (null == page) {
			Message error = new Message();
			error.setResultCode("400005");
			error.setResultMsg("page mistake");
			String jsonString = JSON.toJSONString(error);
			res = copiedBuffer(jsonString, CharsetUtil.UTF_8);
			return res;
		}
		if (null == policyName) {
			Message error = new Message();
			error.setResultCode("400010");
			error.setResultMsg("policyName mistake");
			String jsonString = JSON.toJSONString(error);
			res = copiedBuffer(jsonString, CharsetUtil.UTF_8);
			return res;
		}
		if (null == status) {
			Message error = new Message();
			error.setResultCode("400011");
			error.setResultMsg("status mistake");
			String jsonString = JSON.toJSONString(error);
			res = copiedBuffer(jsonString, CharsetUtil.UTF_8);
			return res;
		}
		return resq;
	}

	/**
	 * 鏂板瑙勫垯
	 * 
	 * @param map
	 * @return
	 */
	public ByteBuf createPolicy(Map map) {
		ByteBuf res = null;
		try {

			String policyGroupId = (String) map.get("policyGroupId");
			String policy = (String) map.get("policy");
			Long groupId = Long.valueOf(policyGroupId);
			res = restfulEngineService.createPolicy(policy, groupId);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 缂栬緫瑙勫垯-鑾峰彇缂栬緫淇℃伅
	 * 
	 * @param map
	 * @return
	 */
	public ByteBuf findOnePolicy(Map map) {
		ByteBuf res = null;
		String id = (String) map.get("id");

		res = restfulEngineService.getPolicyById(Long.valueOf(id));
		return res;
	}

	/**
	 * 缂栬緫瑙勫垯-鎻愪氦缂栬緫淇℃伅
	 * 
	 * @param map
	 * @return
	 */
	public ByteBuf editPolicy(Map map) {
		ByteBuf res = null;
		String policy = (String) map.get("policy");
		res = restfulEngineService.editPolicy(policy);
		return res;
	}

	/**
	 * 鍒犻櫎瑙勫垯
	 * 
	 * @param map
	 * @return
	 */
	public ByteBuf deletePolicy(Map map) {
		ByteBuf res = null;
		String id = (String) map.get("id");
		String token = (String) map.get("token");
		res = restfulEngineService.deletePolicy(Long.valueOf(id));
		return res;
	}

	public ByteBuf enablePolicy(Map map) {
		ByteBuf res = null;
		String id = (String) map.get("id");
		String token = (String) map.get("token");
		res = restfulEngineService.enablePolicy(Long.valueOf(id));

		return res;
	}

	public ByteBuf disablePolicy(Map map) {
		ByteBuf res = null;
		String id = (String) map.get("id");
		String token = (String) map.get("token");
		res = restfulEngineService.disablePolicy(Long.valueOf(id));
		return res;
	}

	// *****************************************************************************************************************

	public ByteBuf fetchPolicy(Map map) {
		ByteBuf res = null;
		String token = (String) map.get("token");
		String policyName = (String) map.get("policyName");
		res = restfulEngineService.fetchPolicy(policyName);
		return res;
	}

	/**
	 * 规则引擎短信模板检索
	 * @param map
	 * @return
	 */
	public ByteBuf fetchSmsTemplate(Map map) {
		ByteBuf res = null;
		String smsTemplateName = (String) map.get("smsTemplateName");
		String token = (String) map.get("token");
		res = restfulEngineService.fetchSmsTemplate(smsTemplateName);
		return res;
	}
	
	/**
	 * 规则引擎邮件模板检索
	 * @param map
	 * @return
	 */
	public ByteBuf fetchEmailTemplate(Map map) {
		ByteBuf res = null;
		String emailTemplateName = (String) map.get("emailTemplateName");
		String token = (String) map.get("token");
		
		res = restfulEngineService.fetchEmailTemplate(emailTemplateName, token);
		return res;
	}

	/**
	 * 规则引擎区域检索
	 * @param map
	 * @return
	 */
	public ByteBuf fetchArea(Map map) {
		ByteBuf res = null;
		String areaName = (String) map.get("areaName");
		String token = (String) map.get("token");
		res = restfulEngineService.fetchArea(areaName);
		return res;
	}

	/**
	 * 规则引擎监测站检索
	 * @param map
	 * @return
	 */
	public ByteBuf fetchMonitorStation(Map map) {
		ByteBuf res = null;
		String monitorStationName = (String) map.get("monitorStationName");
		String token = (String) map.get("token");
		res = restfulEngineService.fetchMonitorStation(monitorStationName);
		return res;
	}
	
	/**
	 * 规则引擎监测点检索
	 * @param map
	 * @return
	 */
	public ByteBuf fetchMonitorPoint(Map map) {
		ByteBuf res = null;
		String monitorPointName = (String) map.get("monitorPointName");
		String token = (String) map.get("token");
		res = restfulEngineService.fetchMonitorPoint(monitorPointName);
		return res;
	}
	
	/**
	 * 规则引擎恒温恒湿设备检索
	 * @param map
	 * @return
	 */
	public ByteBuf fetchConstantTh(Map map) {
		ByteBuf res = null;
		String constantThName = (String) map.get("constantThName");
		String token = (String) map.get("token");
		
		res = restfulEngineService.fetchConstantTh(constantThName, token);
		return res;
	}
	
	/**
	 * 规则引擎展柜检索
	 * @param map
	 * @return
	 */
	public ByteBuf fetchShowCase(Map map) {
		ByteBuf res = null;
		String showCaseName = (String) map.get("showCaseName");
		String token = (String) map.get("token");
		
		res = restfulEngineService.fetchShowCase(showCaseName, token);
		return res;
	}

	/**
	 * 
	 * @param map
	 *            http协议的请求参数
	 * @return
	 * 
	 *         activity_constant_thdata的分页查询
	 */
	public ByteBuf listMicroEnvironment(Map map) {
		ByteBuf res = null;
		String pageString = (String) map.get("page");
		String limitString = (String) map.get("limit");
		String token = (String) map.get("token");
		int page = Integer.parseInt(pageString);
		int limit = Integer.parseInt(limitString);
		res = restfulEngineService.listMicroEnvironment(page,limit);
		return res;
	}

	public ByteBuf findCurrentMicroEnvironment(Map map){
		ByteBuf res = null;
		String idString = (String) map.get("id");
		String token = (String) map.get("token");
		int id = Integer.parseInt(idString);
		res = restfulEngineService.findCurrentMicroEnvironment(id);
		return res;
	}
	
	
	
	
	public ByteBuf findSettingMicroEnvironment(Map map){
		ByteBuf res = null;
		String idString = (String) map.get("id");
		String token = (String) map.get("token");
		int id = Integer.parseInt(idString);
		res = restfulEngineService.findSettingMicroEnvironment(id);
		return res;
	}

	/**
	 * 
	 * @param map
	 *            http协议的请求参数
	 * @return
	 * 
	 *        devices
	 */

	public ByteBuf editMicroEnvironment(Map map) {
		return null;
	}

	/**
	 * 
	 * @param map
	 *            http协议的请求参数
	 * @return
	 * 
	 *        devices
	 */
	public ByteBuf findHistoryMicroEnvironment(Map map) {
		ByteBuf res = null;
		String idString = (String) map.get("id");
		String token = (String) map.get("token");
		int id = Integer.parseInt(idString);
		res = restfulEngineService.findHistoryMicroEnvironment(id);
		return res;
	}

	/**
	 * 处理运行模式
	 * @param map
	 * @return
	 */
	public ByteBuf processOperatingMode(Map map) {
		ByteBuf res = null;
		String idString = (String) map.get("id");
		String token = (String) map.get("token");
		String operator = (String) map.get("operator");
		
		res = restfulEngineService.processOperatingMode(idString, token, operator);
		return res;
	}
	
	/**
	 * 历史数据上报
	 * @param map
	 * @return
	 */
	public ByteBuf historyRecordReport(Map map) {
		ByteBuf res = null;
		String idString = (String) map.get("id");
		String token = (String) map.get("token");
		String operator = (String) map.get("operator");
		
		res = restfulEngineService.historyRecordReport(idString, token, operator);
		return res;
	}
	public ByteBuf engineUpdateSensorMacList(Map map) {
		ByteBuf res = null;
		String id = (String) map.get("id");
		String token = (String) map.get("token");
		
		res = restfulEngineService.engineUpdateSensorMacList(id, token);
		return res;
	}
	public ByteBuf engineUpdateNetTopologyList(Map map) {
		ByteBuf res = null;
		String id = (String) map.get("id");
		String token = (String) map.get("token");
		
		res = restfulEngineService.engineUpdateNetTopologyList(id, token);
		return res;
	}
}
