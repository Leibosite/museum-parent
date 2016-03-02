package com.qingruan.museum.domain.model.weather;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.qingruan.museum.domain.model.DomainBase;

@Getter
@Setter
public class WeatherDataHistory extends DomainBase {
	private static final long serialVersionUID = 1860743392165996210L;
	private List<HistoryData> historyDatas = new ArrayList<HistoryData>();
	private String speedDirection;
}
