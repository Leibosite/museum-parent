package com.qingruan.museum.domain.model.weather;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.qingruan.museum.domain.model.DomainBase;

@Getter
@Setter
public class WeatherDataOverview extends DomainBase {

	private static final long serialVersionUID = 1860743392165996210L;
	private List<WeatherData> dataOverviews = new ArrayList<WeatherData>();
	private String speedDirection;
}
