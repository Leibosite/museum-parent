/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.qingruan.museum.dao.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.qingruan.museum.dao.entity.User;

public interface UserDao extends PagingAndSortingRepository<User, Long> {
	User findByName(String name);
}
