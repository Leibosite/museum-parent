package com.qingruan.museum.admin.web.alarm;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.qingruan.museum.admin.web.permission.form.AccessGroupQueryForm;


import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.qingruan.museum.admin.web.permission.form.AccessGroupQueryForm;


import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.qingruan.museum.admin.web.permission.form.AccessGroupQueryForm;


import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.qingruan.museum.admin.web.permission.form.AccessGroupQueryForm;

@Slf4j
@Controller
@RequestMapping(value = "/alarm")
public class AlarmController {
	@RequestMapping(value = "/list")
	public String list(
			@ModelAttribute("queryForm") AccessGroupQueryForm queryForm,
			@RequestParam(required = false, value = "page") Integer page,
			Model model, HttpServletRequest request) {
	
//		Page<AccessGroup> accessGroups;
//		accessGroups = groupManager.getPaged(queryForm,
//				RequestParamsUtil.getPageRequest(page));
//
//		model.addAttribute("pageObjects", accessGroups);
//		model.addAttribute("queryForm", queryForm);
//		model.addAttribute("pageUrl", RequestParamsUtil.getCurrentURL(request));
	
		return "/alarm/list";
	}

}
