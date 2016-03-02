package com.qingruan.museum.pma.drlbuilder;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qingruan.museum.dao.entity.PolicyGroup;
import com.qingruan.museum.pma.drlbuilder.bean.ConstraintBean;
import com.qingruan.museum.pma.drlbuilder.bean.DRLTemplatesBean;
import com.qingruan.museum.pma.drlbuilder.bean.DelayTaskBean;
import com.qingruan.museum.pma.drlbuilder.bean.NormalPatternBean;
import com.qingruan.museum.pma.drlbuilder.bean.WhenBean;
import com.qingruan.museum.pma.meta.DomainModel;
import com.qingruan.museum.pma.meta.InterfaceModel;
import com.qingruan.museum.pma.meta.MethodModel;
import com.qingruan.museum.pma.model.Constraint;
import com.qingruan.museum.pma.model.Execution;
import com.qingruan.museum.pma.model.Policy;
import com.qingruan.museum.pma.model.enums.OperatorType;
import com.qingruan.museum.pma.model.enums.ValueType;
import com.qingruan.museum.pma.service.RuleModelContainer;
import com.qingruan.museum.pma.util.ExceptionLogUtil;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

/**
 * ClassName:FreeMaker <br/>
 * Reason: 使用freemaker生成drl模版 <br/>
 * Date: 2014-5-27 下午2:14:01 <br/>
 * 
 * @author tommy
 * @version
 * @see
 */

@Slf4j
@Service
public class FreeMarker {
	@Autowired
	private RuleModelContainer ruleModelContainer;
	public static final String IPCAN_PATTERN_MARK = "ipcanContext";
	/**
	 * constraint中，对象型的条件
	 */
	private List<Constraint> memberConstraints = new ArrayList<Constraint>();

	/**
	 * constraint中，函数型的条件
	 */
	private List<Constraint> serviceConstraints = new ArrayList<Constraint>();

	private boolean ipcanDomainModelAppear;

	public DrlDescription build(Policy policy, PolicyGroup policyGroup) {
		try {
			// policy准备
			preProcess(policy);
			// DRL描述对象
			DrlDescription drlDescription = new DrlDescription();
			// 生成DRL脚本
			String drlContent = DRLbuilder(policy);
			drlDescription.setContent(drlContent);
			drlDescription.setCategory(policyGroup.getGroupName());
			return drlDescription;
		} catch (Exception e) {
			log.error("fail to build {} : {}.", policyGroup.getGroupName(),
					policy.getPolicyName());
			log.error(ExceptionLogUtil.getErrorStack(e));

			return null;
		}
	}

	/**
	 * 生成DRL文件
	 * 
	 * @param policy
	 * @return
	 */
	public String DRLbuilder(Policy policy) {
		log.debug("DRLbuilder...............start");
		Writer stringWriter = new StringWriter();
		try {
			// DRL模版对象
			DRLTemplatesBean iDRLTemplatesBean = generateDRLTemplatesBean(policy);
			// 模版管理对象
			Configuration cfg = new Configuration();
			cfg.setDirectoryForTemplateLoading(new File(FreeMarker.class.getResource("/templates").getPath()));
//			cfg.setDirectoryForTemplateLoading(new File("/Users/14cells/templates"));
			cfg.setObjectWrapper(new DefaultObjectWrapper());
			Template temp = cfg.getTemplate("DrlTemplate.ftl");
			// 此处注释掉，可以输出drl
			// OutputStream fileout = new FileOutputStream(new File(
			// FreeMarker.class.getResource("D:/temp/pcc-rule.txt")
			// .getPath()), true);
			// Writer out = new OutputStreamWriter(fileout);
			// temp.process(iDRLTemplatesBean, out);
			temp.process(iDRLTemplatesBean, stringWriter);
			// out.flush();
			// out.close();
			return stringWriter.toString();
		} catch (Exception e) {
			log.error("DRLbuilder  error :" + ExceptionLogUtil.getErrorStack(e));
			return stringWriter.toString();
		}
	}

	/**
	 * preProcess: policy 准备 将constraints 分类
	 * 
	 * @author tommy
	 * @param policy
	 * @date 2014-5-30
	 */
	private void preProcess(Policy policy) {
		// 将constraints 分类
		memberConstraints.clear();
		serviceConstraints.clear();

		for (Constraint constraint : policy.getConstraints()) {
			if (constraint.getIsService()) {
				serviceConstraints.add(constraint);
			} else {
				memberConstraints.add(constraint);
			}
		}
		// 填充salience
		if (policy.getSalience() == null) {
			policy.setSalience(0);
		}
	}

	/**
	 * 生成DRL脚本内容
	 * 
	 * @param policy
	 * @return
	 */
	private DRLTemplatesBean generateDRLTemplatesBean(Policy policy) {
		DRLTemplatesBean iDRLTemplatesBean = new DRLTemplatesBean();

		// 从前端传入的policy对象，收集所有的模式、member、service
		Set<DomainModel> domainModels = collectDomainModels(policy);

		// 添加DRL需要的所有imports
		appendImports(domainModels, iDRLTemplatesBean);

		// 添加DRL需要的全局对象（bsService）
		appendGlobles(policy, iDRLTemplatesBean);

		// 特殊处理**防止ipcanContext未设置的情况
		ipcanDomainModelAppear = ipcanDomainModelAppear(domainModels);
		// rule 开始部分
		iDRLTemplatesBean.setRuleName(policy.getPolicyName());
		iDRLTemplatesBean.setSalience(policy.getSalience());
		iDRLTemplatesBean.setPolicyId(policy.getId());
		// when pattern
		appendRuleWhenPart(policy, iDRLTemplatesBean);
		// then pattern
		appendRuleThenPart(policy, iDRLTemplatesBean);
		return iDRLTemplatesBean;
	}

	/**
	 * 把用到的所有domain model
	 * 
	 * @param policy
	 * @return
	 */
	private Set<DomainModel> collectDomainModels(Policy policy) {
		HashSet<DomainModel> domainModels = new HashSet<DomainModel>();
		/*
		 * 目前传进来的是IpcanContext模式, 后面手工加了Delay member Constraints 集合
		 */
		for (Constraint constraint : memberConstraints) {
			/*
			 * del,与上面判断重复
			 */
			if (!constraint.getIsService()) {
				String patterName = getNonservicePatternName(constraint);
				DomainModel patternModel = ruleModelContainer
						.findDomainModel(patterName);
				domainModels.add(patternModel);
			}
			/*
			 * collect Member Constraint DomainModels
			 */
			DomainModel domainModel = ruleModelContainer
					.findDomainModel(constraint.getName());
			if (domainModel != null) {
				domainModels.add(domainModel);
			}
		}
		/*
		 * Service Constraints 集合
		 */
		for (Constraint constraint : serviceConstraints) {
			InterfaceModel interfaceModel = findInterfaceModel(constraint
					.getName());
			if (interfaceModel != null) {
				collectInterfaceDomainModels(constraint.getName(),
						interfaceModel, domainModels, false);
			}
		}
		/*
		 * Executions 集合
		 */
		for (Execution execution : policy.getExecutions()) {
			InterfaceModel interfaceModel = findInterfaceModel(execution
					.getName());
			if (interfaceModel != null) {
				collectInterfaceDomainModels(execution.getName(),
						interfaceModel, domainModels, true);
			}
		}
		return domainModels;
	}

	/*
	 * interfaceDomain models
	 */
	private void collectInterfaceDomainModels(String methodName,
			InterfaceModel interfaceModel, Set<DomainModel> domainModels,
			boolean skipReturn) {
		MethodModel methodModel = findMatchedMethodModel(interfaceModel,
				methodName);

		if (methodModel != null) {

			if (!skipReturn && methodModel.getReturnModel() != null) {
				domainModels.add(methodModel.getReturnModel());
			}

			for (DomainModel domainModel : methodModel.getParams()) {
				domainModels.add(domainModel);
			}
		}
	}

	private boolean ipcanDomainModelAppear(Set<DomainModel> domainModels) {
		for (DomainModel domainModel : domainModels) {
			if (domainModel.getClassName().equalsIgnoreCase("ipcanContext")) {
				return true;
			}
		}
		return false;
	}

	private void appendImports(Set<DomainModel> domainModels,
			DRLTemplatesBean iDRLTemplatesBean) {
		HashSet<String> imports = new HashSet<String>();
		// add 默认import
		imports.add("com.qingruan.museum.engine.service.rule.DelayTaskAgency");
		imports.add("com.qingruan.museum.engine.service.rule.DelayTask");
		// add 其他import
		for (DomainModel domainModel : domainModels) {
			if (domainModel.getPackageInfo() != null) {
				imports.add(domainModel.getPackageInfo() + "."
						+ domainModel.getClassName());
			}
		}
		iDRLTemplatesBean.setImports(imports);
	}

	/**
	 * add globless
	 */
	private void appendGlobles(Policy policy, DRLTemplatesBean iDRLTemplatesBean) {
		HashSet<InterfaceModel> interfaceModels = new HashSet<InterfaceModel>();
		for (Constraint constraint : serviceConstraints) {
			InterfaceModel interfaceModel = findInterfaceModel(constraint
					.getName());
			if (interfaceModel != null) {
				interfaceModels.add(interfaceModel);

			}
		}
		iDRLTemplatesBean.setInterfaceModels(interfaceModels);
	}

	/*
	 * when部分
	 */
	private void appendRuleWhenPart(Policy policy,
			DRLTemplatesBean iDRLTemplatesBean) {
		WhenBean wb = new WhenBean();
		// IpcanContext(member.subMember="XXX",member2.....);
		wb.setNormalPatterns(appendNormalPatterns(iDRLTemplatesBean));
		// 从源代码看，此处唯一，没有需要配置的地方
		// appendDelayTaskAgencyPattern(iDRLTemplatesBean);
		// service constraint
		wb.setServiceConstraints(appendServiceEval());
		iDRLTemplatesBean.setWhenBean(wb);
	}

	/**
	 * 拼装IpcanContext类型的pattern
	 * 
	 * @param drlContentBuffer
	 */
	private List<NormalPatternBean> appendNormalPatterns(
			DRLTemplatesBean iDRLTemplatesBean) {
		List<NormalPatternBean> normalPatternBeans = new ArrayList<NormalPatternBean>();
		Map<String, List<Constraint>> classifiedConstraints = classifyNonserviceConstraint();

		if (ipcanDomainModelAppear
				&& ipcanPatternExist(classifiedConstraints) == false) {
			normalPatternBeans.add(appendSingleNormalPattern(
					IPCAN_PATTERN_MARK, null));
		}

		for (Entry<String, List<Constraint>> entry : classifiedConstraints
				.entrySet()) {
			normalPatternBeans.add(appendSingleNormalPattern(entry.getKey(),
					entry.getValue()));
		}
		return normalPatternBeans;
	}

	// 判断ipcontent是否存在
	private boolean ipcanPatternExist(
			Map<String, List<Constraint>> classifiedConstraints) {
		for (Map.Entry<String, List<Constraint>> entry : classifiedConstraints
				.entrySet()) {
			if (entry.getKey().equals(IPCAN_PATTERN_MARK)) {
				return true;
			}
		}

		return false;
	}

	private Map<String, List<Constraint>> classifyNonserviceConstraint() {
		Map<String, List<Constraint>> classifiedConstraints = new HashMap<String, List<Constraint>>();

		for (Constraint constraint : memberConstraints) {
			String patternName = getNonservicePatternName(constraint);

			List<Constraint> constraints = classifiedConstraints
					.get(patternName);
			if (constraints == null) {
				constraints = new ArrayList<Constraint>();

				classifiedConstraints.put(patternName, constraints);
			}

			constraints.add(constraint);
		}

		return classifiedConstraints;
	}

	// 生成单个NormalPatternBean
	private NormalPatternBean appendSingleNormalPattern(String patternName,
			List<Constraint> constraints) {
		NormalPatternBean normalPatternBean = new NormalPatternBean();
		normalPatternBean.setPatternName(patternName);
		normalPatternBean.setIpatternName(DrlBuilderUtil
				.ConvertFirstCharToUpper(patternName));
		normalPatternBean
				.setMemberConstraints(appendMemberConstraints(constraints));
		return normalPatternBean;
	}

	private String getNonservicePatternName(Constraint constraint) {
		DomainModel domainModel = ruleModelContainer.findDomainModel(constraint
				.getName());
		int firstDotIndex = domainModel.getCallingExpress().indexOf(".");
		return domainModel.getCallingExpress().substring(0, firstDotIndex);
	}

	private List<ConstraintBean> appendMemberConstraints(
			List<Constraint> constraints) {
		List<ConstraintBean> constraintBeans = new ArrayList<ConstraintBean>();
		if (constraints != null) {
			for (int i = 0; i < constraints.size(); i++) {
				constraintBeans.add(appendSingleMemberConstraint(constraints
						.get(i)));
			}
		}
		return constraintBeans;
	}

	// 生成单个 ConstraintBean对象 该对象用于。。。。TODO
	private ConstraintBean appendSingleMemberConstraint(Constraint constraint) {
		ConstraintBean constraintBean = new ConstraintBean();
		String memberFetchExp = getMemberFetchExp(constraint);
		constraintBean.setMemberFetchExp(memberFetchExp);
		DomainModel domainModel = ruleModelContainer.findDomainModel(constraint
				.getName());
		String memberConstraintOperator = getMemberConstraintOperator(
				constraint, domainModel.getValueType());
		constraintBean.setMemberConstraintOperator(memberConstraintOperator);
		return constraintBean;
	}

	private String getMemberFetchExp(Constraint constraint) {
		DomainModel domainModel = ruleModelContainer.findDomainModel(constraint
				.getName());
		int firstDotIndex = domainModel.getCallingExpress().indexOf(".");

		return domainModel.getCallingExpress().substring(firstDotIndex + 1);
	}

	private String getMemberConstraintOperator(Constraint constraint,
			ValueType valueType) {

		String adjustedValue = adjustValue(constraint.getValue(), valueType);

		if (adjustedValue.equals("null") || StringUtils.isEmpty(adjustedValue)) {
			return null;
		}

		StringBuffer memberConstraintOperator = new StringBuffer();

		if (valueType == ValueType.STRING) {
			memberConstraintOperator.append(".equals(");
			memberConstraintOperator.append(adjustedValue);
			memberConstraintOperator.append(')');

			if (constraint.getOperatorType() == OperatorType.NOT_EQUALS) {
				memberConstraintOperator.append("==false");
			}
		} else {
			memberConstraintOperator.append(constraint.getOperatorType()
					.getValue());

			memberConstraintOperator.append(adjustedValue);
		}

		return memberConstraintOperator.toString();
	}

	/**
	 * appendServiceEval:When -> eval 部分
	 * 
	 * @author tommy
	 * @param drlContentBuffer
	 * @date 2014-5-29
	 */
	private List<ConstraintBean> appendServiceEval() {
		int size = serviceConstraints.size();
		List<ConstraintBean> lc = new ArrayList<ConstraintBean>();
		for (int i = 0; i < size; i++) {
			lc.add(appendSingleServiceConstraint(serviceConstraints.get(i)));
		}
		return lc;
	}

	private ConstraintBean appendSingleServiceConstraint(Constraint constraint) {
		ConstraintBean constraintBean = new ConstraintBean();
		InterfaceModel interfaceModel = findInterfaceModel(constraint.getName());

		MethodModel matchedMethodModel = findMatchedMethodModel(interfaceModel,
				constraint.getName());

		String memberConstraintOperator = getMemberConstraintOperator(
				constraint, matchedMethodModel.getReturnModel().getValueType());
		if (memberConstraintOperator == null) {
			memberConstraintOperator = "false";
			return constraintBean;
		}
		String callingExpress = matchedMethodModel.getCallingExpress();
		constraintBean.setCallingExpress(callingExpress);
		// set MemberConstraintOperator
		constraintBean.setMemberConstraintOperator(memberConstraintOperator);
		// set CallingExpress
		constraintBean
				.setCallingExpress(matchedMethodModel.getCallingExpress());
		constraintBean
				.setServiceConstraintMethodParams(appendServiceConstraintMethodParams(
						constraint.getParam(), matchedMethodModel.getParams()));
		return constraintBean;
	}

	private MethodModel findMatchedMethodModel(InterfaceModel interfaceModel,
			String specifiedMethodName) {
		log.info("interfaceModel is " + interfaceModel);
		if (interfaceModel.getMethodModels() == null) {
			log.error("findMatchedMethodModel - interfaceModel.getMethodModels is null.");
			return new MethodModel();
		}

		return interfaceModel.getMethodModels().get(specifiedMethodName);
	}

	// 生成 ServiceConstraintMethodParams集合
	private List<String> appendServiceConstraintMethodParams(
			List<String> params, List<DomainModel> paramModels) {
		List<String> ls = new ArrayList<String>();
		for (int i = 0; i < params.size(); i++) {
			ls.add(adjustValue(params.get(i), paramModels.get(i).getValueType()));
		}
		return ls;
	}

	/**
	 * appendRuleThenPart: then部分
	 * 
	 * @author tommy
	 * @param policy
	 * @param iDRLTemplatesBean
	 * @date 2014-5-29
	 */
	private void appendRuleThenPart(Policy policy,
			DRLTemplatesBean iDRLTemplatesBean) {
		List<DelayTaskBean> delayTaskBeans = new ArrayList<DelayTaskBean>();
		if (!policy.getExecutions().isEmpty()) {
			// 生成DelayTaskBeans 集合
			for (Execution execution : policy.getExecutions()) {
				DelayTaskBean delayTaskBean = appendSingleServiceDelayCall(
						execution, policy.getSalience(), policy.getId());
				if (delayTaskBean != null) {
					delayTaskBeans.add(delayTaskBean);
				}
			}
		}
		iDRLTemplatesBean.setDelayTaskBeans(delayTaskBeans);
	}

	// 生成单个DelayTaskBean
	private DelayTaskBean appendSingleServiceDelayCall(Execution execution,
			Integer salience, Long policyId) {
		DelayTaskBean delayTaskBean = new DelayTaskBean();
		List<String> params = execution.getParam();
		delayTaskBean.setParamSize(params.size());
		delayTaskBean.setBusinessServiceName(execution.getName());
		delayTaskBean.setPolicyId(policyId + "L");
		delayTaskBean.setSalience(salience);
		log.info("appendSingleServiceDelayCall - {}", execution.getName());
		InterfaceModel interfaceModel = findInterfaceModel(execution.getName());
		if (interfaceModel == null) {
			log.error("appendSingleServiceDelayCall - interfaceModel is null.");
			return null;
		}
		MethodModel matchedMethodModel = findMatchedMethodModel(interfaceModel,
				execution.getName());
		List<DomainModel> paramModels = matchedMethodModel.getParams();
		List<String> ps = new ArrayList<String>();
		if (params.size() != paramModels.size()) {
			return null;
		}
		for (int i = 0; i < params.size(); i++) {
			ps.add(adjustValue(params.get(i), paramModels.get(i).getValueType()));
		}
		delayTaskBean.setParams(ps);
		return delayTaskBean;
	}

	// 生成DelayTaskBean中的单个param
	private String adjustValue(String value, ValueType valueType) {
		if (value.equals("null")) {
			return value;
		}
		if (StringUtils.isEmpty(value) && valueType != ValueType.STRING) {
			return "null";
		}
		if (valueType == ValueType.DOUBLE) {
			return value + "D";
		}
		if (valueType == ValueType.LONG) {
			return value + "L";
		} else if (valueType == ValueType.STRING) {
			return "\"" + value + "\"";
		} else {
			return value;
		}
	}

	private InterfaceModel findInterfaceModel(String callingExpression) {
		String interfaceName = getInterfaceName(callingExpression);
		return ruleModelContainer.findInterfaceModel(interfaceName);
	}

	private String getInterfaceName(String callingExpression) {
		int firstDotIndex = callingExpression.indexOf(".");
		if (firstDotIndex < 0) {
			return callingExpression;
		}
		return callingExpression.substring(0, firstDotIndex);
	}

}
