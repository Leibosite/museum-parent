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
public class MonitorDataModel {

	private double co2;
	private double temperature;
	private double humidity;
	private double lighting;
	private double soil_salinity;
	private double soil_temperature;
	private double soil_humidity;
}
