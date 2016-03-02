package com.qingruan.museum.api.service.rest.impl;

import static io.netty.buffer.Unpooled.copiedBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qingruan.museum.api.exception.ExceptionLog;
import com.qingruan.museum.api.service.rest.RestfulEngineService;
import com.qingruan.museum.api.service.rest.domain.EnvironmentBean;
import com.qingruan.museum.api.service.rest.domain.EnvironmentHistoryBean;
import com.qingruan.museum.api.service.rest.domain.EnvironmentHistoryMessage;
import com.qingruan.museum.api.service.rest.domain.EnvironmentListBean;
import com.qingruan.museum.api.service.rest.domain.EnvironmentListMessage;
import com.qingruan.museum.api.service.rest.domain.EnvironmentMessage;
import com.qingruan.museum.api.service.rest.domain.EnvironmentSettingBean;
import com.qingruan.museum.api.service.rest.domain.PolicyGroupListMsg;
import com.qingruan.museum.api.service.rest.domain.PolicyGroupMsg;
import com.qingruan.museum.api.service.rest.domain.PolicyGroupResponse;
import com.qingruan.museum.api.service.rest.domain.PolicyListMsg;
import com.qingruan.museum.api.service.rest.domain.PolicyMsg;
import com.qingruan.museum.api.service.rest.domain.PolicyResponse;
import com.qingruan.museum.api.service.rest.domain.ResponseBaseMsg;
import com.qingruan.museum.api.service.rest.fetch.Message;
import com.qingruan.museum.api.service.rest.fetch.MessageBase;
import com.qingruan.museum.dao.entity.Device;
import com.qingruan.museum.dao.entity.EmailTemplate;
import com.qingruan.museum.dao.entity.PolicyGroup;
import com.qingruan.museum.dao.entity.PolicyWrapper;
import com.qingruan.museum.dao.entity.RepoArea;
import com.qingruan.museum.dao.entity.SmsTemplate;
import com.qingruan.museum.dao.entity.record.ActivityConstantThData;
import com.qingruan.museum.dao.repository.DeviceDao;
import com.qingruan.museum.dao.repository.EmailTemplateDao;
import com.qingruan.museum.dao.repository.PolicyGroupDao;
import com.qingruan.museum.dao.repository.PolicyWrapperDao;
import com.qingruan.museum.dao.repository.RepoAreaDao;
import com.qingruan.museum.dao.repository.SmsTemplateDao;
import com.qingruan.museum.dao.repository.record.ActivityConstantThDataDao;
import com.qingruan.museum.domain.models.enums.DeviceType;
import com.qingruan.museum.domain.models.enums.MonitorDataType;
import com.qingruan.museum.exception.WebApiException;
import com.qingruan.museum.exception.enums.WebApiExceptionType;
import com.qingruan.museum.framework.esb.redispushpop.RedisMQPushSender;
import com.qingruan.museum.framework.jackson.JSONUtil;
import com.qingruan.museum.framework.util.PropertiesUtils;
import com.qingruan.museum.msg.DataContent;
import com.qingruan.museum.msg.MsgHeader;
import com.qingruan.museum.msg.MsgProperty;
import com.qingruan.museum.msg.MsgProperty.ApplicationType;
import com.qingruan.museum.msg.MsgProperty.CmdType;
import com.qingruan.museum.msg.MuseumMsg;
import com.qingruan.museum.msg.notification.DeviceDataMsg;
import com.qingruan.museum.msg.sensor.MtmNetInfo;
import com.qingruan.museum.msg.sensor.SensorData;
import com.qingruan.museum.msg.sensor.SensorHistoryRecord;
import com.qingruan.museum.msg.sensor.SensorHistoryRecord.HistoryReportMode;
import com.qingruan.museum.msg.sensor.SensorMsg;
import com.qingruan.museum.msg.sensor.SensorMsg.SensorAppType;
import com.qingruan.museum.msg.sensor.SensorOperatingMode;
import com.qingruan.museum.msg.sensor.SensorOperatingMode.OperatingMode;
import com.qingruan.museum.pma.model.Policy;

@Slf4j
@Service
//TODO:验证需要提成AOP,重复代码太多
public class RestfulEngineServiceImpl implements RestfulEngineService {
	@Autowired
	private PolicyGroupDao policyGroupDao;
	@Autowired
	private PolicyWrapperDao policyWrapperDao;
	@Autowired
	private SmsTemplateDao smsTemplateDao;
	@Autowired
	private EmailTemplateDao emailTemplateDao;
	@Autowired
	private RepoAreaDao repoAreaDao;
	@Autowired
	private DeviceDao deviceDao;
	@Autowired
	private ActivityConstantThDataDao activityConstantThDataDao;
	@Autowired
	private RedisMQPushSender redisMQPushSender;
	// TODO:
//	@Autowired
//	private SendSensorCmdService sendSensorCmdService;
	final static Integer SUCCESS = 2001;
	final static String SUCCESS_MSG = "success";
	final static Integer UNSUCCESS = 5001;
	final static String ERROR_MSG = "error";
	final static Integer ENABLE = 1;
	final static Integer DISABLE = 0;

	@Override
	public ByteBuf getpolicyGroupList(Integer page, Integer limit) {
		log.info("-----------------start getpolicyGroupList--------------------- ");
		ByteBuf content = null;
		PolicyGroupListMsg policyGroupListMsg = new PolicyGroupListMsg();
		try {
			if (page == null)
				throw new WebApiException(WebApiExceptionType.MISS_PAGE);
			else if (limit == null)
				throw new WebApiException(WebApiExceptionType.MISS_LIMIT);
			else {

				List<PolicyGroup> policyGroups = policyGroupDao.findAll();
				if (policyGroups == null || policyGroups.size() == 0)
					throw new WebApiException(
							WebApiExceptionType.POLICY_GROUP_NOT_EXIST_IN_DB);

				policyGroupListMsg.setResultCode(SUCCESS);
				policyGroupListMsg.setResultMsg(SUCCESS_MSG);
				for (PolicyGroup group : policyGroups) {
					PolicyGroupMsg policyGroupMsg = new PolicyGroupMsg();
					policyGroupMsg.setId(group.getId());
					policyGroupMsg.setGroupName(group.getGroupName());
					policyGroupMsg.setDesp(group.getDesp());
					policyGroupListMsg.getPolicyGroupList().add(policyGroupMsg);

				}

				content = copiedBuffer(JSONUtil.serialize(policyGroupListMsg),
						CharsetUtil.UTF_8);
			}

		} catch (WebApiException e) {
			policyGroupListMsg.setResultCode(e.getExceptionType().value());
			policyGroupListMsg.setResultMsg(e.getExceptionType().toString());
			content = copiedBuffer(JSONUtil.serialize(policyGroupListMsg),
					CharsetUtil.UTF_8);
			return content;

		}
		log.info("-----------------{end}---getpolicyGroupList--{content}-------is:------------ "
				+ content);
		return content;
	}

	@Override
	public ByteBuf getPolicyGroupByName(String policyGroupName, Integer limit) {
		log.info("-----------------start getPolicyGroupByName--------------------- ");
		ByteBuf content = null;
		PolicyGroupListMsg policyGroupListMsg = new PolicyGroupListMsg();
		try {
			if (StringUtils.isBlank(policyGroupName))
				throw new WebApiException(
						WebApiExceptionType.MISS_POLICY_GROUP_NAME);
			else if (limit == null)
				throw new WebApiException(WebApiExceptionType.MISS_LIMIT);
			else {

				List<PolicyGroup> policyGroups = policyGroupDao
						.findByGroupNameLike(policyGroupName);
				if (policyGroups == null || policyGroups.size() == 0)
					throw new WebApiException(
							WebApiExceptionType.POLICY_GROUP_NOT_EXIST_IN_DB);

				policyGroupListMsg.setResultCode(SUCCESS);
				policyGroupListMsg.setResultMsg(SUCCESS_MSG);
				for (PolicyGroup group : policyGroups) {
					PolicyGroupMsg policyGroupMsg = new PolicyGroupMsg();

					policyGroupMsg.setId(group.getId());
					policyGroupMsg.setGroupName(group.getGroupName());
					policyGroupMsg.setDesp(group.getDesp());
					policyGroupListMsg.getPolicyGroupList().add(policyGroupMsg);

				}

				content = copiedBuffer(JSONUtil.serialize(policyGroupListMsg),
						CharsetUtil.UTF_8);
			}

		} catch (WebApiException e) {
			policyGroupListMsg.setResultCode(e.getExceptionType().value());
			policyGroupListMsg.setResultMsg(e.getExceptionType().toString());
			content = copiedBuffer(JSONUtil.serialize(policyGroupListMsg),
					CharsetUtil.UTF_8);
			return content;

		}
		log.info("-----------------{end}---getPolicyGroupByName--{content}-------is:------------ "
				+ content);
		return content;
	}

	@Override
	public ByteBuf createPolicyGroup(String policyGroup) {
		log.info("-----------------start createPolicyGroup--------------------- ");
		ByteBuf content = null;
		PolicyGroupListMsg policyGroupListMsg = new PolicyGroupListMsg();
		try {
			if (StringUtils.isBlank(policyGroup))
				throw new WebApiException(WebApiExceptionType.MISS_POLICY_GROUP);

			else {
				try {
					PolicyGroupMsg policyGroupMsg = JSONUtil.deserialize(
							policyGroup, PolicyGroupMsg.class);
					if (policyGroupMsg == null)
						throw new WebApiException(
								WebApiExceptionType.JSON_DESERIALIZE_ERROR);
					else {
						if (StringUtils.isBlank(policyGroupMsg.getGroupName())
								|| StringUtils
										.isBlank(policyGroupMsg.getDesp()))

							throw new WebApiException(
									WebApiExceptionType.POLICY_GROUP_PARAMETER_ERROR);

						PolicyGroup entity = new PolicyGroup();
						entity.setGroupName(policyGroupMsg.getGroupName());
						entity.setDesp(policyGroupMsg.getDesp());
						policyGroupDao.save(entity);
						ResponseBaseMsg responseBaseMsg = new ResponseBaseMsg();
						responseBaseMsg.setResultCode(SUCCESS);
						responseBaseMsg.setResultMsg(SUCCESS_MSG);
						content = copiedBuffer(
								JSONUtil.serialize(responseBaseMsg),
								CharsetUtil.UTF_8);

					}
				} catch (Exception e) {
					throw new WebApiException(
							WebApiExceptionType.JSON_DESERIALIZE_ERROR);
				}

			}

		} catch (WebApiException e) {
			policyGroupListMsg.setResultCode(e.getExceptionType().value());
			policyGroupListMsg.setResultMsg(e.getExceptionType().toString());
			content = copiedBuffer(JSONUtil.serialize(policyGroupListMsg),
					CharsetUtil.UTF_8);
			return content;

		}
		log.info("-----------------{end}---createPolicyGroup--{content}-------is:------------ "
				+ content);
		return content;
	}

	@Override
	public ByteBuf getPolicyGroupById(Long id) {
		log.info("-----------------start getPolicyGroupById--------------------- ");
		ByteBuf content = null;
		PolicyGroupResponse policyGroupResponse = new PolicyGroupResponse();
		try {
			if (id == null)
				throw new WebApiException(
						WebApiExceptionType.MISS_POLICY_GROUP_ID);

			else {
				PolicyGroup policyGroup = policyGroupDao.findOne(id);
				if (policyGroup == null)
					throw new WebApiException(
							WebApiExceptionType.POLICY_GROUP_NOT_EXIST_IN_DB);

				policyGroupResponse.setResultCode(SUCCESS);
				policyGroupResponse.setResultMsg(SUCCESS_MSG);
				PolicyGroupMsg policyGroupMsg = new PolicyGroupMsg();
				policyGroupMsg.setId(policyGroup.getId());
				policyGroupMsg.setGroupName(policyGroup.getGroupName());
				policyGroupMsg.setDesp(policyGroup.getDesp());
				policyGroupResponse.setPolicyGroupMsg(policyGroupMsg);

				content = copiedBuffer(JSONUtil.serialize(policyGroupResponse),
						CharsetUtil.UTF_8);
			}

		} catch (WebApiException e) {
			policyGroupResponse.setResultCode(e.getExceptionType().value());
			policyGroupResponse.setResultMsg(e.getExceptionType().toString());
			content = copiedBuffer(JSONUtil.serialize(policyGroupResponse),
					CharsetUtil.UTF_8);
			return content;

		}
		log.info("-----------------{end}---getPolicyGroupById--{content}-------is:------------ "
				+ content);
		return content;
	}

	@Override
	public ByteBuf editPolicyGroup(String policyGroup) {
		log.info("-----------------start editPolicyGroup--------------------- ");
		ByteBuf content = null;
		ResponseBaseMsg responseBaseMsg = new ResponseBaseMsg();
		try {
			if (StringUtils.isBlank(policyGroup))
				throw new WebApiException(WebApiExceptionType.MISS_POLICY_GROUP);

			else {
				try {
					PolicyGroupMsg policyGroupMsg = JSONUtil.deserialize(
							policyGroup, PolicyGroupMsg.class);
					if (policyGroupMsg == null)
						throw new WebApiException(
								WebApiExceptionType.JSON_DESERIALIZE_ERROR);
					else {
						if (StringUtils.isBlank(policyGroupMsg.getGroupName())
								|| StringUtils
										.isBlank(policyGroupMsg.getDesp())
								|| policyGroupMsg.getId() == null)

							throw new WebApiException(
									WebApiExceptionType.POLICY_GROUP_PARAMETER_ERROR);
						try {
							policyGroupDao.updatePolicyGroupById(
									policyGroupMsg.getGroupName(),
									policyGroupMsg.getDesp(),
									policyGroupMsg.getId());
						} catch (Exception e) {
							log.error(ExceptionLog.getErrorStack(e));
							throw new WebApiException(
									WebApiExceptionType.OPERATION_OF_DATABASE_ERROR);
						}

						responseBaseMsg.setResultCode(SUCCESS);
						responseBaseMsg.setResultMsg(SUCCESS_MSG);

						content = copiedBuffer(
								JSONUtil.serialize(responseBaseMsg),
								CharsetUtil.UTF_8);

					}
				} catch (Exception e) {
					e.printStackTrace();
					throw new WebApiException(
							WebApiExceptionType.JSON_DESERIALIZE_ERROR);
				}

			}

		} catch (WebApiException e) {
			responseBaseMsg.setResultCode(e.getExceptionType().value());
			responseBaseMsg.setResultMsg(e.getExceptionType().toString());
			content = copiedBuffer(JSONUtil.serialize(responseBaseMsg),
					CharsetUtil.UTF_8);
			return content;

		}
		log.info("-----------------{end}---editPolicyGroup--{content}-------is:------------ "
				+ content);
		return content;
	}

	@Override
	public ByteBuf deletePolicyGroup(Long id) {
		log.info("-----------------start deletePolicyGroup--------------------- ");
		ByteBuf content = null;
		ResponseBaseMsg responseBaseMsg = new ResponseBaseMsg();
		try {
			if (id == null)
				throw new WebApiException(
						WebApiExceptionType.MISS_POLICY_GROUP_ID);

			else {
				try {
					policyGroupDao.delete(id);
					responseBaseMsg.setResultCode(SUCCESS);
					responseBaseMsg.setResultMsg(SUCCESS_MSG);

					content = copiedBuffer(JSONUtil.serialize(responseBaseMsg),
							CharsetUtil.UTF_8);
				} catch (Exception e) {
					throw new WebApiException(
							WebApiExceptionType.OPERATION_OF_DATABASE_ERROR);
				}

			}

		} catch (WebApiException e) {
			responseBaseMsg.setResultCode(e.getExceptionType().value());
			responseBaseMsg.setResultMsg(e.getExceptionType().toString());
			content = copiedBuffer(JSONUtil.serialize(responseBaseMsg),
					CharsetUtil.UTF_8);
			return content;

		}
		log.info("-----------------{end}---deletePolicyGroup--{content}-------is:------------ "
				+ content);
		return content;
	}

	@Override
	public ByteBuf getpolicyList(Integer page, Integer limit,
			String policyGroupId) {
		log.info("-----------------start getpolicyList--------------------- ");
		ByteBuf content = null;
		PolicyListMsg policyListMsg = new PolicyListMsg();
		try {
			if (page == null)
				throw new WebApiException(WebApiExceptionType.MISS_PAGE);
			else if (limit == null)
				throw new WebApiException(WebApiExceptionType.MISS_LIMIT);
			else if (StringUtils.isBlank(policyGroupId))
				throw new WebApiException(
						WebApiExceptionType.MISS_POLICY_GROUP_ID);
			else {
				Long id = Long.valueOf(policyGroupId);
				PolicyGroup policyGroup = policyGroupDao.findOne(id);
				if (policyGroup == null)
					throw new WebApiException(
							WebApiExceptionType.POLICY_GROUP_NOT_EXIST_IN_DB);

				policyListMsg.setResultCode(SUCCESS);
				policyListMsg.setResultMsg(SUCCESS_MSG);
				PolicyGroupMsg policyGroupMsg = new PolicyGroupMsg();
				policyGroupMsg.setId(policyGroup.getId());
				policyGroupMsg.setGroupName(policyGroup.getGroupName());
				policyGroupMsg.setDesp(policyGroup.getDesp());
				List<PolicyMsg> policies = new ArrayList<PolicyMsg>();

				for (PolicyWrapper policyWrapper : policyGroup.getPolicies()) {

					String policyContent = policyWrapper.getPolicyContent();
					if (StringUtils.isNoneBlank(policyContent)) {
						Policy policy = null;
						try {
							policy = JSONUtil.deserialize(policyContent,
									Policy.class);

						} catch (Exception e) {
							log.error("--------{error JSONUtil.deserialize(policyContent,Policy.class)}----------------");
							log.error(ExceptionLog.getErrorStack(e));
							throw new WebApiException(
									WebApiExceptionType.JSON_DESERIALIZE_ERROR);

						}
						if (policy != null) {
							if (policy.getId() == null)
								policy.setId(policyWrapper.getId());
							PolicyMsg policyMsg = new PolicyMsg();
							policyMsg.setId(policyWrapper.getId());
							policyMsg.setName(policy.getPolicyName());
							policyMsg.setDesp(policyWrapper.getDesp());
							policyMsg.setSalience(policy.getSalience()
									.toString());
							policyMsg.setContent(policy);

							policyMsg.setStatus(policyWrapper.getStatus());
							policies.add(policyMsg);
						}

					}

				}

				policyGroupMsg.setPolicies(policies);
				policyListMsg.setPolicyGroupMsg(policyGroupMsg);

				content = copiedBuffer(JSONUtil.serialize(policyListMsg),
						CharsetUtil.UTF_8);
			}

		} catch (WebApiException e) {
			policyListMsg.setResultCode(e.getExceptionType().value());
			policyListMsg.setResultMsg(e.getExceptionType().toString());
			content = copiedBuffer(JSONUtil.serialize(policyListMsg),
					CharsetUtil.UTF_8);
			return content;

		}
		log.info("-----------------{end}---getpolicyList--{content}-------is:------------ "
				+ content);
		return content;
	}

	@Override
	public ByteBuf getPolicyGroupByNameAndStatusAndGroupId(String policyName,
			Integer limit, Integer status, Long policyGroupId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ByteBuf createPolicy(String policy, Long policyGroupId) {
		log.info("-----------------start createPolicy--------------------- ");
		ByteBuf content = null;
		ResponseBaseMsg responseBaseMsg = new ResponseBaseMsg();
		try {

			if (policyGroupId == null)
				throw new WebApiException(
						WebApiExceptionType.MISS_POLICY_GROUP_ID);
			else if (StringUtils.isBlank(policy))
				throw new WebApiException(WebApiExceptionType.MISS_POLICY);
			else {

				PolicyGroup policyGroup = policyGroupDao.findOne(policyGroupId);
				if (policyGroup == null)
					throw new WebApiException(
							WebApiExceptionType.POLICY_GROUP_NOT_EXIST_IN_DB);
				try {
					PolicyMsg policyMsg = JSONUtil.deserialize(policy,
							PolicyMsg.class);
					if (policyMsg.getContent() == null)
						throw new WebApiException(
								WebApiExceptionType.MISS_POLICY_CONTENT);
					Policy p = policyMsg.getContent();
					PolicyWrapper policyWrapper = new PolicyWrapper();
					policyWrapper.setPolicyGroupId(policyGroupId);
					policyWrapper.setDesp(p.getDesp());
					policyWrapper.setStatus(0);
					policyWrapper.setPolicyContent(JSONUtil.serialize(p));
					policyWrapperDao.save(policyWrapper);
					responseBaseMsg.setResultCode(SUCCESS);
					responseBaseMsg.setResultMsg(SUCCESS_MSG);
				} catch (Exception e) {
					log.error(ExceptionLog.getErrorStack(e));

					throw new WebApiException(
							WebApiExceptionType.JSON_DESERIALIZE_ERROR);
				}

				content = copiedBuffer(JSONUtil.serialize(responseBaseMsg),
						CharsetUtil.UTF_8);
			}

		} catch (WebApiException e) {
			responseBaseMsg.setResultCode(e.getExceptionType().value());
			responseBaseMsg.setResultMsg(e.getExceptionType().toString());
			content = copiedBuffer(JSONUtil.serialize(responseBaseMsg),
					CharsetUtil.UTF_8);
			return content;

		}
		log.info("-----------------{end}---createPolicy--{content}-------is:------------ "
				+ content);
		return content;
	}

	@Override
	public ByteBuf getPolicyById(Long id) {

		log.info("-----------------start getPolicyById--------------------- ");
		ByteBuf content = null;
		PolicyResponse policyResponse = new PolicyResponse();
		try {

			if (id == null) {
				log.info("-----------------[bs]---{miss parameter of policy id}----------|瑙勫垯鐨処D缂哄け锛屾姏鍑哄紓甯竱----------- ");
				throw new WebApiException(WebApiExceptionType.MISS_POLICY_ID);
			}

			else {

				PolicyWrapper policyWrapper = policyWrapperDao.findOne(id);
				if (policyWrapper == null) {
					log.info("-----------------[bs]---{policy not exist in database}----------|鏁版嵁搴撲腑涓嶅瓨鍦ㄨ瑙勫垯|-----------id is: "
							+ id);
					throw new WebApiException(
							WebApiExceptionType.POLICY_GROUP_NOT_EXIST_IN_DB);
				}

				try {
					Policy policy = JSONUtil.deserialize(
							policyWrapper.getPolicyContent(), Policy.class);

					PolicyMsg policyMsg = new PolicyMsg();
					policyMsg.setContent(policy);
					policyMsg.setId(id);
					policyMsg.setName(policy.getPolicyName());
					policyMsg.setDesp(policy.getDesp());
					policyMsg.setSalience(policy.getSalience().toString());
					policyResponse.setPolicyMsg(policyMsg);
					policyResponse.setResultCode(SUCCESS);
					policyResponse.setResultMsg(SUCCESS_MSG);

				} catch (Exception e) {
					log.error("-----------------[bs]---{json deserialize error}----------|JSON鍙嶅簭鍒楀寲閿欒|-----------");

					log.error(ExceptionLog.getErrorStack(e));
					throw new WebApiException(
							WebApiExceptionType.JSON_DESERIALIZE_ERROR);
				}

				content = copiedBuffer(JSONUtil.serialize(policyResponse),
						CharsetUtil.UTF_8);
			}

		} catch (WebApiException e) {
			policyResponse.setResultCode(e.getExceptionType().value());
			policyResponse.setResultMsg(e.getExceptionType().toString());
			content = copiedBuffer(JSONUtil.serialize(policyResponse),
					CharsetUtil.UTF_8);
			return content;

		}
		log.info("-----------------{end}---getPolicyById--{content}-------is:------------ "
				+ content);
		return content;

	}

	@Override
	public ByteBuf editPolicy(String policy) {

		log.info("-----------------start editPolicy--------------------- ");
		ByteBuf content = null;
		ResponseBaseMsg responseBaseMsg = new ResponseBaseMsg();
		try {

			if (StringUtils.isBlank(policy)) {
				log.info("-----------------[bs]---{miss parameter of policy}----------|瑙勫垯缂哄け锛屾姏鍑哄紓甯竱----------- ");
				throw new WebApiException(WebApiExceptionType.MISS_POLICY);
			}

			else {
				try {
					PolicyMsg policyMsg = JSONUtil.deserialize(policy,
							PolicyMsg.class);
					String policyContent = JSONUtil.serialize(policyMsg
							.getContent());

					// policyWrapperDao.updatePolicyById(policyContent,
					// policyMsg.getDesp(), policyMsg.getId());
					this.updatePolicyById(policyContent, policyMsg.getDesp(),
							policyMsg.getId());

					responseBaseMsg.setResultCode(SUCCESS);
					responseBaseMsg.setResultMsg(SUCCESS_MSG);

				} catch (Exception e) {
					log.error("-----------------[bs]---{json deserialize error}----------|JSON鍙嶅簭鍒楀寲閿欒|-----------");
					log.error(ExceptionLog.getErrorStack(e));
					throw new WebApiException(
							WebApiExceptionType.JSON_DESERIALIZE_ERROR);
				}

				content = copiedBuffer(JSONUtil.serialize(responseBaseMsg),
						CharsetUtil.UTF_8);
			}

		} catch (WebApiException e) {
			responseBaseMsg.setResultCode(e.getExceptionType().value());
			responseBaseMsg.setResultMsg(e.getExceptionType().toString());
			content = copiedBuffer(JSONUtil.serialize(responseBaseMsg),
					CharsetUtil.UTF_8);
			return content;

		}
		log.info("-----------------{end}---editPolicy--{content}-------is:------------ "
				+ content);
		return content;

	}

	@Override
	public ByteBuf deletePolicy(Long id) {

		log.info("-----------------start deletePolicy--------------------- ");
		ByteBuf content = null;
		ResponseBaseMsg responseBaseMsg = new ResponseBaseMsg();
		try {

			if (id == null) {
				log.info("-----------------[bs]---{miss parameter of policy id}----------|瑙勫垯ID缂哄け锛屾姏鍑哄紓甯竱----------- ");
				throw new WebApiException(WebApiExceptionType.MISS_POLICY_ID);
			}

			else {
				try {

					this.deletePolicyById(id);

					responseBaseMsg.setResultCode(SUCCESS);
					responseBaseMsg.setResultMsg(SUCCESS_MSG);

				} catch (Exception e) {
					log.error("-----------------[bs]---{json deserialize error}----------|JSON鍙嶅簭鍒楀寲閿欒|-----------");
					log.error(ExceptionLog.getErrorStack(e));
					throw new WebApiException(
							WebApiExceptionType.OPERATION_OF_DATABASE_ERROR);
				}

				content = copiedBuffer(JSONUtil.serialize(responseBaseMsg),
						CharsetUtil.UTF_8);
			}

		} catch (WebApiException e) {
			responseBaseMsg.setResultCode(e.getExceptionType().value());
			responseBaseMsg.setResultMsg(e.getExceptionType().toString());
			content = copiedBuffer(JSONUtil.serialize(responseBaseMsg),
					CharsetUtil.UTF_8);
			return content;

		}
		log.info("-----------------{end}---deletePolicy--{content}-------is:------------ "
				+ content);
		return content;

	}

	@Override
	public ByteBuf enablePolicy(Long id) {

		log.info("-----------------start enablePolicy--------------------- ");
		ByteBuf content = null;
		ResponseBaseMsg responseBaseMsg = new ResponseBaseMsg();
		try {

			if (id == null) {
				log.info("-----------------[bs]---{miss parameter of policy id}----------|瑙勫垯ID缂哄け锛屾姏鍑哄紓甯竱----------- ");
				throw new WebApiException(WebApiExceptionType.MISS_POLICY_ID);
			}

			else {
				try {

					this.enablePolicyById(id, ENABLE);

					responseBaseMsg.setResultCode(SUCCESS);
					responseBaseMsg.setResultMsg(SUCCESS_MSG);

				} catch (Exception e) {
					log.error("-----------------[bs]---{enable policy status error}----------|婵�娲昏鍒欑姸鎬侀敊璇瘄-----------");
					log.error(ExceptionLog.getErrorStack(e));
					throw new WebApiException(
							WebApiExceptionType.OPERATION_OF_DATABASE_ERROR);
				}

				content = copiedBuffer(JSONUtil.serialize(responseBaseMsg),
						CharsetUtil.UTF_8);
			}

		} catch (WebApiException e) {
			responseBaseMsg.setResultCode(e.getExceptionType().value());
			responseBaseMsg.setResultMsg(e.getExceptionType().toString());
			content = copiedBuffer(JSONUtil.serialize(responseBaseMsg),
					CharsetUtil.UTF_8);
			return content;

		}
		log.info("-----------------{end}---enablePolicy--{content}-------is:------------ "
				+ content);
		return content;

	}

	@Override
	public ByteBuf disablePolicy(Long id) {

		log.info("-----------------start disablePolicy--------------------- ");
		ByteBuf content = null;
		ResponseBaseMsg responseBaseMsg = new ResponseBaseMsg();
		try {

			if (id == null) {
				log.info("-----------------[bs]---{miss parameter of policy id}----------|瑙勫垯ID缂哄け锛屾姏鍑哄紓甯竱----------- ");
				throw new WebApiException(WebApiExceptionType.MISS_POLICY_ID);
			}

			else {
				try {

					this.disablePolicyById(id, DISABLE);

					responseBaseMsg.setResultCode(SUCCESS);
					responseBaseMsg.setResultMsg(SUCCESS_MSG);

				} catch (Exception e) {
					log.error("-----------------[bs]---{disable policy error}----------|鍘绘縺娲昏鍒欑姸鎬侀敊璇瘄-----------");
					log.error(ExceptionLog.getErrorStack(e));
					throw new WebApiException(
							WebApiExceptionType.OPERATION_OF_DATABASE_ERROR);
				}

				content = copiedBuffer(JSONUtil.serialize(responseBaseMsg),
						CharsetUtil.UTF_8);
			}

		} catch (WebApiException e) {
			responseBaseMsg.setResultCode(e.getExceptionType().value());
			responseBaseMsg.setResultMsg(e.getExceptionType().toString());
			content = copiedBuffer(JSONUtil.serialize(responseBaseMsg),
					CharsetUtil.UTF_8);
			return content;

		}
		log.info("-----------------{end}---disablePolicy--{content}-------is:------------ "
				+ content);
		return content;

	}

	// ***********************************************************************************************************************
	@Override
	public ByteBuf fetchPolicy(String policyName) {
		log.info("-----------------start fetchPolicy--------------------- ");
		ByteBuf content = null;
		MessageBase messageBase = new MessageBase();
		try {

			if (StringUtils.isBlank(policyName)) {
				log.info("-----------------[bs]---{miss parameter of policy Name}----------|规则名字错误  缺失----------- ");
				throw new WebApiException(WebApiExceptionType.MISS_POLICY_NAME);
			}

			else {
				try {

					List<PolicyWrapper> fetchPolicyByName = this
							.fetchPolicyByName(policyName);
					List<Message> listData = new ArrayList<Message>();
					for (int i = 0; i < fetchPolicyByName.size(); i++) {
						Message message = new Message();
						PolicyWrapper policyWrapper = fetchPolicyByName.get(i);
						message.setId(policyWrapper.getId());
						message.setName(policyWrapper.getPolicyContent());
						listData.add(message);

					}
					messageBase.setResultCode(SUCCESS);
					messageBase.setResultMsg(SUCCESS_MSG);
					messageBase.setListDatas(listData);

				} catch (Exception e) {
					log.error("-----------------[bs]---{fetch Policy error}----------|鍘绘縺娲昏鍒欑姸鎬侀敊璇瘄-----------");
					log.error(ExceptionLog.getErrorStack(e));
					throw new WebApiException(
							WebApiExceptionType.OPERATION_OF_DATABASE_ERROR);
				}

				content = copiedBuffer(JSONUtil.serialize(messageBase),
						CharsetUtil.UTF_8);
			}

		} catch (WebApiException e) {
			messageBase.setResultCode(e.getExceptionType().value());
			messageBase.setResultMsg(e.getExceptionType().toString());
			content = copiedBuffer(JSONUtil.serialize(messageBase),
					CharsetUtil.UTF_8);
			return content;

		}
		log.info("-----------------{end}---fetchPolicy--{content}-------is:------------ "
				+ content);
		return content;
	}

	@Override
	public ByteBuf fetchSmsTemplate(String smsTemplateName) {

		log.info("-----------------start fetchPolicy--------------------- ");
		ByteBuf content = null;
		MessageBase messageBase = new MessageBase();
		try {

			if (StringUtils.isBlank(smsTemplateName)) {
				log.info("-----------------[bs]---{miss parameter of smsTemplateName}----------|短信模板错误  缺失----------- ");
				throw new WebApiException(
						WebApiExceptionType.MISS_SMSTEMPLATE_Name);
			}

			else {
				try {

					List<SmsTemplate> smsTemplateByName = this
							.fetchSmsTemplateByName(smsTemplateName);

					List<Message> listData = new ArrayList<Message>();
					for (int i = 0; i < smsTemplateByName.size(); i++) {
						Message message = new Message();
						SmsTemplate smsTemplate = smsTemplateByName.get(i);
						message.setId(smsTemplate.getId());
						message.setName(smsTemplate.getName());
						listData.add(message);

					}
					messageBase.setResultCode(SUCCESS);
					messageBase.setResultMsg(SUCCESS_MSG);
					messageBase.setListDatas(listData);

				} catch (Exception e) {
					log.error("-----------------[bs]---{fetch  SmsTemplate error}----------|短信模板错误-----------");
					log.error(ExceptionLog.getErrorStack(e));
					throw new WebApiException(
							WebApiExceptionType.OPERATION_OF_DATABASE_ERROR);
				}

				content = copiedBuffer(JSONUtil.serialize(messageBase),
						CharsetUtil.UTF_8);
			}

		} catch (WebApiException e) {
			messageBase.setResultCode(e.getExceptionType().value());
			messageBase.setResultMsg(e.getExceptionType().toString());
			content = copiedBuffer(JSONUtil.serialize(messageBase),
					CharsetUtil.UTF_8);
			return content;

		}
		log.info("-----------------{end}---fetchPolicy--{content}-------is:------------ "
				+ content);
		return content;

	}

	@Override
	public ByteBuf fetchArea(String areaName) {
		log.info("-----------------start fetchArea--------------------- ");
		ByteBuf content = null;
		MessageBase messageBase = new MessageBase();
		try {

			if (StringUtils.isBlank(areaName)) {
				log.info("-----------------[bs]---{miss parameter of areaName}----------|地域名字错误  缺失----------- ");
				throw new WebApiException(WebApiExceptionType.MISS_AREA_Name);
			}

			else {
				try {

					List<RepoArea> fetchAreaByName = this
							.fetchAreaByName(areaName);

					List<Message> listData = new ArrayList<Message>();
					for (int i = 0; i < fetchAreaByName.size(); i++) {
						Message message = new Message();
						RepoArea repoArea = fetchAreaByName.get(i);
						message.setId(repoArea.getId());
						message.setName(repoArea.getName());
						listData.add(message);

					}
					messageBase.setResultCode(SUCCESS);
					messageBase.setResultMsg(SUCCESS_MSG);
					messageBase.setListDatas(listData);

				} catch (Exception e) {
					log.error("-----------------[bs]---{fetch  areaName error}----------|地域名字错误-----------");
					log.error(ExceptionLog.getErrorStack(e));
					throw new WebApiException(
							WebApiExceptionType.OPERATION_OF_DATABASE_ERROR);
				}

				content = copiedBuffer(JSONUtil.serialize(messageBase),
						CharsetUtil.UTF_8);
			}

		} catch (WebApiException e) {
			messageBase.setResultCode(e.getExceptionType().value());
			messageBase.setResultMsg(e.getExceptionType().toString());
			content = copiedBuffer(JSONUtil.serialize(messageBase),
					CharsetUtil.UTF_8);
			return content;

		}
		log.info("-----------------{end}---fetchPolicy--{content}-------is:------------ "
				+ content);
		return content;
	}

	@Override
	public ByteBuf fetchMonitorPoint(String monitorPointName) {
		log.info("-----------------start fetchMonitorPoint--------------------- ");
		ByteBuf content = null;
		MessageBase messageBase = new MessageBase();
		try {

			if (StringUtils.isBlank(monitorPointName)) {
				log.info("-----------------[bs]---{miss parameter of monitorPointName}----------|监控点名字错误  缺失----------- ");
				throw new WebApiException(
						WebApiExceptionType.MISS_MONITOR_POINT_Name);
			}

			else {
				try {

					List<Device> fetchMonitorPointByName = this
							.fetchMonitorPointByName(monitorPointName);

					List<Message> listData = new ArrayList<Message>();
					for (int i = 0; i < fetchMonitorPointByName.size(); i++) {
						Message message = new Message();
						Device device = fetchMonitorPointByName.get(i);
						message.setId(device.getId());
						message.setName(device.getName());
						listData.add(message);

					}
					messageBase.setResultCode(SUCCESS);
					messageBase.setResultMsg(SUCCESS_MSG);
					messageBase.setListDatas(listData);

				} catch (Exception e) {
					log.error("-----------------[bs]---{fetch  monitorPointName error}----------|监控点名字错误-----------");
					log.error(ExceptionLog.getErrorStack(e));
					throw new WebApiException(
							WebApiExceptionType.OPERATION_OF_DATABASE_ERROR);
				}

				content = copiedBuffer(JSONUtil.serialize(messageBase),
						CharsetUtil.UTF_8);
			}

		} catch (WebApiException e) {
			messageBase.setResultCode(e.getExceptionType().value());
			messageBase.setResultMsg(e.getExceptionType().toString());
			content = copiedBuffer(JSONUtil.serialize(messageBase),
					CharsetUtil.UTF_8);
			return content;

		}
		log.info("-----------------{end}---fetchMonitorPoint--{content}-------is:------------ "
				+ content);
		return content;
	}

	@Override
	public ByteBuf fetchMonitorStation(String monitorStationName) {
		log.info("-----------------start fetchMonitorStation--------------------- ");
		ByteBuf content = null;
		MessageBase messageBase = new MessageBase();
		try {

			if (StringUtils.isBlank(monitorStationName)) {
				log.info("-----------------[bs]---{miss parameter of monitorStationName}----------|监控站名字错误  缺失----------- ");
				throw new WebApiException(
						WebApiExceptionType.MISS_MONITOR_STATION_Name);
			}

			else {
				try {

					List<Device> fetchMonitorStationtByName = this
							.fetchMonitorStationtByName(monitorStationName);

					List<Message> listData = new ArrayList<Message>();
					for (int i = 0; i < fetchMonitorStationtByName.size(); i++) {
						Message message = new Message();
						Device device = fetchMonitorStationtByName.get(i);
						message.setId(device.getId());
						message.setName(device.getName());
						listData.add(message);

					}
					messageBase.setResultCode(SUCCESS);
					messageBase.setResultMsg(SUCCESS_MSG);
					messageBase.setListDatas(listData);

				} catch (Exception e) {
					log.error("-----------------[bs]---{fetch  monitorStationName error}----------|监控站名字错误-----------");
					log.error(ExceptionLog.getErrorStack(e));
					throw new WebApiException(
							WebApiExceptionType.OPERATION_OF_DATABASE_ERROR);
				}

				content = copiedBuffer(JSONUtil.serialize(messageBase),
						CharsetUtil.UTF_8);
			}

		} catch (WebApiException e) {
			messageBase.setResultCode(e.getExceptionType().value());
			messageBase.setResultMsg(e.getExceptionType().toString());
			content = copiedBuffer(JSONUtil.serialize(messageBase),
					CharsetUtil.UTF_8);
			return content;

		}
		log.info("-----------------{end}---monitorStationName--{content}-------is:------------ "
				+ content);
		return content;
	}
	
	@Override
	public ByteBuf fetchConstantTh(String constantThName, String token) {
		log.info("开始检索恒温恒湿设备");
		ByteBuf content = null;
		MessageBase messageBase = new MessageBase();
		
		try {
			String restfulToken = PropertiesUtils.readValue("restful.token");
			
			if (StringUtils.isBlank(constantThName)) {
				log.info("设定参数错误 constantThName");
				throw new WebApiException(WebApiExceptionType.MISS_CONSTANT_TH_NAME);
			}
			
			if(token == null || !token.equals(restfulToken)) {
				log.info("设定参数错误 token");
				throw new WebApiException(WebApiExceptionType.TOKEN_ERROR);
			}

			List<Device> fetchConstantThByName = this.fetchConstantThByName(constantThName);

			List<Message> listData = new ArrayList<Message>();
			
			for (int i = 0; i < fetchConstantThByName.size(); i++) {
				Device device = fetchConstantThByName.get(i);
				Message message = new Message();
				message.setId(device.getId());
				message.setName(device.getName());
				listData.add(message);
			}
			messageBase.setResultCode(SUCCESS);
			messageBase.setResultMsg(SUCCESS_MSG);
			messageBase.setListDatas(listData);

		} catch (WebApiException e) {
			log.error("处理运行状态异常：" + ExceptionLog.getErrorStack(e));
			messageBase.setResultCode(e.getExceptionType().value());
			messageBase.setResultMsg(e.getExceptionType().toString());

		} catch (Exception e) {
			log.error("处理运行状态异常：" + ExceptionLog.getErrorStack(e));
			messageBase.setResultCode(UNSUCCESS);
			messageBase.setResultMsg(ERROR_MSG);
		} finally {
			content = copiedBuffer(JSONUtil.serialize(messageBase), CharsetUtil.UTF_8);
		}
		
		log.info("检索恒温恒湿设备结束 content is '{}'", content);
		return content;
	}
	
	@Override
	public ByteBuf fetchShowCase(String showCaseName, String token) {
		log.info("开始检索展柜");
		ByteBuf content = null;
		MessageBase messageBase = new MessageBase();
		
		try {
			String restfulToken = PropertiesUtils.readValue("restful.token");
			
			if (StringUtils.isBlank(showCaseName)) {
				log.info("设定参数错误 showCaseName");
				throw new WebApiException(WebApiExceptionType.MISS_SHOW_CASE_NAME);
			}
			
			if(token == null || !token.equals(restfulToken)) {
				log.info("设定参数错误 token");
				throw new WebApiException(WebApiExceptionType.TOKEN_ERROR);
			}

			List<RepoArea> fetchShowCaseByName = this.fetchShowCaseByName(showCaseName);

			List<Message> listData = new ArrayList<Message>();
			
			for (int i = 0; i < fetchShowCaseByName.size(); i++) {
				RepoArea repoArea = fetchShowCaseByName.get(i);
				Message message = new Message();
				message.setId(repoArea.getId());
				message.setName(repoArea.getName());
				listData.add(message);
			}
			messageBase.setResultCode(SUCCESS);
			messageBase.setResultMsg(SUCCESS_MSG);
			messageBase.setListDatas(listData);

		} catch (WebApiException e) {
			log.error("处理运行状态异常：" + ExceptionLog.getErrorStack(e));
			messageBase.setResultCode(e.getExceptionType().value());
			messageBase.setResultMsg(e.getExceptionType().toString());

		} catch (Exception e) {
			log.error("处理运行状态异常：" + ExceptionLog.getErrorStack(e));
			messageBase.setResultCode(UNSUCCESS);
			messageBase.setResultMsg(ERROR_MSG);
		} finally {
			content = copiedBuffer(JSONUtil.serialize(messageBase), CharsetUtil.UTF_8);
		}
		
		log.info("检索展柜结束 content is '{}'", content);
		return content;
	}
	
	@Override
	public ByteBuf fetchEmailTemplate(String emailTemplateName, String token) {
		log.info("开始检索邮件模板");
		ByteBuf content = null;
		MessageBase messageBase = new MessageBase();
		
		try {
			String restfulToken = PropertiesUtils.readValue("restful.token");
			
			if (StringUtils.isBlank(emailTemplateName)) {
				log.info("设定参数错误 emailTemplate");
				throw new WebApiException(WebApiExceptionType.MISS_EAMIL_TEMPLATE_NAME);
			}
			
			if(token == null || !token.equals(restfulToken)) {
				log.info("设定参数错误 token");
				throw new WebApiException(WebApiExceptionType.TOKEN_ERROR);
			}

			List<EmailTemplate> fetchEmailTemplateByName = this.fetchEmailTemplateByName(emailTemplateName);

			List<Message> listData = new ArrayList<Message>();
			
			for (int i = 0; i < fetchEmailTemplateByName.size(); i++) {
				EmailTemplate emailTemplate = fetchEmailTemplateByName.get(i);
				Message message = new Message();
				message.setId(emailTemplate.getId());
				message.setName(emailTemplate.getName());
				listData.add(message);
			}
			messageBase.setResultCode(SUCCESS);
			messageBase.setResultMsg(SUCCESS_MSG);
			messageBase.setListDatas(listData);

		} catch (WebApiException e) {
			log.error("处理运行状态异常：" + ExceptionLog.getErrorStack(e));
			messageBase.setResultCode(e.getExceptionType().value());
			messageBase.setResultMsg(e.getExceptionType().toString());

		} catch (Exception e) {
			log.error("处理运行状态异常：" + ExceptionLog.getErrorStack(e));
			messageBase.setResultCode(UNSUCCESS);
			messageBase.setResultMsg(ERROR_MSG);
		} finally {
			content = copiedBuffer(JSONUtil.serialize(messageBase), CharsetUtil.UTF_8);
		}
		
		log.info("检索邮件模板结束 content is '{}'", content);
		return content;
	}

	/**
	 * 
	 * 
	 * 
	 */

	@Override
	public ByteBuf listMicroEnvironment(int page, int limit) {
		log.info("-----------------start listMicroEnvironment--------------------- ");
		ByteBuf content = null;
		EnvironmentListMessage messageBase = new EnvironmentListMessage();
		try {

			if (0 == page && 0 == limit) {
				log.info("-----------------[bs]---{miss parameter of page or limit}----------|参数错误  缺失----------- ");
				throw new WebApiException(
						WebApiExceptionType.MISS_MONITOR_STATION_Name);
			}

			else {
				List<Device> devicePage = this.pagingDevice(page, limit);
				int sum = devicePage.size();
				int pageSum = 0;
				if (sum % limit == 0) {
					pageSum = sum / limit;
				} else {
					pageSum = sum / limit + 1;
				}

				int begin = 0;
				int end = 0;
				begin = (page - 1) * limit;
				end = page * limit;

				List<EnvironmentListBean> listData = new ArrayList<EnvironmentListBean>();

				for (int i = begin; i < devicePage.size() && i < end; i++) {
					Device device = devicePage.get(i);
					EnvironmentListBean bean = new EnvironmentListBean();
					bean.setDesp(device.getDesp());
					bean.setDeviceNo(device.getDeviceNo());
					bean.setName(device.getName());
					bean.setId(device.getId());
					listData.add(bean);

				}

				messageBase.setResultCode(SUCCESS);
				messageBase.setResultMsg(SUCCESS_MSG);
				messageBase.setData(listData);
				content = copiedBuffer(JSONUtil.serialize(messageBase),
						CharsetUtil.UTF_8);
			}

		} catch (WebApiException e) {
			messageBase.setResultCode(e.getExceptionType().value());
			messageBase.setResultMsg(e.getExceptionType().toString());
			content = copiedBuffer(JSONUtil.serialize(messageBase),
					CharsetUtil.UTF_8);
			return content;

		}
		log.info("-----------------{end}---listMicroEnvironment--{content}-------is:------------ "
				+ content);
		return content;
	}

	@Override
	public ByteBuf findCurrentMicroEnvironment(long id) {
		log.info("-----------------start fetchMonitorStation--------------------- ");
		ByteBuf content = null;
		EnvironmentMessage messageBase = new EnvironmentMessage();
		try {

			if (0 == id) {
				log.info("-----------------[bs]---{miss parameter of monitorStationName}----------|微环境错误  缺失----------- ");
				throw new WebApiException(
						WebApiExceptionType.MISS_MONITOR_STATION_Name);
			}

			else {
				try {

					Device device = this.findById(id);
					String currentMonitorValue = device
							.getCurrentMonitorValue();
					DeviceDataMsg dataMsg = JSONUtil.deserialize(
							currentMonitorValue, DeviceDataMsg.class);
					List<DataContent> datas = dataMsg.getDatas();
					List<EnvironmentBean> data = messageBase.getData();

					for (int i = 0; i < datas.size(); i++) {
						DataContent dataContent = datas.get(i);
						if (MonitorDataType.HUMIDITY == dataContent
								.getMonitorDataType()) {
							EnvironmentBean environmentBean = new EnvironmentBean();
							environmentBean
									.setMonitorDataType(MonitorDataType.HUMIDITY);
							environmentBean.setValue(dataContent.getValue());
							data.add(environmentBean);
						}
					}

					/**
					 * 
					 */
					messageBase.setResultCode(SUCCESS);
					messageBase.setResultMsg(SUCCESS_MSG);

				} catch (Exception e) {
					log.error("-----------------[bs]---{fetch  monitorStationName error}----------|微环境名字错误-----------");
					log.error(ExceptionLog.getErrorStack(e));
					throw new WebApiException(
							WebApiExceptionType.OPERATION_OF_DATABASE_ERROR);
				}

				content = copiedBuffer(JSONUtil.serialize(messageBase),
						CharsetUtil.UTF_8);
			}

		} catch (WebApiException e) {
			messageBase.setResultCode(e.getExceptionType().value());
			messageBase.setResultMsg(e.getExceptionType().toString());
			content = copiedBuffer(JSONUtil.serialize(messageBase),
					CharsetUtil.UTF_8);
			return content;

		}
		log.info("-----------------{end}---monitorStationName--{content}-------is:------------ "
				+ content);
		return content;
	}

	@Override
	public ByteBuf findSettingMicroEnvironment(long id) {
		log.info("-----------------start fetchMonitorStation--------------------- ");
		ByteBuf content = null;
		EnvironmentMessage messageBase = new EnvironmentMessage();
		try {

			if (0 == id) {
				log.info("-----------------[bs]---{miss parameter of monitorStationName}----------|微环境错误  缺失----------- ");
				throw new WebApiException(
						WebApiExceptionType.MISS_MONITOR_STATION_Name);
			}

			else {
				try {

					Device device = this.findById(id);
					String currentMonitorValue = device
							.getSettingMonitorValue();
					DeviceDataMsg dataMsg = JSONUtil.deserialize(
							currentMonitorValue, DeviceDataMsg.class);
					List<DataContent> datas = dataMsg.getDatas();
					List<EnvironmentBean> data = messageBase.getData();

					for (int i = 0; i < datas.size(); i++) {
						DataContent dataContent = datas.get(i);
						if (MonitorDataType.HUMIDITY == dataContent
								.getMonitorDataType()) {
							EnvironmentBean environmentBean = new EnvironmentBean();
							environmentBean
									.setMonitorDataType(MonitorDataType.HUMIDITY);
							environmentBean.setValue(dataContent.getValue());
							data.add(environmentBean);
						}
					}

					/**
					 * 
					 */
					messageBase.setResultCode(SUCCESS);
					messageBase.setResultMsg(SUCCESS_MSG);

				} catch (Exception e) {
					log.error("-----------------[bs]---{fetch  monitorStationName error}----------|微环境名字错误-----------");
					log.error(ExceptionLog.getErrorStack(e));
					throw new WebApiException(
							WebApiExceptionType.OPERATION_OF_DATABASE_ERROR);
				}

				content = copiedBuffer(JSONUtil.serialize(messageBase),
						CharsetUtil.UTF_8);
			}

		} catch (WebApiException e) {
			messageBase.setResultCode(e.getExceptionType().value());
			messageBase.setResultMsg(e.getExceptionType().toString());
			content = copiedBuffer(JSONUtil.serialize(messageBase),
					CharsetUtil.UTF_8);
			return content;

		}
		log.info("-----------------{end}---monitorStationName--{content}-------is:------------ "
				+ content);
		return content;
	}

	@Override
	public ByteBuf editMicroEnvironment(String data) {
		log.info("-----------------start editMicroEnvironment--------------------- ");
		ByteBuf content = null;
		ResponseBaseMsg messageBase = new ResponseBaseMsg();
		try {
			if (StringUtils.isNoneBlank(data)) {
				log.info("-----------------[bs]---{miss parameter of data}----------|设定数据错误  缺失----------- ");
				throw new WebApiException(
						WebApiExceptionType.MISS_MONITOR_STATION_Name);
			} else {
				try {
					EnvironmentSettingBean environmentSettingBean = JSONUtil
							.deserializeGson(data, EnvironmentSettingBean.class);

					Device device = deviceDao.findById(environmentSettingBean
							.getId());

					String settingMonitorValue = device
							.getSettingMonitorValue();

					DeviceDataMsg dataMsg = JSONUtil.deserialize(
							settingMonitorValue, DeviceDataMsg.class);

					List<DataContent> datas = dataMsg.getDatas();

					for (int i = 0; i < datas.size(); i++) {
						DataContent dataContent = datas.get(i);
						if (environmentSettingBean.getMonitorDataType() == dataContent
								.getMonitorDataType()) {
							dataContent.setValue(environmentSettingBean
									.getValue());
							break;

						}

					}

					String setting_monitor_value = JSONUtil.serialize(dataMsg);
					deviceDao.updateDeivceSettingMonitorValue(
							setting_monitor_value,
							environmentSettingBean.getId());

					/**
					 * 
					 */
					messageBase.setResultCode(SUCCESS);
					messageBase.setResultMsg(SUCCESS_MSG);

				} catch (Exception e) {
					log.error("-----------------[bs]---{fetch  monitorStationName error}----------|微环境名字错误-----------");
					log.error(ExceptionLog.getErrorStack(e));
					throw new WebApiException(
							WebApiExceptionType.OPERATION_OF_DATABASE_ERROR);
				}

				content = copiedBuffer(JSONUtil.serialize(messageBase),
						CharsetUtil.UTF_8);
			}

		} catch (WebApiException e) {
			messageBase.setResultCode(e.getExceptionType().value());
			messageBase.setResultMsg(e.getExceptionType().toString());
			content = copiedBuffer(JSONUtil.serialize(messageBase),
					CharsetUtil.UTF_8);
			return content;

		}
		log.info("-----------------{end}---monitorStationName--{content}-------is:------------ "
				+ content);
		return content;

	}

	@Override
	public ByteBuf findHistoryMicroEnvironment(int id) {
		log.info("-----------------start findHistoryMicroEnvironment--------------------- ");
		ByteBuf content = null;
		EnvironmentHistoryMessage messageBase = new EnvironmentHistoryMessage();
		try {

			if (0 == id) {
				log.info("-----------------[bs]---{miss parameter of MicroEnvironmentID}----------|微环境ID错误  缺失----------- ");
				throw new WebApiException(
						WebApiExceptionType.MISS_MONITOR_STATION_Name);
			}

			else {
				try {

					List<ActivityConstantThData> list = this
							.HistoryMicroEnvironmentByTimeStamp(id);

					List<EnvironmentHistoryBean> datas = messageBase.getData();

					for (int i = 0; i < list.size(); i++) {
						ActivityConstantThData activityConstantThData = list
								.get(i);
						MonitorDataType monitorDataType = activityConstantThData
								.getObjectType();
						Double value = activityConstantThData.getValue();
						Timestamp timestamp = activityConstantThData
								.getDateTime();
						long timestampLong = timestamp.getTime();
						EnvironmentHistoryBean environmentHistoryBean = new EnvironmentHistoryBean();
						environmentHistoryBean
								.setMonitorDataType(monitorDataType);
						environmentHistoryBean.setTimeStamp(timestampLong);
						environmentHistoryBean.setValue(value);
						datas.add(environmentHistoryBean);
					}

					/**
					 * 
					 */
					messageBase.setResultCode(SUCCESS);
					messageBase.setResultMsg(SUCCESS_MSG);

				} catch (Exception e) {
					log.error("-----------------[bs]---{fetch  monitorStationName error}----------|微环境名字错误-----------");
					log.error(ExceptionLog.getErrorStack(e));
					throw new WebApiException(
							WebApiExceptionType.OPERATION_OF_DATABASE_ERROR);
				}

				content = copiedBuffer(JSONUtil.serialize(messageBase),
						CharsetUtil.UTF_8);
			}

		} catch (WebApiException e) {
			messageBase.setResultCode(e.getExceptionType().value());
			messageBase.setResultMsg(e.getExceptionType().toString());
			content = copiedBuffer(JSONUtil.serialize(messageBase),
					CharsetUtil.UTF_8);
			return content;

		}
		log.info("-----------------{end}---findHistoryMicroEnvironment--{content}-------is:------------ "
				+ content);
		return content;
	}

//	@Transactional(readOnly = false)
	private void updatePolicyById(String policyContent, String desp, Long id) {
		policyWrapperDao.updatePolicyById(policyContent, desp, id);

	}

	@Transactional(readOnly = false)
	private void deletePolicyById(Long id) {
		policyWrapperDao.delete(id);

	}

	@Transactional(readOnly = false)
	private void enablePolicyById(Long id, Integer status) {
		policyWrapperDao.updatePolicyStatusById(status, id);

	}

	@Transactional(readOnly = false)
	private void disablePolicyById(Long id, Integer status) {
		policyWrapperDao.updatePolicyStatusById(status, id);

	}

	@Transactional(readOnly = false)
	private List<PolicyWrapper> fetchPolicyByName(String policyName) {
		policyName = "%" + policyName + "%";
		List<PolicyWrapper> findByPolicyContentLike = policyWrapperDao
				.findByPolicyContentLike(policyName);
		return findByPolicyContentLike;

	}

	@Transactional(readOnly = false)
	private List<SmsTemplate> fetchSmsTemplateByName(String smsTemplateName) {
		smsTemplateName = "%" + smsTemplateName + "%";
		List<SmsTemplate> templateContentLike = smsTemplateDao
				.findByTemplateContentLike(smsTemplateName);
		return templateContentLike;
	}
	
	@Transactional(readOnly = false)
	private List<EmailTemplate> fetchEmailTemplateByName(String emailTemplateName) {
		emailTemplateName = "%" + emailTemplateName + "%";
		List<EmailTemplate> templateContentLike = emailTemplateDao
				.findByTemplateContentLike(emailTemplateName);
		return templateContentLike;
	}

	@Transactional(readOnly = false)
	private List<RepoArea> fetchAreaByName(String areaName) {
		areaName = "%" + areaName + "%";
		List<RepoArea> findByNameLike = repoAreaDao.findByNameLike(areaName);
		return findByNameLike;
	}

	@Transactional(readOnly = false)
	private List<Device> fetchMonitorPointByName(String monitorPointName) {
		monitorPointName = "%" + monitorPointName + "%";
		List<Device> findMonitorPoint = deviceDao
				.findMonitorPoint(monitorPointName);
		return findMonitorPoint;
	}

	@Transactional(readOnly = false)
	private List<Device> fetchMonitorStationtByName(String monitorStationName) {
		monitorStationName = "%" + monitorStationName + "%";
		List<Device> findMonitorStation = deviceDao
				.findMonitorStation(monitorStationName);
		return findMonitorStation;
	}
	
	@Transactional(readOnly = false)
	private List<Device> fetchConstantThByName(String constantThName) {
		constantThName = "%" + constantThName + "%";
		List<Device> findConstantThName = deviceDao.findConstantTh(constantThName);
		return findConstantThName;
	}
	
	@Transactional(readOnly = false)
	private List<RepoArea> fetchShowCaseByName(String showCaseName) {
		showCaseName = "%" + showCaseName + "%";
		List<RepoArea> findShowCaseNameName = repoAreaDao.findShowCase(showCaseName);
		return findShowCaseNameName;
	}

	@Transactional(readOnly = false)
	private Device findById(long id) {
		Device device = deviceDao.findById(id);
		return device;
	}

	@Transactional(readOnly = false)
	private List<Device> pagingDevice(int page, int limit) {
		List<Device> allDevice = deviceDao.findAllDevice();

		return allDevice;
	}

	@Transactional(readOnly = false)
	private List<ActivityConstantThData> HistoryMicroEnvironmentByTimeStamp(
			int id) {

		Date dateNow = new Date();
		Calendar cl = Calendar.getInstance();
		cl.setTime(dateNow);
		cl.add(Calendar.MONTH, -1); // 一个月
		Date dateFrom = cl.getTime();
		Long startTime = dateFrom.getTime();
		Long endTime = dateNow.getTime();
		List<ActivityConstantThData> findActivityConstantThDataByAreaIdBetweenAndTimeScope = activityConstantThDataDao
				.findActivityConstantThDataByAreaIdBetweenAndTimeScope(
						(long) id, startTime, endTime);

		return findActivityConstantThDataByAreaIdBetweenAndTimeScope;
	}

	@Override
	public ByteBuf processOperatingMode(String idString, String token, String operator) {
		log.info("开始处理运行状态设定");

		ByteBuf content = null;
		ResponseBaseMsg messageBase = new ResponseBaseMsg();

		try {
			String restfulToken = PropertiesUtils.readValue("restful.token");
			
			Device device = null;
			
			if(token == null || !token.equals(restfulToken)) {
				log.info("设定参数错误 token");
				throw new WebApiException(WebApiExceptionType.TOKEN_ERROR);
			}
			if (StringUtils.isBlank(idString)) {
				log.info("设定参数错误 id");
				throw new WebApiException(WebApiExceptionType.ID_ERROR);
			} else {
				Long id = Long.parseLong(idString);
				device = deviceDao.findById(id);
				if (device == null || !device.getDeviceType().equals(DeviceType.MASTER_GATEWAY)) {
					log.info("设定参数错误 id");
					throw new WebApiException(WebApiExceptionType.ID_ERROR);
				}
			}
			if(operator == null || !(operator.equals("debug") || operator.equals("production"))) {
				log.info("设定参数错误 operator");
				throw new WebApiException(WebApiExceptionType.OPERATOR_ERROR);
			}
			
			MuseumMsg museumMsg = new MuseumMsg();
			
			MsgHeader msgHeader = new MsgHeader();
			msgHeader.setSendFrom("ENGINE");
			msgHeader.setSendTo("NETTY");
			msgHeader.setTimeStamp(System.currentTimeMillis());
			museumMsg.setMsgHeader(msgHeader);
			
			MsgProperty msgProperty = new MsgProperty();
			msgProperty.setCmdType(CmdType.REQUEST);
			msgProperty.setApplicationType(ApplicationType.SENSOR);
			museumMsg.setMsgProperty(msgProperty);
			
			SensorMsg sensorMsg = new SensorMsg();
			
			SensorOperatingMode sensorOperatingStatus = new SensorOperatingMode();
			sensorOperatingStatus.setId(device.getDeviceNo());
			
			if (operator.equals("debug")) {
				sensorOperatingStatus.setOperatingMode(OperatingMode.DEBUG);
			}
			if (operator.equals("production")) {
				sensorOperatingStatus.setOperatingMode(OperatingMode.PRODUCTION);
			}
			
			sensorMsg.setSensorOperatingMode(sensorOperatingStatus);
			sensorMsg.setSensorAppType(SensorAppType.ENGINE_START_DEBUG_MODE);
			
			// 由于 data 与 mtmNetInfo 属性判断非空，初始化一个空的实体
			SensorData SensorData = new SensorData();
			SensorData.setMtmNetInfo(new MtmNetInfo());
			sensorMsg.setData(SensorData);
			
			museumMsg.setMsgBody(sensorMsg);
			log.info("MuseumMsg数据实体准备完毕");
			
			String msg = JSONUtil.serialize(museumMsg);
			if (StringUtils.isNotBlank(msg)) {
				this.redisMQPushSender.sendEngineToNetty(msg);
				log.info("消息发送至Netty端完毕");
			}
			else {
				log.info("数据实体序列化为空");
			}

			messageBase.setResultCode(SUCCESS);
			messageBase.setResultMsg(SUCCESS_MSG);

		} catch (WebApiException e) {
			log.error("处理运行状态异常：\r\n" + ExceptionLog.getErrorStack(e));
			messageBase.setResultCode(e.getExceptionType().value());
			messageBase.setResultMsg(e.getExceptionType().toString());
		} catch (Exception e) {
			log.error("处理运行其他异常：\r\n" + ExceptionLog.getErrorStack(e));
			messageBase.setResultCode(UNSUCCESS);
			messageBase.setResultMsg(ERROR_MSG);
		} finally {
			content = copiedBuffer(JSONUtil.serialize(messageBase), CharsetUtil.UTF_8);
		}

		log.info("设定状态处理结束 content is '{}'", content);
		return content;
	}
	
	@Override
	public ByteBuf historyRecordReport(String idString, String token, String operator) {
		log.info("开始下发历史数据上报指令");

		ByteBuf content = null;
		ResponseBaseMsg messageBase = new ResponseBaseMsg();

		try {
			String restfulToken = PropertiesUtils.readValue("restful.token");
			Device device = null;
			
			if(token == null || !token.equals(restfulToken)) {
				log.info("设定参数错误 token");
				throw new WebApiException(WebApiExceptionType.TOKEN_ERROR);
			}
			if (StringUtils.isBlank(idString)) {
				log.info("设定参数错误 id");
				throw new WebApiException(WebApiExceptionType.ID_ERROR);
			} else {
				Long id = Long.parseLong(idString);
				device = deviceDao.findById(id);
				if (device == null || !(device.getDeviceType().equals(DeviceType.MONITORING_POINT_ONE) || device.getDeviceType().equals(DeviceType.MONITORING_POINT_TWO))) {
					log.info("设定参数错误 id");
					throw new WebApiException(WebApiExceptionType.ID_ERROR);
				}
			}
			if(operator == null || !(operator.equals("start") || operator.equals("stop"))) {
				log.info("设定参数错误 operator");
				throw new WebApiException(WebApiExceptionType.OPERATOR_ERROR);
			}
			
			MuseumMsg museumMsg = new MuseumMsg();
			
			MsgHeader msgHeader = new MsgHeader();
			msgHeader.setSendFrom("ENGINE");
			msgHeader.setSendTo("NETTY");
			msgHeader.setTimeStamp(System.currentTimeMillis());
			museumMsg.setMsgHeader(msgHeader);
			
			MsgProperty msgProperty = new MsgProperty();
			msgProperty.setCmdType(CmdType.REQUEST);
			msgProperty.setApplicationType(ApplicationType.SENSOR);
			museumMsg.setMsgProperty(msgProperty);
			
			SensorMsg sensorMsg = new SensorMsg();
			
			SensorHistoryRecord sensorHistoryRecord = new SensorHistoryRecord();
			sensorHistoryRecord.setMacAddr(device.getMacAddr());
			sensorHistoryRecord.setTopologyPath(getTopologyPath(device.getParentId()));
			
			if (operator.equals("start")) {
				sensorHistoryRecord.setHistoryReportMode(HistoryReportMode.START);
			}
			if (operator.equals("stop")) {
				sensorHistoryRecord.setHistoryReportMode(HistoryReportMode.STOP);
			}
			
			sensorMsg.setSensorHistoryRecord(sensorHistoryRecord);
			sensorMsg.setSensorAppType(SensorAppType.ENGINE_GET_HISTORY_RECORD);
			
			// 由于 data 与 mtmNetInfo 属性判断非空，初始化一个空的实体
			SensorData SensorData = new SensorData();
			SensorData.setMtmNetInfo(new MtmNetInfo());
			sensorMsg.setData(SensorData);
			
			museumMsg.setMsgBody(sensorMsg);
			log.info("MuseumMsg数据实体准备完毕");
			
			String msg = JSONUtil.serialize(museumMsg);
			if (StringUtils.isNotBlank(msg)) {
				this.redisMQPushSender.sendEngineToNetty(msg);
				log.info("消息发送至Netty端完毕");
			}
			else {
				log.info("数据实体序列化为空");
			}

			messageBase.setResultCode(SUCCESS);
			messageBase.setResultMsg(SUCCESS_MSG);

		} catch (WebApiException e) {
			log.error("处理运行状态异常：\r\n" + ExceptionLog.getErrorStack(e));
			messageBase.setResultCode(e.getExceptionType().value());
			messageBase.setResultMsg(e.getExceptionType().toString());
		} catch (Exception e) {
			log.error("处理运行其他异常：\r\n" + ExceptionLog.getErrorStack(e));
			messageBase.setResultCode(UNSUCCESS);
			messageBase.setResultMsg(ERROR_MSG);
		} finally {
			content = copiedBuffer(JSONUtil.serialize(messageBase), CharsetUtil.UTF_8);
		}

		log.info("下发历史数据上报指令结束 content is '{}'", content);
		return content;
	}
	
	@Transactional(readOnly = false)
	private String getTopologyPath(Long id) {
		StringBuffer topologyPathBuffer = new StringBuffer();
		
		while(id != null) {
			Device device = deviceDao.findById(id);
			topologyPathBuffer.insert(0, device.getDeviceNo());
			topologyPathBuffer.insert(0, "-");
			id = device.getParentId();
		}
		
		if(StringUtils.isNotBlank(topologyPathBuffer.toString())) {
			topologyPathBuffer.delete(0, 1);
		}
		
		return topologyPathBuffer.toString();
	}

	@Override
	public ByteBuf engineUpdateSensorMacList(String id, String token) {

		log.info("{start}---{engineUpdateSensorMacList}----");

		ByteBuf content = null;
		ResponseBaseMsg messageBase = new ResponseBaseMsg();

		try {
			String restfulToken = PropertiesUtils.readValue("restful.token");
			
			if(token == null || !token.equals(restfulToken)) {
				log.info("设定参数错误 token");
				throw new WebApiException(WebApiExceptionType.TOKEN_ERROR);
			}
			if (StringUtils.isBlank(id)) {
				log.info("设定参数错误 id");
				throw new WebApiException(WebApiExceptionType.ID_ERROR);
			} else {
				Long deviceId = Long.parseLong(id);
//				sendSensorCmdService.engineUpdateSensorMacList(deviceId);
				messageBase.setResultCode(SUCCESS);
				messageBase.setResultMsg(SUCCESS_MSG);
			}

		} catch (WebApiException e) {
			log.error("处理运行状态异常：\r\n" + ExceptionLog.getErrorStack(e));
			messageBase.setResultCode(e.getExceptionType().value());
			messageBase.setResultMsg(e.getExceptionType().toString());
		} catch (Exception e) {
			log.error("处理运行其他异常：\r\n" + ExceptionLog.getErrorStack(e));
			messageBase.setResultCode(UNSUCCESS);
			messageBase.setResultMsg(ERROR_MSG);
		} finally {
			content = copiedBuffer(JSONUtil.serialize(messageBase), CharsetUtil.UTF_8);
		}
		log.info("{start}---{engineUpdateSensorMacList}----");
		return content;
	}

	@Override
	public ByteBuf engineUpdateNetTopologyList(String id, String token) {

		log.info("{start}---{engineUpdateNetTopologyList}----");

		ByteBuf content = null;
		ResponseBaseMsg messageBase = new ResponseBaseMsg();

		try {
			String restfulToken = PropertiesUtils.readValue("restful.token");
			
			if(token == null || !token.equals(restfulToken)) {
				log.info("设定参数错误 token");
				throw new WebApiException(WebApiExceptionType.TOKEN_ERROR);
			}
			if (StringUtils.isBlank(id)) {
				log.info("设定参数错误 id");
				throw new WebApiException(WebApiExceptionType.ID_ERROR);
			} else {
				Long deviceId = Long.parseLong(id);
				//TODO:
//				sendSensorCmdService.engineUpdateNetTopologyList(deviceId);
				messageBase.setResultCode(SUCCESS);
				messageBase.setResultMsg(SUCCESS_MSG);
			}

		} catch (WebApiException e) {
			log.error("处理运行状态异常：\r\n" + ExceptionLog.getErrorStack(e));
			messageBase.setResultCode(e.getExceptionType().value());
			messageBase.setResultMsg(e.getExceptionType().toString());
		} catch (Exception e) {
			log.error("处理运行其他异常：\r\n" + ExceptionLog.getErrorStack(e));
			messageBase.setResultCode(UNSUCCESS);
			messageBase.setResultMsg(ERROR_MSG);
		} finally {
			content = copiedBuffer(JSONUtil.serialize(messageBase), CharsetUtil.UTF_8);
		}
		log.info("{start}---{engineUpdateSensorMacList}----");
		return content;
	}
}
