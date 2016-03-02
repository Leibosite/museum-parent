package com.qingruan.highcharts.api;

public class HighchartsFactory {

    public HighchartsFactory() {
        super();
    }

    public ChartOptions createChartOptions() {
        return new ChartOptions();
    }

    public Point createPoint() {
        return new Point();
    }

    public Series createSeries() {
        return new Series();
    }

}