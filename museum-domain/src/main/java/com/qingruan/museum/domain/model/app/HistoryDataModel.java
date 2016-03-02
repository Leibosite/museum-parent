package com.qingruan.museum.domain.model.app;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author leibosite
 *
 */
@Setter
@Getter
public class HistoryDataModel {
	private Long showroom_id ;
	private String start_time;
	private String end_time;
	private double data[] = new double[12*31];
}
