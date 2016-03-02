package com.qingruan.museum.domain.model.weather;

import lombok.Getter;
import lombok.Setter;

import com.qingruan.museum.domain.models.enums.MonitorDataType;
@Getter
@Setter
public class WeatherData {
private MonitorDataType monitorDataType;
private Double value;
private Double reference;
}
