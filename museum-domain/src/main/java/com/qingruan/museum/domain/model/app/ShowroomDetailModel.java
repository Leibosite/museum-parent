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
public class ShowroomDetailModel {
	private String result_code;
	private String result_message;
	private ShowRoomInformationModel showroom_information = new ShowRoomInformationModel();
	private MonitorDataModel monitor_data;
	private List<HelpAdviceModel> showroom_advice = new ArrayList<HelpAdviceModel>();
	private HistoryDataModel history_data;
	private List<MonitorMqiDetailDataModel> monitor_mqi_data = new ArrayList<MonitorMqiDetailDataModel>();
}