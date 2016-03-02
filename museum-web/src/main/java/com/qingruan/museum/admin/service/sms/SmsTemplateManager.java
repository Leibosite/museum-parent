package com.qingruan.museum.admin.service.sms;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.qingruan.museum.dao.entity.SmsTemplate;
import com.qingruan.museum.dao.repository.SmsTemplateDao;
import com.qingruan.museum.framework.spring.data.jpa.util.SpecificationUtil;

/**
 * 短信模板配置
 * 
 * @author tommy
 * 
 */
@Slf4j
@Component
@Transactional(readOnly = true)
public class SmsTemplateManager {

	@Autowired
	private SmsTemplateDao templateDao;

	public Page<SmsTemplate> getPaged(String template, Pageable pageable) {
		return templateDao.findAll(SpecificationUtil.like(SmsTemplate.class,
				"templateContent", template), pageable);
	}

	public SmsTemplate getOne(Long id) {
		return templateDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void delete(Long id) {
		templateDao.delete(id);
		log.info("delete sms template {} ", id);
	}

	@Transactional(readOnly = false)
	public void save(SmsTemplate template) {
		templateDao.save(template);
		log.info("save sms template {} ", template.toString());
	}

	public List<SmsTemplate> getAllInfosByNameLike(String name) {
		return templateDao.findByTemplateContentLike(name);
	}

}
