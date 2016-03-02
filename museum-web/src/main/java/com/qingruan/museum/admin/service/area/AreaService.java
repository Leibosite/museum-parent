package com.qingruan.museum.admin.service.area;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qingruan.museum.dao.entity.RepoArea;
import com.qingruan.museum.dao.repository.RepoAreaDao;

@Service
public class AreaService {
	@Autowired
	private RepoAreaDao repoAreaDao;

	public List<RepoArea> getAllInfosByNameLike(String name) {
		return repoAreaDao.findByNameLike(name);
	}
}
