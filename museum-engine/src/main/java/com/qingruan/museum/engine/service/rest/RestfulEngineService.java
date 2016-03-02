package com.qingruan.museum.engine.service.rest;

import io.netty.buffer.ByteBuf;

public interface RestfulEngineService {
	/**
	 * 获取规则列表
	 * 
	 * @param page
	 * @param limit
	 * @return
	 */
	ByteBuf getpolicyGroupList(final Integer page, final Integer limit);

	/**
	 * 查找规则组
	 * 
	 * @param policyGroupName
	 * @param limit
	 * @return
	 */
	ByteBuf getPolicyGroupByName(final String policyGroupName,
			final Integer limit);

	/**
	 * 新增规则组
	 * 
	 * @param policyGroup
	 * @return
	 */
	ByteBuf createPolicyGroup(final String policyGroup);

	ByteBuf getPolicyGroupById(final Long id);

	ByteBuf editPolicyGroup(final String policyGroup);

	ByteBuf deletePolicyGroup(final Long id);

	/**
	 * 获取规则列表
	 * 
	 * @param page
	 * @param limit
	 * @return
	 */
	ByteBuf getpolicyList(final Integer page, final Integer limit,
			final String policyGroupId);

	/**
	 * 根据条件查询规则组
	 * 
	 * @param policyGroupName
	 * @param limit
	 * @return
	 */
	ByteBuf getPolicyGroupByNameAndStatusAndGroupId(final String policyName,
			final Integer limit, final Integer status, final Long policyGroupId);

	/**
	 * 创建规则
	 * 
	 * @param policyGroup
	 * @return
	 */
	ByteBuf createPolicy(final String policy, final Long policyGroupId);

	ByteBuf getPolicyById(final Long id);

	ByteBuf editPolicy(final String policy);

	ByteBuf deletePolicy(final Long id);

	ByteBuf enablePolicy(final Long id);

	ByteBuf disablePolicy(final Long id);

	ByteBuf fetchPolicy(final String policyName);

	ByteBuf fetchSmsTemplate(final String smsTemplateName);

	ByteBuf fetchEmailTemplate(final String emailTemplateName,
			final String token);

	ByteBuf fetchArea(final String areaName);

	ByteBuf fetchMonitorPoint(final String monitorPointName);

	ByteBuf fetchMonitorStation(final String monitorStationName);

	ByteBuf fetchConstantTh(final String constantThName, final String token);

	ByteBuf fetchShowCase(final String showCaseName, final String token);

	/*
	 * 
	 */

	ByteBuf listMicroEnvironment(final int page, final int limit);

	ByteBuf findCurrentMicroEnvironment(final long id);

	ByteBuf findSettingMicroEnvironment(final long id);

	ByteBuf editMicroEnvironment(final String Data);

	ByteBuf findHistoryMicroEnvironment(final int id);

	/**
	 * 修改运行模式
	 * 
	 * @param data
	 * @return
	 */
	ByteBuf processOperatingMode(String idString, String token, String operator);

	/**
	 * 历史数据上报
	 * 
	 * @param data
	 * @return
	 */
	ByteBuf historyRecordReport(String idString, String token, String operator);

	/**
	 * 修改运行模式
	 * 
	 * @param data
	 * @return
	 */
	ByteBuf engineUpdateSensorMacList(final String id, final String token);

	/**
	 * 历史数据上报
	 * 
	 * @param data
	 * @return
	 */
	ByteBuf engineUpdateNetTopologyList(final String id, final String token);

	/**
	 * 设置恒温恒湿机的温度和湿度
	 * 
	 * @param id
	 * @param token
	 * @return
	 */
	ByteBuf setConstantTh(String id, String presentValue, String topLimit,
			String lowerLimit, String fluctuationValue, String token,String monitorDataType);

}
