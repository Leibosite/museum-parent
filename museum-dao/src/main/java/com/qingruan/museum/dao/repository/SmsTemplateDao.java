package com.qingruan.museum.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qingruan.museum.dao.entity.SmsTemplate;

public interface SmsTemplateDao extends JpaRepository<SmsTemplate, Long>,
		JpaSpecificationExecutor<SmsTemplate> {

	List<SmsTemplate> findByTemplateContentLike(String templateContent);
}
