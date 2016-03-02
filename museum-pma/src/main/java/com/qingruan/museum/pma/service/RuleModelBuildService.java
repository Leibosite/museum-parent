package com.qingruan.museum.pma.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.qingruan.museum.pma.annotation.ConstraintSelection;
import com.qingruan.museum.pma.annotation.DomainElement;
import com.qingruan.museum.pma.annotation.ExecutionSelection;
import com.qingruan.museum.pma.annotation.MethodElement;
import com.qingruan.museum.pma.meta.DomainModel;
import com.qingruan.museum.pma.meta.InterfaceModel;
import com.qingruan.museum.pma.meta.MethodModel;
import com.qingruan.museum.pma.model.enums.ValueType;
import com.qingruan.museum.pma.util.I18nUtils;

public class RuleModelBuildService implements BeanPostProcessor {

	private static final String inlineType = "inline";

	// @Autowired
	// private RuleModelContainer ruleModelContainer;

	public RuleModelContainer ruleModelContainer = new RuleModelContainer();

	// // for domain generator

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		this.ScanForPmaMeta(bean.getClass());
		return bean;
	}

	public void ScanForPmaMeta(Class<?> beanClass) {
		this.ScanForPmaMeta(beanClass, null);
	}

	public void ScanForPmaMeta(Class<?> beanClass, ApplicationContext context) {

		ConstraintSelection constraintSelection = beanClass
				.getAnnotation(ConstraintSelection.class);

		ExecutionSelection executionSelection = beanClass
				.getAnnotation(ExecutionSelection.class);

		if (constraintSelection != null) {
			if (constraintSelection.isService()) {

				InterfaceModel model = this._buildInterfaceModel(beanClass,
						context);
				// RuleModelContainer.getInstance()
				// .getBaseConstraintInterfaceModels()
				// .add(model.getCallingExpress());
				ruleModelContainer.getBaseConstraintInterfaceModels().add(
						model.getCallingExpress());
			} else {
				DomainModel model = this._buildDomainModel(beanClass,
						beanClass.getAnnotation(DomainElement.class), "");
				// RuleModelContainer.getInstance()
				// .getBaseConstraintDomainModels()
				// .add(model.getCallingExpress());
				ruleModelContainer.getBaseConstraintDomainModels().add(
						model.getCallingExpress());
			}
		}
		// 条件和执行暂时不能兼容
		else if (executionSelection != null) {

			InterfaceModel model = this
					._buildInterfaceModel(beanClass, context);

			// RuleModelContainer.getInstance().getBaseExecutionInterfaceModels()
			// .add(model.getCallingExpress());
			ruleModelContainer.getBaseExecutionInterfaceModels().add(
					model.getCallingExpress());
		}

		// return bean;

	}

	/**
	 * Build interface model. No matter the model is for constraint or
	 * execution.
	 * 
	 * @param beanClass
	 * @return
	 */
	public InterfaceModel _buildInterfaceModel(Class<?> beanClass,
			ApplicationContext context) {
		InterfaceModel interfaceModel = new InterfaceModel();
		DomainElement whenElementAnnotation = beanClass
				.getAnnotation(DomainElement.class);
		interfaceModel.setDisplayName(I18nUtils
				.getI18nValue(whenElementAnnotation.name()));// +++i18n TODO
		interfaceModel.setDescription(whenElementAnnotation.description());
		interfaceModel.setClassName(beanClass.getSimpleName());
		interfaceModel.setPackageInfo(beanClass.getPackage().getName());
		interfaceModel.setCallingExpress(this
				.firstCharToLowerCase(interfaceModel.getClassName()));

		// 获得ClassName(首字母小写，用于查询bean)
		String className = RuleExecuteHelper
				.convertFirstToLowerCase(RuleExecuteHelper
						.getInterfaceName(beanClass.getSimpleName()));
		Object bean = null;
		if (context != null) {
			bean = context.getBean(className);
		}
		interfaceModel.setServiceBean(bean);
		interfaceModel.setMethodAccess(MethodAccess.get(beanClass));

		interfaceModel.setMethodModels(new HashMap<String, MethodModel>());

		for (Method method : beanClass.getMethods()) {
			MethodElement me = method.getAnnotation(MethodElement.class);
			DomainElement de = method.getAnnotation(DomainElement.class);
			if (me == null)
				continue;
			MethodModel mm = new MethodModel();
			mm.setDescription(me.description());
			mm.setDisplayName(I18nUtils.getI18nValue(me.name()));// +++i18n TODO
			mm.setServiceName(method.getName());
			// Set ConflictSolverService
			// 如果是默认的Object.Class，conflictTargetbean就设置成null，与RuleCore的conflictobject判断保持一致
			if (me.conflictTarget().equals(Object.class)) {
				mm.setConflictTarget(null);
			} else {
				if (context != null) {
					mm.setConflictTarget(context.getBean(me.conflictTarget()));
				}

			}
			mm.setDependParam(me.dependParam());

			Class<?> t = method.getReturnType();
			// TODO 是否需要更多的void信息
			if (t.getSimpleName().equals("void")) {
				// log.info("这个是void!!!!!!!!!!!!");
			} else
				mm.setReturnModel(this._buildDomainModel(t, de, ""));
			mm.setParams(new ArrayList<DomainModel>());
			mm.setCallingExpress(interfaceModel.getCallingExpress() + "."
					+ this.firstCharToLowerCase(mm.getServiceName()));

			Annotation[][] an = method.getParameterAnnotations();
			Class<?>[] cl = method.getParameterTypes();

			for (int i = 0; i < an.length; i++) {
				for (int j = 0; j < an[i].length; j++) {
					if (an[i][j].annotationType().equals(DomainElement.class)) {

						mm.getParams().add(
								this._buildDomainModel(cl[i],
										(DomainElement) an[i][j], ""));
					}
				}
			}

			interfaceModel.getMethodModels().put(mm.getCallingExpress(), mm);
		}
		// RuleModelContainer.getInstance().getInterfaceModels()
		// .put(interfaceModel.getCallingExpress(), interfaceModel);
		ruleModelContainer.getInterfaceModels().put(
				interfaceModel.getCallingExpress(), interfaceModel);
		return interfaceModel;
	}

	private String firstCharToLowerCase(String exp) {
		return exp.substring(0, 1).toLowerCase() + exp.substring(1);
	}

	private void setEazyCallingExpress(DomainModel o) {
		o.setCallingExpress(o.getCallingExpress() + ".value.content");
	}

	// 添加CallingTree的构建。
	public DomainModel _buildDomainModel(Class<?> beanClass,
			DomainElement domainAnnotation, String callingTree) {
		// 如果是long int这种primitive的类型，转化为包装类
		if (beanClass.isPrimitive()) {
			beanClass = ClassUtils.primitiveToWrapper(beanClass);
		}
		// log.info("有一个新的来客：" + beanClass.getSimpleName());
		// log.info("calling:  " + callingTree);

		DomainModel o = new DomainModel();
		o.setDisplayName(I18nUtils.getI18nValue(domainAnnotation.name()));
		o.setDescription(domainAnnotation.description());
		o.setClassName(beanClass.getSimpleName());
		/*
		 * Added start by wanglimei at 2014-2-12 for extra property PCRFVXXXX
		 * e.g extraProp = "datepick", use for add time control
		 */
		o.setExtraProp(domainAnnotation.extraProp());
		o.setMultiple(domainAnnotation.isMultiple());
		o.setPackageInfo(beanClass.getPackage().getName());
		o.setSubObjects(new ArrayList<DomainModel>());
		o.setStaticValue(domainAnnotation.staticValue());
		o.setChosenClassName(domainAnnotation.chosenClassName());

		// FIXME 设置o的类型，需要一个公共的识别类型方法。
		if (beanClass.equals(Integer.class)
		// TODO
		// || beanClass.equals(Unsigned32.class)
		// || beanClass.equals(Integer32.class)
		) {
			// System.out.print("整型   ");
			o.setValueType(ValueType.INTEGER);
		} else if (beanClass.isEnum()) {
			// set the enum class to container
			// RuleModelContainer.getInstance().setUpEnum(beanClass);
			ruleModelContainer.setUpEnum(beanClass);
			// log.info(o.getClassName() + "    是一个enum");
			o.setValueType(ValueType.ENUM);
		} else if (beanClass.equals(Long.class)
		// || beanClass.equals(Unsigned64.class)
		// || beanClass.equals(Integer64.class)
		) {
			o.setValueType(ValueType.LONG);
			// System.out.print("长整型");
		} else if (beanClass.equals(Double.class)
		// || beanClass.equals(Unsigned64.class)
		// || beanClass.equals(Integer64.class)
		) {
			o.setValueType(ValueType.DOUBLE);
			// System.out.print("长整型");
		} else if (beanClass.equals(String.class)
		// || beanClass.equals(OctetString.class)
		// || beanClass.equals(IPFilterRule.class)
				|| beanClass.equals(Time.class)
		// || beanClass.equals(DiameterURI.class)
		// || beanClass.equals(Diamldent.class)
		// || beanClass.equals(Address.class)
		// || beanClass.equals(UTF8String.class)
		) {
			o.setValueType(ValueType.STRING);
			// System.out.print("字符串");
		} else if (beanClass.equals(Boolean.class)) {
			o.setValueType(ValueType.BOOLEAN);
		} else if (beanClass.getClassLoader() != null) {
			o.setValueType(ValueType.OBJECT);
			// System.out.print("对象");
		}

		if (StringUtils.isBlank(callingTree)) {
			o.setCallingExpress(this.firstCharToLowerCase(o.getClassName()));
		} else {
			o.setCallingExpress(callingTree);
		}

		// 设置calling express
		// 考虑content.value的情况，以简化前端
		DomainModel specificType = new DomainModel();
		specificType.setClassName(inlineType);
		specificType.setValueType(o.getValueType());// overwrite the object type
													// with sepecific value type
													// FIXME
		if (o.getClassName().equals("Integer32")) {
			return specificType;
		} else if (o.getClassName().equals("Integer64")) {
			return specificType;
		} else if (o.getClassName().equals("OctetString")) {
			return specificType;
		} else if (o.getClassName().equals("IPFilterRule")) {
			return specificType;
		} else if (o.getClassName().equals("Time")) {
			return specificType;
		} else if (o.getClassName().equals("Unsigned32")) {
			return specificType;
		} else if (o.getClassName().equals("Unsigned64")) {
			return specificType;
		} else if (o.getClassName().equals("UTF8String")) {
			return specificType;
		} else if (o.getClassName().equals("DiameterURI")) {
			return specificType;
		} else if (o.getClassName().equals("DiamIdent")) {
			return specificType;
		} else if (o.getClassName().equals("Address")) {
			return specificType;
		}

		// else if(wrapperedClass.)
		// System.out.println(beanClass.getSimpleName());
		// log.info(o.getClassName());
		// log.info(o.getCallingExpress());
		// log.info(o.getValueType().toString());
		if (o.getValueType() != null
				&& o.getValueType().equals(ValueType.OBJECT)
				// 把重复的去掉
				&& ruleModelContainer.getDomainModels().get(
						o.getCallingExpress()) == null // 为了去重
		) {
			Field[] fList = beanClass.getDeclaredFields();
			for (Field f : fList) {
				f.setAccessible(true);
				DomainElement fieldElement = f
						.getAnnotation(DomainElement.class);
				if (fieldElement == null)
					continue;
				DomainModel subObject = this
						._buildDomainModel(f.getType(), fieldElement,
								o.getCallingExpress() + "." + f.getName());// 递归
				if (subObject.getClassName().equals(inlineType)) {
					this.setEazyCallingExpress(o);
					o.setValueType(subObject.getValueType());
				} else
					o.getSubObjects().add(subObject);
			}
		}
		// RuleModelContainer.getInstance().getDomainModels()
		// .put(o.getCallingExpress(), o);

		// 为了去重
		if (ruleModelContainer.getDomainModels().get(o.getCallingExpress()) == null)
			ruleModelContainer.getDomainModels().put(o.getCallingExpress(), o);
		return o;
	}
}
