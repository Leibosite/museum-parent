package com.qingruan.highcharts.api.serializer;

import java.util.Map;

import com.qingruan.highcharts.api.base.Style;


public class StyleSerializer extends Serializer<Style> {

    @Override
    public Map<String, String> getProperties(Style instance) {
        return instance.getProperties();
    }

}
