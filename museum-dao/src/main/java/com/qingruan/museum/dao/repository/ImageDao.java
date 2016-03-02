package com.qingruan.museum.dao.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.qingruan.museum.dao.entity.Image;

public interface ImageDao extends PagingAndSortingRepository<Image, Long> {

}
