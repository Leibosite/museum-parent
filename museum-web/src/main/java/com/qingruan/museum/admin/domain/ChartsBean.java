/**
 2015年3月4日
 14cells
 
 */
package com.qingruan.museum.admin.domain;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author tommy
 * 
 */
@Getter
@Setter
public class ChartsBean {
	private List<String> x = new ArrayList<String>();
	private List<DataBean> dataBeans = new ArrayList<DataBean>();
}
