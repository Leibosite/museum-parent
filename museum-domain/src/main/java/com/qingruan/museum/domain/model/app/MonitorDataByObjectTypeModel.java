package com.qingruan.museum.domain.model.app;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author leibosite
 *
 */
@Getter
@Setter
public class MonitorDataByObjectTypeModel {
	private String result_code;
	private String result_message;
	private List<MonitorMqiDetailDataModel> monitor_mqi_data = new ArrayList<MonitorMqiDetailDataModel>();
}
