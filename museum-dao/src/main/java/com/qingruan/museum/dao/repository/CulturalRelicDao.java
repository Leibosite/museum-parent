package com.qingruan.museum.dao.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.qingruan.museum.dao.entity.CulturalRelic;

public interface CulturalRelicDao extends PagingAndSortingRepository<CulturalRelic, Long>,JpaSpecificationExecutor<CulturalRelic>{

}
