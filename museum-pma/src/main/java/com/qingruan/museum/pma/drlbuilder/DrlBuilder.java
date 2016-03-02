package com.qingruan.museum.pma.drlbuilder;

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
import com.qingruan.museum.pma.meta.DomainModel;
import com.qingruan.museum.pma.meta.InterfaceModel;
import com.qingruan.museum.pma.meta.MethodModel;
import com.qingruan.museum.pma.model.Constraint;
import com.qingruan.museum.pma.model.Execution;
import com.qingruan.museum.pma.model.Policy;
import com.qingruan.museum.pma.model.enums.OperatorType;
import com.qingruan.museum.pma.model.enums.ValueType;
import com.qingruan.museum.pma.service.RuleModelContainer;

@Slf4j
@Service
/**
 * PMA-drl builder，
 * 从界面组装的Policy对象生成DRL脚本
 * @author tommy
 *
 */
public class DrlBuilder {
	public static final char LINE_BREAK = '\n';

	public static final char QUOTATION = '\"';

	public static final char DOT = '.';

	public static final char SEMICOLON = ';';

	public static final char TAB = '\t';

	public static final char COLON = ':';

	public static final char LEFT_BRACKET = '(';

	public static final char RIGHT_BRACKET = ')';

	public static final char COMMA = ',';

	public static final char BLANK_SPACE = ' ';

	public static final char EQUAL_SIGN = '=';

	public static final String AND_MARK = "&&";

	public static final String NEW_MARK = "new";

	public static final String NULL_MARK = "null";

	private static String TYPE_MARK(String typePath) {
		int lastDotIndex = typePath.lastIndexOf(DOT);

		if (lastDotIndex < 0) {
			return typePath;
		} else {
			return typePath.substring(lastDotIndex + 1);
		}
	}

	public static final String LONG_TYPE_MARK = TYPE_MARK(Long.class.getName());
	public static final String DOUBLE_TYPE_MARK = TYPE_MARK(Double.class.getName());

	public static final String STRING_TYPE_MARK = TYPE_MARK(String.class
			.getName());

	public static final char LONG_DATA_SUFFIX = 'L';
	public static final char DOUBLE_DATA_SUFFIX = 'D';

	// public static final String SERVICE_PATTERN_HOLDER_MARK =
	// "ServicePatternHolder";

	// public static final String IPCANSESSION_VAR_MARK = "ipcanSession";

	public static final String DELAY_TASK_AGENCY_MARK = "DelayTaskAgency";

	public static final String DELAY_TASK_AGENCY_VAR_MARK = "delayTaskAgency";

	public static final String ADD_DELAY_TASK_METHOD_MARK = "addDelayTask";

	public static final String DELAY_TASK_MARK = "DelayTask";

	public static final String DELAY_TASK_VAR_MARK = "delayTask";

	public static final String SET_BUSINESS_SERVICE_NAME_METHOD_MARK = "setBusinessServiceName";

	public static final String ADD_PARAM_METHOD_MARK = "addParam";

	public static final String SET_SALIENCE_MARK = "setSalience";

	public static final String IPCAN_PATTERN_MARK = "ipcanContext";

	public static final String ADD_EFFECT_POLICY_MARK = "addEffectPolicy";

	public static final String SET_POLICY_ID_MARK = "setPolicyId";

	@Autowired
	private RuleModelContainer ruleModelContainer;

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
			preProcess(policy);

			DrlDescription drlDescription = new DrlDescription();

			// 生成DRL脚本
			String drlContent = generateDrlContent(policy);
			drlDescription.setContent(drlContent.toString());

			drlDescription.setCategory(policyGroup.getGroupName());

			return drlDescription;
		} catch (Exception e) {
			log.info("fail to build {} : {}.", policyGroup.getGroupName(),
					policy.getPolicyName());
			log.info(e.toString());

			return null;
		}
	}

	private void preProcess(Policy policy) {
		// //-----------------------------------------------
		// //TODO delete
		// policy.getConstraints().remove(0);
		// policy.getExecutions().remove(0);
		// //-----------------------------------------------

		classifyConstraints(policy);

		if (policy.getSalience() == null) {
			policy.setSalience(0);
		}
	}

	private void classifyConstraints(Policy policy) {
		memberConstraints.clear();
		serviceConstraints.clear();

		for (Constraint constraint : policy.getConstraints()) {
			if (constraint.getIsService()) {
				serviceConstraints.add(constraint);
			} else {
				memberConstraints.add(constraint);
			}
		}
	}

	/**
	 * 生成DRL脚本内容
	 * 
	 * @param policy
	 * @return
	 */
	private String generateDrlContent(Policy policy) {
		StringBuffer drlContentBuffer = new StringBuffer();

		// 从前端传入的policy对象，收集所有的模式、member、service
		Set<DomainModel> domainModels = collectDomainModels(policy);

		// 添加DRL需要的所有imports
		appendImports(domainModels, drlContentBuffer);

		// 添加DRL需要的全局对象（bsService）
		appendGlobles(policy, drlContentBuffer);

		// 特殊处理**防止ipcanContext未设置的情况
		ipcanDomainModelAppear = ipcanDomainModelAppear(domainModels);

		appendRule(policy, drlContentBuffer);

		return drlContentBuffer.toString();
	}

	/**
	 * 把用到的所有domain model
	 * 
	 * @param policy
	 * @return
	 */
	private Set<DomainModel> collectDomainModels(Policy policy) {
		HashSet<DomainModel> domainModels = new HashSet<DomainModel>();

		/**
		 * 目前传进来的是IpcanContext模式, 后面手工加了Delay
		 */
		collectPatternDomainModels(domainModels);
		collectMemberConstraintDomainModels(domainModels);
		collectConstraintServiceDomainModels(domainModels);
		collectExecutionServiceDomainModels(policy.getExecutions(),
				domainModels);

		return domainModels;
	}

	private boolean ipcanDomainModelAppear(Set<DomainModel> domainModels) {
		for (DomainModel domainModel : domainModels) {
			if (domainModel.getClassName().equalsIgnoreCase(IPCAN_PATTERN_MARK)) {
				return true;
			}
		}

		return false;
	}

	private void appendImports(Set<DomainModel> domainModels,
			StringBuffer drlContentBuffer) {
		appendDefaultImports(drlContentBuffer);

		HashSet<String> classPaths = new HashSet<String>();
		for (DomainModel domainModel : domainModels) {
			if (isBuiltInType(domainModel)) {
				continue;
			}

			classPaths.add(generateImportClassPath(domainModel.getClassName(),
					domainModel.getPackageInfo()));
		}

		for (String classPath : classPaths) {
			appendSingleImport(classPath, drlContentBuffer);
		}
	}

	/**
	 * 
	 * @param domainModels
	 */
	private void collectPatternDomainModels(Set<DomainModel> domainModels) {
		for (Constraint constraint : memberConstraints) {
			// TODO del,与上面判断重复
			if (constraint.getIsService()) {
				continue;
			}

			String patterName = getNonservicePatternName(constraint);

			DomainModel patternModel = ruleModelContainer
					.findDomainModel(patterName);

			domainModels.add(patternModel);
		}
	}

	private void collectMemberConstraintDomainModels(
			Set<DomainModel> domainModels) {
		for (Constraint constraint : memberConstraints) {
			DomainModel domainModel = ruleModelContainer
					.findDomainModel(constraint.getName());
			if (domainModel == null) {
				continue;
			}

			domainModels.add(domainModel);
		}
	}

	private void collectConstraintServiceDomainModels(
			Set<DomainModel> domainModels) {
		for (Constraint constraint : serviceConstraints) {
			InterfaceModel interfaceModel = findInterfaceModel(constraint
					.getName());

			if (interfaceModel == null) {
				continue;
			}

			collectInterfaceDomainModels(constraint.getName(), interfaceModel,
					domainModels, false);
		}
	}

	private void collectExecutionServiceDomainModels(
			List<Execution> executions, Set<DomainModel> domainModels) {
		for (Execution execution : executions) {
			InterfaceModel interfaceModel = findInterfaceModel(execution
					.getName());

			if (interfaceModel == null) {
				continue;
			}

			collectInterfaceDomainModels(execution.getName(), interfaceModel,
					domainModels, true);
		}
	}

	private void collectInterfaceDomainModels(String methodName,
			InterfaceModel interfaceModel, Set<DomainModel> domainModels,
			boolean skipReturn) {
		MethodModel methodModel = findMatchedMethodModel(interfaceModel,
				methodName);

		if (methodModel == null) {
			return;
		}

		if (!skipReturn && methodModel.getReturnModel() != null) {
			domainModels.add(methodModel.getReturnModel());
		}

		for (DomainModel domainModel : methodModel.getParams()) {
			domainModels.add(domainModel);
		}
	}

	private boolean isBuiltInType(DomainModel domainModel) {
		if (domainModel.getPackageInfo() == null) {
			return true;
		} else {
			return false;
		}
	}

	private void appendDefaultImports(StringBuffer drlContentBuffer) {
		appendSingleImport(
				"com.qingruan.museum.engine.service.rule.DelayTaskAgency",
				drlContentBuffer);

		appendSingleImport("com.qingruan.museum.engine.service.rule.DelayTask",
				drlContentBuffer);

		// TODO
	}

	private String generateImportClassPath(String className, String packageInfo) {
		StringBuffer stringBuffer = new StringBuffer();

		stringBuffer.append(packageInfo);

		stringBuffer.append(DOT);

		stringBuffer.append(className);

		return stringBuffer.toString();
	}

	private void appendSingleImport(String classPath,
			StringBuffer drlContentBuffer) {
		drlContentBuffer.append("import ");

		drlContentBuffer.append(classPath);

		drlContentBuffer.append(SEMICOLON);

		drlContentBuffer.append(LINE_BREAK);
	}

	private void appendGlobles(Policy policy, StringBuffer drlContentBuffer) {
		Set<InterfaceModel> interfaceModels = collectServiceConstraintInterfaceModels();

		for (InterfaceModel interfaceModel : interfaceModels) {
			appendSingleGloble(interfaceModel, drlContentBuffer);
		}
	}

	private Set<InterfaceModel> collectServiceConstraintInterfaceModels() {
		HashSet<InterfaceModel> interfaceModels = new HashSet<InterfaceModel>();

		for (Constraint constraint : serviceConstraints) {
			InterfaceModel interfaceModel = findInterfaceModel(constraint
					.getName());
			if (interfaceModel == null) {
				continue;
			}

			interfaceModels.add(interfaceModel);
		}

		return interfaceModels;
	}

	private void appendSingleGloble(InterfaceModel interfaceModel,
			StringBuffer drlContentBuffer) {
		drlContentBuffer.append("global ");

		drlContentBuffer.append(interfaceModel.getPackageInfo());

		drlContentBuffer.append(DOT);

		drlContentBuffer.append(interfaceModel.getClassName());

		drlContentBuffer.append(BLANK_SPACE);

		drlContentBuffer.append(interfaceModel.getCallingExpress());

		drlContentBuffer.append(SEMICOLON);

		drlContentBuffer.append(LINE_BREAK);
	}

	private void appendRule(Policy policy, StringBuffer drlContentBuffer) {
		// bolin-1:rule "${policy.getPolicyName()}"
		appendRuleName(policy.getPolicyName(), drlContentBuffer);

		// bolin-2: salience ${policy.getSalience()}
		appendSalience(policy.getSalience(), drlContentBuffer);

		// TODO 目前条件只有与的关系（比如eval里用逗号隔开）
		// 将来可能要添加或、非
		// TODO pattern方式，存在enum锁的问题
		appendRuleWhenPart(policy, drlContentBuffer);

		appendRuleThenPart(policy, drlContentBuffer);

		drlContentBuffer.append("end");
	}

	private void appendRuleName(String ruleName, StringBuffer drlContentBuffer) {
		drlContentBuffer.append("rule ");

		drlContentBuffer.append(QUOTATION);
		drlContentBuffer.append(ruleName);
		drlContentBuffer.append(QUOTATION);
		drlContentBuffer.append(LINE_BREAK);
	}

	private void appendSalience(Integer salience, StringBuffer drlContentBuffer) {
		if (salience == null) {
			return;
		}

		drlContentBuffer.append("salience ");

		drlContentBuffer.append(Integer.toString(salience));

		drlContentBuffer.append(LINE_BREAK);
	}

	private void appendRuleWhenPart(Policy policy, StringBuffer drlContentBuffer) {
		// bolin-3:when
		drlContentBuffer.append("when");
		drlContentBuffer.append(LINE_BREAK);

		// IpcanContext(member.subMember="XXX",member2.....);
		appendNormalPatterns(drlContentBuffer);

		appendDelayTaskAgencyPattern(drlContentBuffer);

		// service constraint
		appendServiceEval(drlContentBuffer);
	}

	/**
	 * 拼装IpcanContext类型的pattern
	 * 
	 * @param drlContentBuffer
	 */
	private void appendNormalPatterns(StringBuffer drlContentBuffer) {
		Map<String, List<Constraint>> classifiedConstraints = classifyNonserviceConstraint();

		if (ipcanDomainModelAppear
				&& ipcanPatternExist(classifiedConstraints) == false) {
			appendSingleNormalPattern(IPCAN_PATTERN_MARK, null,
					drlContentBuffer);
		}

		for (Entry<String, List<Constraint>> entry : classifiedConstraints
				.entrySet()) {
			appendSingleNormalPattern(entry.getKey(), entry.getValue(),
					drlContentBuffer);
		}
	}

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

	private void appendSingleNormalPattern(String patternName,
			List<Constraint> constraints, StringBuffer drlContentBuffer) {
		// bolin-4:${patternName}:${PatternName}();
		drlContentBuffer.append(TAB);
		drlContentBuffer.append(patternName);
		drlContentBuffer.append(COLON);
		drlContentBuffer.append(DrlBuilderUtil
				.ConvertFirstCharToUpper(patternName));
		drlContentBuffer.append(LEFT_BRACKET);

		appendMemberConstraints(constraints, drlContentBuffer);

		drlContentBuffer.append(RIGHT_BRACKET);

		drlContentBuffer.append(SEMICOLON);

		drlContentBuffer.append(LINE_BREAK);
	}

	private String getNonservicePatternName(Constraint constraint) {
		DomainModel domainModel = ruleModelContainer.findDomainModel(constraint
				.getName());

		int firstDotIndex = domainModel.getCallingExpress().indexOf(DOT);

		return domainModel.getCallingExpress().substring(0, firstDotIndex);
	}

	private void appendMemberConstraints(List<Constraint> constraints,
			StringBuffer drlContentBuffer) {
		if (constraints == null) {
			return;
		}

		int size = constraints.size();

		for (int i = 0; i < size - 1; i++) {
			appendSingleMemberConstraint(constraints.get(i), drlContentBuffer);

			drlContentBuffer.append(COMMA);
		}

		if (!constraints.isEmpty()) {
			appendSingleMemberConstraint(constraints.get(size - 1),
					drlContentBuffer);
		}
	}

	private void appendSingleMemberConstraint(Constraint constraint,
			StringBuffer drlContentBuffer) {
		String memberFetchExp = getMemberFetchExp(constraint);
		drlContentBuffer.append(memberFetchExp);

		DomainModel domainModel = ruleModelContainer.findDomainModel(constraint
				.getName());

		String memberConstraintOperator = getMemberConstraintOperator(
				constraint, domainModel.getValueType());
		drlContentBuffer.append(memberConstraintOperator);
	}

	private String getMemberFetchExp(Constraint constraint) {
		DomainModel domainModel = ruleModelContainer.findDomainModel(constraint
				.getName());

		int firstDotIndex = domainModel.getCallingExpress().indexOf(DOT);

		return domainModel.getCallingExpress().substring(firstDotIndex + 1);
	}

	private String getMemberConstraintOperator(Constraint constraint,
			ValueType valueType) {

		String adjustedValue = adjustValue(constraint.getValue(), valueType);

		if (adjustedValue.equals(NULL_MARK)
				|| StringUtils.isEmpty(adjustedValue)) {
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

	private void appendDelayTaskAgencyPattern(StringBuffer drlContentBuffer) {
		// if (serviceConstraints.isEmpty()) {
		// return;
		// }

		drlContentBuffer.append(TAB);

		drlContentBuffer.append(DELAY_TASK_AGENCY_VAR_MARK);

		drlContentBuffer.append(COLON);

		drlContentBuffer.append(DELAY_TASK_AGENCY_MARK);

		drlContentBuffer.append(LEFT_BRACKET);

		// appendServiceConstraints(drlContentBuffer);

		drlContentBuffer.append(RIGHT_BRACKET);

		drlContentBuffer.append(SEMICOLON);

		drlContentBuffer.append(LINE_BREAK);
	}

	private void appendServiceEval(StringBuffer drlContentBuffer) {
		if (serviceConstraints.isEmpty()) {
			return;
		}
		// bolin-5:eval(basicInfoCheckService.rATType(ipcanContext)==RATType.EUTRAN);

		drlContentBuffer.append(TAB);

		drlContentBuffer.append("eval");

		drlContentBuffer.append(LEFT_BRACKET);

		appendServiceConstraints(drlContentBuffer);

		drlContentBuffer.append(RIGHT_BRACKET);

		drlContentBuffer.append(SEMICOLON);

		drlContentBuffer.append(LINE_BREAK);
	}

	private void appendServiceConstraints(StringBuffer drlContentBuffer) {
		int size = serviceConstraints.size();

		for (int i = 0; i < size - 1; i++) {
			appendSingleServiceConstraint(serviceConstraints.get(i),
					drlContentBuffer);

			// drlContentBuffer.append(COMMA);
			drlContentBuffer.append(AND_MARK);
		}

		if (!serviceConstraints.isEmpty()) {
			appendSingleServiceConstraint(serviceConstraints.get(size - 1),
					drlContentBuffer);
		}
	}

	private void appendSingleServiceConstraint(Constraint constraint,
			StringBuffer drlContentBuffer) {
		InterfaceModel interfaceModel = findInterfaceModel(constraint.getName());

		MethodModel matchedMethodModel = findMatchedMethodModel(interfaceModel,
				constraint.getName());

		String memberConstraintOperator = getMemberConstraintOperator(
				constraint, matchedMethodModel.getReturnModel().getValueType());
		if (memberConstraintOperator == null) {
			drlContentBuffer.append("false");

			return;
		}

		drlContentBuffer.append(matchedMethodModel.getCallingExpress());

		drlContentBuffer.append(LEFT_BRACKET);

		appendServiceConstraintMethodParams(constraint.getParam(),
				matchedMethodModel.getParams(), drlContentBuffer);

		drlContentBuffer.append(RIGHT_BRACKET);

		drlContentBuffer.append(memberConstraintOperator);
	}

	private MethodModel findMatchedMethodModel(InterfaceModel interfaceModel,
			String specifiedMethodName) {
		// for (MethodModel methodModel : interfaceModel.getMethodModels()) {
		// String callingExp = methodModel.getCallingExpress();
		// int lastDotIdx = callingExp.lastIndexOf(DOT);
		// String methodName = callingExp.substring(lastDotIdx+1);
		//
		// if (methodName.equals(specifiedMethodName)) {
		// return methodModel;
		// }
		// }
		//
		// return null;

		if (interfaceModel.getMethodModels() == null) {
			log.error("findMatchedMethodModel - interfaceModel.getMethodModels is null.");
		}

		return interfaceModel.getMethodModels().get(specifiedMethodName);
	}

	private void appendServiceConstraintMethodParams(List<String> params,
			List<DomainModel> paramModels, StringBuffer drlContentBuffer) {
		if (params == null || params.isEmpty()) {
			return;
		}

		int size = params.size();

		String adjustedParam;

		for (int i = 0; i < size - 1; i++) {
			adjustedParam = adjustValue(params.get(i), paramModels.get(i)
					.getValueType());

			drlContentBuffer.append(adjustedParam);

			drlContentBuffer.append(COMMA);
		}

		adjustedParam = adjustValue(params.get(size - 1),
				paramModels.get(size - 1).getValueType());
		drlContentBuffer.append(adjustedParam);
	}

	private void appendRuleThenPart(Policy policy, StringBuffer drlContentBuffer) {
		drlContentBuffer.append("then");

		drlContentBuffer.append(LINE_BREAK);

		drlContentBuffer.append(TAB);
		drlContentBuffer.append(DELAY_TASK_AGENCY_VAR_MARK);
		drlContentBuffer.append(DOT);
		drlContentBuffer.append(ADD_EFFECT_POLICY_MARK);
		drlContentBuffer.append(LEFT_BRACKET);
		drlContentBuffer.append(policy.getId().toString() + 'L');
		drlContentBuffer.append(RIGHT_BRACKET);
		drlContentBuffer.append(SEMICOLON);
		drlContentBuffer.append(LINE_BREAK);

		if (!policy.getExecutions().isEmpty()) {
			// appendDelayTaskAgency(policy.getExecutions().size(),
			// drlContentBuffer);

			appendServiceDelayCalls(policy.getExecutions(),
					policy.getSalience(), policy.getId(), drlContentBuffer);
		}
	}

	// private void appendDelayTaskAgency(int capacity, StringBuffer
	// drlContentBuffer) {
	// drlContentBuffer.append(TAB);
	//
	// drlContentBuffer.append(IPCANSESSION_VAR_MARK);
	// drlContentBuffer.append(DOT);
	// drlContentBuffer.append(DELAY_TASK_AGENCY_VAR_MARK);
	// drlContentBuffer.append(EQUAL_SIGN);
	// drlContentBuffer.append(NEW_MARK);
	// drlContentBuffer.append(BLANK_SPACE);
	// drlContentBuffer.append(DELAY_TASK_AGENCY_MARK);
	// drlContentBuffer.append(LEFT_BRACKET);
	// drlContentBuffer.append(Integer.toString(capacity));
	// drlContentBuffer.append(RIGHT_BRACKET);
	//
	// drlContentBuffer.append(SEMICOLON);
	//
	// drlContentBuffer.append(LINE_BREAK);
	// }

	private void appendServiceDelayCalls(List<Execution> executions,
			Integer salience, Long policyId, StringBuffer drlContentBuffer) {
		drlContentBuffer.append(TAB);
		drlContentBuffer.append(DELAY_TASK_MARK);
		drlContentBuffer.append(BLANK_SPACE);
		drlContentBuffer.append(DELAY_TASK_VAR_MARK);
		drlContentBuffer.append(EQUAL_SIGN);
		drlContentBuffer.append(NULL_MARK);
		drlContentBuffer.append(SEMICOLON);
		drlContentBuffer.append(LINE_BREAK);

		for (Execution execution : executions) {
			appendSingleServiceDelayCall(execution, salience, policyId,
					drlContentBuffer);
		}
	}

	private void appendSingleServiceDelayCall(Execution execution,
			Integer salience, Long policyId, StringBuffer drlContentBuffer) {
		drlContentBuffer.append(TAB);

		List<String> params = execution.getParam();

		drlContentBuffer.append(DELAY_TASK_VAR_MARK);
		drlContentBuffer.append(EQUAL_SIGN);
		drlContentBuffer.append(NEW_MARK);
		drlContentBuffer.append(BLANK_SPACE);
		drlContentBuffer.append(DELAY_TASK_MARK);
		drlContentBuffer.append(LEFT_BRACKET);
		drlContentBuffer.append(params.size());
		drlContentBuffer.append(RIGHT_BRACKET);
		drlContentBuffer.append(SEMICOLON);
		drlContentBuffer.append(LINE_BREAK);

		// 设置方法名：比如bsProvisionPcc
		drlContentBuffer.append(TAB);
		drlContentBuffer.append(DELAY_TASK_VAR_MARK);
		drlContentBuffer.append(DOT);
		drlContentBuffer.append(SET_BUSINESS_SERVICE_NAME_METHOD_MARK);
		drlContentBuffer.append(LEFT_BRACKET);
		drlContentBuffer.append(QUOTATION);
		// drlContentBuffer.append(getInterfaceName(execution.getName()));//该处改为s使用完整callExpress
		drlContentBuffer.append(execution.getName());
		drlContentBuffer.append(QUOTATION);
		drlContentBuffer.append(RIGHT_BRACKET);
		drlContentBuffer.append(SEMICOLON);
		drlContentBuffer.append(LINE_BREAK);

		log.info("appendSingleServiceDelayCall - {}", execution.getName());
		InterfaceModel interfaceModel = findInterfaceModel(execution.getName());

		if (interfaceModel == null) {
			log.error("appendSingleServiceDelayCall - interfaceModel is null.");
		}

		MethodModel matchedMethodModel = findMatchedMethodModel(interfaceModel,
				execution.getName());

		appendServiceDelayCallParams(params, matchedMethodModel,
				drlContentBuffer);

		drlContentBuffer.append(TAB);
		drlContentBuffer.append(DELAY_TASK_VAR_MARK);
		drlContentBuffer.append(DOT);
		drlContentBuffer.append(SET_SALIENCE_MARK);
		drlContentBuffer.append(LEFT_BRACKET);
		drlContentBuffer.append(salience.toString());
		drlContentBuffer.append(RIGHT_BRACKET);
		drlContentBuffer.append(SEMICOLON);
		drlContentBuffer.append(LINE_BREAK);

		drlContentBuffer.append(TAB);
		drlContentBuffer.append(DELAY_TASK_VAR_MARK);
		drlContentBuffer.append(DOT);
		drlContentBuffer.append(SET_POLICY_ID_MARK);
		drlContentBuffer.append(LEFT_BRACKET);
		drlContentBuffer.append(policyId.toString() + 'L');
		drlContentBuffer.append(RIGHT_BRACKET);
		drlContentBuffer.append(SEMICOLON);
		drlContentBuffer.append(LINE_BREAK);

		drlContentBuffer.append(TAB);
		drlContentBuffer.append(DELAY_TASK_AGENCY_VAR_MARK);
		drlContentBuffer.append(DOT);
		drlContentBuffer.append(ADD_DELAY_TASK_METHOD_MARK);
		drlContentBuffer.append(LEFT_BRACKET);
		drlContentBuffer.append(DELAY_TASK_VAR_MARK);
		drlContentBuffer.append(RIGHT_BRACKET);
		drlContentBuffer.append(SEMICOLON);
		drlContentBuffer.append(LINE_BREAK);
	}

	private void appendServiceDelayCallParams(List<String> params,
			MethodModel matchedMethodModel, StringBuffer drlContentBuffer) {
		List<DomainModel> paramModels = matchedMethodModel.getParams();

		if (params.size() != paramModels.size()) {
			return;
		}

		for (int i = 0; i < params.size(); i++) {
			appendSingleServiceDelayCallParam(params.get(i),
					paramModels.get(i), drlContentBuffer);
		}
	}

	private void appendSingleServiceDelayCallParam(String param,
			DomainModel paramModel, StringBuffer drlContentBuffer) {
		appendSingleServiceDelayCallParam(param, paramModel.getValueType(),
				drlContentBuffer);
	}

	private void appendSingleServiceDelayCallParam(String param,
			ValueType valueType, StringBuffer drlContentBuffer) {
		String adjustedParam = adjustValue(param, valueType);

		drlContentBuffer.append(TAB);
		drlContentBuffer.append(DELAY_TASK_VAR_MARK);
		drlContentBuffer.append(DOT);
		drlContentBuffer.append(ADD_PARAM_METHOD_MARK);
		drlContentBuffer.append(LEFT_BRACKET);
		drlContentBuffer.append(adjustedParam);
		drlContentBuffer.append(RIGHT_BRACKET);
		drlContentBuffer.append(SEMICOLON);
		drlContentBuffer.append(LINE_BREAK);
	}

	private String adjustValue(String value, ValueType valueType) {
		if (value.equals(NULL_MARK)) {
			return value;
		}
		if (StringUtils.isEmpty(value) && valueType != ValueType.STRING) {
			return NULL_MARK;
		}

		if (valueType == ValueType.LONG) {
			return value + LONG_DATA_SUFFIX;
		} else if (valueType == ValueType.STRING) {
			return QUOTATION + value + QUOTATION;
		} else if(valueType == ValueType.DOUBLE){
			
			
			return value + DOUBLE_DATA_SUFFIX;
		}
		else {
			return value;
		}
	}

	private InterfaceModel findInterfaceModel(String callingExpression) {
		String interfaceName = getInterfaceName(callingExpression);

		return ruleModelContainer.findInterfaceModel(interfaceName);
	}

	private String getInterfaceName(String callingExpression) {
		int firstDotIndex = callingExpression.indexOf(DOT);

		if (firstDotIndex < 0) {
			return callingExpression;
		}

		return callingExpression.substring(0, firstDotIndex);
	}
}
