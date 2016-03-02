package com.qingruan.highcharts.api.serie;

import com.qingruan.highcharts.api.utils.ArrayString;

public class SeriesCenter extends ArrayString {

    private static final long serialVersionUID = 1L;

    public String getX() {
        return get(0);
    }

    public String getY() {
        return get(1);
    }

    public void setX(String x) {
        add(0, x);
    }

    public void setY(String y) {
        add(1, y);
    }
}