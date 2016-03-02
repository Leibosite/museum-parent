package com.qingruan.museum.domain.model.app;

/**
 * @author leibosite
 */
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MuseumEnvironmentDataModel {
	private double pm_2_5;
	private double co2;
	private double temperature;
	private double humidity;
	private double lighting;
	private double uv;
	private String name;
	private double total_score;
}
