package com.qingruan.museum.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qingruan.museum.dao.entity.CaseActions;

public interface CaseActionsDao extends JpaRepository<CaseActions, Long>,
JpaSpecificationExecutor<CaseActions>{

}
