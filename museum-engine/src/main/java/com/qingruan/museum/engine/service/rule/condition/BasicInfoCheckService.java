package com.qingruan.museum.engine.service.rule.condition;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.qingruan.museum.pma.annotation.ConstraintSelection;
import com.qingruan.museum.pma.annotation.DomainElement;
import com.qingruan.museum.pma.annotation.MethodElement;
import com.qingruan.museum.session.IpcanContext;
/**
 * 基本信息检查
 * @author tommy
 *
 */
@Slf4j
@Service
@ConstraintSelection(isService = true)
@DomainElement(name = "condition.BasicInfoCheck")
public class BasicInfoCheckService {

	private static final String PROPERTY_MISS_MARK = "PROPERTY MISS";


	/**
	 * 判断硬件版本
	 * @param ipcanContext
	 * @return
	 */
	@MethodElement(name = "method.hardwareVersion")
	@DomainElement(name = "domain.specifiedHardwareVersion")
	public String hardwareVersion(
			@DomainElement(name = "ipcan context", staticValue = "ipcanContext") IpcanContext ipcanContext) {
		String hardwareVersion = null;

		return hardwareVersion;
	}
	
	
	/**
	 * 判断软件版本
	 * @param ipcanContext
	 * @return
	 */
	@MethodElement(name = "method.softwareVersion")
	@DomainElement(name = "domain.specifiedSoftwareVersion")
	public String softwareVersion(
			@DomainElement(name = "ipcan context", staticValue = "ipcanContext") IpcanContext ipcanContext) {
		String softwareVersion = null;

		return softwareVersion;
	}

	
	}

