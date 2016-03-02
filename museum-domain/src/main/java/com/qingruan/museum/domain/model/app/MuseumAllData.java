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
public class MuseumAllData {
	private String result_code;
	private String result_message;
	private MuseumEnvironmentDataModel environment_data;
	private List<ShowroomModel> showroom = new ArrayList<ShowroomModel>();
}
