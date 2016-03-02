package com.qingruan.museum.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qingruan.museum.dao.entity.EmailTemplate;

public interface EmailTemplateDao extends JpaRepository<EmailTemplate, Long>,
		JpaSpecificationExecutor<EmailTemplate> {

	List<EmailTemplate> findByTemplateContentLike(String templateContent);
}
